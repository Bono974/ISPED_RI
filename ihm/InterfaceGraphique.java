package projet.ihm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import projet.index.IndexerAbs;
import projet.recherche.Recherche;
import projet.recherche.ScoreEtChemin;

@SuppressWarnings("serial")
public class InterfaceGraphique extends JFrame {
	List<IndexerAbs>listIndexer;

	JButton b;
	JButton bIndex;
	JButton bRepertoire;
	JTextField champSaisie;
	JPanel p;
	JMenuBar jmb;
	JTabbedPane onglets;
	JTextArea resultatsArea;
	JScrollPane scrollResultatsArea;

	JPanel ongletRecherche;
	JPanel ongletIndexation;
	JLabel labOnto;
	JLabel labOnto2;
	JLabel retourAction;
	JComboBox<String> choixLangueIndex;
	JComboBox<String> choixLangueRecherche;
	
	GridBagConstraints gbc = new GridBagConstraints();

	File repertoire;

	String imageGoogle = "/Users/bruno/Documents/workspace/RI/src/GOOGLE-VECTORLOGO-BIZ-128x128.png";
	//String imageGoogle = "/home/yoann/workspace/GOOGLE-VECTORLOGO-BIZ-128x128.png";
	
	public JComboBox<String> listeChoixLangue(String utilisation) {

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

	public void creationOngletIndex() throws IOException {

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
		
		// Taille de l'onglet
		//ongletIndexation.setPreferredSize(new Dimension(400, 80));
		onglets.addTab("Indexation", ongletIndexation);

	}

	public void creationOngletRecherche() throws IOException {
		ongletRecherche = new JPanel();
		ongletRecherche.setLayout(new GridBagLayout());
		onglets.addTab("Recherche", ongletRecherche);

		// Icone
		BufferedImage myPicture = ImageIO.read(new File(imageGoogle));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));

		gbc.fill = GridBagConstraints.CENTER;
		//gbc.gridx = 0;
		//gbc.gridy = 0;
		
		ongletRecherche.add(picLabel, gbc);

		// Champ saisie recherche
		champSaisie = new JTextField();		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 250;
		ongletRecherche.add(champSaisie, gbc);

		// Bouton rechercher
		b = new JButton("Rechercher");
		BoutonListRecherche blis = new BoutonListRecherche();
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
		//JLabel labOnto = new JLabel("Nous vous suggérons d'ajouter ces mots à votre recherche : ");
		//labOnto2 = new JLabel("mot1, mot2, mot3");

		labOnto = new JLabel();
		labOnto2 = new JLabel();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 6;
		ongletRecherche.add(labOnto, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 6;
		ongletRecherche.add(labOnto2, gbc);

		// Aire des résultats
		resultatsArea = new JTextArea();
		scrollResultatsArea = new JScrollPane(resultatsArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.ipadx = 500;
		gbc.ipady = 300;
		ongletRecherche.add(scrollResultatsArea, gbc);
	}

	public InterfaceGraphique (List<IndexerAbs>listIndexer, String titre, int x, int y, int w, int h) throws IOException {

		super(titre);
		//this.setBounds(x,y,w,h);
		this.setVisible(true);
		this.listIndexer = listIndexer;

		// onglet
		onglets = new JTabbedPane();
		creationOngletIndex();
		creationOngletRecherche();


		this.getContentPane().add(onglets);
		this.setVisible(true);

		this.setMinimumSize(new Dimension(800, 700));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//permet de fermer la fenêtre quand l’utilisateur clique sur la croix

		//this.setBounds(x,y,w,h); //Position et taille dans l’écran

		this.setVisible(true); //Dessine la fenêtre au 1er plan, par dessus 

	}


	class BoutonListRep implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			File repertoireCourant = null;
			try {
				repertoireCourant = new File(".").getCanonicalFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JFileChooser choixRepertoire = new JFileChooser(repertoireCourant);
			choixRepertoire.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			// affichage
			choixRepertoire.showOpenDialog(null);
			// récupération du fichier sélectionné
			repertoire = choixRepertoire.getSelectedFile();
			System.out.println(repertoire.toString());
		}
	}

	class BoutonListIndex implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			// On regarde la langue choisie
			
			String langueChoisieIndex = (String) choixLangueIndex.getSelectedItem();
			if (!(langueChoisieIndex == null || langueChoisieIndex.isEmpty())) {
				System.out.println("L'indexation va s'effectuer en "+langueChoisieIndex);
				for(IndexerAbs cur: listIndexer) {
					if (cur.getLangue().equalsIgnoreCase(langueChoisieIndex)) {
						cur.action(repertoire.toString());
						System.out.println("Indexation du répertoire "+repertoire.toString()+" effectuée.");
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

					// Nombre de documents retournés
					//int nbDocuments = resultatsRecherche.size();
					int position;
					resultatsArea.setSelectionStart(0);
					resultatsArea.setSelectionEnd(resultatsArea.getText().length());
					resultatsArea.setText("");

					for (ScoreEtChemin cur: resultatsRecherche) {

						String doc = cur.getChemin()+"\n";
						System.out.println(doc);
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
					String listeMotsOnto = null;
					// Réinitialisation des labels
					labOnto.setText("");
					labOnto2.setText("");
					if (!(resultatsOnto == null || resultatsOnto.isEmpty())) {

						labOnto.setText("Nous vous suggérons d'ajouter ces mots à votre recherche : ");
						for (String motOnto: resultatsOnto) {

							if (listeMotsOnto == null) {
								listeMotsOnto = motOnto;
							}
							else {
								listeMotsOnto = listeMotsOnto+", "+motOnto;
							}
							labOnto2.setText(listeMotsOnto);

						}
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}


		}

	}

}

