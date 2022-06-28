import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class mainUI {
	
	public static void main(String[] args) {
			
		String userCommand = "";
        Scanner userInput = new Scanner(System.in);

		PersonIdentity pI = new PersonIdentity();
		FileIdentifier fI = new FileIdentifier();
		BiologicalRelation bR = new BiologicalRelation();
		Genealogy gC = new Genealogy();
	
		List<String> names = new ArrayList<>();
		List<String> fileLocations = new ArrayList<>();
		PersonIdentity person;
		PersonIdentity child;
		PersonIdentity partner;
		PersonIdentity person1;
		PersonIdentity person2;
		
		Set<PersonIdentity> pSet = new HashSet<>();
		Set<FileIdentifier> fSet = new HashSet<>();
		List<FileIdentifier> fList = new ArrayList<>();
		
		List<String> noteAndRefList = new ArrayList<>();
		
		FileIdentifier fileLocation;
		
		Map<String, String> attributesForPerson = new HashMap<>();
		attributesForPerson.put("dob", null);
		attributesForPerson.put("birthlocation", null);
		attributesForPerson.put("dod", null);
		attributesForPerson.put("deathlocation", null);
		attributesForPerson.put("gender", null);
		attributesForPerson.put("occupation", null);
		
		Map<String, String> mediaAttributes = new HashMap<>();
		attributesForPerson.put("dateOfPicture", null);
		attributesForPerson.put("Location", null);
		
		do {
			
			System.out.println("Select one: pI for PersonIdentity  / fI for FileIdentifier / gC for Genealogy");
			
			System.out.println("To show table enter: showTable");
			
			userCommand = userInput.next();
			
			if(userCommand.equalsIgnoreCase("pI")) {
				System.out.println("Select one: addPerson / recordAttributes / recordReference / recordNote "
						+ "/ recordChild / recordPartnering / recordDissolution");
				
				userCommand = userInput.next();
				
				if(userCommand.equalsIgnoreCase("addperson")) {
					
					System.out.println("Input person name: ");
					userCommand = userInput.next();
					pI.addPerson(userCommand);
				
				}else if (userCommand.equalsIgnoreCase("recordAttributes")) {
					
					System.out.println("Enter name to whom you want to add atrributes: ( " + names.toString() + " )");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter date of birth in (YYYY-MM-DD) format: ");
					attributesForPerson.put("dob", userInput.next());
					
					System.out.println("Enter birth location : ");
					attributesForPerson.put("birthlocation", userInput.next());
					
					System.out.println("Enter date of death in (YYYY-MM-DD) format : ");
					attributesForPerson.put("dod", userInput.next());
					
					System.out.println("Enter death location: ");
					attributesForPerson.put("deathlocation", userInput.next());
					
					System.out.println("Enter gender : ");
					attributesForPerson.put("gender", userInput.next());
					
					System.out.println("Enter occupation : ");
					attributesForPerson.put("occupation", userInput.next());
					
					pI.recordAttributes(person, attributesForPerson);
					
				}else if(userCommand.equalsIgnoreCase("recordReference")) {
					
					System.out.println("Enter name to whom you want to add reference: ( " + names.toString() + " )");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter reference ");
					userCommand = userInput.next();
					
					pI.recordReference(person, userCommand);
					
				}else if(userCommand.equalsIgnoreCase("recordNote")) {
					
					System.out.println("Enter name to whom you want to add note: ( " + names.toString() + " )");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter note ");
					userCommand = userInput.next();
					
					pI.recordNote(person, userCommand);
					
				}else if(userCommand.equalsIgnoreCase("recordChild")) {
					
					System.out.println("Enter name to whom you want to add child information: ( " + names.toString() + " )");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter child name: " + names.toString() );
					userCommand = userInput.next();
					child = gC.findPerson(userCommand);
					
					
					pI.recordChild(person, child);
					
				}else if(userCommand.equalsIgnoreCase("recordPartnering")) {
					
					System.out.println("Enter name to whom you want to add partner: ( " + names.toString() + " )");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter partner name: " + names.toString() );
					userCommand = userInput.next();
					partner = gC.findPerson(userCommand);
					
					
					pI.recordPartnering(person, partner);
					
				}else if(userCommand.equalsIgnoreCase("recordDissolution")) {
					
					System.out.println("Enter name to whom you want to add dissolution: ( " + names.toString() + " )");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter dissolution name: " + names.toString() );
					userCommand = userInput.next();
					partner = gC.findPerson(userCommand);
					
					pI.recordDissolution(person, partner);
				
				}
			}else if(userCommand.equalsIgnoreCase("fI")) {
				System.out.println("Select one: addMediaFile / recordMediaAttributes / peopleInMedia / tagMedia");
				
				userCommand = userInput.next();
				
				if(userCommand.equalsIgnoreCase("addMediaFile")) {
					
					System.out.println("Input file location: ");
					userCommand = userInput.next();
					fI.addMediaFile(userCommand);
					
				}else if(userCommand.equalsIgnoreCase("recordMediaAttributes")) {
					
					System.out.println("Input file location to that you want to add attributes: (" + fileLocations.toString() + ")");
					userCommand = userInput.next();
					fileLocation = gC.findMediaFile(userCommand);
					
					System.out.println("Enter date of picture in (YYYY-MM-DD) format: ");
					mediaAttributes.put("dateOfPicture", userInput.next());
					
					System.out.println("Enter location: ");
					mediaAttributes.put("Location", userInput.next());
					
					fI.recordMediaAttributes(fileLocation, mediaAttributes);
					
				}else if (userCommand.equalsIgnoreCase("peopleInMedia")) {
					
					List<PersonIdentity> people = new ArrayList<>();
					
					System.out.println("Input file location to that you want to add people: (" + fileLocations.toString()+ ")");
					userCommand = userInput.next();
					fileLocation = gC.findMediaFile(userCommand);
					
					System.out.println("Enter name to whom you want to add reference: ( " + names.toString() + " )");
					do {
						userCommand = userInput.next();
						people.add(gC.findPerson(userCommand));
						System.out.println("continue or break");
						userCommand = userInput.next();
					}while(userCommand.equalsIgnoreCase("break"));
					
					fI.peopleInMedia(fileLocation, people);
					
				}else if(userCommand.equalsIgnoreCase("tagMedia")) {
					
					System.out.println("Input file location to that you want to add people: (" + fileLocations.toString()+ ")");
					userCommand = userInput.next();
					fileLocation = gC.findMediaFile(userCommand);
					
					System.out.println("Enter tag: ");
					userCommand = userInput.next();
					fI.tagMedia(fileLocation, userCommand);
				}
			}else if(userCommand.equalsIgnoreCase("gC")) {
				
				System.out.println("Select one: findPerson | findMediaFile | findRelation"
						+ " | descendents | ancestores | notesAndReferences | findMediaByTag | findMediaByLocation |"
						+ "findIndividualsMedia | findBiologicalFamilyMedia");
				
				userCommand = userInput.next();
				
				if(userCommand.equalsIgnoreCase("findPerson")) {

					System.out.println("Enter person name: ");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
				}else if(userCommand.equalsIgnoreCase("findMediaFile")) {
				
					System.out.println("Enter file location: ");
					userCommand = userInput.next();
					fI = gC.findMediaFile(userCommand);
					
				}else if(userCommand.equalsIgnoreCase("findRelation")) {
					
					System.out.println("Enter person1 name: ");
					userCommand = userInput.next();
					person1 = gC.findPerson(userCommand);
					
					System.out.println("Enter person2 name: ");
					userCommand = userInput.next();
					person2 = gC.findPerson(userCommand);
					
					bR = gC.findRelation(person1, person2);
					
					if(bR != null) {
						System.out.println("Counsinship: " + bR.cousinship + ", Removal: " + bR.separation);
					}
	
				}else if(userCommand.equalsIgnoreCase("descendents")) {
					
					System.out.println("Enter person name: ");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter generations: ");
					int generations = userInput.nextInt();
					
					pSet = gC.descendents(person, generations);
	
				}else if(userCommand.equalsIgnoreCase("descendents")) {
					
					System.out.println("Enter person name: ");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter generations: ");
					int generations = userInput.nextInt();
					
					pSet = gC.descendents(person, generations);
	
				}else if(userCommand.equalsIgnoreCase("ancestores")) {
					
					System.out.println("Enter person name: ");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					System.out.println("Enter generations: ");
					int generations = userInput.nextInt();
					
					pSet = gC.ancestores(person, generations);
	
				}else if(userCommand.equalsIgnoreCase("notesAndReferences")) {
					
					System.out.println("Enter person name: ");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					
					noteAndRefList = gC.notesAndReferences(person);
	
					System.out.println("List: " + noteAndRefList.toString());
						
				}else if(userCommand.equalsIgnoreCase("findMediaByTag")) {
					
					System.out.println("Enter tag: ");
					userCommand = userInput.next();
					String tag = userCommand;
					
					System.out.println("Enter start date: ");
					userCommand = userInput.next();
					String startDate = userCommand;
					
					System.out.println("Enter end date: ");
					userCommand = userInput.next();
					String endDate = userCommand;
					
					fSet = gC.findMediaByTag(tag, startDate, endDate);
					
					System.out.println(fSet);
						
				}else if(userCommand.equalsIgnoreCase("findMediaByLocation")) {
					
					System.out.println("Enter location: ");
					userCommand = userInput.next();
					String location = userCommand;
					
					System.out.println("Enter start date: ");
					userCommand = userInput.next();
					String startDate = userCommand;
					
					System.out.println("Enter end date: ");
					userCommand = userInput.next();
					String endDate = userCommand;
					
					fSet = gC.findMediaByTag(location, startDate, endDate);
				
				}else if(userCommand.equalsIgnoreCase("findIndividualsMedia")) {
					
					do {
						System.out.println("Enter person name: ");
						userCommand = userInput.next();
						person = gC.findPerson(userCommand);
						pSet.add(person);
						System.out.println("Continue | break");
						userCommand = userInput.next();
					}while(userCommand.equalsIgnoreCase("break"));
					
					System.out.println("Enter start date: ");
					userCommand = userInput.next();
					String startDate = userCommand;
					
					System.out.println("Enter end date: ");
					userCommand = userInput.next();
					String endDate = userCommand;
					
					fList = gC.findIndividualsMedia(pSet, startDate, endDate);
					
				}else if(userCommand.equalsIgnoreCase("findBiologicalFamilyMedia")) {
					

					System.out.println("Enter person name: ");
					userCommand = userInput.next();
					person = gC.findPerson(userCommand);
					
					fList = gC.findBiologicalFamilyMedia(person);
					
					System.out.println(fList);
				
				}
				
			}else if(userCommand.equalsIgnoreCase("showTable")) {
				
				
				
				System.out.println("Enter table name: (childinfo | familyinfo | mediainfo | noterefinfo"
						+ " | occupationinfo | partneringinfo | peoplemediainfo | tagmediainfo)" );
				
				String tableName = userInput.next();
				
				new ShowTable(pI, tableName);
				
			}
			
			System.out.println("Quit?");
			
			userCommand = userInput.next();
			
		}while(!userCommand.equalsIgnoreCase("quit"));
		
		userInput.close();
		
	}
	
}
