package projet.sgbd;

import java.util.List;

/**
 * 
 * Cette Classe contient les codes des requetes SQL utilisees.
 * 
 * @author Yoann Keravec
 * 
 * 
 * 
 */
public class Requete {

	/**
	 * Correspond à la chaîne de caractère contenant le code SQL.
	 */
	public String requeteALancer;
	
	/**
	 * Cette méthode permet de récupérer les métadonnées associées aux documents
	 * correspondant à des critères de recherche
	 * 
	 */
	public void RequeteMetadonnees(List<String> listeDocuments) {
		
		String listeDoc = null;
		
		// preparation de la liste de documents
		for (String curseur: listeDocuments) {
			if (listeDoc == null) {
				listeDoc = "'"+curseur+"'";
			}
			else {
				listeDoc += ","+"'"+curseur+"'";
			}
		}
		// requete
		this.requeteALancer = "select ID, FILENAME, TITRE, AUTEUR, DATE, SUJET "
				+ "from PROJETRI.TAB_METADATA "
				+ "where FILENAME in ("+ listeDoc + ");";		
	}
	
	/**
	 * Getter de l'attribut requeteALancer.
	 * 
	 * @return valeur de requeteALancer
	 * @see Requete#requeteALancer
	 */
	public String getRequeteALancer() {
		return this.requeteALancer;
	}
	
}
