package projet.index;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;


public abstract class IndexerAbs {

	protected FSDirectory dir;
	protected IndexWriterConfig config;
	
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
	protected Tika tika;

	protected ArrayList<String> nomColonne = new ArrayList<String>();

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


		dir = FSDirectory.open(new File(indexLocation));
		config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		


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
	private void indexFileOrDirectory(String fileName) throws FileNotFoundException, IOException {
		//===================================================
		//gets the list of files in a folder (if user has submitted
		//the name of a folder) or gets a single file name (is user
		//has submitted only the file name) 
		//===================================================
		writer = new IndexWriter(this.dir, this.config);
		File nomFichier = new File(fileName);
		if (!nomFichier.exists()) {
			throw new FileNotFoundException(fileName + "n'existe pas.");
		}
		
		addFiles(nomFichier);
		

		int originalNumDocs = writer.numDocs();
		for (File f : queue) {
			FileInputStream fr = null;
			try {
				Document doc = new Document();

				//===================================================
				// add contents of file
				//===================================================
				fr = new FileInputStream(f);
				tika = new Tika();
				Metadata metadata = new Metadata();
				Reader texte = tika.parse(fr,metadata);
				doc.add(new TextField("contents", texte));
				doc.add(new StringField("path", f.getPath(), Field.Store.YES));
				doc.add(new StringField("filename", f.getName(), Field.Store.YES));

				if (writer.getConfig().getOpenMode() == OpenMode.CREATE){
					writer.addDocument(doc);
					System.out.println("Added: " + f);
				} else{
					writer.updateDocument(new Term("path", f.getPath()), doc);
				}
				System.out.println("Added: " + f);
				texte.close();
			} catch (Exception e) {
				System.out.println("Could not add: " + f);
				e.printStackTrace();
			} finally {
				try {
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	public void indexDatabase(String table, String database) throws SQLException, IOException{

		String url = "jdbc:mysql://localhost:3306/" + database;
		String user = "root";
		String passwd = "";

		Connection conn = DriverManager.getConnection(url, user, passwd);

		String requete = "SELECT * FROM " + table + " ;";
		PreparedStatement requetePrepare = conn.prepareStatement(requete,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
		ResultSet result = requetePrepare.executeQuery();
		ResultSetMetaData resultMeta = requetePrepare.getMetaData();

		while(result.next()){
			Document doc = new Document();

			for(int i = 1; i<resultMeta.getColumnCount(); i++){
				doc.add(new TextField(resultMeta.getColumnName(i), result.getObject(i).toString(), Field.Store.YES));
			}
			writer.addDocument(doc);

		}
		requetePrepare.close();
		result.close();

	}

	private void closeIndex() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Cette fonction permet de lancer l'indexation d'un répertoire de documents.
	 * @param source Chemin de localisation des documents à indexer.
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	public void action(String source) throws FileNotFoundException, IOException {
		indexFileOrDirectory(source);
		closeIndex();
	}
	
}
