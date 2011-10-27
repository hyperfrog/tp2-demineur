package appDemineur.form;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 * La classe AppFrame permet de créer une fenêtre qui sert de contenant
 * pour le plateau de jeu du Démineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 * 
 */

public class AppFrame extends JFrame implements ComponentListener, ItemListener
{
	// Dimension initiale de la fenêtre
	private static final Dimension INIT_SIZE = new Dimension(624, 683);
	
	// Largeur minimale de la fenêtre
	private static final int MIN_WIDTH = 350;

	// Hauteur minimale de la fenêtre
	private static final int MIN_HEIGHT = 410;
	
	// Titre de la fenêtre
	private static final String INIT_TITLE = "Démineur par Alexandre Tremblay et Christian Lesage";
	
	// Objet du plateau de jeu
	private Board mBoard;
	
	//
	private JMenuBar menuBar;
	
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
	
	/**
	 * Crée une nouvelle fenêtre contenant un nouveau plateau de jeu.
	 */
	public AppFrame()
	{
		super();
		
		this.menuBar = new JMenuBar();
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
		
		this.aboutMenu.setText("À Propos...");
		this.aboutMenu.setActionCommand("ABOUT");
		
		this.helpMenu.setText("Aide");
		this.helpMenu.setActionCommand("HELP");
		
		//
		this.difficultyGroup.add(this.easyDifficultyOptMenu);
		this.difficultyGroup.add(this.normalDifficultyOptMenu);
		this.difficultyGroup.add(this.hardDifficultyOptMenu);
		
		//
		this.gameMenu.add(this.newGameMenu);
		this.gameMenu.addSeparator();
		this.gameMenu.add(this.easyDifficultyOptMenu);
		this.gameMenu.add(this.normalDifficultyOptMenu);
		this.gameMenu.add(this.hardDifficultyOptMenu);
		this.gameMenu.addSeparator();
		this.gameMenu.add(this.cheatChkMenu);
		this.gameMenu.addSeparator();
		this.gameMenu.add(this.scoresMenu);
		
		//
		this.infoMenu.add(this.aboutMenu);
		this.infoMenu.add(this.helpMenu);
		
		//
		this.menuBar.add(this.gameMenu);
		this.menuBar.add(this.infoMenu);
		
		//
		this.setJMenuBar(this.menuBar);
		
		this.setTitle(AppFrame.INIT_TITLE);
		this.setSize(AppFrame.INIT_SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.mBoard = new Board();
		this.getContentPane().add(this.mBoard);
		
		this.newGameMenu.addActionListener(this.mBoard);
		this.scoresMenu.addActionListener(this.mBoard);
		this.aboutMenu.addActionListener(this.mBoard);
		this.helpMenu.addActionListener(this.mBoard);
		
		this.easyDifficultyOptMenu.addItemListener(this);
		this.normalDifficultyOptMenu.addItemListener(this);
		this.hardDifficultyOptMenu.addItemListener(this);
		this.cheatChkMenu.addItemListener(this);
		
		this.addComponentListener(this);
	}
	
	/**
	 * 
	 */
	@Override
	public void itemStateChanged(ItemEvent evt)
	{
		JMenuItem e = (JMenuItem) evt.getItem();
		
		if (e.getActionCommand().equals("CHEATS"))
		{
			this.mBoard.setCheatMode(evt.getStateChange() == ItemEvent.SELECTED);
		}
		else if ((e.getActionCommand().equals("0") || e.getActionCommand().equals("1") || e.getActionCommand().equals("2")) 
				&& evt.getStateChange() == ItemEvent.SELECTED)
		{
			this.mBoard.setCurrentLevel(Integer.parseInt(e.getActionCommand()));
		}
	}
	
	/**
	 * Redessine la fenêtre.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 * 
	 * @param g Graphics de la fenêtre
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		this.mBoard.redraw();		
	}

	/** 
	 * Méthode appelée quand la fenêtre est cachée.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 * 
	 * @param e événement déclencheur
	 */
	@Override
	public void componentHidden(ComponentEvent arg0)
	{
	}
	
	/**
	 * Méthode appelée quand la fenêtre est déplacée.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 * 
	 * @param e événement déclencheur
	 */
	@Override
	public void componentMoved(ComponentEvent arg0)
	{
	}

	/**
	 * Méthode appelée quand la fenêtre est redimensionnée.
	 * On s'assure que ses dimensions respectent la largeur et la hauteur minimales permises.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 * 
	 * @param e événement déclencheur
	 */
	@Override
	public void componentResized(ComponentEvent arg0)
	{
		int width = getWidth();
		int height = getHeight();
		// Vérifie si la largeur et la hauteur sont inférieures 
		// à la valeur minimale permise pour chacune
		boolean resize = false;
		if (width < AppFrame.MIN_WIDTH)
		{
			resize = true;
			width = AppFrame.MIN_WIDTH;
		}
		if (height < AppFrame.MIN_HEIGHT)
		{
			resize = true;
			height = AppFrame.MIN_HEIGHT;
		}
		if (resize)
		{
			setSize(width, height);
		}
	}

	/**
	 * Méthode appelée quand la fenêtre est affichée.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 * 
	 * @param e événement déclencheur
	 */
	@Override
	public void componentShown(ComponentEvent arg0)
	{
	}
}
