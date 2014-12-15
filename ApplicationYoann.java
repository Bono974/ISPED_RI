package projet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import projet.index.EnIndexerImpl;
import projet.index.FrIndexerImpl;
import projet.index.IndexerAbs;
import projet.recherche.Recherche;
import projet.recherche.ScoreEtChemin;


public class ApplicationYoann {

	//private static String source = "/home/yoann/Documents/CR3/";
	private static String indexLocationFR = "/home/yoann/Documents/IndexRI/FR/";
	private static String indexLocationEN = "/home/yoann/Documents/IndexRI/EN/";

	public static void main(String[] args) throws IOException {

		List<IndexerAbs>listIndexer = new ArrayList<IndexerAbs>();
		listIndexer.add(new EnIndexerImpl(indexLocationEN));
		listIndexer.add(new FrIndexerImpl(indexLocationFR));

		/*
		// Indexation
		//La première étape de la méthode est de retrouver l'indexer associé à la langue
		for (IndexerAbs elementIndex: listIndexer) {

			if (elementIndex.getLangue().equalsIgnoreCase("Fr")) {
				// On index les documents
				elementIndex.action("/home/yoann/Documents/CR3/");
			}
		}
		*/
		
		// Recherche
		Recherche rechercheur = new Recherche();
		List<String>motsARechercher = new ArrayList<String>();
		//motsARechercher.add("transplantation");
		//motsARechercher.add("oedème");
		motsARechercher.add("greffe");
		//motsARechercher.add("plop");
		motsARechercher.add("bourgeons");

		List<ScoreEtChemin> resultatsRecherche = null;
		//resultatsRecherche = rechercheur.search(listIndexer, "Fr", motsARechercher);
		resultatsRecherche = rechercheur.search(listIndexer, motsARechercher);

		//Lecture des résultats
		System.out.println("Début de la recherche");
		if (!(resultatsRecherche == null)) {
			for (ScoreEtChemin cur: resultatsRecherche) {
				
				System.out.println("Document : "+cur.getScore()+" "+cur.getChemin());
			}
		}
		

		//InterfacePrincipaleAbs ipa = new InterfaceConsoleImpl(listIndexer);
		//ipa.run();
	}

}
