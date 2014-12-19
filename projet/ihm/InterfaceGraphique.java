package projet.ihm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import java.awt.Toolkit;

import projet.index.IndexerAbs;
import projet.recherche.Recherche;
import projet.recherche.RetourDocument;
import projet.recherche.ScoreEtChemin;

@SuppressWarnings("serial")
public class InterfaceGraphique extends JFrame {

	/**
	 * Liste des indexer à traiter
	 */
	final List<IndexerAbs>listIndexer;

	/**
	 * Bouton de lancement de la recherche
	 */
	JButton b;

	/**
	 * Bouton au lancement de l'index
	 */
	JButton bIndex;

	/**
	 * Bouton du choix de répertoire
	 */
	JButton bRepertoire;
	JTextField champSaisie;
	JPanel p;
	JMenuBar jmb;
	JTabbedPane onglets;
	JTextArea resultatsArea;
	JTextArea resultatRechercheOnto;
	JScrollPane scrollResultatsArea;
	JScrollPane scrollResultatsAreaOnto;

	JPanel ongletRecherche;
	JPanel ongletIndexation;
	JLabel labOnto;
	JLabel labOnto2;
	JLabel retourAction;
	JLabel nomRepLabel;
	JComboBox<String> choixLangueIndex;
	JComboBox<String> choixLangueRecherche;

	GridBagConstraints gbc = new GridBagConstraints();

	File repertoire;

	String imageLogo = "../mantle-lucene.png";

	/**
	 * Constructeur de InterfaceGraphique
	 * @param listIndexer Liste des index à traiter
	 * @param titre Titre de la fenêtre
	 * @throws IOException
	 */
	public InterfaceGraphique (final List<IndexerAbs>listIndexer, String titre) throws IOException {
		super(titre);
		this.setVisible(true);
		this.listIndexer = listIndexer;

		// onglet
		onglets = new JTabbedPane();
		creationOngletIndex();
		creationOngletRecherche();
		
		this.getContentPane().add(onglets);
		this.setVisible(true);

		this.setMinimumSize(new Dimension(900, 700));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//permet de fermer la fenêtre quand l’utilisateur clique sur la croix

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		this.setVisible(true); //Dessine la fenêtre au 1er plan, par dessus 
		//this.setResizable(false);
	}

	/**
	 * Cette méthode gère la combobox de choix de la langue
	 * pour l'onglet Indexation et l'onglet Recherche
	 * @param utilisation Indique si on est dans un contexte d'indexation
	 * de recherche. Pour l'indexation, le choix d'une langue est obligatoire
	 * Pour la recherche il est possible de n'en choisir aucune
	 * @return La ComboBox à afficher
	 */
	private JComboBox<String> listeChoixLangue(String utilisation) {
		JComboBox<String> choixLangue;

		List<String> listeLangue = new ArrayList<String>();
		if (utilisation.equalsIgnoreCase("recherche")) {
			listeLangue.add("");
		}

		for (IndexerAbs indexATraiter: listIndexer) {
			listeLangue.add(indexATraiter.getLangue());
			// On trie par ordre alpha
		}
		Collections.sort(listeLangue);
		choixLangue = new JComboBox<String>();
		for (String langue: listeLangue) {
			choixLangue.addItem(langue);
			// On trie par ordre alpha
		}
		return choixLangue;
	}

	/**
	 * Cette méthode gère l'affichage du contenu de l'onglet Indexation
	 * @throws IOException
	 */
	private void creationOngletIndex() throws IOException {
		// Panel de l'index
		ongletIndexation = new JPanel();

		JLabel titreOngletIndexation = new JLabel("Choisissez le répertoire à indexer");
		ongletIndexation.add(titreOngletIndexation);

		// Bouton Repertoire
		bRepertoire = new JButton("Parcourir...");
		//bRepertoire.setAlignmentX(CENTER_ALIGNMENT);
		BoutonListRep blisRepertoire = new BoutonListRep();
		bRepertoire.addActionListener(blisRepertoire);
		ongletIndexation.add(bRepertoire);

		// Liste de choix de langues
		choixLangueIndex = listeChoixLangue("indexation");
		ongletIndexation.add(choixLangueIndex);

		// Bouton Indexation
		bIndex = new JButton("Indexer");
		//bIndex.setAlignmentX(CENTER_ALIGNMENT);
		BoutonListIndex blisIndex = new BoutonListIndex();
		bIndex.addActionListener(blisIndex);
		ongletIndexation.add(bIndex);

		retourAction = new JLabel();
		ongletIndexation.add(retourAction);

		// Label du nom de répertoire
		nomRepLabel = new JLabel();
		ongletIndexation.add(nomRepLabel);

		// Taille de l'onglet
		//ongletIndexation.setPreferredSize(new Dimension(400, 80));
		onglets.addTab("Indexation", ongletIndexation);
	}

