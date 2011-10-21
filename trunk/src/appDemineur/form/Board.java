package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import appDemineur.model.Cell.CellState;
import appDemineur.model.Game;

/**
 * La classe Board gère l'interface du jeu du Démineur.
 * C'est elle qui gère les évènements de l'interface utilisateur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Board extends JPanel implements ActionListener, MouseListener
{
	// 
	public static final int GRID_SIZE = 16;
	// 
	public static final int GRID_MINE = 40;

	// Objet de la partie courante
	private Game currentGame = null;
	// 
	private JPanel buttonPanel;
	// 
	private JPanel gamePanel;
	// 
	private JButton newGridButton;
	
	/**
	 * Construit un plateau de jeu.
	 * Le plateau d'une partie initiale est d'une dimension de 16x16
	 * et contient 40 mines.
	 */
	public Board()
	{
		super();
		
        // Initialise les composantes
		this.buttonPanel = new JPanel();
		this.gamePanel = new JPanel();
		this.newGridButton = new JButton();
		
		this.setLayout(new BorderLayout());
		
		this.newGridButton.setText("Nouvelle grille");
		this.newGridButton.setActionCommand("NEW_GRID");
		
		this.buttonPanel.add(this.newGridButton);
		this.buttonPanel.setBackground(Color.WHITE);
		
		this.add(buttonPanel, BorderLayout.PAGE_END);
		this.add(gamePanel, BorderLayout.CENTER);
		
		// Spécifie les écouteurs pour les boutons
		this.gamePanel.addMouseListener(this);
		this.newGridButton.addActionListener(this);
		
		// Replay va se charger de créer une nouvelle partie 
		this.replay();
	}
	
	// Réinitialise la partie
	private void replay()
	{
		// Crée une nouvelle partie
		this.currentGame = new Game(Board.GRID_SIZE, Board.GRID_SIZE, Board.GRID_MINE);
	}
	
	// Retourne la plus grande taille de grille possible d'entrer à l'intérieur des dimensions du « gamePanel ».
	private float getMaximumGridSize()
	{
		float sizeWidth = (float) this.gamePanel.getWidth() / Board.GRID_SIZE;
		float sizeHeight = (float) this.gamePanel.getHeight() / Board.GRID_SIZE;
		
		float size = sizeWidth;
		
		if (sizeHeight < sizeWidth)
		{
			size = sizeHeight;
		}
		
		return size;
	}
	
	// Retourne le point indiquant la coordonnée pour centrer la grille de jeu.
	private Point getCenterPoint()
	{
		int x = (int) ((this.gamePanel.getWidth() / 2) - ((Board.GRID_SIZE * this.getMaximumGridSize()) / 2));
		int y = (int) ((this.gamePanel.getHeight() / 2) - ((Board.GRID_SIZE * this.getMaximumGridSize()) / 2));
		
		return new Point(x, y);
	}
	
	/**
	 * Redessine le plateau de jeu.
	 * Vous ne devriez pas à appeler cette méthode directement.
	 * Sa visibilité est à «package» pour que AppFrame puisse l'appeler.   
	 */
	void redraw()
	{
		Graphics buffer = null;
		Image bufferImg = null;
		
		if (this.gamePanel.getWidth() > 0 && this.gamePanel.getHeight() > 0)
		{
			bufferImg = this.createImage(this.gamePanel.getWidth(), this.gamePanel.getHeight());
			buffer = bufferImg.getGraphics();
		}
		
		Graphics g = this.gamePanel.getGraphics();
		
		if (g != null && buffer != null)
		{
			buffer.setClip(0, 0, this.gamePanel.getWidth(), this.gamePanel.getHeight());
			this.currentGame.redraw(buffer, this.getMaximumGridSize(), this.getCenterPoint());
			g.drawImage(bufferImg, 0, 0, this);
		}
	}
	
	/**
	 * Reçoit et traite les événements relatifs aux boutons
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("NEW_GRID"))
		{
			this.currentGame.startNewGame(Board.GRID_SIZE, Board.GRID_SIZE, Board.GRID_MINE);
			this.redraw();
		}
	}
	
	/**
	 * Reçoit et traite les événements relatifs au clic de souris.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	public void mouseClicked(MouseEvent evt)
	{
		boolean changed = false;
		System.out.println(String.format("Clic à (%d, %d)", evt.getX(), evt.getY()));
		
		// Work in progress ....
		if (evt.getButton() == MouseEvent.BUTTON1)
		{			
			if (this.currentGame.changeCellState(
					(int) ((evt.getX() - this.getCenterPoint().x) / this.getMaximumGridSize()), 
					(int) ((evt.getY() - this.getCenterPoint().y) / this.getMaximumGridSize()),
					CellState.SHOWN))
			{
				changed = true;
			}
		}
		else if (evt.getButton() == MouseEvent.BUTTON3)
		{
			if (this.currentGame.changeCellState(
					(int) ((evt.getX() - this.getCenterPoint().x) / this.getMaximumGridSize()), 
					(int) ((evt.getY() - this.getCenterPoint().y) / this.getMaximumGridSize()),
					CellState.FLAGGED))
			{
				changed = true;
			}
		}
		
		if (changed)
		{
			this.redraw();
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}