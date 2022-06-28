import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ShowTable {
	
	public ShowTable(PersonIdentity pI, String tableName){
		try {
			
			ResultSet rs = pI.stmt.executeQuery("select * from " + tableName);
			
			ResultSetMetaData rsmd = rs.getMetaData();

			int columnsNumber = rsmd.getColumnCount();
			
			for(int i = 1; i <= columnsNumber; i++) {
				System.out.printf("%-15s", rsmd.getColumnName(i));
			}
			System.out.println();
			
			while(rs.next()) {
				
				for(int i = 1; i <= columnsNumber; i++) {
					System.out.printf("%-15s", rs.getString(i) );
				}
				System.out.println();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
