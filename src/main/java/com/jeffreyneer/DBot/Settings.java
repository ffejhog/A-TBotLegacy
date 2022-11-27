/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot;


import java.net.URI;
import java.net.URISyntaxException;

public class Settings {
    private String botTokenPublic = "";
    private String botTokenTest = "";
    private boolean debugMode = false;



    public String getBotToken(){
        if(debugMode){
            return botTokenTest;
        }else{
            return botTokenPublic;
        }
    }

    public String getChangelog() {
        StringBuilder output = new StringBuilder();
        output.append("Dbot now runs on a Raspberry Pi on my desk in a docker conatiner. Db runs on the same RPI now" );
        return output.toString();
    }
    public String getVersion(){
        String version = getClass().getPackage().getImplementationVersion();
        if(version == null){
            return "debug";
        }else{
            return version;
        }
    }

    public URI getDatabaseURI() throws URISyntaxException{
        if(debugMode){
            return new URI("");
        }else{
            //return new URI(System.getenv("DATABASE_URL"));
            return new URI("");
        }
    }


}
