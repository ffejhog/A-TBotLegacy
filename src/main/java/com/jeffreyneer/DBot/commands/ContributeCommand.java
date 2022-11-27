package com.jeffreyneer.DBot.commands;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContributeCommand extends Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        boolean admin = false;
        if(e.getAuthor().getName().equals("ffejhog") && e.getAuthor().getDiscriminator().equals("5596")){
            admin = true;
        }
        sendPrivate(e.getAuthor().openPrivateChannel().complete(), args, admin);
        e.getMessage().delete().reason("Removing command clutter").queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("?contribute");
    }

    @Override
    public String getDescription() {
        return "Allows you to get access to the git repo if you desire";
    }

    @Override
    public String getName() {
        return "Contribute command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(
                "?contribute   **OR**  ?contribute *<command>*\n"
                        + "?contribute - Returns contribution info about the bot\n"
                        + "?contribute join <Github Username> - Adds the github username to the private repo for read access\n"
                        + "__Example:__ ?contribute");
    }

    private void sendPrivate(PrivateChannel channel, String[] args, boolean admin) {
        if (args.length == 1) {
            channel.sendMessage("Use the command ?contribute join <GITHUB USERNAME> to add yourself to read only access for DBots repo. " +
                    "Any changes you wish to make must be done in a fork, then make a pull request to merge your fork to master. " +
                    "");
        } else {
            if (args[1].toLowerCase().equals("join")) {
                if (addContributor(args[2])) {
                    channel.sendMessage(new MessageBuilder().append(args[2]).append(" has been added to the repo collaborators").build());
                } else {
                    channel.sendMessage(new MessageBuilder().append(args[2]).append(" has failed to be added to the repo collaborators. ")
                            .append("This is because the user either doesn't exist(or is already added to the repo) or something broke").build());
                }
            }else if(args[1].toLowerCase().equals("remove") && admin){
                if (removeContributor(args[2])) {
                    channel.sendMessage(new MessageBuilder().append(args[2]).append(" has been removed from the repo collaborators").build());
                } else {
                    channel.sendMessage(new MessageBuilder().append(args[2]).append(" has failed to be removed from the repo collaborators. ")
                            .append("This is because the user either doesn't exist or something broke").build());
                }
            }
             else {
                channel.sendMessage(new MessageBuilder()
                        .append("The only command supported now is join").build()).queue();
            }
        }
    }

    private boolean addContributor(String userNameToAdd){
        Github github = new RtGithub("bbc3be3547250c18652d9e9b02340840e18cab8c");
        Repo repo = github.repos().get(new Coordinates.Simple("ffejhog", "A-TDiscordBot"));
        try{
            if(repo.collaborators().isCollaborator(userNameToAdd)){
                return false;
            }
            repo.collaborators().add(userNameToAdd);

        }catch(IOException e){
            return false;
        }

        return true;
    }

    private boolean removeContributor(String userNameToRemove){
        Github github = new RtGithub("bbc3be3547250c18652d9e9b02340840e18cab8c");
        Repo repo = github.repos().get(new Coordinates.Simple("ffejhog", "A-TDiscordBot"));
        try{
            if(!repo.collaborators().isCollaborator(userNameToRemove)){
                return false;
            }
            repo.collaborators().remove(userNameToRemove);

        }catch(IOException e){
            return false;
        }

        return true;
    }
}
