import java.io.IOException;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.util.Version;


public class EnIndexerImpl extends IndexerAbs{
	public EnIndexerImpl(String indexLocation, String source) throws IOException{
		super("En", indexLocation, source, new EnglishAnalyzer(Version.LUCENE_40));


	}
}
