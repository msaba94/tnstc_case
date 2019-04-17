/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemigration.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author sabapathy
 */
public class MySqlHelper {

    private final Connection conn;

    public MySqlHelper() {
        conn = MySqlConntion.connection();
    }

    public void insertUser(Iterator<Document> userDocuments) {

        String query = "INSERT INTO USER (mongodbId, firstName, lastName, email, phoneNumber, loggedOnFlag, role, userStatus, enteredOn, enteredBy, updatedOn, companyId, teams, activateUserToken) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            
            while (userDocuments.hasNext()) {
                Document next = userDocuments.next();
                
                List<String> teams = (List<String>) next.get("teams");
                ObjectMapper objMap = new ObjectMapper();
                
                PreparedStatement stmt = conn.prepareStatement(query);
                ObjectId mongoId =  next.getObjectId("_id");
                stmt.setString(1, (String) mongoId.toHexString());
                stmt.setString(2, (String) next.getString("firstName"));
                stmt.setString(3, (String) next.getString("lastName"));
                stmt.setString(4, (String) next.getString("email"));
                stmt.setString(5, (String) next.getString("phoneNumber"));
                stmt.setBoolean(6, (Boolean) next.getBoolean("loggedOnFlag"));
                stmt.setString(7, (String) next.getString("role"));
                stmt.setString(8, (String) next.getString("userStatus"));
                stmt.setString(9, (String) next.getString("enteredOn"));
                stmt.setString(10, (String) next.getString("enteredBy"));
                stmt.setString(11, (String) next.getString("updatedOn"));
                stmt.setString(12, (String) next.getString("companyId"));
                stmt.setObject(13, objMap.defaultPrettyPrintingWriter().writeValueAsString(teams));
                stmt.setString(14, (String) next.getString("activateUserToken"));
                
                stmt.executeUpdate();
                
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
