package projet.ihm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import projet.index.IndexerAbs;
import projet.recherche.Recherche;
import projet.recherche.ScoreEtChemin;

public class InterfaceConsoleImpl extends InterfacePrincipaleAbs{


	public InterfaceConsoleImpl(List<IndexerAbs> lIndexer) {
		super(lIndexer, "Console");
	}

	public static String cheminIndex(){


		BufferedReader br3 = new BufferedReader(
				new InputStreamReader(System.in));
		String indexLocation = null;
		try {
			System.out.println("Entrez le chemin ou vous voulez creer l'index : (e.g. /tmp/index or c:\\temp\\index)");
			indexLocation = br3.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Scanner sc3 = new Scanner(System.in);
		return indexLocation;

	}

	public static String cheminSource(){


		BufferedReader br4 = new BufferedReader(
				new InputStreamReader(System.in));
		String indexSource = null;
		System.out.println("Entrez le chemin complet des documents a ajouter dans l'index: (e.g. /tmp/index or c:\\temp\\index)");
		try {
			indexSource = br4.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indexSource;

	}

	public void run() {
		// Local variable
		String swValue;

		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		//List<IndexerAbs>listIndexer = new ArrayList<IndexerAbs>();
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

					String source = cheminSource();
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
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

}
