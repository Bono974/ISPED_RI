package projet.ihm;
import java.util.List;

import projet.index.IndexerAbs;

public abstract class InterfacePrincipaleAbs {
	/**
	 * Interface choisie par utilisateur
	 */
	protected String interfaceChoisie;
	
	/**
	 * Liste des Index à traiter
	 */
	protected List<IndexerAbs> lIndexer;

	/**
	 * Constructeur
	 * @param lIndexer
	 * @param interfaceChoisie
	 */
	public InterfacePrincipaleAbs(List<IndexerAbs> lIndexer, String interfaceChoisie) {
			this.interfaceChoisie = interfaceChoisie;
			this.lIndexer = lIndexer;
	}
	
	public abstract void run();
}