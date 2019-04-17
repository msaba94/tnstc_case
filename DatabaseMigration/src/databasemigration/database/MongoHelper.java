/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemigration.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Iterator;
import org.bson.Document;

/**
 *
 * @author sabapathy
 */
public class MongoHelper {
    
    private final MongoDatabase mongoDatabase;
    
    private static MongoCollection<Document> accountCollections;
    private static MongoCollection<Document> candidateCollections;
    private static MongoCollection<Document> clientCollections;
    private static MongoCollection<Document> clientRecuriterCollections;
    private static MongoCollection<Document> clientSubmissionCollections;
    private static MongoCollection<Document> commentCollections;
    private static MongoCollection<Document> companyCollections;
    private static MongoCollection<Document> mediaFileCollections;
    private static MongoCollection<Document> poisitionCollections;
    private static MongoCollection<Document> technologyCollections;
    private static MongoCollection<Document> userCollections;
    private static MongoCollection<Document> teamCollections;
    private static MongoCollection<Document> requirementCollections;
    private static MongoCollection<Document> submissionCollections;
    private static MongoCollection<Document> visaStatusCollections;
    
    public MongoHelper() {
        mongoDatabase = MongoConnection.mongoConnection();
        
        accountCollections = mongoDatabase.getCollection("ACCOUNT");
        candidateCollections = mongoDatabase.getCollection("CANDIDATE");
        clientCollections = mongoDatabase.getCollection("CLIENT");
        clientRecuriterCollections = mongoDatabase.getCollection("CLIENT_RECURITER");
        clientSubmissionCollections = mongoDatabase.getCollection("CLIENT_SUBMISSION");
        commentCollections = mongoDatabase.getCollection("COMMENT");
        companyCollections = mongoDatabase.getCollection("COMPANY");
        mediaFileCollections = mongoDatabase.getCollection("MEDIA_FILE");
        poisitionCollections = mongoDatabase.getCollection("POSITION");
        technologyCollections = mongoDatabase.getCollection("TECHNOLOGY");
        userCollections = mongoDatabase.getCollection("USER");
        teamCollections = mongoDatabase.getCollection("TEAM");
        requirementCollections = mongoDatabase.getCollection("REQUIREMENT");
        submissionCollections = mongoDatabase.getCollection("SUBMISSION");
        visaStatusCollections = mongoDatabase.getCollection("VISA_STATUS");
    }
    
    public Iterator<Document> getAllUser() {
        FindIterable<Document> users = userCollections.find();
        Iterator<Document> userIterator = users.iterator();
        return userIterator;
    }
    
}
