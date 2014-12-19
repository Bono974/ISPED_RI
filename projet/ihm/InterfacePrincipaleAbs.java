package projet.ihm;
import java.util.List;

import projet.index.IndexerAbs;

public abstract class InterfacePrincipaleAbs {
	
	protected String interfaceChoisie;
	public List<IndexerAbs> lIndexer;

	
	public InterfacePrincipaleAbs(List<IndexerAbs> lIndexer, String interfaceChoisie) {
			this.interfaceChoisie = interfaceChoisie;
			this.lIndexer = lIndexer;
	}
	public abstract void run();


}