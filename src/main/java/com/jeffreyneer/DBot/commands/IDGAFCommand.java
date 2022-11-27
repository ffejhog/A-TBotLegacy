
/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IDGAFCommand extends Command{

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args){
        sendMessage(e, "https://www.youtube.com/watch?v=vsa1ZvzFgvU");
    }

    @Override
    public List<String> getAliases(){
        return Arrays.asList("?idgaf", "?idontgiveafuck");
    }

    @Override
    public String getDescription(){
        return "Tell everyone you don't give a fuck in youtube fashion";
    }

    @Override
    public String getName(){
        return "I Don't Give a Fuck Command";
    }

    @Override
    public List<String> getUsageInstructions(){
        return Collections.singletonList(
                "?idgaf   **OR**  ?idontgiveafuck\n"
                        + "?idgaf - Posts the IDGAF video to the current channel\n");
    }
}
