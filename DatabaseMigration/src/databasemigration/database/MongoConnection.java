/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemigration.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sabapathy
 */
public class MongoConnection {

    // mongo db params
    private static final String host = "66.175.215.173";
    private static final int port = 27017;
    private static final String database = "RTS";
    private static final String userName = "rts-dev-user";
    private static final String password = "rtsDevUser";
    private static final String authenticationDatabase = "RTS";

    public static MongoDatabase mongoConnection() {

        ServerAddress serverAddress = new ServerAddress(host, port);
        MongoCredential credential = MongoCredential.createCredential(userName, authenticationDatabase,
                password.toCharArray());
        List<MongoCredential> auths = new ArrayList<>();
        auths.add(credential);
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();
        MongoClient mongoClient = new MongoClient(serverAddress, auths, options);
        MongoDatabase db = mongoClient.getDatabase(database);
        
        return db;

    }

    
}
