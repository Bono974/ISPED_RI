//package com.lucenetutorial.apps;
package projet.recherche;

import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import projet.index.IndexerAbs;
import projet.sgbd.ConnexionMySQL;
import projet.sgbd.Requete;

import java.io.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recherche {


	/**
	 * Cette attribut correspond à l'ontologie
	 * utilisée
	 * @see Ontologie
	 */
	private Ontologie onto;

	/**
	 * Getter de l'attribut onto
	 * @see Recherche#onto
	 * @return onto
	 */
	public Ontologie getOntologie() {
		return this.onto;
	}

	/**
	 * Cette Methode renvoie une liste de RetourDocument
	 * @param listeDocuments
	 * @return Une liste de RetourDocument
	 * @see RetourDocument
	 */
	public List<RetourDocument> rechercheSGDB(List<ScoreEtChemin> listeDocuments) {


		ConnexionMySQL maConnexion = new ConnexionMySQL();

		// Chargement du driver MySQL
		maConnexion.chargementDriver();

		// Création de la connexion avec la base
		if (maConnexion.initConnexionMySQL() == 1) {
			System.out.println("La base de donnée n'est pas accessible.");
			return null;
		}

		// Création du Statement
		maConnexion.creationStatement();

		Requete maRequete = new Requete();
		List<RetourDocument> listeRetourDocument = new ArrayList<RetourDocument>();
		List<String> listDocumentsString = new ArrayList<String>();


		// Récupération des noms de documents
		for (ScoreEtChemin curseurSC: listeDocuments) {
			// On split pour n'avoir que le nom du fichier et pas le chemin absolu
			String nomFichier = curseurSC.getChemin().substring(curseurSC.getChemin().lastIndexOf("/")+1);
			//System.out.println("Chemin = "+curseurSC.getChemin()+" et nom du fichier "+nomFichier);
			listDocumentsString.add(nomFichier);

		}


		maRequete.RequeteMetadonnees(listDocumentsString);
		ResultSet monResultat = maConnexion.executerRequete(maRequete);
		try {
			// réinitialise le cursor
			if (monResultat != null) {
				monResultat.beforeFirst();
			}
			else return null;

			// Parcours du résultat de la requête
			while (monResultat.next()) {
				int idDocument = monResultat.getInt("ID");
				String NomDocument = monResultat.getString("FILENAME");
				String titreDocument = monResultat.getString("TITRE");
				String auteurDocument = monResultat.getString("AUTEUR");
				Date dateDocument = monResultat.getDate("DATE");
				String sujetDocument = monResultat.getString("SUJET");
				RetourDocument monRetour = new RetourDocument(idDocument, NomDocument, titreDocument, auteurDocument, dateDocument, sujetDocument);
				listeRetourDocument.add(monRetour);
			}


		}
		catch (SQLException e) { 
			System.err.println("Erreur lors de la generation de la liste de document. " + e.getMessage());
			e.printStackTrace();
		}
		// Fermeure de l'objet Statement
		maConnexion.fermetureStatement();
		// Fermeture de la connexion à la base
		maConnexion.fermetureConnexion();
		return listeRetourDocument;
	}



	/**
	 * Setter de l'attribut onto
	 *  @param ontoEntree Ontologie en entrée
	 *  @see Recherche#onto
	 */
	public void setOntologie(Ontologie ontoEntree) {
		this.onto = ontoEntree;
	}

	/**
	 * Cette méthode effectue un dédoublonnage au sein d'une liste de ScoreEtChemin
	 * Le dédoublonnage s'effectue sur le chemin, sans prise en compte du score
	 * La liste est ensuite triée du score le plus fort au score au moins fort
	 * @param tabEntree
	 * @return Une liste de ScoreEtChemin sans doublon
	 * @see ScoreEtChemin
	 */
	@SuppressWarnings("unchecked")
	public List<ScoreEtChemin> dedoubleTableau (List<ScoreEtChemin> tabEntree) {
		@SuppressWarnings("rawtypes")
		Set<ScoreEtChemin> set = new HashSet();		
		set.addAll(tabEntree);
		List<ScoreEtChemin> sortie = new ArrayList<ScoreEtChemin>(set);
		Collections.sort(sortie, Collections.reverseOrder());
		return sortie;

	}

	/**
	 * Cette méthode effectue une recherhche au sein d'un index à partir
	 * d'une liste de mots, d'un chemin de l'index et d'un analyzer
	 * @param listeMots
	 * @param indLocation
	 * @param analyseur
	 * @return Une liste de ScoreEtChemin trié du score le plus fort au score le plus faible
	 * @throws IOException
	 * @see StopwordAnalyzerBase
	 * @see ScoreEtChemin
	 */
	public List<ScoreEtChemin> rechercheMots(List<String> listeMots, String indLocation, StopwordAnalyzerBase analyseur ) throws IOException {
		// On effectue maintenant la recherche surs mots de liste en entrée
		File repertoire = new File(indLocation);
		if (repertoire.exists()) {
			if (DirectoryReader.indexExists(FSDirectory.open(repertoire))) {
				IndexReader reader = DirectoryReader.open(FSDirectory.open(repertoire));
				IndexSearcher searcher = new IndexSearcher(reader);
				TopScoreDocCollector collector = null;
				List <ScoreEtChemin> tableauSortie = new ArrayList<ScoreEtChemin>();
				String motARechercher = null;
				try {
					// On parcours la liste de mots à chercher
					for (String motARechercher1: listeMots) {
						motARechercher = motARechercher1;
						Query q = new QueryParser(Version.LUCENE_40, "contents", analyseur).parse(motARechercher);
						collector = TopScoreDocCollector.create(50, true);
						searcher.search(q, collector);
						ScoreDoc[] hits = collector.topDocs().scoreDocs;
						for(int j=0;j<hits.length;++j) {
							int docId = hits[j].doc;
							Document d = searcher.doc(docId);
							String cheminDocument = d.get("path");
							float scoreDocument = hits[j].score;
							tableauSortie.add(new ScoreEtChemin(scoreDocument,cheminDocument));
						}
					}
					//tableauSortie = trieTableau(tableauSortie);
					// Une fois le tableau rempli  on le trie, selon le score
					// par ordre décroissant
					Collections.sort(tableauSortie, Collections.reverseOrder());
					reader.close();
				} catch (Exception e) {
					System.out.println("Error searching " + motARechercher + " : " + e.getMessage() + " "+ searcher.toString());
				}
				return tableauSortie;

			}
			else {
				return null;
			}

		}
		else {
			return null;
		}


	}

	/**
	 * 
	 *  Cette fonction permet d'effectuer une recherche à partir de :
	 *  d'une liste d'IndexerAbs, d'une langue et d'une liste de mots
	 *  Elle restitue une liste de couple (score ; nom du document)
	 *  triée du score le plus fort au score le plus faible
	 *  @param lIndexer Une liste d'Indexer
	 *  @param langue La langue de l'index
	 *  @param listeMots La liste de mot à rechercher
	 *  @see ScoreEtChemin
	 *  @see IndexerAbs
	 */
	public List<ScoreEtChemin> search(List<IndexerAbs> lIndexer, String langue, List<String> listeMots)  throws IOException {

		// On vérifie d'abord si la liste de mots à chercher est non null
		if ((listeMots == null || listeMots.isEmpty())) {
			return null;
		}
		else {
			// On initialise l'analyser et le répertoire de l'index
			StopwordAnalyzerBase analyzer = null;
			String indexLocation = null;
			//La première étape de la méthode est de retrouver l'indexer associé à la langue
			for (IndexerAbs elementIndex: lIndexer) {

				if (elementIndex.getLangue().equalsIgnoreCase(langue)) {
					// C'est l'index qui nous interesse, on récupère l'analyser associé
					analyzer = elementIndex.getAnalyzer();
					// on récupère le chemin de l'index
					indexLocation = elementIndex.getIndexLocation();
				}
			}
			List <ScoreEtChemin> tableauSortie = null;
			tableauSortie = rechercheMots(listeMots, indexLocation, analyzer);
			//tableauSortie = trieTableau(tableauSortie);
			return tableauSortie;
		}


	}


	/**
	 * 
	 *  Cette fonction permet d'effectuer une recherche à partir de :
	 *  d'une liste d'IndexerAbs et d'une liste de mots sans précision sur la langue.
	 *  La recherche s'effectue donc sur tous les index disponibles.
	 *  Un dédoublonnage des documents restitués est effectué.
	 *  Elle restitue une liste de couple (score ; nom du document)
	 *  triée du score le plus fort au score le plus faible
	 *  @param lIndexer Une liste d'Indexer
	 *  @param listeMots La liste de mot à rechercher
	 *  @see ScoreEtChemin
	 *  @see IndexerAbs
	 */
	public List<ScoreEtChemin> search(List<IndexerAbs> lIndexer, List<String> listeMots)  throws IOException {

		// On vérifie d'abord si la liste de mots à chercher est non null
		if ((listeMots == null || listeMots.isEmpty())) {
			return null;
		}
		else {
			StopwordAnalyzerBase analyzer = null;
			String indexLocation = null;
			List <ScoreEtChemin> tableauSortie = null;
			// On lance la recherche pour chaque index
			for (IndexerAbs indexATraiter: lIndexer) {
				analyzer = indexATraiter.getAnalyzer();
				// on récupère le chemin de l'index
				indexLocation = indexATraiter.getIndexLocation();
				List <ScoreEtChemin> tableauSortieUneLangue = null;
				tableauSortieUneLangue = rechercheMots(listeMots, indexLocation, analyzer);
				if (!(tableauSortieUneLangue == null)) {
					if (tableauSortie == null ) {
						tableauSortie = tableauSortieUneLangue;
					}
					else {
						tableauSortie.addAll(tableauSortieUneLangue);
					}
				}
			}

			// Une fois les recherches effectuées, on dédoublonne selon le nom
			// du document
			if (!(tableauSortie == null)) {
				tableauSortie = dedoubleTableau(tableauSortie);
			}
			return tableauSortie;
		}
	}

	/**
	 * Cette fonction effectue la recherche au sein de l'ontologie à partir d'une liste de mots
	 * et restitue une liste de mots suggérés
	 */
	public List<String> rechercheOntologie(List<String> listeMots){

		List<String> resultatsOnto = new ArrayList<String>();

		resultatsOnto.add("mot4");
		resultatsOnto.add("mot5");
		resultatsOnto.add("mot6");
		return resultatsOnto;


	}

}
