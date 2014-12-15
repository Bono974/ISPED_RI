package projet.ihm;

import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import projet.index.IndexerAbs;

public class InterfaceGUI extends InterfacePrincipaleAbs {

	private JFrame monInterface;
	
	public InterfaceGUI(List<IndexerAbs> lIndexer) throws IOException {
		super(lIndexer, "graphique");
		this.monInterface = new InterfaceGraphique (lIndexer, "Google Onto",400,300,1100	,400);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		monInterface.setVisible(true);
		// TODO Auto-generated method stub
		
	}

}
