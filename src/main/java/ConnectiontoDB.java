import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectiontoDB implements Constant {
	
	Connection con;

	static Statement stmt;
	
	/**
	 * 
	 * @return statement to execute query
	 */
	Statement connectToDB() {
			
			if (stmt != null) {

				return stmt;

			}else {
		
			
				try {
					
					Class.forName("com.mysql.cj.jdbc.Driver");  
							
					con = DriverManager.getConnection( Constant.url, Constant.username, Constant.password);
				
					stmt = con.createStatement();
				
					return stmt;
				
				}catch(Exception e){
			
					System.out.println("Fail to connect.");
				
					return null;
				}
		
			}	
	
	}
	
}
