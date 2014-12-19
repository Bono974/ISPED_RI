package projet.recherche;

import java.sql.Date;

public class RetourDocument {

	/**
	 * Identifiant du document récupéré de la  la table des métadonnées.
	 */
	int idDocument;
	
	/**
	 * Nom du document récupéré de la  la table des métadonnées.
	 */
	String nomDocument;
	
	/**
	 * Titre du document récupéré de la  la table des métadonnées.
	 */
	String titreDocument;
	
	/**
	 * Nom de l'auteur du document récupéré de la  la table des métadonnées.
	 */
	String auteurDocument;
	
	/**
	 * Date de publication du document récupérée de la table des métadonnées.
	 */
	Date dateDocument;
	
	/**
	 * Sujet du document récupéré de la table des métadonnées.
	 */
	String sujetDocument;

	/**
	 * Constructeur de la classe RetourDocument
	 * @param idDocument identifiant du document
	 * @param NomDocument nom du document
	 * @param titreDocument titre du document
	 * @param auteurDocument auteur du document
	 * @param dateDocument date du document
	 * @param sujetDocument sujet du document
	 */
	public RetourDocument (int idDocument,	String nomDocument,	String titreDocument, String auteurDocument, Date dateDocument, String sujetDocument) {
		this.idDocument = idDocument;
		this.nomDocument = nomDocument;
		this.titreDocument = titreDocument;
		this.auteurDocument = auteurDocument;
		this.dateDocument = dateDocument;
		this.sujetDocument = sujetDocument;
	}

	/**
	 * Getter de l'attribut idDocument
	 * @return l'identifiant du document
	 * @see RetourDocument#idDocument
	 */
	public int getIdDocument() {
		return idDocument;
	}

	/**
	 * Setter de l'attribut idDocument
	 * @param idDocument
	 * @see RetourDocument#idDocument
	 */
	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}

	/**
	 * Getter de l'attribut nomDocument
	 * @return Le nom du document
	 * @see RetourDocument#NomDocument
	 */
	public String getNomDocument() {
		return nomDocument;
	}

	/**
	 * Setter de l'attribut nomDocument
	 * @param nomDocument
	 * @see RetourDocument#nomDocument
	 */
	public void setNomDocument(String nomDocument) {
		this.nomDocument = nomDocument;
	}

	/**
	 * Getter de l'attribut titreDocument
	 * @return Le titre du document
	 * @see RetourDocument#titreDocument
	 */
	public String getTitreDocument() {
		return titreDocument;
	}
	
	/**
	 * Setter de l'attribut titreDocument
	 * @param titreDocument
	 * @see RetourDocument#titreDocument
	 */
	public void setTitreDocument(String titreDocument) {
		this.titreDocument = titreDocument;
	}

	/**
	 * Getter de l'attribut auteurDocument
	 * @return L'auteur du document
	 * @see RetourDocument#auteurDocument
	 */
	public String getAuteurDocument() {
		return auteurDocument;
	}
	
	/**
	 * Setter de l'attribut
	 * @param auteurDocument
	 * @see RetourDocument#auteurDocument
	 */
	public void setAuteurDocument(String auteurDocument) {
		this.auteurDocument = auteurDocument;
	}

	/**
	 * Getter de l'attribut dateDocument
	 * @return La date du document
	 * @see RetourDocument#dateDocument
	 */
	public Date getDateDocument() {
		return dateDocument;
	}

	/**
	 * Setter de l'attribut dateDocument
	 * @param dateDocument
	 * @see RetourDocument#dateDocument
	 */
	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}

	/**
	 * Getter de l'attribut sujetDocument
	 * @return Le sujet du document
	 * @see RetourDocument#sujetDocument
	 */
	public String getSujetDocument() {
		return sujetDocument;
	}

	/**
	 * Setter de l'attribut sujetDocument
	 * @param sujetDocument
	 * @see RetourDocument#sujetDocument
	 */
	public void setSujetDocument(String sujetDocument) {
		this.sujetDocument = sujetDocument;
	}
	
}
