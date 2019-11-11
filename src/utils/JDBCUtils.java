package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {

	public static Connection getConn() {
		Connection conn=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String jdbcUrl="jdbc:mysql:///easymall";
			conn=DriverManager.getConnection(jdbcUrl,"root","123");
			return conn;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void close(Connection conn,Statement stat,ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				rs=null;
			}
		}
		if(stat!=null){
			try{
				stat.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
			finally{
				stat=null;
			}
		}
		if(conn!=null){
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
			finally{
				conn=null;
			}
		}
	}
}

