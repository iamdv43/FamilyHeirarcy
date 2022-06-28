import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyMap {

	Statement stmt;
	
	/**
	 *
	 * @return map that has parent as key and their children in values
	 */
	Map<Integer, List<Integer>> createMap() { 		
		
		Map<Integer, List<Integer>> familyMap = new HashMap<>();
		
		ConnectiontoDB cDB = new ConnectiontoDB();
	 	stmt =  cDB.connectToDB();
	 	
	 	int id;
	 	int pId = 0;
	 	
	 	try {

	 		ResultSet rs = stmt.executeQuery("select * from " + Constant.childinfo);

	 		ResultSetMetaData rsmd = rs.getMetaData();

			int columnsNumber = rsmd.getColumnCount();
	 		
	 		while(rs.next()) {

	 			for(int i = 1; i <= columnsNumber; i++) {
		 			
	 				id = rs.getInt(i);
	 				
	 				if(i == 1) {
	 					
	 					pId = id;
	 					
	 					if(familyMap.containsKey(id)) {
	 						
	 						continue;
	 					
	 					}else {
	 						List<Integer> childList = new ArrayList<>();	 					
	 						familyMap.put(id, childList);
	 					
	 					}
	 					
	 				}else {
	 					
	 					List<Integer> childList = familyMap.get(pId);
	 					
	 					childList.add(id);
	 					
	 					familyMap.put(pId, childList);
	 					
	 					
	 				}
	 			
	 			}

	 		}
	 		
	 		
	 	} catch (SQLException e) {

			e.printStackTrace();

	 	}
	
	 	return familyMap;
	}
	
}