	/**
	 * Cette méthode gère l'affichage de l'onglet Recherche
	 * @throws IOException
	 */
	private void creationOngletRecherche() throws IOException {
		ongletRecherche = new JPanel();
		ongletRecherche.setLayout(new GridBagLayout());
		onglets.addTab("Recherche", ongletRecherche);

		// Icone
		JLabel picLabel = new JLabel(new ImageIcon(getClass().getResource(imageLogo)));

		gbc.fill = GridBagConstraints.CENTER;
		ongletRecherche.add(picLabel, gbc);

		// Champ saisie recherche
		BoutonListRecherche blis = new BoutonListRecherche();

		champSaisie = new JTextField();		
		champSaisie.addActionListener(blis);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 250;
		ongletRecherche.add(champSaisie, gbc);

		// Bouton rechercher
		b = new JButton("Rechercher");
		b.addActionListener(blis);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipadx = 20;
		ongletRecherche.add(b, gbc);

		// Choix de la langue
		JLabel labelLangue = new JLabel("Choix de la langue (optionnel)");
		choixLangueRecherche = listeChoixLangue("recherche");

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		ongletRecherche.add(labelLangue, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.ipadx = 20;
		ongletRecherche.add(choixLangueRecherche, gbc);

		// Panneau suggestion ontologie
		labOnto = new JLabel();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 6;
		ongletRecherche.add(labOnto, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 6;

		// Aire des résultats
		resultatsArea = new JTextArea();
		scrollResultatsArea = new JScrollPane(resultatsArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.ipadx = 500;
		gbc.ipady = 200;
		ongletRecherche.add(scrollResultatsArea, gbc);

		resultatRechercheOnto = new JTextArea();
		scrollResultatsAreaOnto = new JScrollPane(resultatRechercheOnto,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.ipadx = 500;
		gbc.ipady = 200;
		ongletRecherche.add(scrollResultatsAreaOnto, gbc);
	}



	/**
	 * Classe de gestion du bouton de parcours des répertoires à indexer
	 * @author yoann
	 *
	 */
	class BoutonListRep implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			File repertoireCourant = null;
			try {
				repertoireCourant = new File(".").getCanonicalFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			JFileChooser choixRepertoire = new JFileChooser(repertoireCourant);
			choixRepertoire.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			// affichage
			int returnVal = choixRepertoire.showOpenDialog(null);
			// récupération du fichier sélectionné

			String res = "Aucun répertoire choisi";
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				repertoire = choixRepertoire.getSelectedFile();
				System.out.println(repertoire.toString());
				res = repertoire.toString();
			} else System.out.println(res);
		
			// On réinitialise le label de compte rendu de résultat d'indexation
			retourAction.setText("");

			// On affiche le nom du répertoire.
			nomRepLabel.setText(res);
		}
	}

	/**
	 * Classe de gestion du bouton Indexer
	 * @author yoann
	 *
	 */
	class BoutonListIndex implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// On regarde la langue choisie
			String langueChoisieIndex = (String) choixLangueIndex.getSelectedItem();
			if (!(langueChoisieIndex == null || langueChoisieIndex.isEmpty())) {
				System.out.println("L'indexation va s'effectuer en "+langueChoisieIndex);
				for(IndexerAbs cur: listIndexer) {
					if (cur.getLangue().equalsIgnoreCase(langueChoisieIndex)) {
						try {
							cur.action(repertoire.toString());
						} catch (FileNotFoundException e1) {
							nomRepLabel.setText("Le répertoire n'existe pas.");

						} catch (IOException e1) {
							nomRepLabel.setText("Le répertoire d'index n'existe pas.");
						}
						System.out.println("Indexation du répertoire "+repertoire.toString()+" effectuée.");
						nomRepLabel.setText("");
						retourAction.setText("Indexation du répertoire "+repertoire.toString()+" effectuée.");
					}
				}
			}
			else {
				System.out.println("Aucune langue choisie. L'indexation ne peut être lancée.");
				retourAction.setText("Aucune langue choisie. L'indexation ne peut être lancée.");
			}
			// On lance l'indexation du répertoire
		}
	}

