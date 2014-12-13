package projet.recherche;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Ontologie {
    private OntModel base;

    public Ontologie () {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        //InputStream in = FileManager.get().open("/Users/bruno/Dropbox/ISPED/inf203/ISPED_RI/data/ontology/Thesaurus.owl");
        InputStream in = FileManager.get().open("/Users/bruno/Dropbox/ISPED/ime302_projet/final/animanga.owl");

        if (in == null)
            throw new IllegalArgumentException("Fichier non existant.");
        this.base = (OntModel) m.read(in, "RDF/XML");
    }

    private List<String> search(List<String> entryWords) {
        OntClass cur;
        List<String> res = new ArrayList<String>();
        List<String> entryWordsTmp = new ArrayList<String>(entryWords);

        OntModel inf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, this.base);
        System.out.println("tata");
        ExtendedIterator<OntClass> top = inf.listClasses();
        top.next(); // Nothing concept
        top.next(); // Thing concept

        while(top.hasNext()) { // Top levels concepts
            cur = top.next();
            res.addAll(recTree(cur, entryWordsTmp)); // From top to bottom (recursive fct)
        }

        return new ArrayList<String>(new HashSet<String>(res)); // Remove all occurences
    }

    private List<String> recTree(OntClass cur, List<String> entryWords){
        List<String> res = new ArrayList<String>();
        ExtendedIterator<OntProperty> tmp1 = cur.listDeclaredProperties(); // List all cur's properties
        while(tmp1.hasNext()) {
            OntProperty cur2 = tmp1.next(); // Triplet [Concept, prop, Concept]

            OntResource domain = cur2.getDomain();
            String prop = cur2.getLocalName();
            OntResource range = cur2.getRange();

            if (domain.getURI() == null || range.getURI() == null)
                continue;

            for(String ew:entryWords){
                // TODO Revoir les conditions pour ajouter l'info (domain/prop/range) dans la liste entryWords
                // pour l'expansion de requête
                if (domain.getURI().contains(ew) || range.getURI().contains(ew)) {
                    res.add(prop);
                    res.add(range.getLocalName());
                    res.add(domain.getLocalName());
                }
            }
        }

        if (cur.hasSubClass()) {
            ExtendedIterator<OntClass> subClasses= cur.listSubClasses(); // Follow the subclasses in recursive way
            while (subClasses.hasNext())
                //System.out.println(subClasses.next());
                res.addAll(recTree(subClasses.next(), entryWords));
        }
        return res;
    }

    public static void main(String args[]){
        Ontologie toto = new Ontologie();

        List<String> lword = new ArrayList<String>();
        lword.add("anime");
        lword.add("manga");
        lword.add("seiyuu");

        List<String> res = toto.search(lword);
        System.out.println(res);
    }
}
