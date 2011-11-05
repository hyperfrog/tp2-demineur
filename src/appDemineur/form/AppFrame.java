package appDemineur.form;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 * La classe AppFrame permet de cr�er une fen�tre qui sert de contenant
 * pour le plateau de jeu du D�mineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 * 
 */
public class AppFrame extends JFrame implements ComponentListener
{
	// Sert � l'impl�mentation du singleton
	private static AppFrame instance = null;
	
	// Dimension initiale de la fen�tre
	private static final Dimension INIT_SIZE = new Dimension(640, 745);
	
	// Largeur minimale de la fen�tre
	private static final int MIN_WIDTH = 640;

	// Hauteur minimale de la fen�tre
	private static final int MIN_HEIGHT = 480;
	
	// Titre de la fen�tre
	private static final String INIT_TITLE = "D�mineur par Alexandre Tremblay et Christian Lesage";
	
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
	
	/*
	 * Cr�e la fen�tre du jeu, qui contient les menus et le plateau de jeu
	 */
	protected AppFrame()
	{
		super();
		
		this.setNativeLookAndFeel();
		
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
		
		this.easyDifficultyOptMenu.setText("D�butant");
		this.easyDifficultyOptMenu.setActionCommand("0");
		
		this.normalDifficultyOptMenu.setText("Interm�diaire");
		this.normalDifficultyOptMenu.setActionCommand("1");
		this.normalDifficultyOptMenu.setSelected(true);
		
		this.hardDifficultyOptMenu.setText("Expert");
		this.hardDifficultyOptMenu.setActionCommand("2");
		
		this.cheatChkMenu.setText("Tricher");
		this.cheatChkMenu.setActionCommand("CHEATS");
		this.cheatChkMenu.setSelected(false);
		
		this.scoresMenu.setText("Meilleurs temps...");
		this.scoresMenu.setActionCommand("SCORES");
		
		this.aboutMenu.setText("� propos...");
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
		
		this.mBoard = new Board(this);
		this.getContentPane().add(this.mBoard);
		
		this.newGameMenu.addActionListener(this.mBoard);
		this.scoresMenu.addActionListener(this.mBoard);
		this.aboutMenu.addActionListener(this.mBoard);
		this.helpMenu.addActionListener(this.mBoard);
		
		this.cheatChkMenu.addItemListener(this.mBoard);
		
		this.addComponentListener(this);
	}
	
	
	// TODO : Tester le singleton?
	/**
	 * Cr�e et retourne la fen�tre du jeu en tant que singleton.
	 *  
	 * La fen�tre du jeu contient les menus et un plateau de jeu de type Board.
	 * 
	 * @see appDemineur.form.Board
	 * 
	 * @return l'instance du singleton
	 */
	public static AppFrame getInstance()
	{
		if (AppFrame.instance == null)
		{
			AppFrame.instance = new AppFrame();
		}
		return AppFrame.instance;
	}
	
	/**
	 * Retourne le niveau s�lectionn� dans le menu pour la prochaine partie.
	 * 
	 * @return le niveau s�lectionn� dans le menu pour la prochaine partie
	 */
	public int getNextGameLevel()
	{
		return Integer.parseInt(this.difficultyGroup.getSelection().getActionCommand());
	}
	
	/**
	 * Retourne l'�tat de l'�l�ment de menu �Tricher�.
	 * 
	 * @return l'�tat de l'�l�ment de menu �Tricher�
	 */
	public boolean getCheatMode()
	{
		return this.cheatChkMenu.isSelected();
	}
	
	/** 
	 * M�thode appel�e quand la fen�tre est cach�e.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 * 
	 * @param e �v�nement d�clencheur
	 */
	@Override
	public void componentHidden(ComponentEvent e)
	{
	}
	
	/**
	 * M�thode appel�e quand la fen�tre est d�plac�e.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 * 
	 * @param e �v�nement d�clencheur
	 */
	@Override
	public void componentMoved(ComponentEvent e)
	{
	}

	/**
	 * M�thode appel�e quand la fen�tre est redimensionn�e.
	 * On s'assure que ses dimensions respectent la largeur et la hauteur minimales permises.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 * 
	 * @param e �v�nement d�clencheur
	 */
	@Override
	public void componentResized(ComponentEvent e)
	{
		int width = getWidth();
		int height = getHeight();
		// V�rifie si la largeur et la hauteur sont inf�rieures 
		// � la valeur minimale permise pour chacune
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
	 * M�thode appel�e quand la fen�tre est affich�e.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 * 
	 * @param e �v�nement d�clencheur
	 */
	@Override
	public void componentShown(ComponentEvent e)
	{
	}
	
	private void setNativeLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.out.println("Error setting native LAF: " + e);
		}
	}
	
}
