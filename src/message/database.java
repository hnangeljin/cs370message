package message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database {
	
	String userName ="openmrs";
	String password = "YLr5iLCXOgGN";
	String serverName = "127.0.0.1";
	String portNumber = "3317";
	String dbName = "openmrs";
	Connection connection;
	public Connection connect(){
		try{
			try {
	            // The newInstance() call is a work around for some
	            // broken Java implementations

	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	        } catch (Exception ex) {
	            // handle the error
	        }
	    	Connection conn = DriverManager.getConnection( "jdbc:mysql://" +serverName +":" + portNumber + "/"+dbName,userName, password); //host, username, password
	    	System.out.println("Connected Successful!"); //output connected successfully   
	    	connection = conn;
	    	return conn;
	    }
	    catch(Exception e){
	    	//display error if not connection
	    	System.out.println("Connection Error!!!");
	    	
	    }
		return null;
	}
	
		
		 
}