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

/**
 * La classe AppHelpDialog sert de boîte de dialogue pour l'aide. 
 *
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class AppHelpDialog extends JDialog implements ActionListener, WindowListener
{
    // Panneau contenant le bouton fermer
	private JPanel buttonsPanel;
    
    // Bouton pour fermer la boîte
    private JButton closeButton;
    
    // Panneau contenant les TextPanes
    private JPanel helpPanel;

    // TextPane contenant le but du jeu 
    private JTextPane goalTextPane;
    
    // TextPane contenant les règles du jeu 
    private JTextPane rulesTextPane;
    
	/**
	 * Construit la boîte de dialogue d'aide. 
	 * 
	 * @param parent objet parent de la boîte de dialogue
	 */
	public AppHelpDialog(AppFrame parent)
	{
		super(parent);
		
		this.setTitle("Aide");
		this.setResizable(false);
		this.setModal(true);
		
		// Initialise les composants
		this.initComponents();
		
		// Remplit les TextPanes avec l'information de l'aide du jeu
		this.showHelp();
		
		this.pack();
		
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// Cette boîte de dialogue implémente son propre écouteur Window
		this.addWindowListener(this);
	}

	// Initialise les composants
    private void initComponents()
    {
        this.buttonsPanel = new JPanel();
        this.closeButton = new JButton();
        this.helpPanel = new JPanel();
        this.goalTextPane = new JTextPane();
        this.rulesTextPane = new JTextPane();

        this.buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        this.closeButton.setText("Fermer");
        this.closeButton.setActionCommand("CLOSE");
        this.buttonsPanel.add(this.closeButton);

        this.getContentPane().add(this.buttonsPanel, BorderLayout.PAGE_END);

        this.helpPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
        
        this.goalTextPane.setMaximumSize(null);
        this.goalTextPane.setMinimumSize(null);
        this.goalTextPane.setPreferredSize(new Dimension(100, 60));
        this.goalTextPane.setEditable(false);
        this.goalTextPane.setOpaque(false);
        this.helpPanel.add(this.goalTextPane);

        this.rulesTextPane.setMaximumSize(null);
        this.rulesTextPane.setMinimumSize(null);
        this.rulesTextPane.setPreferredSize(new Dimension(100, 60));
        this.rulesTextPane.setEditable(false);
        this.rulesTextPane.setOpaque(false);
        this.helpPanel.add(this.rulesTextPane);

        this.getContentPane().add(this.helpPanel, BorderLayout.CENTER);
        
        // Spécifie les écouteurs d'action pour les boutons
        this.closeButton.addActionListener(this);
    }
    
	// Remplit les TextPanes avec l'information de l'aide
    private void showHelp()
    {
    	String goal = "But";
    	
    	String rules = "Règles";
    	
		// Remplit les TextPanes
		this.goalTextPane.setText(goal);
		this.rulesTextPane.setText(rules);
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
	public void windowActivated(WindowEvent evt)
	{
	}

	@Override
	public void windowClosed(WindowEvent evt)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent evt)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent evt)
	{
	}

	@Override
	public void windowIconified(WindowEvent evt)
	{
	}

	@Override
	public void windowOpened(WindowEvent evt)
	{
	}
}
