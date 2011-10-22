package appDemineur.form;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

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
	// Dimension initiale de la fen�tre
	private static final Dimension INIT_SIZE = new Dimension(624, 683);
	
	// Largeur minimale de la fen�tre
	private static final int MIN_WIDTH = 400;

	// Hauteur minimale de la fen�tre
	private static final int MIN_HEIGHT = 300;
	
	// Titre de la fen�tre
	private static final String INIT_TITLE = "D�mineur par Alexandre Tremblay et Christian Lesage";
	
	// Objet du plateau de jeu
	private Board mBoard; 
	
	/**
	 * Cr�e une nouvelle fen�tre contenant un nouveau plateau de jeu.
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
	 * Redessine la fen�tre.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 * 
	 * @param g Graphics de la fen�tre
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		this.mBoard.redraw();		
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
	public void componentHidden(ComponentEvent arg0)
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
	public void componentMoved(ComponentEvent arg0)
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
	public void componentResized(ComponentEvent arg0)
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
	public void componentShown(ComponentEvent arg0)
	{
	}
	
}
