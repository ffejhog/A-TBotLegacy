
/*
 * Copyright (c) Jeffrey Neer 2017.
 */

package com.jeffreyneer.DBot;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Connection databaseConnection;

    public Database(URI inputURI){
        try{
            String username = inputURI.getUserInfo().split(":")[0];
            String password = inputURI.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + inputURI.getHost() + ':' + inputURI.getPort() + inputURI.getPath();
            databaseConnection = DriverManager.getConnection(dbUrl, username, password);
        }catch (SQLException e){
            System.out.print("Failed to establish connection with database");
        }

    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