	/**
	 * Classe de gestion du bouton Rechercher
	 * @author yoann
	 *
	 */
	class BoutonListRecherche implements ActionListener {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void actionPerformed(ActionEvent e) {
			// On prépare la liste de mots à rechercher
			String motsARechercher = champSaisie.getText();
			// On vérifie qu'un mot a été saisie ou bien qu'il y a autre chose que des caractères blancs
			if (motsARechercher == null || motsARechercher.equals("") || motsARechercher.trim().equals("") ) {
				resultatsArea.setText("Aucun mot à rechercher n'a été saisie.");
			}
			else {
				// On transforme la saisie en liste de mots
				String[] tabMots = motsARechercher.split(" ");
				List<String> listMots = new ArrayList(Arrays.asList(tabMots));

				// On lance la rechercher à partir de la liste de mots saisis
				Recherche rechercheur = new Recherche();
				List<ScoreEtChemin> resultatsRecherche = null;
				try {

					String langueChoisie = (String) choixLangueRecherche.getSelectedItem();
					if (!(langueChoisie == null || langueChoisie.isEmpty())) {
						System.out.println("La recherche s'effectue sur l'index de la langue "+langueChoisie);
						resultatsRecherche = rechercheur.search(listIndexer, langueChoisie, listMots);
					}
					else {
						System.out.println("La recherche s'effectue sur toute les langues");
						resultatsRecherche = rechercheur.search(listIndexer, listMots);
					}

					List<RetourDocument> retourDoc = rechercheur.rechercheSGDB(resultatsRecherche);
					
					// Nombre de documents retournés
					//int nbDocuments = resultatsRecherche.size();
					int position;
					resultatsArea.setSelectionStart(0);
					resultatsArea.setSelectionEnd(resultatsArea.getText().length());
					resultatsArea.setText("");

					for (ScoreEtChemin cur: resultatsRecherche) {
						String nomCourt = cur.getChemin().substring(cur.getChemin().lastIndexOf("/")+1);
						String complement = null;

						System.out.println("Nom court= "+nomCourt);

						// On prépare la ligne à afficher						
						if (retourDoc != null) {
							for (RetourDocument curseurRetour: retourDoc) {
								if (nomCourt.equalsIgnoreCase(curseurRetour.getNomDocument())) {
									// Le document a des métadonnées, on les affiche

									complement = " (TITRE = "+curseurRetour.getTitreDocument()
											+ ", AUTEUR = "+curseurRetour.getAuteurDocument()
											+ ", DATE = "+curseurRetour.getDateDocument()
											+ ", SUJET = "+curseurRetour.getSujetDocument()+")";
								}
							}
						}
						String doc = null;
						if (complement == null) {
							doc = cur.getChemin()+"\n";
						}
						else {
							doc = cur.getChemin()+complement+"\n";
						}

						System.out.println("Document : "+cur.getScore()+" "+doc);
						position = resultatsArea.getCaretPosition();
						resultatsArea.insert(doc,position);
						resultatsArea.setLineWrap(true);
					}
					// Recherche Ontologie
					List<String> resultatsOnto = new ArrayList<String>();
					//resultatsOnto = rechercheur.rechercheOntologie(listMots);
					resultatsOnto = rechercheur.rechercheOntologie(listMots);
					// Récupération des mots de l'ontologie
					// Réinitialisation des labels
					labOnto.setText("");
					resultatRechercheOnto.setSelectionStart(0);
					resultatRechercheOnto.setSelectionEnd(resultatsArea.getText().length());
					resultatRechercheOnto.setText("");
					if (!(resultatsOnto == null || resultatsOnto.isEmpty())) {
						labOnto.setText("Nous vous suggérons d'ajouter ces mots à votre recherche : ");
						int positionOnto;

						for (String motOnto: resultatsOnto) {
							positionOnto = resultatRechercheOnto.getCaretPosition();
							resultatRechercheOnto.insert(motOnto+"\n",positionOnto);
							resultatRechercheOnto.setLineWrap(true);
							System.out.println(motOnto);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}

