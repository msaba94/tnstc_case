/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mactapplication.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author my
 */
public class SQLConnection {

    public static Connection connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:CUDDALORE_REG.sqlite");
            return conn;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
