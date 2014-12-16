package projet.recherche;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
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
	// aero utilise des ontologies sur le web dont : iao.owl
	// Lors de l'exécution il se peut qu'une exception 'socket error' intervienne <-- embettant

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

			// TODO
			// Problème : getOntologyIRI / getDefaultDocumentIRI
			// peuvent ne pas donner le bon URI ...
			// Certaines ontologies donnent http://purl.obolibrary.org/obo/
			// d'autres http://purl.obolibrary.org/obo/aero.owl
			// Les séparateurs peuvent être différents aussi par ontologies : / ou #
			
			//IRI uri = ontology.getOntologyID().getOntologyIRI();
			IRI uri = ontology.getOntologyID().getDefaultDocumentIRI();

			//if (uri != null)
			//	defaultURI = uri.toString();
			
			defaultURI = "http://purl.obolibrary.org/obo/";
			
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
		OWLClass curClass;
		IRI tmp;
		
		// On récupère directement les annotations si un concept est évident
		// évident : si un mot entré correspond exactement à un concept de l'ontologie
		
		// TODO
		// Problème : censé fonctionner avec AERO_0000358 qui est un concept d'aero.owl mais non.
		
		String sep = "/";
//		String sep = "#";
		for (String cur: motsEntres) {
			tmp = IRI.create(this.defaultURI+sep+cur);
			if (this.ontology.containsClassInSignature(tmp)) { // test si le concept existe
				curClass = factory.getOWLClass(tmp);
				for (OWLAnnotation annotation : curClass.getAnnotations(this.ontology, factory.getRDFSLabel())) {
					// Parcours les annotations
					if (annotation.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) annotation.getValue();
						res.add(val.getLiteral());
					}
				}
			}
		}
		
		// Visiteur qui marche sur un chemin sur l'ontologie
		// L'idée : s'arrêter sur les noeuds qui ont des annotations, et voir si ils contiennent 
		// au moins un des mots de la liste motsEntres
		// si oui : on rajoute le concept associé à la liste 'res' étendue
		
		// TODO
		// Problèmes : si il y a plusieurs annotations, il s'arrête qu'à la première.
		// je ne sais toujours pas comment récupérer le concept (OWLClass et son nom)
		// qui est associé à l'annotation dont on se situe lors du parcours du visiteur
		
		/*
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
				
				for (OWLEntity toto: axiom.getSignature()) {
					System.out.println(toto + "TITIITTIITITITITITITITI");
				}
				System.out.println(axiom.getValue());

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
				//System.out.println("TOTOTOTOOTOT" + getCurrentAxiom());
				return null;
			}
			
		};
		walker.walkStructure(visitor);      
*/
		return res;
	}
	
	public static void main(String args[]) {
		Ontologie ontology = new Ontologie();
		List<String> motsEntres = new ArrayList<String>();
		motsEntres.add("anaphylaxis");
		motsEntres.add("cardiovascular");
		motsEntres.add("AERO_0000358");

		List<String> conceptsEtendus = ontology.expansionRequete(motsEntres);
		System.out.println(conceptsEtendus);
	}
}
