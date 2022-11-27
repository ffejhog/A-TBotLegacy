
/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot.commands;

import com.jeffreyneer.DBot.Settings;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VersionCommand extends Command{
    Settings settings = new Settings();

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        sendPrivate(e.getAuthor().openPrivateChannel().complete(), args);
        e.getMessage().delete().reason("Removing command clutter").queue();

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("?version");
    }

    @Override
    public String getDescription() {
        return "Returns information about the bots current version";
    }

    @Override
    public String getName() {
        return "Version Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(
                "?version   **OR**  ?version *<command>*\n"
                        + "?version - returns the current version of the bot\n"
                        + "?version <command> - Returns the requested info about the bot (Currently only changelog is supported)\n"
                        + "__Example:__ ?version changelog");
    }

    private void sendPrivate(PrivateChannel channel, String[] args) {
        if (args.length == 1)
        {
            channel.sendMessage(new MessageBuilder()
                    .append("Version: ")
                    .append(settings.getVersion()).build()).queue();
        }
        else
        {
            if(args[1].toLowerCase().equals("changelog")){
                channel.sendMessage(new MessageBuilder()
                        .append("Changelog: ")
                        .append(settings.getChangelog()).build()).queue();

            }else{
                channel.sendMessage(new MessageBuilder()
                        .append("The only command supported now is changelog").build()).queue();
                channel.sendMessage(new MessageBuilder()
                        .append("Version: ")
                        .append(settings.getVersion()).build()).queue();
            }
        }
    }

}

