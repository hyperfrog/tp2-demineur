package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

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
	private int currentLevel = 1;

	// Objet de la partie courante
	private Game currentGame = null;
	
	// 
	private boolean cheatMode = false;
	
	// 
	private JPanel gamePanel;
	
	// 
	private JButton newGameButton;
	
	// 
	private JPanel buttonPanel;
	
	//   
	private JLabel timerLabel;
	
	//  
	private JLabel flagsLabel;
	
	//
	private Timer timer;
	
	//
	private int elapsedTime;
	
	/**
	 * Construit un plateau de jeu.
	 * Le plateau d'une partie initiale est d'une dimension de 16x16
	 * et contient 40 mines.
	 */
	public Board()
	{
		super();
		
        // Initialise les composantes
		this.gamePanel = new JPanel();
		this.buttonPanel = new JPanel();
		this.newGameButton = new JButton();
		this.timerLabel = new JLabel();
		this.flagsLabel = new JLabel();
		
		this.setLayout(new BorderLayout());
		
		this.newGameButton.setText("Nouvelle grille");
		this.newGameButton.setActionCommand("NEW_GAME");
		
		this.buttonPanel.setLayout(new GridLayout(0, 3));
		this.buttonPanel.setBackground(Color.WHITE);
		
		this.buttonPanel.add(this.timerLabel);
		this.buttonPanel.add(this.newGameButton);
		this.buttonPanel.add(this.flagsLabel);
		
		this.add(this.gamePanel, BorderLayout.CENTER);
		this.add(this.buttonPanel, BorderLayout.PAGE_START);
		
		// Spécifie les écouteurs pour les boutons
		this.gamePanel.addMouseListener(this);
		this.newGameButton.addActionListener(this);
		
		this.timer = new Timer(1000, this);
		this.timer.setActionCommand("TICK");
		this.timer.start();
		
		// Replay va se charger de créer une nouvelle partie 
		this.replay();
	}
	
	// Réinitialise la partie
	private void replay()
	{
		int response = 0;
		
		// Demande une confirmation si une partie est en cours
		if (this.currentGame != null && !this.currentGame.isOver())
		{
			response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir commencer une nouvelle partie ? \nLa partie en cours n'est pas terminée.",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		}
		// Si le joueur est certain de vouloir recommencer 
		if (response == 0)
		{
			// Crée une nouvelle partie
//			this.currentGame = new Game(Board.LEVELS[Board.CURRENT_LEVEL].dim.width, Board.LEVELS[Board.CURRENT_LEVEL].dim.height, Board.LEVELS[Board.CURRENT_LEVEL].mineAmount);
			this.currentGame = new Game(this.currentLevel);
			
			this.timerLabel.setText("Temps : 0");
			this.flagsLabel.setText("Mines : " + this.currentGame.getMineAmount());
			
			this.elapsedTime = 0;
			this.timer.restart();
		}
	}
	
	// Retourne la plus grande taille de cellule possible en fonction des dimensions du « gamePanel ».
	private float getCellSize()
	{
		float size = 0;
		
		if (this.currentGame.getWidth() > 0 && this.currentGame.getHeight() > 0)
		{
			float width = (float) this.gamePanel.getWidth() / this.currentGame.getWidth();
			float height = (float) this.gamePanel.getHeight() / this.currentGame.getHeight();

			size = height < width ? height : width;
		}
		
		return size >= 0 ? size : 0;
	}
	
	// Retourne le point du coin supérieur droit de la grille de jeu 
	// de manière à centrer celle-ci.
	private Point getGridOffset()
	{
		float cellSize = this.getCellSize();
		int x = Math.round((this.gamePanel.getWidth() - (this.currentGame.getWidth() * cellSize)) / 2);
		int y = Math.round((this.gamePanel.getHeight() - (this.currentGame.getHeight() * cellSize)) / 2);
		
		return new Point(x > 0 ? x : 0, y > 0 ? y : 0);
	}
	
	/**
	 * Redessine le plateau de jeu.
	 * Vous ne devriez pas avoir à appeler cette méthode directement.
	 * Sa visibilité est à «package» pour que AppFrame puisse l'appeler.   
	 */
	void redraw()
	{
		Graphics buffer = null;
		Image bufferImg = null;

		float cellSize = this.getCellSize();
		int gridWidth = Math.round(cellSize * this.currentGame.getWidth()) >= 1 ? Math.round(cellSize * this.currentGame.getWidth()) : 1;
		int gridHeight = Math.round(cellSize * this.currentGame.getHeight()) >= 1 ? Math.round(cellSize * this.currentGame.getHeight()) : 1;
		
//		System.out.println(cellSize + ", " + gridSize);
		
		bufferImg = this.createImage(gridWidth, gridHeight);
		buffer = bufferImg.getGraphics();
		
		Graphics g = this.gamePanel.getGraphics();
		
		if (g != null && buffer != null)
		{
			Point gridOffset = this.getGridOffset();

			this.currentGame.redraw(buffer, cellSize, this.getCheatMode());
			g.drawImage(bufferImg, gridOffset.x, gridOffset.y, this);
		}
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setCheatMode(boolean enabled)
	{
		this.cheatMode = enabled;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getCheatMode()
	{
		return this.cheatMode;
	}
	
	/**
	 * 
	 * @param newLevel
	 */
	public void setCurrentLevel(int newLevel)
	{
		this.currentLevel = (newLevel < 0 || newLevel > 2) ? 0 : newLevel;
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
		if (evt.getActionCommand().equals("TICK"))
		{
			if (!this.currentGame.isOver())
			{
				this.elapsedTime++;
				this.timerLabel.setText("Temps : " + this.elapsedTime);
			}
		}
		else if (evt.getActionCommand().equals("NEW_GAME"))
		{
			this.replay();
			this.redraw();
		}
		else if (evt.getActionCommand().equals("SCORES"))
		{
			//
		}
		else if (evt.getActionCommand().equals("ABOUT"))
		{
			//
		}
		else if (evt.getActionCommand().equals("HELP"))
		{
			//
		}
	}
	
	/**
	 * Reçoit et traite les événements relatifs aux clics de la souris.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	public void mouseReleased(MouseEvent evt)
	{
//		System.out.println(String.format("Clic à (%d, %d)", evt.getX(), evt.getY()));
		
		if (!this.currentGame.isOver())
		{
			Boolean show = null;
			
			if (evt.getButton() == MouseEvent.BUTTON1) // Bouton gauche
			{
				show = true;
			}
			else if (evt.getButton() == MouseEvent.BUTTON3) // Bouton droit
			{
				show = false;
			}

			if (show != null)
			{
				Point gridOffset = this.getGridOffset();

				if (evt.getX() - gridOffset.x >= 0 && evt.getY() - gridOffset.y >= 0)
				{
					float cellSize = this.getCellSize();

					if (this.currentGame.changeCellState(
							(int)((evt.getX() - gridOffset.x) / cellSize), 
							(int)((evt.getY() - gridOffset.y) / cellSize),
							show))
					{
						this.redraw();
						this.flagsLabel.setText("Mines : " + (this.currentGame.getMineAmount() - this.currentGame.getNbFlags()));
					}
				}
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
}