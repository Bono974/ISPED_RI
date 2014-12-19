package projet.recherche;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Ontologie {
	private OntModel model;
	private String ontologyFile = "/home/yoann/Documents/atc.owl";
	
	public Ontologie() {
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		model.setStrictMode(false); // Jena ne gère pas les fichiers OWL2
		InputStream in = FileManager.get().open(ontologyFile);
		
		if (in == null)
			throw new IllegalArgumentException("Fichier non existant");
		this.model = (OntModel) model.read(in, null, "TTL");
	}
	
	public List<String> expansionRequete(List<String> motsEntres) {
		// Return a list of new keywords associated by entry keywords from ontology
		
		List<String> motsEntresEtendus = new ArrayList<String>();
		ExtendedIterator<OntClass> topLevels = model.listClasses();
		OntClass cur;
		while(topLevels.hasNext()) {
			cur = topLevels.next(); 
			String libelle = cur.getLabel(null);
			if (isToKeepAsMatch(libelle, motsEntresEtendus)) {
				motsEntresEtendus.add(libelle);
				motsEntresEtendus.addAll(getBranchAnnotated(cur));
			}
			motsEntresEtendus.addAll(recTree(cur, motsEntres));
		}
		return new ArrayList<String>(new HashSet<String>(motsEntresEtendus)); // Ugly way to clear all occurences
	}
	
	private List<String> getParentsAnnotated(OntClass cur) {
		// Get all the parents labels into a string list 
		List<String> res = new ArrayList<String>();
		
		ExtendedIterator<OntClass> parents = cur.listSuperClasses();
		OntClass tmp;
		String libelle;
		while(parents.hasNext()) {
			tmp = parents.next();
			libelle = tmp.getLabel(null);
			res.add(libelle);
			if (tmp.isHierarchyRoot()) // No other things up there, we can browse the next parent
				continue;
			res.addAll(getParentsAnnotated(tmp));
		}
		return res;
	}
	
	private List<String> getChildsAnnotated(OntClass cur) {
		// Get all the parents labels into a string list 
		List<String> res = new ArrayList<String>();
		
		ExtendedIterator<OntClass> childs = cur.listSubClasses();
		OntClass tmp;
		String libelle;
		while(childs.hasNext()) {
			tmp = childs.next();
			libelle = tmp.getLabel(null);
			res.add(libelle);
			if (!(tmp.hasSubClass())) // No other things up there, we can browse the next parent
				continue;
			res.addAll(getChildsAnnotated(tmp));
		}
		return res;
	}
	
	
	private List<String> getBranchAnnotated(OntClass cur) {
		// Get all the branches labels into a string list (sub/super)
		List<String> res = new ArrayList<String>();
		
		res.addAll(getParentsAnnotated(cur));
		res.addAll(getChildsAnnotated(cur));

		return res;
	}
	
	private boolean isToKeepAsMatch(String libelle, List<String> motsEntres) {
		// Return if at least one word from motsEntres get a match from a libelle (per word)
		// Suppose that libelle is not already splitted
		// AND motsEntres is a list of unique words
		List<String> libelleSplit = new ArrayList<String>(Arrays.asList(libelle.split(" ")));
		
		for (String keyWord: motsEntres)
			for (String split: libelleSplit)
				if (split.equalsIgnoreCase(keyWord)) // Match exactly a word from entry word to a labeled concept from ontology
					return true;
		return false;
	}
	
	private List<String> recTree(OntClass root, List<String> motsEntresOrig) {
		// Browse all subclasses 
		List<String> res = new ArrayList<String>();
		
		ExtendedIterator<OntClass> subClasses = root.listSubClasses();
		OntClass cur;
		while(subClasses.hasNext()) {
			cur = subClasses.next(); 
			String libelle = cur.getLabel(null);

			if (isToKeepAsMatch(libelle, motsEntresOrig))
				res.addAll(getBranchAnnotated(cur));
			res.addAll(recTree(cur, motsEntresOrig));
		}
		return res;
	}
	
	public static void main(String[] args){
		Ontologie onto = new Ontologie();
		List<String> motsEntres = new ArrayList<String>();
		motsEntres.add("clonidine");
		//motsEntres.add("iron");

		List<String> res = onto.expansionRequete(motsEntres);
		System.out.println(res);
	}
}