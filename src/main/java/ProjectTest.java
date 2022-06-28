import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ProjectTest {

	Map<String, String> attributes = new HashMap<String, String>();
	Map<String, String> attributesFI = new HashMap<String, String>();
	List<PersonIdentity> personList = new ArrayList<>();
	List<FileIdentifier> fileLList = new ArrayList<>();
	List<String> nameList = new ArrayList<>();
	List<String> fileList = new ArrayList<>();
	Set<PersonIdentity> personSet = new LinkedHashSet<>();
	Set<FileIdentifier> fileSet = new LinkedHashSet<>();
	
	
	@Test
	void program() {
	
		//----------------------------------
		/*
			For PersonIdentity; 
		*/
		//----------------------------------
		
		PersonIdentity pI = new PersonIdentity();
		
		//------------addPerson--------
		
		PersonIdentity p1 = pI.addPerson("a");
		
		PersonIdentity p2 = pI.addPerson("b");
		
		PersonIdentity p3 = pI.addPerson("c");
		
		PersonIdentity p4 = pI.addPerson("d");
		
		PersonIdentity p5 = pI.addPerson("e");
		
		PersonIdentity p6 = pI.addPerson("f");
		
		PersonIdentity p7 = pI.addPerson("g");
		
		PersonIdentity p8 = pI.addPerson("h");
		
		PersonIdentity p9 = pI.addPerson("i");
		
		PersonIdentity p10 = pI.addPerson("j");
		
		PersonIdentity p11 = pI.addPerson("k");
		
		PersonIdentity p12 = pI.addPerson("l");
		
		PersonIdentity p13 = pI.addPerson("m");
		
		//----------recordAttributes-------------------
		

		attributes = new HashMap<>();
		
		attributes.put("dob", "1994-07-08");
		attributes.put("dobL", "Bharuch");
		attributes.put("dod", "2075-04-24");
		attributes.put("dodL", "Bharuch");
		attributes.put("gender", "male");
		attributes.put("occupation", "manager");
		
		
		assertTrue(pI.recordAttributes(p1, attributes));
		
		attributes = new HashMap<>();
		
		attributes.put("dob", "1998");
		attributes.put("birthlocation", "Bharuch");
		attributes.put("dod", "2080-05-24");
		attributes.put("deathlocation", "Bharuch");
		attributes.put("gender", "female");
		attributes.put("occupation", "manager");
		
		
		assertTrue(pI.recordAttributes(p2, attributes));
		
		attributes = new HashMap<>();
		
		attributes.put("dob", "1993-01-01");
		attributes.put("birthlocation", "Halifax");
		attributes.put("dod", "2050-08-24");
		attributes.put("deathlocation", "Toronto");
		attributes.put("gender", "male");
		attributes.put("occupation", "o3");
		
		assertTrue(pI.recordAttributes(p3, attributes));
		
		attributes = new HashMap<>();
		
		attributes.put("dob", "2000");
		attributes.put("dod", "1997-01-01");
		attributes.put("occupation", "o4");
		
		assertTrue(pI.recordAttributes(p4, attributes));
		
		
		attributes = new HashMap<>();
		attributes.put("occupation", "o5");
		
		assertTrue(pI.recordAttributes(p5, attributes));
		
		attributes = new HashMap<>();
		attributes.put("occupation", "o5_1");
		
		assertTrue(pI.recordAttributes(p5, attributes));
		
		//---------------recordReference and recordNote---------------
		
		assertTrue(pI.recordReference(p1, "reference1"));
		assertTrue(pI.recordNote(p1, "note1"));
		assertTrue(pI.recordReference(p1, "reference2"));
		assertTrue(pI.recordReference(p2, "ref1"));
		assertTrue(pI.recordNote(p2, "note2"));
		assertTrue(pI.recordReference(p3, "reference3"));
		assertTrue(pI.recordNote(p3, "note3"));
		assertTrue(pI.recordReference(p4, "reference4"));
		assertTrue(pI.recordReference(p4, "ref4"));
		assertTrue(pI.recordNote(p1, "note11"));
		
		assertFalse(pI.recordNote(p1, ""));
		
		//--------------recordChild-------------------------------
		
		assertTrue(pI.recordChild(p4, p1));
		assertTrue(pI.recordChild(p4, p2));
		assertTrue(pI.recordChild(p4, p3));
		assertTrue(pI.recordChild(p7, p5));
		assertTrue(pI.recordChild(p7, p6));
		assertTrue(pI.recordChild(p9, p7));
		assertTrue(pI.recordChild(p9, p8));
		assertTrue(pI.recordChild(p10, p4));
		assertTrue(pI.recordChild(p10, p9));
		assertTrue(pI.recordChild(p12, p11));
		assertTrue(pI.recordChild(p13, p9));
		assertTrue(pI.recordChild(p13, p12));
		
		PersonIdentity p15 = pI.addPerson("l");
		
		assertFalse(pI.recordChild(p15, p9));
		assertFalse(pI.recordChild(p1, p4));
		assertFalse(pI.recordChild(p4, p1));
		assertFalse(pI.recordChild(p13, p13));
		
		
		//---------------recordPartnering and recordDissolution-------------------------
		
		assertTrue(pI.recordPartnering(p1, p2));
		assertTrue(pI.recordPartnering(p3, p4));
		assertTrue(pI.recordDissolution(p1, p2));
		assertTrue(pI.recordPartnering(p7, p10));
		
		assertFalse(pI.recordPartnering(p7, p10));
		assertFalse(pI.recordPartnering(p2, p1));
		
		
		//----------------------------------
		/*
				For FileIdentifier; 
		*/
		//----------------------------------
		
		FileIdentifier fI = new FileIdentifier();
		
		//--------------addMediaFile-----------------------
		
		
		FileIdentifier fI1 = fI.addMediaFile("a1.jpg");
		FileIdentifier fI2 = fI.addMediaFile("b2.jpg");
		FileIdentifier fI3 = fI.addMediaFile("c3.jpg");
		FileIdentifier fI4 = fI.addMediaFile("d4.jpg");
		FileIdentifier fI5 = fI.addMediaFile("e5.jpg");
		FileIdentifier fI6 = fI.addMediaFile("f6.jpg");
		FileIdentifier fI7 = fI.addMediaFile("h7.jpg");
		FileIdentifier fI8 = fI.addMediaFile("i8.jpg");
		FileIdentifier fI9 = fI.addMediaFile("j9.jpg");
		
		//-------------recordMediaAttributes-----------------
		
		attributesFI.put("dateOfPicture", "2021-11-30");
		attributesFI.put("Location", "l1");
		
		assertTrue(fI.recordMediaAttributes(fI1, attributesFI));
		
		attributesFI.put("dateOfPicture", "2021-12-1");
		attributesFI.put("Location", "l2");
		
		assertTrue(fI.recordMediaAttributes(fI2, attributesFI));

		attributesFI.put("dateOfPicture", null);
		attributesFI.put("Location", "l3");
		
		assertTrue(fI.recordMediaAttributes(fI3, attributesFI));
		
		attributesFI.put("dateOfPicture", "2020-01-30");
		attributesFI.put("Location", "l2");
		attributesFI.put("city", "Halifax");
		attributesFI.put("country", "Canada");
		
		assertTrue(fI.recordMediaAttributes(fI4, attributesFI));
		
		
		attributesFI = new HashMap<>();
		attributesFI.put("dateOfPicture", "2019-09-24");
		attributesFI.put("Location", "l5");
		
		assertTrue(fI.recordMediaAttributes(fI5, attributesFI));

		attributesFI = new HashMap<>();
		attributesFI.put("dateOfPicture", "2019-03-09");
		attributesFI.put("Location", "l2");
		
		assertTrue(fI.recordMediaAttributes(fI6, attributesFI));

		attributesFI = new HashMap<>();
		attributesFI.put("dateOfPicture", "2018-11-10");
		attributesFI.put("Location", "l7");
		
		assertTrue(fI.recordMediaAttributes(fI7, attributesFI));
		
		
		attributesFI = new HashMap<>();
		attributesFI.put("dateOfPicture", "2019-09-24");
		attributesFI.put("Location", "l2");
		
		assertTrue(fI.recordMediaAttributes(fI8, attributesFI));
		
		attributesFI = new HashMap<>();
		attributesFI.put("dateOfPicture", "2017-04-07");
		attributesFI.put("Location", "l1");
		
		assertTrue(fI.recordMediaAttributes(fI9, attributesFI));
		
		
		//------------------tagMedia------------------------
		
		assertTrue(fI.tagMedia(fI1, "t1"));
		assertTrue(fI.tagMedia(fI2, "t2"));
		assertTrue(fI.tagMedia(fI3, "t3"));
		assertTrue(fI.tagMedia(fI4, "t4"));
		assertTrue(fI.tagMedia(fI5, "t5"));
		assertTrue(fI.tagMedia(fI6, "t6"));
		assertTrue(fI.tagMedia(fI7, "t3"));
		assertTrue(fI.tagMedia(fI8, "t8"));
		assertTrue(fI.tagMedia(fI9, "t3"));
		
		//-------------------peopleInMedia---------------------
		
		
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		personList.add(p4);
		
		
		assertTrue(fI.peopleInMedia(fI1, personList));
		
		personList.clear();
		personList.add(p3);
		personList.add(p2);
		personList.add(p4);
		
		assertTrue(fI.peopleInMedia(fI2, personList));
		
		
		personList.clear();
		personList.add(p5);
		personList.add(p3);
		
		assertTrue(fI.peopleInMedia(fI4, personList));
		
		personList.clear();
		personList.add(p1);
		personList.add(p2);
		
		assertTrue(fI.peopleInMedia(fI9, personList));
		
		personList.clear();
		personList.add(p11);
		personList.add(p1);
		personList.add(p10);
		
		assertTrue(fI.peopleInMedia(fI8, personList));
		
		personList.clear();
		personList.add(p10);
		
		assertTrue(fI.peopleInMedia(fI3, personList));
		
		
		personList = new ArrayList<>();
		
		assertFalse(fI.peopleInMedia(fI2, personList));
		
		//-------------------Reporting-----------------------------
		
		BiologicalRelation bR = new BiologicalRelation();
		
		Genealogy g = new Genealogy();
		
		assertEquals("a",g.findName(p1));
		
		assertEquals("a1.jpg",g.findMediaFile(fI1));
		
		
		bR = g.findRelation( p1, p2);
		assertEquals(0, bR.getCousinship());
		assertEquals(0, bR.getRemoval());
		
		bR = g.findRelation( p1, p5);
		assertEquals(1, bR.getCousinship());
		assertEquals(1, bR.getRemoval());
		
		bR = g.findRelation( p5, p10);
		assertEquals(-1, bR.getCousinship());
		assertEquals(3, bR.getRemoval());
		
		bR = g.findRelation( p6, p8);
		assertEquals(0, bR.getCousinship());
		assertEquals(1, bR.getRemoval());
		
		bR = g.findRelation( p3, p13);
		assertEquals(null, bR);
		
		//--------------------------------------------------------------
		personSet = g.descendents(p10, 3);
		
		for(PersonIdentity p : personSet) {
			nameList.add(g.findName(p));
		}
		
		List<String> nList = Arrays.asList("d","i","a","b","c","g","h","e","f");
		
		assertEquals(nList,nameList);
		
		personSet = g.descendents(p10, -1);
		
		Set<PersonIdentity> personSet1 = new LinkedHashSet<>();
		
		assertEquals(personSet1, personSet);
		
		personSet = g.ancestores(p9, 1);	
		
		nameList.clear();
		
		for(PersonIdentity p : personSet) {
			nameList.add(g.findName(p));
		}
		
		nList = Arrays.asList("j","m");
		
		assertEquals(nList,nameList);
		
		//-----------------------------------------------------------------
		
		nList = new ArrayList<>();
		nList.add("reference1");
		nList.add("note1");
		nList.add("reference2");
		nList.add("note11");
		
		nameList = g.notesAndReferences(p1);
		
		assertEquals(nList,nameList);
		
		//----------------------------------------------------------------
		
		fileSet = g.findMediaByTag("t3", "2017-01-01", "2021-12-01");
		
		for(FileIdentifier i : fileSet) {
			fileList.add(g.findMediaFile(i));
		}
		
		nList = Arrays.asList("c3.jpg", "h7.jpg", "j9.jpg");
		
		assertEquals(nList, fileList);
		
		//-------------------------------------------------------------------
		
		fileSet = g.findMediaByLocation("Halifax", "2017-01-01", "2021-12-01");
		
		fileList.clear();
		
		for(FileIdentifier i : fileSet) {
			fileList.add(g.findMediaFile(i));
		}
		
		nList = Arrays.asList("d4.jpg");
		
		assertEquals(nList, fileList);
		
		//------------------------------------------------------
		
		Set<PersonIdentity> people = new LinkedHashSet<>();
		
		people.add(p10);
		people.add(p3);
		
		
		fileLList = g.findIndividualsMedia( people, "2017-01-01", "2021-12-01");
		
		fileList.clear();
		
		for(FileIdentifier i : fileLList) {
			fileList.add(g.findMediaFile(i));
		}
		
		nList = Arrays.asList("i8.jpg", "d4.jpg", "a1.jpg", "b2.jpg", "c3.jpg");
		
		assertEquals(nList, fileList);
		
		//---------------------------------------------------------------
		
		fileLList = g.findBiologicalFamilyMedia(p10);
		
		fileList.clear();
		
		for(FileIdentifier i : fileLList) {
			fileList.add(g.findMediaFile(i));
		}
		
		nList = Arrays.asList("a1.jpg", "b2.jpg");
		
		assertEquals(nList, fileList);
		
		//-------------------------------------------------------------------
		
		System.out.println("\n" + Constant.familyinfo);
		new ShowTable(pI, Constant.familyinfo);
		
		System.out.println("\n occupationinfo" );
		new ShowTable(pI, "occupationinfo");
		
		System.out.println("\n" + Constant.noteinfo);
		new ShowTable(pI, Constant.noteinfo);
			
		System.out.println("\n" + Constant.childinfo);
		new ShowTable(pI, Constant.childinfo);
		
		System.out.println("\n" + Constant.partneringinfo);
		new ShowTable(pI, Constant.partneringinfo);
		
		System.out.println("\n" + Constant.mediainfo);
		new ShowTable(pI, Constant.mediainfo);
		
		System.out.println("\n" + Constant.tagmediainfo);
		new ShowTable(pI, Constant.tagmediainfo);
		
		System.out.println("\n" + Constant.peoplemediainfo);
		new ShowTable(pI, Constant.peoplemediainfo);
		
		System.out.println("\n mediadatemethod");
		new ShowTable(pI, "mediadatemethod");
		
		
	}

}
