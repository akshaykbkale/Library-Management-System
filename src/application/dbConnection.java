package application; 
import java.sql.*;

public class dbConnection
{


public Connection Getconnection()throws ClassNotFoundException,SQLException
    {
	try {
      Class.forName("com.mysql.jdbc.Driver");
 
      System.out.println("OK :/");
 
      Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","Feb@1995*");
      return conn;
	}
	
	catch(Exception e){ 
		System.out.println(e);
		}

 
      return null;
    }
}