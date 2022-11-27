
/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot.commands;


import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GiveCommand extends Command{
    Random rand;

    public GiveCommand(){
        rand = new Random();
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {

        if(args.length > 1){
            try {
                String send = "";
                for(int i = 1; i<args.length;i++){
                    send = send + args[i] + " ";
                }
                send = send.trim().replace(" ", "%20");
                URL url = new URL("https://api.qwant.com/api/search/images?count=100&offset=1&q=" + send);
                InputStream is = url.openStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText).getJSONObject("data").getJSONObject("result");
                JSONArray items = json.getJSONArray("items");
                int randomGen = rand.nextInt(items.length());
                JSONObject selected = items.getJSONObject(randomGen);
                String imgURL = selected.getString("media");
                //Get random between 0-100

                url = new URL(imgURL);
                File file = new File("tmp.jpg");
                FileUtils.copyURLToFile(url, file);
                e.getTextChannel().sendFile(file, "tmp.jpg", null).queue();
            } catch (IOException execpt) {
                sendMessage(e, "Failed to get picture");
                execpt.printStackTrace();
            }


        }else{
            sendMessage(e, new MessageBuilder()
                    .append("Please append one or more tags to search for at the end")
                    .build());
        }


    }

    private String readAll(BufferedReader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp=rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("?give", "?show", "?search");
    }

    @Override
    public String getDescription() {
        return "WIll post a picture of the thing you ask for.";
    }

    @Override
    public String getName() {
        return "Give Command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(
                "?give   **OR**  ?show *<command>*\n"
                        + "?give <command> - Posts the image of the thing you requested\n"
                        + "__Example:__ ?give dog");
    }
}
