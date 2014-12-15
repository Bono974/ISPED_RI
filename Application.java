package projet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import projet.ihm.InterfaceConsoleImpl;
//import projet.ihm.InterfaceGUI;
import projet.ihm.InterfacePrincipaleAbs;
import projet.index.EnIndexerImpl;
import projet.index.FrIndexerImpl;
import projet.index.IndexerAbs;


public class Application {
	
	private static String indexLocationFR = "/home/yoann/Documents/IndexRI/FR/";
	private static String indexLocationEN = "/home/yoann/Documents/IndexRI/EN/";
	
	public static void main(String[] args) throws IOException {
		
		List<IndexerAbs>listIndexer = new ArrayList<IndexerAbs>();
		listIndexer.add(new EnIndexerImpl(indexLocationEN));
		listIndexer.add(new FrIndexerImpl(indexLocationFR));
		
		InterfacePrincipaleAbs ipa = new InterfaceConsoleImpl(listIndexer);
		//InterfacePrincipaleAbs ipa = new InterfaceGUI(listIndexer);
		ipa.run();
	}

}
