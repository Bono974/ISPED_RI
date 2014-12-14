package projet.interfaceGraphique;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import projet.index.EnIndexerImpl;
import projet.index.FrIndexerImpl;
import projet.index.IndexerAbs;
import projet.recherche.Recherche;
import projet.recherche.ScoreEtChemin;

public class InterfaceGraphique extends JFrame {
	static List<IndexerAbs>listIndexer;
	
	JButton b ;
	JTextField champSaisie;
	JPanel p;
	JMenuBar jmb;

	JTextArea resultatsArea;
	
	private static String source = "/home/yoann/Documents/CR3/";
	private static String indexLocationFR = "/home/yoann/Documents/IndexRI/FR/";
	private static String indexLocationEN = "/home/yoann/Documents/IndexRI/EN/";
	
	public InterfaceGraphique (String titre, int x, int y, int w, int h) throws IOException {

		super(titre);

		// Panel
		p = new JPanel();

		// menu
		jmb = new JMenuBar();
		this.setJMenuBar(jmb);
		JMenu mindexer = new JMenu ("Indexer");
		jmb.add(mindexer);
		JMenuItem lancerIndexation = new JMenuItem ("Lancer Indexation");
		mindexer.add(lancerIndexation);
		JMenu mrechercher = new JMenu ("Rechercher");
		jmb.add(mrechercher);
		JMenuItem lancerRecherche = new JMenuItem ("Lancer Recherche");
		mrechercher.add(lancerRecherche);

		// Icone
		BufferedImage myPicture = ImageIO.read(new File("/home/yoann/workspace/GOOGLE-VECTORLOGO-BIZ-128x128.png"));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		picLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(picLabel);

		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(picLabel);

		// Bouton rechercher
		b = new JButton("Rechercher");
		b.setAlignmentX(CENTER_ALIGNMENT);
		BoutonListener blis = new BoutonListener();
		b.addActionListener(blis);

		// Champ saisie recherche
		champSaisie = new JTextField();
		champSaisie.setAlignmentX(CENTER_ALIGNMENT);
		p.add(champSaisie);
		p.add(b);

		// Panneau suggestion ontologie
		JPanel panelOnto = new JPanel();
		JLabel labOnto = new JLabel("Nous vous suggérons d'ajouter ces mots à votre recherche : ");
		labOnto.setAlignmentX(CENTER_ALIGNMENT);
		JLabel labOnto2 = new JLabel("mot1, mot2, mot3");
		labOnto2.setAlignmentX(CENTER_ALIGNMENT);
		p.add(panelOnto);
		panelOnto.add(labOnto);
		panelOnto.add(labOnto2);

		// Aire des résultats
		resultatsArea = new JTextArea(8,1);
		//resultatsArea.setPreferredSize(new Dimension(10,20));
		//resultatsArea.setBounds(10, 20, 30, 40);
		resultatsArea.setAlignmentX(CENTER_ALIGNMENT);
		p.add(resultatsArea);
		
		
		/*
		// Affichage résultats de la recherche
		int nbDocument = 5;

		for (int i = 0 ;i<nbDocument;i++) {
			// Création des Jpanel
			JPanel panel = new JPanel();
			JLabel lab = new JLabel("Label "+i);
			JCheckBox check = new JCheckBox();
			check.setAlignmentX(CENTER_ALIGNMENT);
			lab.setAlignmentX(CENTER_ALIGNMENT);
			p.add(panel);
			panel.add(check);
			panel.add(lab);
		}
		 */

		add("North", p);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//permet de fermer la fenêtre quand l’utilisateur clique sur la croix

		this.setBounds(x,y,w,h); //Position et taille dans l’écran

		this.setVisible(true); //Dessine la fenêtre au 1er plan, par dessus 

	}

	public static void main(String[] args) throws IOException {
		listIndexer = new ArrayList<IndexerAbs>();
		try {
			listIndexer.add(new EnIndexerImpl(indexLocationEN, source));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			listIndexer.add(new FrIndexerImpl(indexLocationFR, source));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		new InterfaceGraphique ("Google Onto",400,300,1100	,400);
	}

	class BoutonListener implements ActionListener {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void actionPerformed(ActionEvent e) {
			
			
			
			// On prépare la liste de mots à rechercher
			String motsARechercher = champSaisie.getText();
			System.out.println(motsARechercher);
			// On transforme la saisie en liste de mots
			String[] tabMots = motsARechercher.split(" ");
			List<String> listMots = new ArrayList(Arrays.asList(tabMots));
			
			// On lance la rechercher à partir de la liste de mots saisis
			Recherche rechercheur = new Recherche();
			List<ScoreEtChemin> resultatsRecherche = null;
			try {
				resultatsRecherche = rechercheur.search(listIndexer, listMots);
				
				// Nombre de documents retournés
				//int nbDocuments = resultatsRecherche.size();
				int position = 0;
				//resultatsArea.setSelectionStart(0);
				//resultatsArea.setSelectionEnd(resultatsArea.getText().length());
				//resultatsArea.set(" ");
				resultatsArea.setText("");
				for (ScoreEtChemin cur: resultatsRecherche) {
					
					System.out.println("Document : "+cur.getScore()+" "+cur.getChemin());
					resultatsArea.insert(cur.getChemin()+"\n",position);
					
					//resultatsArea.setLineWrap(true);
					position++;
					/*
					JPanel panel = new JPanel();
					JLabel lab = new JLabel(cur.getChemin());
					JCheckBox check = new JCheckBox();
					check.setAlignmentX(CENTER_ALIGNMENT);
					lab.setAlignmentX(CENTER_ALIGNMENT);
					p.add(panel);
					panel.add(check);
					panel.add(lab);
					*/
					
				}
							
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

	}

}

