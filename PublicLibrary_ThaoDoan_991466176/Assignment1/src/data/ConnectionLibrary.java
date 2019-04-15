/**
 * Student Name: Doan Ngoc Phuong Thao
 * Student ID: 991466176
 */
package data;
import java.sql.*;

public class ConnectionLibrary {
	
	private static final String url="jdbc:mysql://localhost:3306/publicLibrary";
    private static final String DriverClass="com.mysql.jdbc.Driver";
    private static final String user="root";
    private static final String password="root";

    private ConnectionLibrary() {
		try {
			Class.forName(DriverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * to connect to database everytime we use a query
     * @exception if we can't connect to MySQL
     */
    public static Connection getConnection() throws Exception{
    	Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("ERROR: Connection Failed "+e.getMessage());
		}
		return connection;

   }
 
    /**
     * terminate connection after executing each query
     * @throws Exception if an error occurs
     */
    public static void closeConnection(Connection con, Statement st, ResultSet rs) throws Exception{
            if(con!=null)
                    con.close();
            if(st!=null)
                    st.close();
            if(rs!=null)
                    rs.close();
    }
}
