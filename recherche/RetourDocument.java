package projet.recherche;

import java.sql.Date;

public class RetourDocument {

	int idDocument;
	
	public int getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}

	public String getNomDocument() {
		return NomDocument;
	}

	public void setNomDocument(String nomDocument) {
		NomDocument = nomDocument;
	}

	public String getTitreDocument() {
		return titreDocument;
	}

	public void setTitreDocument(String titreDocument) {
		this.titreDocument = titreDocument;
	}

	public String getAuteurDocument() {
		return auteurDocument;
	}

	public void setAuteurDocument(String auteurDocument) {
		this.auteurDocument = auteurDocument;
	}

	public Date getDateDocument() {
		return dateDocument;
	}

	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}

	public String getSujetDocument() {
		return sujetDocument;
	}

	public void setSujetDocument(String sujetDocument) {
		this.sujetDocument = sujetDocument;
	}

	String NomDocument;
	String titreDocument;
	String auteurDocument;
	Date dateDocument;
	String sujetDocument;

	public RetourDocument (int idDocument,	String NomDocument,	String titreDocument, String auteurDocument, Date dateDocument, String sujetDocument) {
		this.idDocument = idDocument;
		this.NomDocument = NomDocument;
		this.titreDocument = titreDocument;
		this.auteurDocument = auteurDocument;
		this.dateDocument = dateDocument;
		this.sujetDocument = sujetDocument;
	}

	
	
}
