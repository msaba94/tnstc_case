/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author iCT-3
 */
public class SQLConnection {

    public static Connection connection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CUDDALORE_REG","root","Test@123");
            return conn;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
