package projet.sgbd;

import java.sql.*;
import java.util.Vector;

/**
 * 
 * Cette Classe effectue la connexion SQL et le requêtage des tables
 * 
 * @author Yoann Keravec
 * 
 * 
 * 
 */
public class ConnexionMySQL {

	/**
	 * Objet Connection permettant la connexion avec la base MySQl.
	 * 
	 */
	private Connection connexion = null;

	/**
	 * Objet Statement permettant le requ�tage des tables.
	 * 
	 * @see ConnexionMySQL#creationStatement()
	 */
	private Statement statement = null;

	/**
	 * URL de connexion � la base ISPED_INF07 du projet.
	 */
	private String url = "jdbc:mysql://localhost:3306/PROJETRI";

	/**
	 * Login de connexion � la base.
	 */
	private String utilisateur = "root";

	/**
	 * mot de passe de connexion � la base.
	 */
	private String motDePasse = "";

	/**
	 * Methode de chargement du driver MySQL.
	 * 
	 */
	public int chargementDriver() {

		/* Chargement du driver JDBC pour MySQL */
		try {
			System.out.println("[Technique] Chargement du driver...");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("[Technique] Driver charge.");
			return 0;
		} catch (ClassNotFoundException e) {
			System.err
					.println("[Technique] Erreur lors du chargement : le driver n'a pas ete trouve dans le classpath ! <br/>"
							+ e.getMessage());
			e.printStackTrace();
			return 1;
		}

	}

