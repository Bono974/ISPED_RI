import java.io.IOException;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.util.Version;


public class FrIndexerImpl extends IndexerAbs{
	
<<<<<<< HEAD:index/FrIndexerImpl.java
	public FrIndexerImpl(String indexLocation) throws IOException{
		super("Fr", indexLocation, new FrenchAnalyzer(Version.LUCENE_40));
=======
	/**
	 * Constructeur de la classe FrIndexerImpl
	 * @param indexLocation Chemin de l'index crée
	 * @param source Chemin des documents à indexer
	 * @throws IOException
	 */
	public FrIndexerImpl(String indexLocation, String source) throws IOException{
		super("FR", indexLocation, source, new FrenchAnalyzer(Version.LUCENE_40));
>>>>>>> parent of 99fba28... Package index:FrIndexerImpl.java
		
	}
	
	
}
