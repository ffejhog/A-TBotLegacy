/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot.response;

import com.jeffreyneer.DBot.DBot;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class mentionMover extends Response{

    @Override
    public void onReceive(MessageReceivedEvent e) {
        if(e.getTextChannel().getId().equals("135434862856241152") ){
            if(e.getMessage().getMentionedUsers().contains(e.getGuild().getMemberById("135435404009668608").getUser())){
                String messageContent = e.getMessage().getContentStripped().substring(10);
                e.getMessage().delete().reason("Wrong Channel").queue();
                e.getGuild().getTextChannelById("361995386203668480").sendMessage(new MessageBuilder().append(e.getGuild().getMemberById("135435404009668608")).append(messageContent).build()).queue();
            }
        }
    }

}
