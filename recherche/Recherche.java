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

import java.io.*;
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
	 * Setter de l'attribut onto
	 *  @param ontoEntree Ontologie en entrée
	 *  @see Recherche#onto
	 */
	public void setOntologie(Ontologie ontoEntree) {
		this.onto = ontoEntree;
	}

	@SuppressWarnings("unchecked")
	public List<ScoreEtChemin> dedoubleTableau (List<ScoreEtChemin> tabEntree) {
		@SuppressWarnings("rawtypes")
		Set<ScoreEtChemin> set = new HashSet();		
		set.addAll(tabEntree);
		List<ScoreEtChemin> sortie = new ArrayList<ScoreEtChemin>(set);
		Collections.sort(sortie, Collections.reverseOrder());
		return sortie;

	}

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
	 *  d'un Indexer, d'une langue et d'une liste de mots
	 *  Elle restitue une liste de couple (score ; nom du document)
	 *  @param lIndexer Une liste d'Indexer
	 *  @param langue La langue de l'index
	 *  @param listeMots La liste de mot à rechercher
	 */
	public List<ScoreEtChemin> search(List<IndexerAbs> lIndexer, String langue, List<String> listeMots)  throws IOException {

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


	/**
	 * 
	 *  Cette fonction permet d'effectuer une recherche à partir de :
	 *  d'un Indexer et d'une liste de mots sans précision sur la langue.
	 *  La recherche s'effectue donc sur tous les index disponibles.
	 *  Un dédoublonnage des documents restitués est effectué.
	 *  Elle restitue une liste de couple (score ; nom du document)
	 *  @param lIndexer Une liste d'Indexer
	 *  @param listeMots La liste de mot à rechercher
	 */
	public List<ScoreEtChemin> search(List<IndexerAbs> lIndexer, List<String> listeMots)  throws IOException {

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

	/**
	 * Cette fonction effectue la recherche au sein de l'ontologie à partir d'une liste de mots
	 * et restitue une liste de mots suggérés
	 */
	public List<String> rechercheOntologie(List<String> listeMots){
		return null;


	}
	/**
	 * Cette fonction restitue la liste de mots suggérés à partir
	 * de la recherch dans l'ontologie
	 * @return La liste de mots suggérés à partir de la recherche au sein de l'ontologie
	 */
	public List<String> getLastNewKeywordsFromOntology() {
		return null;

	}


}