	/**
	 * Cette m�thode cr�e la connexion � la base MySQL.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * @see ConnexionMySQL#connexion
	 */
	public int initConnexionMySQL() {
		/* Connexion � la base de donn�es */

		try {
			System.out.println("[Technique] Connexion a la base de donnees...");
			this.connexion = DriverManager.getConnection(url, utilisateur,
					motDePasse);
			System.out.println("[Technique] Connexion reussie !");
			return 0;
		} catch (SQLException e) {
			System.err
					.println("[Technique] Erreur lors de la connexion : <br/>"
							+ e.getMessage());
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * Cette m�thode effectue la fermeture de la connexion avec la base MySQL.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * @see ConnexionMySQL#statement
	 */
	public int fermetureConnexion() {
		if (connexion != null) {
			try {
				connexion.close();
				System.out
						.println("[Technique] Fermeture de l'objet Connection.");
				return 0;
			} catch (SQLException e) {
				System.err
						.println("[Technique] Erreur lors de la fermeture de l'objet Connection. "
								+ e.getMessage());
				e.printStackTrace();
				return 1;
			}
		} else
			return 0;
	}

	/**
	 * Cette m�thode cr�e le statement.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * 
	 */
	public int creationStatement() {
		/* Cr�ation de l'objet g�rant les requ�tes */
		try {

			this.statement = connexion.createStatement();
			System.out.println("[Technique] Objet requete cree !");
			return 0;
		} catch (SQLException erreur) {
			System.err.println("[Technique] Erreur � la creation du statement"
					+ erreur.getMessage());
			erreur.printStackTrace();
			return 1;
		}

	}

	/**
	 * Cette m�thode effectue la fermeture de l'objet statement.
	 * 
	 * @return code retour de bonne fin. 0 : OK , 1 : erreur
	 * @see ConnexionMySQL#statement
	 */
	public int fermetureStatement() {
		if (this.statement != null) {
			try {
				statement.close();
				System.out
						.println("[Technique] Fermeture de l'objet Statement.");
				return 0;
			} catch (SQLException erreur) {
				System.err
						.println("[Technique] Erreur lors de la fermeture de l'objet Statement "
								+ erreur.getMessage());
				erreur.printStackTrace();

				return 1;
			}
		} else
			return 0;
	}

	/**
	 * Cette m�thode effectue le lancement d'une requ�te pr�par�e.
	 * 
	 * @param r
	 *            : Objet de type Requete. Il contient le code SQL de la requ�te.
	 * @param idHospi
	 *            : Idenfiant de l'entit� Hospitalisation � rechercher.
	 * @return Retourne un ResultSet contenant le r�sultat de la requ�te
	 * @see Requete
	 */
	public ResultSet executerRequetePreparee(Requete r, int idHospi) {
		PreparedStatement requete = null;
		try {
			requete = this.connexion.prepareStatement(r.getRequeteALancer());
			requete.setInt(1, idHospi);
			return requete.executeQuery();
		} catch (SQLException e) {
			System.err
					.println("[Technique] Erreur lors de la creation de la requete preparee"
							+ e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Cette m�thode g�n�re un ResultSet � partir d'un objet Requete contenant
	 * du code SQL.
	 * 
	 * @param r
	 *            : l'objet Requete
	 * @return un ResultSet
	 * @see Requete
	 */
	public ResultSet executerRequete(Requete r) {

		ResultSet resultat = null;
		/* Ex�cution d'une requ�te de lecture */
		//System.out.println("La requete lanc�e est " + r.getRequeteALancer());

		try {
			resultat = this.statement.executeQuery(r.getRequeteALancer());

		} catch (SQLException e) {
			System.err.println("[Technique] Erreur a l'execution de la requete"
					+ e.getMessage());
			e.printStackTrace();
		}
		return resultat;
	}

	/**
	 * Cette m�thode permet de conna�tre le nombre de lignes pr�sentes dans le ResultSet en entr�e.
	 * @param resultat : L'objet ResultSet dont on veut conna�tre le nombre de lignes.
	 * @return le nombre de lignes pr�sentes dans le ResultSet.
	 */
	public int nbLignesResultat(ResultSet resultat) {

		int nombreLignes;

		try {

			// on place le curseur sur le dernier tuple
			resultat.last();
			// on r�cup�re le num�ro de la ligne
			nombreLignes = resultat.getRow();

			// System.out.println("Nombre de lignes " + nombreLignes);

			// on replace le curseur avant la premi�re ligne
			resultat.beforeFirst();

		} catch (SQLException erreur) {
			System.err
					.println("nbLignesResultat : Erreur a l'exploitation des resultats "
							+ erreur.getMessage());
			erreur.printStackTrace();
			return 1;
		}
		return nombreLignes;

	}

	/**
	 * Cette m�thode permet de conna�tre le nombre de colonnes d'un ResultSet.
	 * @param resultat : le ResultSet dont on veut conna�tre le nombre de colonnes.
	 * @return : le nombre de colonnes.
	 */
	public int nbColonnesResultat(ResultSet resultat) {
		int nombreColonnes;

		try {

			ResultSetMetaData resultatStructure = resultat.getMetaData();
			nombreColonnes = resultatStructure.getColumnCount();

		} catch (SQLException erreur) {
			System.err
					.println("Nb Colonnes : Erreur a l'exploitation des resultats"
							+ erreur.getMessage());
			erreur.printStackTrace();
			return 1;
		}
		return nombreColonnes;

	}

	/**
	 * Cette m�thode cr�e une liste (vecteur) des noms de colonnes pr�sentes 
	 * dans le ResultSet en entr�e.
	 * @param resultat : 
	 * @return : Un vecteur contenant les noms de colonnes.
	 */
	public Vector<String> listeColonnesResultat(ResultSet resultat) {

		try {

			ResultSetMetaData resultatStructure = resultat.getMetaData();
			int nbColonnes = resultatStructure.getColumnCount();
			Vector<String> listeColonnes = new Vector<String>();
			;

			String nomColonne;
			for (int i = 1; i <= nbColonnes; i++) {

				nomColonne = resultatStructure.getColumnName(i);
				System.out.println("Nom de la colonne " + nomColonne);
				listeColonnes.add(nomColonne);

			}
			return listeColonnes;

		} catch (SQLException erreur) {
			System.err
					.println("Liste Colonnes : Erreur a l'exploitation des resultats "
							+ erreur.getMessage());
			erreur.printStackTrace();
			return null;

		}
	}

}
