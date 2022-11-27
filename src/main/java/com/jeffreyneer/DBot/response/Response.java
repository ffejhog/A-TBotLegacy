
/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot.response;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class Response extends ListenerAdapter {

    public abstract void onReceive(MessageReceivedEvent e);

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getAuthor().isBot() && !respondToBots()) {
            return;
        }
        
        onReceive(e);

    }

    protected Message sendMessage(MessageReceivedEvent e, Message message){
        if(e.isFromType(ChannelType.PRIVATE)){
            return e.getPrivateChannel().sendMessage(message).complete();
        }
        else{
            return e.getTextChannel().sendMessage(message).complete();
        }
    }

    protected Message sendMessage(MessageReceivedEvent e, String message){
        return sendMessage(e, new MessageBuilder().append(message).build());
    }

    protected boolean respondToBots(){
        return false;
    }

}
