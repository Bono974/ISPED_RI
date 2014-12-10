import java.util.ArrayList;
import java.util.List;


public class Application {
	
	public static void main(String[] args) {
		
		List<IndexerAbs>listIndexer = new ArrayList<IndexerAbs>();
		listIndexer.add(new EnIndexerImpl(indexLocation, source));
		listIndexer.add(new FrIndexerImpl(indexLocation, source));
		
		InterfacePrincipaleAbs ipa = new InterfaceConsoleImpl(listIndexer);
		ipa.run();
	}

}
