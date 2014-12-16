package projet.recherche;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class Ontologie {
	
	//private String fileOntology = "/Users/bruno/Dropbox/ISPED/inf203/ISPED_RI/data/ontology/Thesaurus.owl";
	//private String fileOntology = "/Users/bruno/Dropbox/ISPED/ime302_projet/final/animanga.owl";
	private String fileOntology = "/Users/bruno/Downloads/aero.owl";

	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private String defaultURI;
	
	public Ontologie (){		
		//System.setProperty("entityExpansionLimit","100000000");
		try {	
			IRI iri;
			long startTime, endTime, duration;
			defaultURI = "ontology does not have an IRI";

			startTime = System.nanoTime();
			
			manager = OWLManager.createOWLOntologyManager();
			iri = IRI.create(new File(fileOntology));
			ontology = manager.loadOntologyFromOntologyDocument(iri);
			
			endTime = System.nanoTime();
			duration = (endTime - startTime) / 1000000; 

			IRI uri = ontology.getOntologyID().getOntologyIRI();
			if (uri != null)
				defaultURI = uri.toString();
			
			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Temps de chargement  : "+ duration + " ms");
			System.out.println(defaultURI);

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	public List<String> expansionRequete(List<String> motsEntres) {
		List<String> res = new ArrayList<String>();
		
		OWLDataFactory factory = this.manager.getOWLDataFactory();
		//OWLClass cancer = factory.getOWLClass(IRI.create(this.defaultURI+"#person"));

		final OWLAnnotationProperty comment = factory.getRDFSComment();
		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ontology));

		OWLOntologyWalkerVisitor visitor = new OWLOntologyWalkerVisitor(walker) {
			@Override
			public Object visit(OWLObjectPropertyAssertionAxiom axiom) {
				System.out.println(axiom);
				return null;
			}
			@Override
			public Object visit(OWLDataPropertyAssertionAxiom axiom) {
				System.out.println(axiom);
				return null;
			}
			@Override
			public Object visit(OWLAnnotationAssertionAxiom axiom) {
				System.out.println("TOTO");
					
				String ref = axiom.getValue().toString();
				boolean flag = false;
				for (String ew: motsEntres) {
					if (ref.contains(ew)) {
						flag = true;
						break;
					}
				}

				if (flag) {
					for (OWLEntity cur: axiom.getSignature())
						if (cur.isOWLClass()) {
							OWLClass tmp = cur.asOWLClass();
							for(OWLAnnotationAssertionAxiom cur2: tmp.getAnnotationAssertionAxioms(ontology))
								res.add(cur2.getValue().toString());
							System.out.println(res);
						}
					res.add(ref);
				}

				System.out.println("TITI");
				return null;
			}
			
			@Override
			public Object visit(OWLObjectSomeValuesFrom desc) {
				//System.out.println(desc);
				//System.out.println("         " + getCurrentAxiom());
				//System.out.println(axiom.getAnnotations(comment));
				return null;
			}
			
		};
		walker.walkStructure(visitor);      

		return res;
	}
	
	public static void main(String args[]) {
		Ontologie ontology = new Ontologie();
		List<String> motsEntres = new ArrayList<String>();
		motsEntres.add("anaphylaxis");
		motsEntres.add("cardiovascular");
		List<String> conceptsEtendus = ontology.expansionRequete(motsEntres);
		System.out.println(conceptsEtendus);
	}
}
