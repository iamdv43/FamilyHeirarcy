import java.util.List;
import java.util.Map;

public interface MediaInformation {
	
	FileIdentifier addMediaFile( String fileLocation );
	
	Boolean recordMediaAttributes( FileIdentifier fileIdentifier, Map<String, String> attributes );
	
	Boolean peopleInMedia( FileIdentifier fileIdentifier, List<PersonIdentity> people );
	
	Boolean tagMedia( FileIdentifier fileIdentifier, String tag );
	
}
