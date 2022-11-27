
/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot.response;

import com.jeffreyneer.DBot.DBot;
import com.jeffreyneer.DBot.Database;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class markovChat extends Response{

    Database database;
    Random rand;

    public markovChat(Database indatabase){
        database = indatabase;
        rand = new Random();
    }

    @Override
    public void onReceive(MessageReceivedEvent e) {
        if(e.getMessage().isMentioned(DBot.getJDAapi().getSelfUser()) && !e.getAuthor().isBot()){
            if(database == null){
                sendMessage(e, "Database Connection failed so I can't talk right now :(");
            }else{
                try{
                    sendMessage(e, botSay());
                }catch(SQLException execpt){
                    sendMessage(e, "¯\\_(ツ)_/¯");
                }

            }
        }
    }

    public String botSay() throws SQLException {
        ArrayList<String> startingWords = new ArrayList<>();
        ArrayList<Integer> startingKey = new ArrayList<>();
        ArrayList<String> nextPossible = new ArrayList<>();
        Statement exeStmt = database.getDatabaseConnection().createStatement();
        ResultSet rs;
        rs = exeStmt.executeQuery("SELECT key,lookupwords FROM markovindex WHERE start=TRUE;");
        while(rs.next()) {
            startingWords.add(rs.getString("lookupwords"));
            startingKey.add(rs.getInt("key"));
        }
        int randomGen = rand.nextInt(startingWords.size());
        String current = startingWords.get(randomGen);
        int currentkey = startingKey.get(randomGen);
        StringBuilder output = new StringBuilder(current);
        try {
            do{


                nextPossible.clear();
                rs = exeStmt.executeQuery("SELECT nextword FROM markovnext WHERE linkkey=" + currentkey + ";");
                while (rs.next()) {
                    nextPossible.add(rs.getString("nextword"));
                }
                randomGen = rand.nextInt(nextPossible.size());
                output.append(" ").append(nextPossible.get(randomGen));
                String[] currentSplit = output.toString().split(" ");
                current = currentSplit[currentSplit.length - 2] + " " + currentSplit[currentSplit.length - 1];
                rs = exeStmt.executeQuery("SELECT key FROM markovindex WHERE lookupwords=\'" + current + "\';");
                while (rs.next()) {
                    currentkey = rs.getInt("key");
                }

            }while(!output.toString().contains("ç"));
        }catch (Exception e){
            return "¯\\_(ツ)_/¯";
        }
        rs.close();
        exeStmt.close();
        return output.substring(0,output.length()-1);
        //return "Eventually I will be able to speak. For now, This is the only thing I can say.";
    }

    public void trainBot(String message) throws SQLException{
        message = message.toLowerCase();
        ArrayList<String> inputdatasplit = new ArrayList<>(Arrays.asList(message.split(" ")));

        if(inputdatasplit.size()<3 || inputdatasplit.get(0).contains("@")){

            return;
        }
        for(int i = 0; i<inputdatasplit.size()-1;i++){
            int wordIndex = lookupMarkovIndex(inputdatasplit.get(i) + " " + inputdatasplit.get(i+1));
            if(wordIndex == -1){
                wordIndex = createMarkovIndex(inputdatasplit.get(i) + " " + inputdatasplit.get(i+1));
            }
            if(i==0){
                updateMarkovWithStart(wordIndex);
            }
            if(i+2 < inputdatasplit.size()){
                updateMarkovNext(inputdatasplit.get(i+2), wordIndex);
            }else{
                updateMarkovNext("ç", wordIndex);
            }

        }

    }

    private int lookupMarkovIndex(String words) throws SQLException{
        int returnKey = -1;
        Statement exeStmt = database.getDatabaseConnection().createStatement();
        ResultSet rs;
        rs = exeStmt.executeQuery("SELECT key FROM markovindex WHERE lookupwords=\'" + words + "\';");
        while(rs.next()) {
            returnKey = rs.getInt("key");
        }

        rs.close();
        exeStmt.close();
        return returnKey;
    }

    private int createMarkovIndex(String words) throws SQLException{
        int returnKey = -1;
        Statement exeStmt = database.getDatabaseConnection().createStatement();
        ResultSet rs;
        exeStmt.execute("INSERT INTO markovindex " + " VALUES (DEFAULT, \'"+ words + "\') RETURNING key;");
        rs = exeStmt.getResultSet();
        while(rs.next()) {
            returnKey = rs.getInt("key");
        }
        rs.close();
        exeStmt.close();
        return returnKey;
    }

    private void updateMarkovWithStart(int key) throws SQLException{
        Statement exeStmt = database.getDatabaseConnection().createStatement();
        exeStmt.executeUpdate("UPDATE markovindex SET start = TRUE WHERE key = " + key + ";");
        exeStmt.close();
    }

    private boolean updateMarkovNext(String word, int key) throws SQLException{

        Statement exeStmt = database.getDatabaseConnection().createStatement();

        exeStmt.executeUpdate("INSERT INTO markovnext (nextword, linkkey) SELECT \'" + word + "\', " + key + " WHERE NOT EXISTS (SELECT key FROM markovnext WHERE nextword = \'" + word + "\' AND linkkey = " + key + ");");
        exeStmt.close();
        return true;
    }

}
