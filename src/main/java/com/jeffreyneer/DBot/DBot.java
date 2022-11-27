
/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot;

import com.jeffreyneer.DBot.commands.*;
import com.jeffreyneer.DBot.response.hateComcastResponse;
import com.jeffreyneer.DBot.response.markovChat;
import com.jeffreyneer.DBot.response.mentionMover;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;

public class DBot {

    //error exit codes.
    public static final int UNABLE_TO_CONNECT_TO_DISCORD = 30;
    public static final int BAD_USERNAME_PASS_COMBO = 31;
    public static final int NO_USERNAME_PASS_COMBO = 32;


    private static JDA JDAapi;

    public static void main(String args[]){
        setupBot();
    }

    public static void setupBot(){
        try{
            System.out.println(""); // Url for adding bot
            Settings settings = new Settings();
            JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken(settings.getBotToken());
            Database database = null;
            //Database stuff
            try{
                database = new Database(settings.getDatabaseURI());
            }catch(URISyntaxException e){
                e.printStackTrace();
            }

            //Add listening Commands
            jdaBuilder.addEventListener(new markovChat(database));
            jdaBuilder.addEventListener(new mentionMover());
            jdaBuilder.addEventListener(new hateComcastResponse());

            //Help initialization
            HelpCommand help = new HelpCommand();
            jdaBuilder.addEventListener(help.registerCommand(help));

            //Add bot commands
            jdaBuilder.addEventListener(help.registerCommand(new IDGAFCommand()));
            jdaBuilder.addEventListener(help.registerCommand(new GiveCommand()));
            jdaBuilder.addEventListener(help.registerCommand(new VersionCommand()));
            jdaBuilder.addEventListener(help.registerCommand(new UptimeCommand()));

            //Login Stuff
            JDAapi = jdaBuilder.buildBlocking();
            JDAapi.getPresence().setGame(Game.of(Game.GameType.DEFAULT,"?help for help"));


        }catch (IllegalArgumentException e)
        {
            System.out.println("No login details provided! Please provide a botToken in the config.");
            System.exit(NO_USERNAME_PASS_COMBO);
        }
        catch (LoginException e)
        {
            System.out.println("The botToken provided in the Config.json was incorrect.");
            System.out.println("Did you modify the Config.json after it was created?");
            System.exit(BAD_USERNAME_PASS_COMBO);
        }
        catch (InterruptedException e)
        {
            System.out.println("Our login thread was interrupted!");
            System.exit(UNABLE_TO_CONNECT_TO_DISCORD);
        }


    }

    public static JDA getJDAapi(){
        return JDAapi;
    }
}
