//package com.lucenetutorial.apps;
package projet;

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

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
		// TODO Auto-generated method stub
		//=========================================================
		// Now search
		//=========================================================

		// On initialise l'analyser et le répertoire de l'index
		StopwordAnalyzerBase analyzer = null;
		String indexLocation = null;
		//La première étape de la méthode est de retrouver l'indexer associé à la langue
		for (Iterator<IndexerAbs> i = lIndexer.iterator(); i.hasNext();) {

			IndexerAbs elementIndex = i.next();
			if (elementIndex.getLangue().equalsIgnoreCase(langue)) {
				// C'est l'index qui nous interesse, on récupère l'analyser associé
				analyzer = elementIndex.getAnalyzer();
				// on récupère le chemin de l'index
				indexLocation = elementIndex.getIndexLocation();
			}
		}

		// On effectue maintenant la recherche surs mots de liste en entrée		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = null;
		String motARechercher = null;
		List <ScoreEtChemin> tableauSortie = new ArrayList<ScoreEtChemin>();
		try {

			collector = TopScoreDocCollector.create(5, true);
			// On parcours la liste de mots à chercher
			for (Iterator<String> i = listeMots.iterator(); i.hasNext();) {
				motARechercher = i.next();
				Query q = new QueryParser(Version.LUCENE_40, "contents", analyzer).parse(motARechercher);
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
			// Une fois le tableau rempli  on le trie, selon le score
			// par ordre décroissant
			Collections.sort(tableauSortie, Collections.reverseOrder()); 

		} catch (Exception e) {
			System.out.println("Error searching " + motARechercher + " : " + e.getMessage() + " "+ searcher.toString());
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
