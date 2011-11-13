package appDemineur.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class AppMenu extends JMenuBar implements ActionListener, ItemListener
{
	//
	private JMenu gameMenu;
	
	//
	private JMenu infoMenu;
	
	//
	private JMenuItem newGameMenu;
	
	//
	private JMenuItem scoresMenu;
	
	//
	private JMenuItem aboutMenu;
	
	//
	private JMenuItem helpMenu;
	
	//
	private JRadioButtonMenuItem easyDifficultyOptMenu;
	
	//
	private JRadioButtonMenuItem normalDifficultyOptMenu;
	
	//
	private JRadioButtonMenuItem hardDifficultyOptMenu;
	
	//
	private JCheckBoxMenuItem cheatChkMenu;
	
	//
	private ButtonGroup difficultyGroup;
	
	// 
	private AppFrame parent = null; 
	
	private static BufferedImage aboutLogo = null;

	
	// Initialisation des images
	static
	{
		try
		{
			AppMenu.aboutLogo = ImageIO.read(Board.class.getResource("../../demineur_logo.png"));
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Incapable de trouver une ou plusieurs image(s) de la classe Board.");
		}
	}
	
	/**
	 * 
	 */
	public AppMenu(AppFrame parent)
	{
		super();
		
		this.parent = parent;
		
		// Initialise les composants
		this.gameMenu = new JMenu();
		this.infoMenu = new JMenu();
		this.newGameMenu = new JMenuItem();
		this.scoresMenu = new JMenuItem();
		this.aboutMenu = new JMenuItem();
		this.helpMenu = new JMenuItem();
		this.easyDifficultyOptMenu = new JRadioButtonMenuItem();
		this.normalDifficultyOptMenu = new JRadioButtonMenuItem();
		this.hardDifficultyOptMenu = new JRadioButtonMenuItem();
		this.cheatChkMenu = new JCheckBoxMenuItem();
		this.difficultyGroup = new ButtonGroup();
		
		this.gameMenu.setText("Partie");
		this.infoMenu.setText("?");
		
		this.newGameMenu.setText("Nouvelle partie");
		this.newGameMenu.setActionCommand("NEW_GAME");
		this.newGameMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		
		this.easyDifficultyOptMenu.setText("Débutant");
		this.easyDifficultyOptMenu.setActionCommand("0");
		
		this.normalDifficultyOptMenu.setText("Intermédiaire");
		this.normalDifficultyOptMenu.setActionCommand("1");
		this.normalDifficultyOptMenu.setSelected(true);
		
		this.hardDifficultyOptMenu.setText("Expert");
		this.hardDifficultyOptMenu.setActionCommand("2");
		
		this.cheatChkMenu.setText("Tricher");
		this.cheatChkMenu.setActionCommand("CHEATS");
		this.cheatChkMenu.setSelected(false);
		
		this.scoresMenu.setText("Meilleurs temps...");
		this.scoresMenu.setActionCommand("SCORES");
		
		this.aboutMenu.setText("À propos...");
		this.aboutMenu.setActionCommand("ABOUT");
		
		this.helpMenu.setText("Aide");
		this.helpMenu.setActionCommand("HELP");
		
		this.difficultyGroup.add(this.easyDifficultyOptMenu);
		this.difficultyGroup.add(this.normalDifficultyOptMenu);
		this.difficultyGroup.add(this.hardDifficultyOptMenu);
		
		this.gameMenu.add(this.newGameMenu);
		this.gameMenu.addSeparator();
		this.gameMenu.add(this.easyDifficultyOptMenu);
		this.gameMenu.add(this.normalDifficultyOptMenu);
		this.gameMenu.add(this.hardDifficultyOptMenu);
		this.gameMenu.addSeparator();
		this.gameMenu.add(this.cheatChkMenu);
		this.gameMenu.addSeparator();
		this.gameMenu.add(this.scoresMenu);
		
		this.infoMenu.add(this.aboutMenu);
		this.infoMenu.add(this.helpMenu);
		
		this.add(this.gameMenu);
		this.add(this.infoMenu);
		
		// Spécifie les écouteurs pour les menus
		this.newGameMenu.addActionListener(this);
		this.scoresMenu.addActionListener(this);
		this.aboutMenu.addActionListener(this);
		this.helpMenu.addActionListener(this);
		this.cheatChkMenu.addItemListener(this);
	}
	
	/*
	 * Affiche la boîte de dialogue À propos
	 */
	private void showAboutDialog()
	{
		AppAboutDialog aboutDialog = new AppAboutDialog(this.parent);
		aboutDialog.setLocationRelativeTo(this.parent);
		aboutDialog.setVisible(true);
	}
	
	/*
	 * Affiche la boîte de dialogue d'aide
	 */
	private void showHelpDialog()
	{
		String aboutText = "Test";
		
		JOptionPane.showMessageDialog(this, 
				aboutText, 
				"Aide", 
				JOptionPane.PLAIN_MESSAGE, 
				AppMenu.aboutLogo != null ? new ImageIcon(AppMenu.aboutLogo) : null);
	}
	
	/**
	 * Affiche la boîte de dialogue des meilleurs temps
	 */
	public void showScoresDialog()
	{
		AppScoresDialog scoresDialog = new AppScoresDialog(this.parent);
		scoresDialog.setLocationRelativeTo(this.parent);
		scoresDialog.setVisible(true);
	}

	
	/**
	 * Retourne le niveau sélectionné dans le menu pour la prochaine partie.
	 * 
	 * @return le niveau sélectionné dans le menu pour la prochaine partie
	 */
	public int getNextGameLevel()
	{
		return Integer.parseInt(this.difficultyGroup.getSelection().getActionCommand());
	}
	
	/**
	 * Retourne l'état de l'élément de menu «Tricher».
	 * 
	 * @return l'état de l'élément de menu «Tricher»
	 */
	public boolean getCheatMode()
	{
		return this.cheatChkMenu.isSelected();
	}
	
	/**
	 * Méthode appelée quand l'état d'un élément de menu change.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @param evt évènement déclencheur
	 * 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent evt)
	{
		JMenuItem e = (JMenuItem) evt.getItem();
		
		if (e.getActionCommand().equals("CHEATS"))
		{
			this.parent.getBoard().repaint();
		}
	}
	
	/**
	 * Reçoit et traite les événements relatifs aux menus.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("NEW_GAME"))
		{
			this.parent.getBoard().replay();
			this.parent.getBoard().repaint();
		}
		else if (evt.getActionCommand().equals("SCORES"))
		{
//			this.parent.getBoard().showScoresDialog();
			this.showScoresDialog();
		}
		else if (evt.getActionCommand().equals("ABOUT"))
		{
//			this.parent.getBoard().showAboutDialog();
			this.showAboutDialog();
		}
		
		else if (evt.getActionCommand().equals("HELP"))
		{
//			this.parent.getBoard().showHelpDialog();
			this.showHelpDialog();
		}
	}
}
