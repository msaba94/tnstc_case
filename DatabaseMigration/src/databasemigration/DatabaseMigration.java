/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemigration;

import databasemigration.database.MongoHelper;
import databasemigration.database.MySqlHelper;
import java.util.Iterator;
import org.bson.Document;

/**
 *
 * @author sabapathy
 */
public class DatabaseMigration {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        MySqlHelper mySqlHelper = new MySqlHelper();
        MongoHelper mongoHelper = new MongoHelper();
        
        Iterator<Document> allUser = mongoHelper.getAllUser();
        mySqlHelper.insertUser(allUser);
        System.out.println("Finish");
    }
    
}
