package projet.index;
import java.io.IOException;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.util.Version;


public class FrIndexerImpl extends IndexerAbs{
	
	public FrIndexerImpl(String indexLocation) throws IOException{
		super("Fr", indexLocation, new FrenchAnalyzer(Version.LUCENE_40));
		
	}
	
	
}
