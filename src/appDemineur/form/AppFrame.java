package appDemineur.form;

import java.awt.Dimension;
import java.awt.Graphics;
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
	// Dimension initiale de la fenêtre
	private static final Dimension INIT_SIZE = new Dimension(624, 683);
	
	// Largeur minimale de la fenêtre
	private static final int MIN_WIDTH = 400;

	// Hauteur minimale de la fenêtre
	private static final int MIN_HEIGHT = 300;
	
	// Titre de la fenêtre
	private static final String INIT_TITLE = "Démineur par Alexandre Tremblay et Christian Lesage";
	
	// Objet du plateau de jeu
	private Board mBoard; 
	
	/**
	 * Crée une nouvelle fenêtre contenant un nouveau plateau de jeu.
	 */
	public AppFrame()
	{
		super();
		
		this.setTitle(AppFrame.INIT_TITLE);
		this.setSize(AppFrame.INIT_SIZE);
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mBoard = new Board();
		this.getContentPane().add(this.mBoard);
		
		this.addComponentListener(this);
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
