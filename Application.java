package projet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import projet.ihm.InterfaceConsoleImpl;
import projet.ihm.InterfaceGUI;
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

		String choix = "0";
		InterfacePrincipaleAbs ipa = null;
		while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3") ) {
			System.out.println("choix de l'interface :");
			System.out.println("1 - Console");
			System.out.println("2 - Interface graphique");
			System.out.println("3 - Quitter");
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(System.in));
			choix = br.readLine();
			switch (choix) {
			case "1":
				System.out.println("Lancement de la console");
				ipa = new InterfaceConsoleImpl(listIndexer);
				break;
			case "2":
				System.out.println("Lancement de l'interface graphique");
				ipa = new InterfaceGUI(listIndexer);
				break;
			case "3":
				System.out.println("Au revoir, A bientôt!");
				System.exit(0);
				break;
			default:
				System.out.println("Votre choix est invalide, veuillez choisir entre 1, 2 et 3	");
				break; 
			}
		}
		ipa.run();
	}

}
