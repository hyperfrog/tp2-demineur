package appDemineur.form;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

/**
 * La classe AppFrame permet de créer une fenêtre qui sert de contenant
 * pour le plateau de jeu du Démineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 * 
 */
public class AppFrame extends JFrame implements ComponentListener
{
	// Sert à l'implémentation du singleton
	private static AppFrame instance = null;
	
	// Dimension initiale de la fenêtre
	private static final Dimension INIT_SIZE = new Dimension(640, 745);
	
	// Largeur minimale de la fenêtre
	private static final int MIN_WIDTH = 640;
	
	// Hauteur minimale de la fenêtre
	private static final int MIN_HEIGHT = 480;
	
	// Titre de la fenêtre
	private static final String INIT_TITLE = "Démineur par Alexandre Tremblay et Christian Lesage";
	
	// Objet du plateau de jeu
	private Board gameBoard;
	
	// Menu de l'application
	private AppMenu appMenu;

	/*
	 * Crée la fenêtre du jeu, qui contient les menus et le plateau de jeu
	 */
	protected AppFrame()
	{
		super();
		
		// Initialise le menu
		this.appMenu = new AppMenu(this);
		
		this.setJMenuBar(this.appMenu);
		
		this.setTitle(AppFrame.INIT_TITLE);
		this.setSize(AppFrame.INIT_SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.gameBoard = new Board(this);
		this.getContentPane().add(this.gameBoard);
		
		// Spécifie les écouteurs pour la fenêtre
		this.addComponentListener(this);
		this.addFocusListener(this.gameBoard);
	}
	
	// TODO : Tester le singleton?
	/**
	 * Crée et retourne la fenêtre du jeu en tant que singleton.
	 *  
	 * La fenêtre du jeu contient les menus et un plateau de jeu de type Board.
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
	 * Retourne l'objet gameBoard qui contient la partie en cours.
	 * 
	 * @return l'objet gameBoard qui contient la partie en cours
	 */
	public Board getBoard()
	{
		return this.gameBoard;
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
	public void componentHidden(ComponentEvent e)
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
	public void componentMoved(ComponentEvent e)
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
	public void componentResized(ComponentEvent e)
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
	public void componentShown(ComponentEvent e)
	{
	}
}
