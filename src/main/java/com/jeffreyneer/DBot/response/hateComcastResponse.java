/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot.response;

import com.jeffreyneer.DBot.DBot;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


import java.io.File;
import java.io.InputStream;

public class hateComcastResponse extends Response{

    @Override
    public void onReceive(MessageReceivedEvent e) {
        if(e.getMessage().getContentRaw().toLowerCase().contains("internet")){
            if(e.getMessage().getContentRaw().toLowerCase().contains("sucks")
                    || e.getMessage().getContentRaw().toLowerCase().contains("crap")
                    || e.getMessage().getContentRaw().toLowerCase().contains("shit")
                    || e.getMessage().getContentRaw().toLowerCase().contains("sucks")
                    || e.getMessage().getContentRaw().toLowerCase().contains("bad")
                    || e.getMessage().getContentRaw().toLowerCase().contains("terrible")
                    || e.getMessage().getContentRaw().toLowerCase().contains("hate")
                    || e.getMessage().getContentRaw().toLowerCase().contains("slow")
                    ){
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream comcastfile = classloader.getResourceAsStream("imgs/comcast.gif");
                Message mention = new MessageBuilder().append(e.getAuthor()).build();
                e.getTextChannel().sendFile(comcastfile, "ThatsTooBad.gif", mention).queue();

            }
        }
    }
}
