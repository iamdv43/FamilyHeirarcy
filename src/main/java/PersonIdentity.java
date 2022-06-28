import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersonIdentity implements PersonInformation, Constant{

	int id; 
	int forId = 1;	
	String name;
	Statement stmt;
	
	List<String> pValues = new ArrayList<>();
	
	Map<String, List<String>> possibleValues = new HashMap<>();
	
	public PersonIdentity() {
		
		ConnectiontoDB cDB = new ConnectiontoDB();
		stmt =  cDB.connectToDB();

		//create a HashMap for possible key values for recordAttribute method
		pValues.add("dateofbirth");
		pValues.add("birthdate");
		pValues.add("dob");
		possibleValues.put("dob",pValues);
		
		pValues = new ArrayList<>();
		pValues.add("dateofdeath");
		pValues.add("deathdate");
		pValues.add("dod");
		possibleValues.put("dod", pValues);
		
		pValues = new ArrayList<>();;
		pValues.add("birthlocation");
		pValues.add("locationofbirth");
		pValues.add("dobl");
		
		possibleValues.put("birthlocation", pValues);
		
		pValues = new ArrayList<>();;
		pValues.add("deathlocation");
		pValues.add("locationofdeath");
		pValues.add("dodl");
		possibleValues.put("deathlocation", pValues);
		
		pValues = new ArrayList<>();;
		pValues.add("gender");
		possibleValues.put("gender", pValues);
		
		pValues = new ArrayList<>();;
		pValues.add("occupation");
		possibleValues.put("occupation", pValues);
		
		
	}

	public PersonIdentity(int forId, String name) {
	
		this.id = forId;
		this.name = name;
		
	}
	
	
	/**
	 * This method add person to the database
	 *  return object of the class that contains id and name
	 *  
	 *  @name name of the person
	 *  @return object of the class that contain name and id of the person
	 */
	@Override
	public PersonIdentity addPerson(String name) {
		int id = 0;
		
		if(name == null || name.equals("")) {
			return null;
		}
		
		try {
			
			//for finding next id 
			ResultSet getID = stmt.executeQuery("select count(id) from " + Constant.familyinfo);
			
			while(getID.next()) {
				id = getID.getInt(1) + 1;
			}
			
		} catch (SQLException e1) {
		
			e1.printStackTrace();
		
		}
		
		
		PersonIdentity person = new PersonIdentity(id, name);
		
		try {
			
			ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.familyinfo).getMetaData();
			
			stmt.executeUpdate("INSERT INTO "+ Constant.familyinfo +" ( " + rsmd.getColumnName(1) +
					", " + rsmd.getColumnName(2) + ") VALUES (" + person.id + ",  '" + person.name + "')");
		
			return person; 
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		
			return null;
		}
		
		
	}

	
	/**
	 * This method records birth date and location, death date and location, gender and occupation
	 * 
	 * @person object of the class, on which attributes will be recorded
	 * @attributes HashMap that has key as column
	 * @return true if any of the attribute store successfully with unsaved column values or false
	 */
	@Override
	public	Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) {
		
		if(person == null) {
			return false;
		}
		
		try {
			
			ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.familyinfo).getMetaData();
			
			Map<String, String> forInsert = new HashMap<>();
			
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
			
			// for date of birth
			if(forInsert.get("dob") != null) {
				if(forInsert.get("dob").length() == 4) { //Only year
					changeValue = forInsert.get("dob") + "-00-00";
					forInsert.put("dob", changeValue);
				}
				
				if(forInsert.get("dob").length() == 7) { //year + month
					changeValue = forInsert.get("dob") + "-00";
					forInsert.put("dob", changeValue);					
				}
				if(forInsert.get("dob").length() == 5) { //month + date
					changeValue = null;
					forInsert.put("dob", changeValue);	
				}
				if(forInsert.get("dob").length() <= 3 || forInsert.get("dob").length() == 6 || 
						forInsert.get("dob").length() == 8 || forInsert.get("dob").length() == 9 ) { // date or month and any invalid input
					changeValue = null;
					forInsert.put("dob", changeValue);	
				}
			}
			
			// for date of death
			if(forInsert.get("dod") != null) {
				if(forInsert.get("dod").length() == 4) {
					changeValue = forInsert.get("dod") + "-00-00";
					forInsert.put("dod", changeValue);
				}
				
				if(forInsert.get("dod").length() == 7) {
					changeValue = forInsert.get("dod") + "-00";
					forInsert.put("dod", changeValue);					
				}
				if(forInsert.get("dod").length() == 5) {
					changeValue = null;
					forInsert.put("dod", changeValue);	
				}
				if(forInsert.get("dod").length() <= 3 || forInsert.get("dod").length() == 6 ||
						forInsert.get("dod").length() == 8 || forInsert.get("dod").length() == 9 ) {
					changeValue = null;
					forInsert.put("dod", changeValue);	
				}
			}

			// checking date of birth is less than date of death
			if(forInsert.get("dob") != null && forInsert.get("dod") != null) {
			
				if(forInsert.get("dob").length() == 10 && forInsert.get("dod").length() == 10) {
					Date start = null, end = null;
					try {
						start = new SimpleDateFormat("yyyy-mm-dd").parse(forInsert.get("dob"));
						end = new SimpleDateFormat("yyyy-mm-dd").parse(forInsert.get("dod"));
						
						if(start.compareTo(end) > 0) {
							System.out.println("please add correct values");
							forInsert.put("dob", null);
							forInsert.put("dod", null);
						}
					} catch (ParseException e) {
						e.printStackTrace();
						return false;
					}
				}

			}
			
			 
			// inserting date of birth
			if(forInsert.keySet().contains(rsmd.getColumnName(3))){ 
			   if(forInsert.get(rsmd.getColumnName(3)) != null) {
				   stmt.executeUpdate("update "+ Constant.familyinfo +" set " 
						+ rsmd.getColumnName(3) +  " = '" + forInsert.get(rsmd.getColumnName(3))  +
						"' where " + rsmd.getColumnName(1) + " = " + person.id);
			   }else {
				   notIncluedInTable.add(rsmd.getColumnName(3));
			   }
			}
			
			// inserting birth location
			if(forInsert.keySet().contains(rsmd.getColumnName(4))) {
				if(forInsert.get(rsmd.getColumnName(4)) != null) {
					stmt.executeUpdate("update "+ Constant.familyinfo +" set " 
							+ rsmd.getColumnName(4) +  " = '" + forInsert.get(rsmd.getColumnName(4)) +
							"' where " + rsmd.getColumnName(1) + " = " + person.id);
				}else {
					notIncluedInTable.add(rsmd.getColumnName(4));
				}
			}
		
			//inserting date of death
			if(forInsert.keySet().contains(rsmd.getColumnName(5))) {
				if(forInsert.get(rsmd.getColumnName(5)) != null) {
					stmt.executeUpdate("update "+ Constant.familyinfo +" set " 
							+ rsmd.getColumnName(5) +  " = '" + forInsert.get(rsmd.getColumnName(5)) +
							"' where " + rsmd.getColumnName(1) + " = " + person.id);
				}else {
					notIncluedInTable.add(rsmd.getColumnName(5));
				}
			}
			
			// inserting death location
			if(forInsert.keySet().contains(rsmd.getColumnName(6))) {
				if(forInsert.get(rsmd.getColumnName(6)) != null) {
					stmt.executeUpdate("update "+ Constant.familyinfo +" set " 
							+ rsmd.getColumnName(6) +  " = '" + forInsert.get(rsmd.getColumnName(6)) +
							"' where " + rsmd.getColumnName(1) + " = " + person.id);
				}else {
					notIncluedInTable.add(rsmd.getColumnName(6));
				}
			}
			
			// inserting gender
			if(forInsert.keySet().contains(rsmd.getColumnName(7))) {
				if(forInsert.get(rsmd.getColumnName(7)) != null) {
					stmt.executeUpdate("update "+ Constant.familyinfo +" set " 
							+ rsmd.getColumnName(7) +  " = '" + forInsert.get(rsmd.getColumnName(7)) +
							"' where " + rsmd.getColumnName(1) + " = " + person.id);
				}else {
					notIncluedInTable.add(rsmd.getColumnName(7));
				}
			}
			
			// inserting occupation in occupation info table
			if(forInsert.keySet().contains("occupation")) {
				if(aKeys.contains("occupation")) {
					if(attributes.get("occupation") != null) {
						stmt.executeUpdate("Insert into occupationinfo values ( " +  person.id + ", '" +  attributes.get("occupation") + " ')");
					}else {
						notIncluedInTable.add("occupation");
					}
				}
			}
			
			if(!notIncluedInTable.isEmpty()) {
				System.out.println(person.id + " Items that are not included in 'familyinfo' table: " + notIncluedInTable.toString());
			}


			return true;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}


	/**
	 * This method record references in noterefinfo table.
	 * @person object of the person on which reference will be stored
	 * @reference 
	 * @return true if recorded successfully else false
	 */
	@Override
	public Boolean recordReference(PersonIdentity person, String reference) {
		
		if(person == null || reference == null || reference.equals("")) {
			return false;
		}
		
		
		
		try {
			
			ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.noteinfo).getMetaData();
			
			stmt.executeUpdate("INSERT INTO "+ Constant.noteinfo +" (" + rsmd.getColumnName(1) + ", " + rsmd.getColumnName(2) +
					", " + rsmd.getColumnName(3) + ") VALUES (" + person.id + ",  '" + reference + "', " + false + ")");
		
			return true; 
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		
			return false;
		}
		
	}

	/**
	 * This method record notes in noterefinfo table.
	 * @person object of the person on which reference will be stored
	 * @reference 
	 * @return true if recorded successfully else false
	 */
	@Override
	public Boolean recordNote(PersonIdentity person, String note) {
	
		if(person == null || note == null || note.equals("")) {
			return false;
		}
		
		try {
			
			ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.noteinfo).getMetaData();
			
			
			// Here, column 3 (checkn) for the checking whether it is note or reference.
			stmt.executeUpdate("INSERT INTO "+ Constant.noteinfo +" (" + rsmd.getColumnName(1) + ", " + rsmd.getColumnName(2) +
					", " + rsmd.getColumnName(3) + ") VALUES (" + person.id + ",  '" + note + "', " + true + ")");
		
			return true; 
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		
			return false;
		}
	
	}

	
	/**
	 * @parent : Object of class PersonIdentity with information of parent
	 * @child :  Object of class PersonIdentity with information of child
	 * This method is used to record child info.
	 * @return : Returns true if relation between parent and child have been successfully recorded and returns false if not.
	 */
	@Override
	public Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
		
		if(parent == null || child == null) {
			return false;
		}
		
		try {
			
			//return false if parent and child have same id.
			if(parent.id == child.id ) {
				return false;
			}
			
			List<Integer> checkParent = new ArrayList<>();
			
			
			//Check the child has at most two parents.
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + Constant.childinfo + " where child = " + child.id );
			
			while(rs.next()) {
				checkParent.add(rs.getInt(1));
			}
			
			for(int i : checkParent) {
				if(parent.id == i) {
					return false;
				}
				
			}
			
			if (checkParent.size() >= 2 ) {
				return false;
			}
			
			
			
			
			//check, there is no bad data available for the child in the table 
			ResultSet rs1 = stmt.executeQuery("SELECT * FROM " + Constant.childinfo + " where child = " + parent.id );
			checkParent = new ArrayList<>();
			
			while(rs1.next()) {
				checkParent.add(rs1.getInt(1));
			}
			
			for(int i : checkParent) {
				if (i == child.id) {
					return false;
				}
					
			}
			
			ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.childinfo).getMetaData();
			
			stmt.executeUpdate("INSERT INTO "+ Constant.childinfo +" (" + rsmd.getColumnName(1) + ", " + rsmd.getColumnName(2) + ") VALUES (" + parent.id + ",  '" + child.id + "')");
		
			return true; 
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		
			return false;
		}
		
	}

	
	/** 
	 * @partner1 : Object of class PersonIdentity with information of Partner 1
	 * @partner2 :  Object of class PersonIdentity with information of Partner 2
	 * This method record partnering relation between partner 1 and partner 2
	 * @return : Returns true if partnering relation between partner 1 and partner 2 have been successfully recorded and returns false if not recorded
	 */
		@Override
		public Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) {
		
			if(partner1 == null || partner2 == null) {
				return false;
			}
			
		try {
			
			
			
			ResultSet rs = stmt.executeQuery("select * from " + Constant.partneringinfo + " where partner = " + partner1.id);
			
			while(rs.next()) {
				if(partner2.id == rs.getInt("id"))
					return false;
			}
			

			rs = stmt.executeQuery("select * from " + Constant.partneringinfo + " where id = " + partner1.id);
			
			while(rs.next()) {
				if(partner2.id == rs.getInt("partner"))
					return false;
			}
			
			rs = stmt.executeQuery("select * from " + Constant.partneringinfo + " where id = " + partner1.id + " or id = " + partner2.id
					+ " or partner = " + partner1.id + " or partner = " + partner2.id);
			
			while(rs.next()) {
				int s = rs.getInt("dissolution");
				if(s < 1) {
					return false;
				}
			}
			
			ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.partneringinfo).getMetaData();
			
			//Records partnering relation between partner 1 and partner 2
			stmt.executeUpdate("INSERT  INTO "+ Constant.partneringinfo +
					" (" + rsmd.getColumnName(1) + ", " + rsmd.getColumnName(2) + ") VALUES (" + partner1.id + ",  '" + partner2.id + "')");
		
			return true; 
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		
			return false;
		}
	}
	

	
	/**
	 * This method record dissolution.
	 * 
	 * @partner1 : Object of class PersonIdentity with information of Partner 1
	 * @partner2 :  Object of class PersonIdentity with information of Partner 2
	 * This method records dissolution relation between partner 1 and partner 2
	 * @return : Returns true if dissolution relation between partner 1 and partner 2 have been successfully recorded and returns false if not recorde
	 */
		@Override
		public Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2) {

			if(partner1 == null || partner2 == null) {
				return false;
			}

		boolean flag = false;
		boolean flag1 = false;
		try {
			//To check if there is partnering relation between partner 1 and partner 2. If yes, then only performs dissolution
			ResultSet rs = stmt.executeQuery("select * from " + Constant.partneringinfo + " where id = " + partner1.id + " or id = " + partner2.id );
			
			while(rs.next()) {
				if(partner2.id == rs.getInt("partner"))
					flag = true;
				if(partner1.id == rs.getInt("partner")) {
					
					flag1 = true;
				}	
			}
			
			if(flag) {
				ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.partneringinfo).getMetaData();
				
				//Recording dissolution information between partner 1 and partner 2
				stmt.executeUpdate("update "+ Constant.partneringinfo +" set " + rsmd.getColumnName(3) 
				+ " = '" + partner2.id + "' where  " + rsmd.getColumnName(1) + " = " + partner1.id);
			
				return true; 
			
			}else if(flag1){
				ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + Constant.partneringinfo).getMetaData();
				
				//Recording dissolution information between partner 1 and partner 2
				stmt.executeUpdate("update "+ Constant.partneringinfo +" set " + rsmd.getColumnName(3) 
				+ " = '" + partner1.id + "' where  " + rsmd.getColumnName(1) + " = " + partner2.id);
				return true;
			}
			else {
				return false;
			}
			
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		
			return false;
		}
		
	}
		
}
