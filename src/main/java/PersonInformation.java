import java.util.Map;

interface PersonInformation {

	PersonIdentity addPerson(String name);
	
	Boolean recordAttributes( PersonIdentity person, Map<String, String> attributes );
	
	Boolean recordReference( PersonIdentity person, String reference );
	
	Boolean recordNote( PersonIdentity person, String note );
	
	Boolean recordChild( PersonIdentity parent, PersonIdentity child );
	
	Boolean recordPartnering( PersonIdentity partner1, PersonIdentity partner2 );
	
	Boolean recordDissolution( PersonIdentity partner1, PersonIdentity partner2 );
	
}
