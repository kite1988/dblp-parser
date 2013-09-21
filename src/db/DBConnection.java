package db;

import java.sql.*;

public class DBConnection {
	// Change the parameters accordingly.
	private static String dbUrl = "jdbc:mysql://127.0.0.1:3306/dblp_inproc?useUnicode=true&characterEncoding=utf-8";
	private static String user = "root";
	private static String password = "123456";

	public static Connection getConn() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			return DriverManager.getConnection(dbUrl, user, password);
		} catch (Exception e) {
			System.out.println("Error while opening a conneciton to database server: "
								+ e.getMessage());
			return null;
		}
	}
}
