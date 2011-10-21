package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import appDemineur.model.Cell;
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
//		this.addMouseListener(this);
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
			
			float sizeWidth = (float) this.gamePanel.getWidth() / Board.GRID_SIZE;
			float sizeHeight = (float) this.gamePanel.getHeight() / Board.GRID_SIZE;
			float size = sizeWidth;
			
			if (sizeHeight < sizeWidth)
			{
				size = sizeHeight;
			}
			
			this.currentGame.redraw(buffer, size);
			
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
			this.currentGame.newGame(Board.GRID_SIZE, Board.GRID_SIZE, Board.GRID_MINE);
			this.redraw();
		}
	}
	
	/**
	 * Reçoit et traite les événements relatifs à la souris
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.ActionEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	public void mouseClicked(MouseEvent evt)
	{
		System.out.println(String.format("Clic à (%d, %d)", evt.getX(), evt.getY()));
		
		float sizeWidth = (float) this.gamePanel.getWidth() / Board.GRID_SIZE;
		float sizeHeight = (float) this.gamePanel.getHeight() / Board.GRID_SIZE;
		float size = sizeWidth;
		
		if (sizeHeight < sizeWidth)
		{
			size = sizeHeight;
		}
		
		if (this.currentGame.changeCellState((int)(evt.getX() / size), (int)(evt.getY() / size)))
		{
			this.redraw();
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}