package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import appDemineur.model.BestTimes;
import appDemineur.model.Game;

/**
 * La classe AppScoresDialog sert de boîte de dialogue pour les meilleurs temps. 
 *
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */

public class AppScoresDialog extends JDialog implements ActionListener, WindowListener
{
    // Panneau contenant les boutons
	private JPanel buttonsPanel;

	// Bouton pour effacer les scores
    private JButton eraseButton;
    
    // Bouton pour fermer la boîte
    private JButton okButton;
    
    // Panneau contenant les TextPanes
    private JPanel scoresPanel;
    
    // TextPane contenant les noms des niveaux 
    private JTextPane levelsTextPane;
    
    // TextPane contenant les meilleurs temps 
    private JTextPane timesTextPane;
    
    // TextPane contenant les noms des joueurs 
    private JTextPane playersTextPane;
    
    // Objet de gestion des meilleurs temps
    private BestTimes bestTimes;
    
	/**
	 * Construit la boîte de dialogue des meilleurs temps. 
	 * 
	 * @param parent objet parent de la boîte de dialogue
	 */
	public AppScoresDialog(AppFrame parent)
	{
		super(parent);
		
		// 
		this.bestTimes = new BestTimes();
		
		this.setTitle("Démineurs les plus rapides");
		this.setResizable(false);
		this.setModal(true);
		
		// Initialise les composants
		this.initComponents();
		
		// Remplit les TextPanes avec l'information des meilleurs temps
		this.showScores();
		
		this.pack();

		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		// Cette boîte de dialogue implémente so propre écouteur Window 
		this.addWindowListener(this);
	}

	// Initialise les composants
    private void initComponents() {

        this.buttonsPanel = new JPanel();
        this.eraseButton = new JButton();
        this.okButton = new JButton();
        this.scoresPanel = new JPanel();
        this.levelsTextPane = new JTextPane();
        this.timesTextPane = new JTextPane();
        this.playersTextPane = new JTextPane();

        this.buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        this.eraseButton.setText("Effacer les scores");
        this.eraseButton.setActionCommand("ERASE");
        this.buttonsPanel.add(this.eraseButton);

        this.okButton.setText("OK");
        this.okButton.setActionCommand("CLOSE");
        this.buttonsPanel.add(this.okButton);

        this.getContentPane().add(this.buttonsPanel, BorderLayout.PAGE_END);

        this.scoresPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

        this.levelsTextPane.setMaximumSize(null);
        this.levelsTextPane.setMinimumSize(null);
        this.levelsTextPane.setPreferredSize(new Dimension(100, 60));
        this.levelsTextPane.setEditable(false);
        this.levelsTextPane.setOpaque(false);
        this.scoresPanel.add(this.levelsTextPane);

        this.timesTextPane.setMaximumSize(null);
        this.timesTextPane.setMinimumSize(null);
        this.timesTextPane.setPreferredSize(new Dimension(100, 60));
        this.timesTextPane.setEditable(false);
        this.timesTextPane.setOpaque(false);
        this.scoresPanel.add(this.timesTextPane);

        this.playersTextPane.setMaximumSize(null);
        this.playersTextPane.setMinimumSize(null);
        this.playersTextPane.setPreferredSize(new Dimension(100, 60));
        this.playersTextPane.setEditable(false);
        this.playersTextPane.setOpaque(false);
        this.scoresPanel.add(this.playersTextPane);

        this.getContentPane().add(this.scoresPanel, BorderLayout.CENTER);
        
        // Spécifie les écouteurs d'action pour les boutons
        this.eraseButton.addActionListener(this);
        this.okButton.addActionListener(this);
    }
    
	// Remplit les TextPanes avec l'information des meilleurs temps
    private void showScores()
    {
    	String levels = "";
    	String times = "";
    	String players = "";
    	
		// Pour chacun des niveaux de difficulté
    	for (int i = 0; i < Game.LEVELS.length; i++)
		{
    		// Ajoute le nom du niveau
			levels += Game.LEVELS[i].displayName + " :\n";
			
			// Obtient le meilleur temps
			String time =  this.bestTimes.getTime(i);
			
			if (time != "")
			{
				times += time + " seconde(s)";
			}
			else
			{
				times += "Aucun record";
			}
			times += "\n";
			
			// Obtient le nom du joueur
			String player = this.bestTimes.getPlayer(i);

			if (player != "")
			{
				players += player;
			}
			players += "\n";
		}
    	
		// Remplit les TextPanes
		this.levelsTextPane.setText(levels);
		this.timesTextPane.setText(times);
		this.playersTextPane.setText(players);
    }
	
    // Efface les données des meilleurs temps
    private void eraseScores()
    {
		for (int i = 0; i < Game.LEVELS.length; i++)
    	{
			this.bestTimes.setPlayer(i, "");
			this.bestTimes.setTime(i, "");
    	}
		this.showScores();
		this.bestTimes.write();
    }
    
    // Ferme la boîte de dialogue
	private void close()
	{
		this.setVisible(false);
		this.dispose();
	}
	
	/**
	 * Reçoit et traite les événements relatifs aux boutons
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("CLOSE"))
		{
			this.close();
		}
		if (evt.getActionCommand().equals("ERASE"))
		{
			this.eraseScores();
		}
	}
	
	/** 
	 * Méthode appelée quand la fenêtre va être fermée.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @param evt événement déclencheur
	 */
	@Override
	public void windowClosing(WindowEvent evt)
	{
		this.close();
	}
	
	@Override
	public void windowActivated(WindowEvent evt) {}

	@Override
	public void windowClosed(WindowEvent evt) {}

	@Override
	public void windowDeactivated(WindowEvent evt) {}

	@Override
	public void windowDeiconified(WindowEvent evt) {}

	@Override
	public void windowIconified(WindowEvent evt) {}

	@Override
	public void windowOpened(WindowEvent evt) {}
	
}
