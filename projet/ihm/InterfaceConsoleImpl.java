package projet.ihm;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import projet.index.IndexerAbs;
import projet.recherche.Recherche;
import projet.recherche.ScoreEtChemin;

public class InterfaceConsoleImpl extends InterfacePrincipaleAbs{

	/**
	 * Constructeur
	 * @param lIndexer
	 */
	public InterfaceConsoleImpl(List<IndexerAbs> lIndexer) {
		super(lIndexer, "Console");
	}

	/**
	 * Methode qui permet de saisir et retourner le chemin ou l'utilisateur veut creer l'index
	 * @return
	 */
	public static String cheminIndex(){
		/**
		 * Déclaration buffer pour recuperer saisie clavier de l'utilisateur
		 */
		BufferedReader br3 = new BufferedReader(
				new InputStreamReader(System.in));
		/**
		 * Declaration du chemin de l'index
		 */
		String indexLocation = null;
		try {
			System.out.println("Entrez le chemin ou vous voulez creer l'index : (e.g. /tmp/index or c:\\temp\\index)");
			indexLocation = br3.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indexLocation;
	}

	/**
	 * Methode qui permet de saisir et retourner le chemin des documents que l'utilisateur veut ajouter a l'index
	 * @return
	 */
	public static String cheminSource(){
		/**
		 * Declaration buffer pour recuperer saisie clavier de l'utilisateur
		 */
		BufferedReader br4 = new BufferedReader(
				new InputStreamReader(System.in));
		/**
		 * Declaration du chemin des documents a ajouter
		 */
		String indexSource = null;
		System.out.println("Entrez le chemin complet des documents a ajouter dans l'index: (e.g. /tmp/index or c:\\temp\\index)");
		try {
			indexSource = br4.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indexSource;
	}

	public void run() {
		// Local variable
		String swValue;
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		Recherche rechercheur = new Recherche();
		while(true){
			System.out.println("===================[     MENU     ]======================");
			System.out.println("| Faites votre choix:                                   |");
			System.out.println("|        1. Indexer                                     |");
			System.out.println("|        2. Rechercher                                  |");
			System.out.println("|        3. Quitter                                     |");
			System.out.println("=========================================================");
			try {
				swValue = br.readLine();

				switch (swValue) {
				case "1":
					String source = null;
					try {
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(System.in));
						String langue_choisie = "";
						System.out.println("Bienvenue dans l'indexation");
						System.out.println("Choisissez une langue parmi:");
						String langues = "";
						for(IndexerAbs cur: this.lIndexer)
							langues += cur.getLangue() +" ";
						System.out.println(langues);

						langue_choisie = br2.readLine();

						source = cheminSource();
						boolean retour = false;
						for(IndexerAbs cur: this.lIndexer) {
							if (cur.getLangue().equalsIgnoreCase(langue_choisie)) {
								cur.action(source);
								System.out.println("Indexation du répertoire "+source+" effectuée.");
								retour = true;
								break;
							}
						}
						if (!retour) System.out.println("Aucune indexation n'a été effectuée.");
						break;
					}
					catch(FileNotFoundException e) {
						System.out.println("Le fichier "+source+" n'existe pas.");
						break;
					}
				case "2":
					BufferedReader br3 = new BufferedReader(
							new InputStreamReader(System.in));
					String mot_rech = "";
					System.out.println("Bienvenue dans la recherche");
					System.out.println("Saisissez votre liste de mots.");

					mot_rech=br3.readLine();
					List<String>motsARechercher = new ArrayList<String>();
					motsARechercher.add(mot_rech);
					List<ScoreEtChemin> resultatsRecherche;
					
					// On vérifie qu'un mot a été saisie ou bien qu'il y a autre chose que des caractères blancs
					if (motsARechercher == null || motsARechercher.equals("") || mot_rech.trim().equals("") ) {
						System.out.println("Aucun mot à rechercher n'a été saisie.");
						break;
					}
					else {
						resultatsRecherche = rechercheur.search(this.lIndexer, motsARechercher);
						if (!(resultatsRecherche == null || resultatsRecherche.isEmpty())) {
							for (ScoreEtChemin cur: resultatsRecherche) 
								System.out.println("Document : "+cur.getScore()+" "+cur.getChemin());
						} else 
							System.out.println("Aucun document ne correspond à votre recherche.");
						break;
					}
				case "3":
					System.out.println("Au revoir, A bientôt!");
					System.exit(0);
					break;
				case "q":
					System.exit(0);
					break;
				case "h":
					System.out.println("AIDE!!!!");
					break;
				default:
					System.out.println("Votre choix est invalide, recommencez!");
					break; 
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

}
