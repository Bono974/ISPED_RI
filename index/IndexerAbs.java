package projet.index;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;


public abstract class IndexerAbs {

	protected StopwordAnalyzerBase analyzer;
	/**
	 * Langue utilisée.
	 */
	protected String langue;
	
	/**
	 * Chemin de l'index crée.
	 */
	protected String indexLocation;
	
	protected IndexWriter writer;
	protected ArrayList<File> queue = new ArrayList<File>();
	protected Tika tika = new Tika();
	
	/**
	 * Constructeur de la classe IndexerAbs. Ce constructeur prend la langue, le chemin de l'index à créer, le chemin des documents à indexer,
	 * et le type d'analyser utilisé en paramètres
	 * @param langue Langue de l'indexer utilisé
	 * @param indexLocation Localisation de l'index
	 * @param source Localisation des sources de documents à indexer
	 * @param analyzer Type d'analyser utilisé
	 * @throws IOException
	 */
	public IndexerAbs (String langue, String indexLocation, StopwordAnalyzerBase analyzer) throws IOException{
		
		
		FSDirectory dir = FSDirectory.open(new File(indexLocation));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		writer = new IndexWriter(dir, config);
		 

		this.langue = langue;
		this.indexLocation = indexLocation;
		this.analyzer = analyzer;
	}
	
	/**
	 * Getter de l'attribut langue.
	 * @return langue
	 */
	//pour obtenir la langue de l'analyzer utilisé.
	public String getLangue(){
		return this.langue;
	}
	
	/**
	 * Getter de l'analyser utilisé.
	 * @return analyzer
	 */
	//pour obtenir l'analyzer utilisé.
	public StopwordAnalyzerBase getAnalyzer(){
		return this.analyzer;
	}
	
	
	/**
	 * Getter du chemin de l'index crée.
	 * @return indexLocation
	 */
	//pour obtenir le chemin de l'index crée.
	public String getIndexLocation(){
		return this.indexLocation;
	}
	
	
	/**
	 * Cette fonction permet d'indexer les documents soumis à partir d'un dossier de documents ou d'un document seul.
	 * @param fileName Nom du dossier de documents
	 * @throws IOException
	 */
	public void indexFileOrDirectory(String fileName) throws IOException {
		//===================================================
		//gets the list of files in a folder (if user has submitted
		//the name of a folder) or gets a single file name (is user
		//has submitted only the file name) 
		//===================================================
		addFiles(new File(fileName));

		int originalNumDocs = writer.numDocs();
		for (File f : queue) {
			FileReader fr = null;
			try {
				Document doc = new Document();

				//===================================================
				// add contents of file
				//===================================================
				fr = new FileReader(f);
				Tika tika = new Tika();
				Reader texte = tika.parse(f);
				doc.add(new TextField("contents", texte));
				doc.add(new StringField("path", f.getPath(), Field.Store.YES));
				doc.add(new StringField("filename", f.getName(), Field.Store.YES));

				writer.addDocument(doc);
				System.out.println("Added: " + f);
				texte.close();
			} catch (Exception e) {
				System.out.println("Could not add: " + f);
				e.printStackTrace();
			} finally {
				fr.close();
			}
		}

		int newNumDocs = writer.numDocs();
		System.out.println("");
		System.out.println("************************");
		System.out.println((newNumDocs - originalNumDocs) + " documents added.");
		System.out.println("************************");

		queue.clear();
	}
	
	
	private void addFiles(File file) {

		if (!file.exists()) {
			System.out.println(file + " does not exist.");
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				addFiles(f);
			}
		} else {
			queue.add(file);
			
			/*
			String filename = file.getName().toLowerCase();
			//===================================================
			// Only index text files
			//===================================================
			if (filename.endsWith(".htm") || filename.endsWith(".html") || 
					filename.endsWith(".xml") || filename.endsWith(".txt") || filename.endsWith(".pdf")){
				queue.add(file);
			} else {
				System.out.println("Skipped " + filename);
			}
			*/
		}
	}
	
	public void closeIndex() throws IOException {
		writer.close();
	}
	
	public void action(String source) throws IOException {
		indexFileOrDirectory(source);
		closeIndex();
	}
	
}
