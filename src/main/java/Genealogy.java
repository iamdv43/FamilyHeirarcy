
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Genealogy {

	Statement stmt;
	Map<Integer, List<Integer>> fTree;
	
	
	public Genealogy() {
	
		ConnectiontoDB cDB = new ConnectiontoDB();
		stmt =  cDB.connectToDB();
		
		FamilyMap fM = new FamilyMap();

		fTree = fM.createMap();
		
	}
	
	/**
	 * 
	 * @param name
	 * @return This method returns the personIdentity class object.
	 * 
	 */
	
	PersonIdentity findPerson( String name) {
		
		int id = 0;
		
		if(name == null || name.equals("")) {
			return null;
		}
		
		try {
			// search name in the database
			ResultSet findP = stmt.executeQuery("select * from " + Constant.familyinfo + " where name = '" + name + "'");
			while(findP.next()) {
				id = findP.getInt(1);
			}
			PersonIdentity p1 = new PersonIdentity( id, name);
			return p1;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;	
		}
	}
	
	/**
	 * 
	 * @param name of file location
	 * @return object of the FileIdentifier class
	 */
	FileIdentifier findMediaFile(String name) {
		
		if(name == null || name.equals("")) {
			return null;
		}
		
		FileIdentifier f1 = null;
		
		try {
			ResultSet rs = stmt.executeQuery("select * from " + Constant.mediainfo + " where fileIdentifier = '" + name +"'");
			
			while(rs.next()) {
				f1 = new FileIdentifier(name);
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		return f1;
	}
	
	/**
	 * 
	 * @param id PersonIdentity class object
	 * @return name of the person associated with the object
	 */
	String findName( PersonIdentity id ) {
		
		if(id == null) {
			return null;
		}
		
		return id.name;
	}
	
	/**
	 * 
	 * @param fileId object of the FileIdentifier class
	 * @return file location
	 */
	String findMediaFile( FileIdentifier fileId ) {
		
		if(fileId == null ) {
			return null;
		}
		return fileId.fileLocation;
	}
	
	/**
	 * This method finds relationship between two person.
	 * @param person1 object of PersonIdentity class
	 * @param person2 object of PersonIdentity class
	 * @return object of BiologicalRelation class, that contains two values, cousin ship and removal
	 */
	BiologicalRelation findRelation( PersonIdentity person1, PersonIdentity person2 ) {
		
		if(person1 == null || person2 == null) {
			return null;
		}
		
		BiologicalRelation bRel = new BiologicalRelation();
		
		bRel = bRel.findRelation(person1, person2);
		
		return bRel;
	}
	
	/**
	 * @param person object of PersonIdentity class 
	 * @param generations
	 * @return set of descendents objects of the person
	 */
	Set<PersonIdentity> descendents( PersonIdentity person, Integer generations ){
		
		Set<PersonIdentity> pSet = new LinkedHashSet<>();
		
		if(person == null || generations <= 0 ) {
			return pSet;
		}
		return descOrAnces(person, generations, "desc" );
		
	}
	
	/**
	 * @param person 
	 * @param generations
	 * @return set of ancestores objects of the person
	 */
	Set<PersonIdentity> ancestores ( PersonIdentity person, Integer generations ){

		Set<PersonIdentity> pSet = new LinkedHashSet<>();
		
		if(person == null || generations <= 0 ) {
			return pSet;
		}
		
		return descOrAnces(person, generations, "ances" );
	}
	
	/**
	 * common method to find ancestores and descendents
	 * @param person
	 * @param generations
	 * @param checkValue to select ances or desc
	 * @return set of objects
	 */
	Set<PersonIdentity> descOrAnces(PersonIdentity person, Integer generations, String checkValue ){
		
		int id = person.id;
		
		List<Integer> ListOfPerson = new ArrayList<>();
		// temporary list to track of next generation
		List<Integer> c = new ArrayList<>();
		// Set of personIdentity objects
		Set<PersonIdentity> pSet = new LinkedHashSet<>();
		
		String forQuery = "";
		int columnNumber = 2;
		if(checkValue.equals("desc")) {
			forQuery = "id";
			
		}else {
			forQuery = "child";
			columnNumber = 1;
		}
		
		
		try {
			int generation = 1;
			int start = 0,end;
			
			while(generation <= generations) {
				
				ResultSet person1 = stmt.executeQuery("select * from " + Constant.childinfo  + " where " + forQuery + " = " + id );
				
				while(person1.next()) {
					ListOfPerson.add(person1.getInt(columnNumber));
				}
			
				// add immediate children information for future reference. 
				if(c.isEmpty()) {
					end = ListOfPerson.size();
					for(int i : ListOfPerson.subList(start, end)) {
						c.add(i);
					}
					start = ListOfPerson.size();
					generation++;
				}
				
				if(!c.isEmpty()) {
					id = c.get(0);
					c.remove(0);
				}
			}
			
			System.out.println( checkValue + " : " + ListOfPerson.toString());
			
			//for converting string to object
			for(int i : ListOfPerson) {
				ResultSet rs =  stmt.executeQuery("select * from " + Constant.familyinfo + " where id = " + i);
				rs.next();
				String name =  rs.getString(2);
				pSet.add(findPerson(name));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return pSet;
		
	}
	
	/**
	 * 
	 * @param person's object
	 * @return list of notes and reference of the person
	 */
	List<String> notesAndReferences( PersonIdentity person ){
		
		
		
		List<String> nAndR = new ArrayList<>();
		
		if(person == null) {
			return nAndR;
		}
		
		try {
			
		    ResultSet rs = stmt.executeQuery("select * from " + Constant.noteinfo  + " where id = " + person.id );
			
		    while(rs.next()) {
		       nAndR.add(rs.getString(2));
		    }
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return nAndR;
	}
	
	/**
	 * @param tag information of the picture.
	 * @param startDate 
	 * @param endDate
	 * @return set of file objects that has the particular tag
	 */
	Set<FileIdentifier> findMediaByTag( String tag , String startDate, String endDate){
		
		
		
		List<String> fMList = new ArrayList<>();
		Set<FileIdentifier> sList = new LinkedHashSet<>();
		
		if(tag == null || tag.equals("")) {
			return sList;
		}
		
		try {
			
			if(startDate == null) {
				startDate = "0000-00-00";
			}
			
			if(endDate == null) {
				endDate = "9999-12-31";
			}
			
		    ResultSet rs = stmt.executeQuery("select * from "+ Constant.mediainfo+ " where fileIdentifier in "
		    		+ " ( select fileIdentifier from " + Constant.tagmediainfo + " where tags = '"+ tag + "') and "
		    		+ "((dateofpicture >= ' " +startDate+ " ' AND dateofpicture <= '"+ endDate + "') or dateOfPicture is null)");
			
		    while(rs.next()) {
		    	fMList.add(rs.getString(1));
		    }
		    
		    System.out.println("MediabyTag " + fMList.toString());
		    
		    //find object for the string and put it in the list
		    for(String i : fMList) {
				 sList.add(findMediaFile(i));
			 }
		
		} catch (SQLException e) {
			e.printStackTrace();
			return sList;
		}
		
		
		return sList;
		
	}
	
	/**
	 * 
	 * @param location
	 * @param startDate
	 * @param endDate
	 * @return the set of objects of fileIdentifier class of particular location
	 */
	Set<FileIdentifier> findMediaByLocation( String location, String startDate, String endDate){
		
	
		List<String> fMList = new ArrayList<>();
		Set<FileIdentifier> sList = new LinkedHashSet<>();
		
		if(location == null || location.equals("")) {
			return sList;
		}
		
		
		if(startDate == null) {
			startDate = "0000-00-00";
		}
		
		if(endDate == null) {
			endDate = "9999-12-31";
		}
		
		try {
				
		    ResultSet rs = stmt.executeQuery("  select * from " + Constant.mediainfo + " where location like '%"+ location +
		    		"%' and ((dateofpicture >= ' " +startDate+ " ' AND dateofpicture <= '"+ endDate +"') or dateOfPicture is null)");
			
		    while(rs.next()) {
		    	fMList.add(rs.getString(1));
		    }
		} catch (SQLException e) {
			e.printStackTrace();
			return sList;
		}
		
		 System.out.println("MediabyLocation: " + fMList.toString());
		
		 //find object for the string and put it in the list
		 for(String i : fMList) {
			 sList.add(findMediaFile(i));
		 }
		 
		return sList;
	}
	
	/**
	 * 
	 * @param people set of objects of the personIdentity class
	 * @param startDate
	 * @param endDate
	 * @return list of fileIdentifier objects that has people mentioned in the list
	 */
	List<FileIdentifier> findIndividualsMedia( Set<PersonIdentity> people, String startDate, String endDate){
	
		
		
		try {
			stmt.executeUpdate("truncate table mediadatemethod");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		List<String> findMediaList = new ArrayList<>();
		
		List<FileIdentifier> fList = new ArrayList<>();
		
		if(people.isEmpty()) {
			return fList;
		}
		
		List<String> file = new ArrayList<>();
		List<String> date = new ArrayList<>();
		
		
		if(startDate == null) {
			startDate = "0000-00-00";
		}
		
		if(endDate == null) {
			endDate = "9999-12-31";
		}
		
		try {
			
			for( PersonIdentity p1 : people ) {
			
				file.clear();
				date.clear();
			
				
				ResultSet rs = stmt.executeQuery(" select mediainfo.fileIdentifier, dateOfPicture from "+ Constant.mediainfo + " join "
				    		+ " ( select fileIdentifier from  "+ Constant.peoplemediainfo + " where peopleid = '" + p1.id + "' ) as m"
				    		+ " on mediainfo.fileIdentifier = m.fileIdentifier "
				    		+ " where ((dateofpicture >= ' " +startDate+ " ' AND dateofpicture <= '"+ endDate + "') or dateOfPicture is null)"
				    		+ " order by dateofpicture ");
				
				while(rs.next()) {
					
					file.add(rs.getString(1));
					date.add(rs.getString(2));
					
				}
				
				//add the values to the temp table for the future records
				for(int i = 0; i < file.size(); i++) {
					if(date.get(i) == null) {
						stmt.executeUpdate("insert ignore into mediadatemethod (fileIdentifier) values ('" + file.get(i) + "' )");
					}else {
						stmt.executeUpdate("insert ignore into mediadatemethod values ('" + file.get(i) + "' ,'" + date.get(i) +"' )");
					}
					

				}
				
			}
			
			//retrieve only value that has date of picture
			ResultSet fileL = stmt.executeQuery("select * from mediadatemethod where dateOfPicture is not null order by dateOfPicture");
			
			
			while(fileL.next()) {
				
				findMediaList.add(fileL.getString(1));
				
			}
			
			//retrieve only value that has not date of picture
			fileL = stmt.executeQuery("select * from mediadatemethod where dateOfPicture is null order by fileIdentifier");
			
			while(fileL.next()) {
				
				findMediaList.add(fileL.getString(1));
				
			}
			 
		} catch (SQLException e) {
			e.printStackTrace();
			return fList;
		}
		
		System.out.println("findMediaL " + findMediaList.toString());
		
		//find object for the method 
		for(String i : findMediaList) {
			fList.add(findMediaFile(i));
		}
		 
		 
		return fList;
		
	}
	
	/**
	 * 
	 * @param person object of the personIdentity class
	 * @return list of fileIdentifier class object
	 */
	List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person){
		
		List<Integer> iChild  = new ArrayList<>();
		Set<PersonIdentity> pSet  = new LinkedHashSet<>();
		List<FileIdentifier> fList = new ArrayList<>();
		
		if(person == null) {
			return fList;
		}
		
		try {
		
			ResultSet childInfo = stmt.executeQuery("select * from " + Constant.childinfo + " where id = " + person.id);
		
			while(childInfo.next()) {
				iChild.add(childInfo.getInt(2));
			}
			
			//create list of objects of the personIdentity class
			for(int i : iChild) {
				ResultSet rs =  stmt.executeQuery("select * from " + Constant.familyinfo + " where id = " + i);
				rs.next();
				String name =  rs.getString(2);
				pSet.add(findPerson(name));
			}

			//find list of fileIdentitfier object using above method
			fList = findIndividualsMedia( pSet, "0000-00-00", "9999-12-31");
			
		
		} catch (SQLException e) {
			e.printStackTrace();
			return fList;
		}
		
		return fList;
		
	}
	
	
	
	
}
