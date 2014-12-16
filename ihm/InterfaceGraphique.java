package projet.ihm;

import java.awt.Dimension;
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
	
	File repertoire;

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
		//ongletIndexation.setLayout(new GroupLayout(ongletIndexation));

		JLabel titreOngletIndexation = new JLabel("Choisissez le répertoire à indexer");
		ongletIndexation.add(titreOngletIndexation);


		// Bouton Repertoire
		bRepertoire = new JButton("Parcourir...");
		bRepertoire.setAlignmentX(CENTER_ALIGNMENT);
		BoutonListRep blisRepertoire = new BoutonListRep();
		bRepertoire.addActionListener(blisRepertoire);
		ongletIndexation.add(bRepertoire);

		// Liste de choix de langues
		choixLangueIndex = listeChoixLangue("indexation");
		ongletIndexation.add(choixLangueIndex);

		// Bouton Indexation
		bIndex = new JButton("Indexer");
		bIndex.setAlignmentX(CENTER_ALIGNMENT);
		BoutonListIndex blisIndex = new BoutonListIndex();
		bIndex.addActionListener(blisIndex);
		ongletIndexation.add(bIndex);

		retourAction = new JLabel();
		ongletIndexation.add(retourAction);
		
		// Taille de l'onglet
		ongletIndexation.setPreferredSize(new Dimension(400, 80));
		onglets.addTab("Indexation", ongletIndexation);

	}

	public void creationOngletRecherche() throws IOException {
		ongletRecherche = new JPanel();
		ongletRecherche.setPreferredSize(new Dimension(1055, 500));
		onglets.addTab("Recherche", ongletRecherche);

		// Icone
		BufferedImage myPicture = ImageIO.read(new File("/home/yoann/workspace/GOOGLE-VECTORLOGO-BIZ-128x128.png"));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		picLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(picLabel);

		ongletRecherche.setLayout(new BoxLayout(ongletRecherche, BoxLayout.PAGE_AXIS));
		ongletRecherche.add(picLabel);

		// Champ saisie recherche
		champSaisie = new JTextField();
		champSaisie.setAlignmentX(CENTER_ALIGNMENT);
		//champSaisie.setPreferredSize(new Dimension(10, 80));
		//champSaisie.setBounds(maximizedBounds);
		ongletRecherche.add(champSaisie);

		// Choix de la langue
		JPanel panelLangue = new JPanel();
		JLabel labelLangue = new JLabel("Choix de la langue (optionnel)");
		choixLangueRecherche = listeChoixLangue("recherche");
		panelLangue.add(labelLangue);
		panelLangue.add(choixLangueRecherche);

		ongletRecherche.add(panelLangue);

		// Bouton rechercher
		b = new JButton("Rechercher");
		b.setAlignmentX(CENTER_ALIGNMENT);
		BoutonListRecherche blis = new BoutonListRecherche();
		b.addActionListener(blis);
		ongletRecherche.add(b);

		// Panneau suggestion ontologie
		JPanel panelOnto = new JPanel();
		//JLabel labOnto = new JLabel("Nous vous suggérons d'ajouter ces mots à votre recherche : ");
		labOnto = new JLabel();
		labOnto.setAlignmentX(CENTER_ALIGNMENT);
		//labOnto2 = new JLabel("mot1, mot2, mot3");
		labOnto2 = new JLabel();
		labOnto2.setAlignmentX(CENTER_ALIGNMENT);
		ongletRecherche.add(panelOnto);
		panelOnto.add(labOnto);
		panelOnto.add(labOnto2);

		// Aire des résultats
		resultatsArea = new JTextArea(8,1);
		scrollResultatsArea = new JScrollPane(resultatsArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ongletRecherche.add(scrollResultatsArea);
		resultatsArea.setPreferredSize(new Dimension(10,20));
		resultatsArea.setBounds(10, 20, 30, 40);
		resultatsArea.setAlignmentX(CENTER_ALIGNMENT);

	}

	public InterfaceGraphique (List<IndexerAbs>listIndexer, String titre, int x, int y, int w, int h) throws IOException {

		super(titre);
		this.setBounds(x,y,w,h);
		this.setVisible(true);
		this.listIndexer = listIndexer;

		// onglet
		onglets = new JTabbedPane();
		creationOngletIndex();
		creationOngletRecherche();


		this.getContentPane().add(onglets);
		this.setVisible(true);


		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//permet de fermer la fenêtre quand l’utilisateur clique sur la croix

		this.setBounds(x,y,w,h); //Position et taille dans l’écran

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

