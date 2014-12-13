import java.io.IOException;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.util.Version;


public class EnIndexerImpl extends IndexerAbs{
	
	/**
	 * Constructeur de la classe EnIndexerImpl.
	 * @param indexLocation Chemin de l'index crée
	 * @param source Chemin des documents à indexer
	 * @throws IOException
	 */
	public EnIndexerImpl(String indexLocation, String source) throws IOException{
		super("EN", indexLocation, source, new EnglishAnalyzer(Version.LUCENE_40));


	}
}
