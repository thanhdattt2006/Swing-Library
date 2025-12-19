package apps;

import java.sql.Connection;

import models.ConnectDB;

public class DbCheck {
	public static void main (String[] args) {
		try {
		    Connection conn = ConnectDB.connection();
		    if (conn != null) {
		        System.out.println("Da ket noi");
		        java.sql.DatabaseMetaData meta = conn.getMetaData();
		        System.out.println("URL, PORT: " + meta.getURL());		        
		    } else {
		        System.out.println("Khong the ket noi");
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
