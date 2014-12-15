package projet.recherche;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

import com.google.common.base.Optional;

public class Ontologie {
	
	private String fileOntology = "/Users/bruno/Dropbox/ISPED/inf203/ISPED_RI/data/ontology/Thesaurus.owl";
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

			// Java 8 ou com.google.common.base.Optional
			// Permet de s'abstenir d'avoir des pointers null exceptions
			Optional<IRI> uri = ontology.getOntologyID().getOntologyIRI();
			if (uri.isPresent())
				defaultURI = uri.get().toString();
			
			System.out.println("Loaded ontology: " + ontology);
			System.out.println("Temps de chargement  : "+ duration + " ms");
			System.out.println(defaultURI);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	public List<OWLClass> expansionRequete(List<String> motsEntres) {
		List<OWLClass> res = new ArrayList<OWLClass>();
		
		OWLDataFactory factory = this.manager.getOWLDataFactory();
		final OWLAnnotationProperty comment = factory.getRDFSComment();
		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ontology));

		OWLOntologyWalkerVisitor visitor = new OWLOntologyWalkerVisitor(walker) {
			@Override
			public void visit(OWLObjectPropertyAssertionAxiom axiom) {
				System.out.println(axiom);
				return;
			}
			@Override
			public void visit(OWLDataPropertyAssertionAxiom axiom) {
				System.out.println(axiom);
				return;
			}
			@Override
			public void visit(OWLAnnotationAssertionAxiom axiom) {
				System.out.println(axiom.getAnnotations(comment));
				return;
			}
			
		};
		walker.walkStructure(visitor);      

		return res;
	}
	
	public static void main(String args[]) {
		Ontologie ontology = new Ontologie();
		List<String> motsEntres = new ArrayList<String>();
		motsEntres.add("cancer");
		motsEntres.add("breast");
		List<OWLClass> conceptsEtendus = ontology.expansionRequete(motsEntres);
		// À partir de ces concepts, on peut retrouver les annotations / noms préférés ...
	}
}
