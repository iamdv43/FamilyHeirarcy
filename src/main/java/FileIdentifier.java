import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FileIdentifier implements MediaInformation{

	String fileLocation;
	Statement stmt;
	
	List<String> pValues = new ArrayList<>();
	
	Map<String, List<String>> possibleValues = new HashMap<>();
	
	/**
	 * Constructor of class FileIdentifier to make connection with database
	 */
	public FileIdentifier() {
		
		ConnectiontoDB cDB = new ConnectiontoDB();
	 	stmt =  cDB.connectToDB();
			
	 	//create a HashMap for possible key values for recordMediaAttribute method
		pValues.add("dateofpicture");
		pValues.add("picturedate");
		pValues.add("date");
		pValues.add("year");
		possibleValues.put("dateOfPicture",pValues);
		
		pValues = new ArrayList<>();
		pValues.add("province");
		pValues.add("country");
		pValues.add("city");
		pValues.add("location");
		possibleValues.put("Location", pValues);
		
	}
	
	public FileIdentifier(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	static List<FileIdentifier> listMedia = new ArrayList<>();
	
	/**
	 * @fileLocation : File name of some picture
	 * This method stores media file information to database 
	 */
	@Override
	public FileIdentifier addMediaFile(String fileLocation) {
		
		if(fileLocation == null || fileLocation.equals("")) {
			return null;
		}
		
		FileIdentifier media = new FileIdentifier(fileLocation);
		listMedia.add(media);
			
		try {
			
			ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.mediainfo).getMetaData();
			
			stmt.executeUpdate("INSERT INTO "+ Constant.mediainfo +" ( " + rsmd.getColumnName(1) + ")"
					+ " VALUES ('" + media.fileLocation + "')");
		
			return media; 
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		
			return null;
		}
		

	}

	/**
	 * @fileIdentifier : Object of class FileIdentifier whose information needs to be recorded
	 * @attribute : List of attributes of that particular media file
	 * This method records attributes of a media file to the database such as date of picture, location and so
	 * @return : Returns true if all the information about a media file have been successfully recorded and false otherwise
	 */
	@Override
	public Boolean recordMediaAttributes(FileIdentifier fileIdentifier, Map<String, String> attribute) {

		if(fileIdentifier == null) {
			return false;
		}
		
		Map<String, String> attributes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		
		attributes.putAll(attribute);
		
		ResultSetMetaData rsmd;
		Map<String, String> forInsert = new HashMap<>();
		
		//Stores location with location, city, province and country's information
		String location = "";
		
		//adding location, city, province and country in single value 
		if(attributes.keySet().contains("location") ) {
			location = location + attributes.get("location");
		}
		
		if(attributes.keySet().contains("city")) {
			if (location.isEmpty()) {
				location = location + attributes.get("city");
			}else {
				location = location + ", " + attributes.get("city");
			}

		}
				
		if(attributes.keySet().contains("province")) {
			if (location.isEmpty()) {
				location = location + attributes.get("province");
			}else {
				location = location + ", " + attributes.get("province");
			}
		}
		
		if(attributes.keySet().contains("country")) {
			if (location.isEmpty()) {
				location = location + attributes.get("country");
			}else {
				location = location + ", " + attributes.get("country");
			}
		}
		
		attributes.put("Location", location);
		
		try {
			rsmd = stmt.executeQuery("SELECT * FROM " + Constant.mediainfo).getMetaData();
			
			// 'forInsert' hash map for updating table value
			forInsert.put(rsmd.getColumnName(2), null);
			forInsert.put(rsmd.getColumnName(3), null);
			
		
		Set<String> aKeys =	attributes.keySet();
		List<String> notIncluedInTable = new ArrayList<>();
		boolean flag;
		
		// finding the proper key for the column name
		for(String aKey : aKeys) {
			flag = true;
			for(String key : possibleValues.keySet()) {
				if(possibleValues.get(key).contains(aKey.toLowerCase())){
					forInsert.put(key, attributes.get(aKey));
					flag = false;
				}
			}
			if(flag) {
				notIncluedInTable.add(aKey);
			}
		}
		
		
		// for altering date value
		String changeValue = null;
		
		// Validates date of birth
		if(forInsert.get("dateOfPicture") != null) {
			
			//If only year is mentioned then it appends month and day to it
			if(forInsert.get("dateOfPicture").length() == 4) { //Only year
				changeValue = forInsert.get("dateOfPicture") + "-00-00";
				forInsert.put("dateOfPicture", changeValue);
			}
			
			//If only year and month are mentioned then it appends day to it
			if(forInsert.get("dateOfPicture").length() == 7) { //year + month
				changeValue = forInsert.get("dateOfPicture") + "-00";
				forInsert.put("dateOfPicture", changeValue);					
			}
			
			//If only month and day is mentioned then it stores null value
			if(forInsert.get("dateOfPicture").length() == 5) { //month + date
				changeValue = null;
				forInsert.put("dateOfPicture", changeValue);	
			}
			
			//If only day or only month is mentioned then it stores null value
			if(forInsert.get("dateOfPicture").length() <= 3 || forInsert.get("dateOfPicture").length() == 6 ||
					forInsert.get("dateOfPicture").length() ==  8 || forInsert.get("dateOfPicture").length() == 9) { // date or month and any other invalid input
				changeValue = null;
				forInsert.put("dateOfPicture", changeValue);	
			}
		}
		
		// adding values to database
		if(forInsert.get(rsmd.getColumnName(2)) != null) {
			stmt.executeUpdate("update "+ Constant.mediainfo +" set " 
					+ rsmd.getColumnName(2) +  " = '" + forInsert.get(rsmd.getColumnName(2))  +
					"' where " + rsmd.getColumnName(1) + " = '" + fileIdentifier.fileLocation + "'");
		}else {
			notIncluedInTable.add(rsmd.getColumnName(2));
		}
		
		if(forInsert.get(rsmd.getColumnName(3)) != null) {
			stmt.executeUpdate("update "+ Constant.mediainfo +" set " 
					+ rsmd.getColumnName(3) +  " = '" + forInsert.get(rsmd.getColumnName(3))  +
					"' where " + rsmd.getColumnName(1) + " = '" + fileIdentifier.fileLocation + "'");
		}else {
			notIncluedInTable.add(rsmd.getColumnName(3));
		}
		
		if(!notIncluedInTable.isEmpty()) {
			System.out.println(fileIdentifier.fileLocation  + " Items that are not included in 'familyinfo' table: " + notIncluedInTable.toString());
		}
		
		return true;
		
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * @fileIdentifier: Object of class FileIdentifier media which contains given list of people
	 * @people: List of people who are present in a particular media
	 * This method records a set of people that appear in the given media file.
	 * @return: Returns true if all the people in the media file have been successfully recorded
	 */
	@Override
	public Boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people) {

		if(fileIdentifier == null || people.isEmpty() ) {
			return false;
		}
		
		for(PersonIdentity p1 : people) {
			try {
				stmt.executeUpdate("INSERT INTO "+ Constant.peoplemediainfo +" VALUES ('"
									+ fileIdentifier.fileLocation + "' , '" + p1.id + "')");
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	
	}

	/***
	 * @fileIdentifier : Object of class FileIdentifier whose tag needs to be stored
	 * @tag: Tag for a particular media file
	 * This method records tags for a media file.
	 * @return: Returns true if tags have been successfully recorded and false if not recorded
	 */
	@Override
	public Boolean tagMedia(FileIdentifier fileIdentifier, String tag) {
		
		if(fileIdentifier == null || tag == null || tag.equals("")) {
			return false;
		}
		
		try {
			stmt.executeUpdate("INSERT INTO "+ Constant.tagmediainfo +" VALUES ('"
								+ fileIdentifier.fileLocation + "' , '" + tag + "')");
			return true; 
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			return false;
		}
	}

}
