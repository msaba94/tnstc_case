package application.database;

import java.sql.Connection;
import java.sql.DriverManager;

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
