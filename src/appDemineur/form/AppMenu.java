package appDemineur.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import appDemineur.model.Game;

/**
 * La classe AppMenu implémente la barre de menus du jeu du démineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class AppMenu extends JMenuBar implements ActionListener, ItemListener
{
	// Menu «Partie»
	private JMenu gameMenu;
	
	// Menu «?»
	private JMenu infoMenu;
	
	// Élément «Nouvelle partie»
	private JMenuItem newGameMenu;
	
	// Élément «Meilleurs temps...»
	private JMenuItem scoresMenu;
	
	// Élément «À propos»
	private JMenuItem aboutMenu;
	
	// Élément «Aide»
	private JMenuItem helpMenu;
	
	// Option «Débutant»
	private JRadioButtonMenuItem easyDifficultyOptMenu;
	
	// Option «Intermédiaire»
	private JRadioButtonMenuItem normalDifficultyOptMenu;
	
	// Option «Expert»
	private JRadioButtonMenuItem hardDifficultyOptMenu;
	
	// Case à cocher «Tricher»
	private JCheckBoxMenuItem cheatChkMenu;
	
	// Groupe d'options des niveaux
	private ButtonGroup difficultyGroup;
	
	// Objet parent
	private AppFrame parent = null;
	
	/**
	 * Construit la barre de menus du jeu application du Démineur.
	 * 
	 * @param parent objet parent de la barre de menu
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
		
		this.easyDifficultyOptMenu.setText(Game.LEVELS[0].displayName);

		this.easyDifficultyOptMenu.setActionCommand("0");
		
		this.normalDifficultyOptMenu.setText(Game.LEVELS[1].displayName);
		this.normalDifficultyOptMenu.setActionCommand("1");
		this.normalDifficultyOptMenu.setSelected(true);
		
		this.hardDifficultyOptMenu.setText(Game.LEVELS[2].displayName);
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
		AppHelpDialog helpDialog = new AppHelpDialog(this.parent);
		helpDialog.setLocationRelativeTo(this.parent);
		helpDialog.setVisible(true);
	}
	
	/**
	 * Affiche la boîte de dialogue des meilleurs temps.
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
			// Nouvelle partie
			this.parent.getBoard().replay();
			this.parent.getBoard().repaint();
		}
		else if (evt.getActionCommand().equals("SCORES"))
		{
			// Affiche les meilleurs temps
			this.showScoresDialog();
		}
		else if (evt.getActionCommand().equals("ABOUT"))
		{
			// Affiche la boîte «À propos»
			this.showAboutDialog();
		}
		
		else if (evt.getActionCommand().equals("HELP"))
		{
			// Affiche la boîte «Aide»
			this.showHelpDialog();
		}
	}
}
