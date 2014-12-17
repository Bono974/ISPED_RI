import java.io.InputStream;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class TestOntology {

	/**
	 * @param args
	 */


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OntDocumentManager mgr = new OntDocumentManager();
		mgr.setProcessImports(false);
		OntModelSpec s = new OntModelSpec(OntModelSpec.OWL_MEM);
		s.setDocumentManager(mgr);
		OntModel m = ModelFactory.createOntologyModel(s, null );

		InputStream in = FileManager.get().open("D:\\atc.owl");
		m.read(in, null, "TTL");

		


		System.out.println("OK");

		ExtendedIterator<OntClass> listeCl = m.listClasses();
		while (listeCl.hasNext()){
			OntClass ot = listeCl.next();
			System.out.print("Nom de la classe : "+ot.getLabel(null));
			OntClass disjointe = ot.getDisjointWith();
			if (disjointe != null) {
				System.out.print(" La classe disjointe est : "+disjointe.getLocalName());
			} else System.out.print("                -----     " + ot.toString());
			System.out.println();

		}

		ExtendedIterator<ObjectProperty>listeProp = m.listObjectProperties();
		while (listeProp.hasNext()){
			ObjectProperty prop = listeProp.next();
			System.out.print("Nom de la propri�t� : "+prop.getLocalName());

			//OntResource dom = prop.getDomain();

			System.out.println();

		}


	}

}
