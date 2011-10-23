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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import appDemineur.model.Cell.CellState;
import appDemineur.model.Game;

/**
 * La classe Board g�re l'interface du jeu du D�mineur.
 * C'est elle qui g�re les �v�nements de l'interface utilisateur.
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
	public static final int MINE_AMOUNT = 40;

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
		
		// Sp�cifie les �couteurs pour les boutons
		this.gamePanel.addMouseListener(this);
		this.newGridButton.addActionListener(this);
		
		// Replay va se charger de cr�er une nouvelle partie 
		this.replay();
	}
	
	// R�initialise la partie
	private void replay()
	{
		int response = 0;
		
		// Demande une confirmation si une partie est en cours
		if (this.currentGame != null && !this.currentGame.isOver())
		{
			response = JOptionPane.showConfirmDialog(this, "�tes-vous s�r de vouloir commencer une nouvelle partie ? \nLa partie en cours n'est pas termin�e.",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		}
		// Si le joueur est certain de vouloir recommencer 
		if (response == 0)
		{
			// Cr�e une nouvelle partie
			this.currentGame = new Game(Board.GRID_SIZE, Board.GRID_SIZE, Board.MINE_AMOUNT);
		}
	}
	
	// Retourne la plus grande taille de cellule possible en fonction des dimensions du � gamePanel �.
	private float getCellSize()
	{
		float width = (float) this.gamePanel.getWidth() / Board.GRID_SIZE;
		float height = (float) this.gamePanel.getHeight() / Board.GRID_SIZE;
		
		float size = height < width ? height : width;
		
		return size > 0 ? size : 0;
	}
	
	// Retourne le point du coin sup�rieur droit de la grille de jeu 
	// de mani�re � centrer celle-ci.
	private Point getGridOffset()
	{
		float cellSize = this.getCellSize();
		int x = Math.round((this.gamePanel.getWidth() - (Board.GRID_SIZE * cellSize)) / 2);
		int y = Math.round((this.gamePanel.getHeight() - (Board.GRID_SIZE * cellSize)) / 2);
		
		return new Point(x > 0 ? x : 0, y > 0 ? y : 0);
	}
	
	/**
	 * Redessine le plateau de jeu.
	 * Vous ne devriez pas avoir � appeler cette m�thode directement.
	 * Sa visibilit� est � �package� pour que AppFrame puisse l'appeler.   
	 */
	void redraw()
	{
		Graphics buffer = null;
		Image bufferImg = null;

		float cellSize = this.getCellSize();
		int gridSize = Math.round(cellSize * Board.GRID_SIZE) >= 1 ? Math.round(cellSize * Board.GRID_SIZE) : 1;
		
//		System.out.println(cellSize + ", " + gridSize);
		
		bufferImg = this.createImage(gridSize, gridSize);
		buffer = bufferImg.getGraphics();
		
		Graphics g = this.gamePanel.getGraphics();
		
		if (g != null && buffer != null)
		{
			Point gridOffset = this.getGridOffset();

			this.currentGame.redraw(buffer, cellSize);
			g.drawImage(bufferImg, gridOffset.x, gridOffset.y, this);
		}
	}
	
	/**
	 * Re�oit et traite les �v�nements relatifs aux boutons
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * @param evt �v�nement d�clencheur
	 */
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("NEW_GRID"))
		{
			this.replay();
			this.redraw();
		}
	}
	
	/**
	 * Re�oit et traite les �v�nements relatifs aux clics de la souris.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 * @param evt �v�nement d�clencheur
	 */
	public void mouseReleased(MouseEvent evt)
	{
//		System.out.println(String.format("Clic � (%d, %d)", evt.getX(), evt.getY()));
		
		if (!this.currentGame.isOver())
		{
			CellState newState = null;

			if (evt.getButton() == MouseEvent.BUTTON1)
			{
				newState = CellState.SHOWN;
			}
			else if (evt.getButton() == MouseEvent.BUTTON3)
			{
				newState = CellState.FLAGGED;
			}

			if (newState != null)
			{
				Point gridOffset = this.getGridOffset();
				float cellSize = this.getCellSize();

				if (this.currentGame.changeCellState(
						(int)((evt.getX() - gridOffset.x) / cellSize), 
						(int)((evt.getY() - gridOffset.y) / cellSize),
						newState))
				{
					this.redraw();
				}
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
}