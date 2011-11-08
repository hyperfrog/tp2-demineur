package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import appDemineur.model.Cell;
import appDemineur.model.Game;
import appDemineur.model.BestTimes;

/**
 * La classe Board implémente l'interface du jeu du Démineur.
 * Elle gère la plupart des évènements de l'interface utilisateur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Board extends JPanel implements ActionListener, MouseListener, ItemListener
{
	// Si vrai, la taille des cellules est fixée au plus grand entier inférieur à la taille possible
	// Si faux, la taille des cellules comporte une partie fractionnaire
	private static final boolean USE_CELL_SIZE_FLOOR = true;
	
	// Objet de la partie courante
	private Game currentGame = null;
	
	// Panneau dans lequel la partie est dessinée
	private DrawingPanel gamePanel;
	
	// Bouton pour créer une nouvelle partie
	private JButton newGameButton;
	
	// Panneau contenant les contrôles
	private JPanel controlPanel;
	
	// Libellé pour la minuterie
	private JLabel timerLabel;
	
	// Libellé pour le nombre de cases marquées d'un drapeau
	private JLabel flagsLabel;
	
	// TODO : la minuterie doit s'arrêter quand la fenêtre n'est pas active
	// Minuterie déclenchée une fois par seconde
	private Timer timer;
	
	// Temps écoulé pour la partie en cours en secondes
	private int elapsedTime;
	
	// Objet parent
	private AppFrame parent = null;
	
	//
	private BestTimes bestTimes = null;
	
	// Images utilisées pour le bouton newGameButton
	private static BufferedImage smileyWon = null;
	private static BufferedImage smileyNormal = null;
	private static BufferedImage smileyLost = null;
	
	// Initialisation des images
	static
	{
		try
		{
			Board.smileyWon = ImageIO.read(Cell.class.getResource("../../smiley_won.png"));
			Board.smileyNormal = ImageIO.read(Cell.class.getResource("../../smiley_normal.png"));
			Board.smileyLost = ImageIO.read(Cell.class.getResource("../../smiley_lost.png"));
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Incapable de trouver une ou plusieurs image(s) de la classe Board.");
		}
	}
	
	/**
	 * Construit un plateau de jeu.
	 * Le plateau d'une partie initiale est d'une dimension de 16x16
	 * et contient 40 mines.
	 * 
	 * @param parent Objet parent du panneau, doit être du type AppFrame
	 */
	public Board(AppFrame parent)
	{
		super();
		
		this.parent = parent;
		this.bestTimes = new BestTimes("c:\\file.xml");
		
        // Initialise les composantes
		this.gamePanel = new DrawingPanel(this);
		this.controlPanel = new JPanel();
		this.newGameButton = new JButton();
		this.timerLabel = new JLabel();
		this.flagsLabel = new JLabel();
		
		this.setLayout(new BorderLayout());
		
		this.newGameButton.setActionCommand("NEW_GAME");
//		this.newGameButton.setMinimumSize(new Dimension(54, 54));
//		this.newGameButton.setMaximumSize(new Dimension(54, 54));
		this.newGameButton.setPreferredSize(new Dimension(60, 60));
		this.newGameButton.setToolTipText("Cliquez ici pour commencer une nouvelle partie.");
		this.newGameButton.setContentAreaFilled(false);
		this.newGameButton.setFocusable(false);
//		this.newGameButton.setFocusPainted(false);
//		this.newGameButton.setBorder(null);
//		this.newGameButton.setBorderPainted(false);
		
		this.timerLabel.setHorizontalAlignment(JLabel.CENTER);
		this.timerLabel.setFont(new Font(null, Font.BOLD, 20));
		this.flagsLabel.setHorizontalAlignment(JLabel.CENTER);
		this.flagsLabel.setFont(new Font(null, Font.BOLD, 20));
		
		this.controlPanel.setLayout(new GridLayout(0, 3));
		this.controlPanel.setBorder(LineBorder.createBlackLineBorder());
		
		JPanel newGameButtonPanel = new JPanel();
		newGameButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		newGameButtonPanel.add(newGameButton);
		
		this.controlPanel.add(this.timerLabel);
		this.controlPanel.add(newGameButtonPanel);
		this.controlPanel.add(this.flagsLabel);
		
		this.add(this.gamePanel, BorderLayout.CENTER);
		this.add(this.controlPanel, BorderLayout.PAGE_START);
		
		// Spécifie les écouteurs pour le panneau de jeu et le bouton
		this.gamePanel.addMouseListener(this);
		this.newGameButton.addActionListener(this);
		
		// Fixe le délai de la minuterie à 1 seconde et spécifie son écouteur
		this.timer = new Timer(1000, this);
		this.timer.setActionCommand("TICK");
		
		// Replay se charge de créer une nouvelle partie 
		this.replay();
	}
	
	// Réinitialise la partie
	private void replay()
	{
		int response = 0;
		
		// Demande une confirmation si une partie est en cours
		if (this.currentGame != null && !this.currentGame.isOver() && this.currentGame.getNbCellsShown() > 0)
		{
			response = JOptionPane.showConfirmDialog(this, 
					"Êtes-vous sûr de vouloir commencer une nouvelle partie ? \nLa partie en cours n'est pas terminée.",
					"Confirmation", JOptionPane.YES_NO_OPTION);
		}
		// Si le joueur est certain de vouloir recommencer 
		if (response == 0)
		{
			// Crée une nouvelle partie
			this.currentGame = new Game(this.parent.getNextGameLevel());
			
			this.flagsLabel.setText("Mines : " + this.currentGame.getMineAmount());
			
			if (Board.smileyNormal != null)
			{
				this.newGameButton.setIcon(new ImageIcon(Board.smileyNormal));
				this.newGameButton.setText("");
			}
			else
			{
				this.newGameButton.setIcon(null);
				this.newGameButton.setText(":¬)");
			}
			
			this.elapsedTime = 0;
//			this.timer.restart();
			this.timer.stop();
			this.timerLabel.setText("Temps : " + String.format("%03d", this.elapsedTime));
			
		}
	}
	
	private void winGame(Game g)
	{
		// On parcours la matrice au complet
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < g.getHeight(); j++)
			{
				// On découvre toutes les cellules qui ne sont pas des mines
				if (!g.getElement(i, j).isMine())
				{
					g.showCell(i, j);
				}
			}
		}
	}
	
	// 
	private void showScoresDialog()
	{
		String scoreboard = "";
		
		String[] difficulty = new String[] {"Débutant", "Intermédiaire", "Expert"};
		
		for (int i = 0; i < 3; i++)
		{
			String time = bestTimes.getData(
					"/best_times/level[@name='" + BestTimes.levelNames[this.currentGame.getCurrentLevel()] + "']/time");
			
			String player = bestTimes.getData(
					"/best_times/level[@name='" + BestTimes.levelNames[this.currentGame.getCurrentLevel()] + "']/player");
			
			scoreboard += String.format("%s :\n%s, %s seconde(s)\n\n", difficulty[i], player, time);
		}
		
		JOptionPane.showMessageDialog(this, scoreboard, "Meilleurs temps...", JOptionPane.PLAIN_MESSAGE, new ImageIcon(Board.smileyWon));
	}
	
	// Cette classe pourrait implémenter le mouse listener
	/**
	 * La classe DrawingPanel ne sert qu'à fournir un accès à 
	 * la méthode paintComponent() du panneau de jeu, qui est un enfant 
	 * du panneau de type Board.
	 * 
	 * @author Christian Lesage
	 * @author Alexandre Tremblay
	 *
	 */
	private static class DrawingPanel extends JPanel
	{
		private Board parent = null;
		
		/**
		 * Construit un panneau dans lequel il est possible de dessiner.
		 * 
		 * @param parent Objet parent du panneau, doit être du type Board
		 */
		public DrawingPanel(Board parent)
		{
			this.parent = parent;
			this.setBackground(Color.LIGHT_GRAY);
		}

		/**
		 * Redessine le plateau de jeu.
		 * Vous ne devriez pas avoir à appeler cette méthode directement.
		 * @param g Graphics dans lequel le panneau doit se dessiner
		 * 
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			if (g != null && this.parent != null)
			{
				BufferedImage image = new BufferedImage(
						this.parent.getGridScreenWidth(), 
						this.parent.getGridScreenHeight(), 
						BufferedImage.TYPE_INT_ARGB);
				
				Graphics g2 = image.getGraphics();

				Point gridOffset = this.parent.getGridOffset();
				
				this.parent.currentGame.redraw(
						g2, 
						this.parent.getCellSize(), 
						this.parent.currentGame.isOver() || this.parent.parent.getCheatMode());
				
				g.drawImage(image, gridOffset.x, gridOffset.y, null);
			}
		}
	}
	
	// Retourne la plus grande taille de cellule possible en fonction des dimensions du « gamePanel ».
	private float getCellSize()
	{
		float size = 0;
		
		if (this.currentGame.getWidth() > 0 && this.currentGame.getHeight() > 0)
		{
			float width = (float) Math.max(this.gamePanel.getWidth(), 0) / this.currentGame.getWidth();
			float height = (float) Math.max(this.gamePanel.getHeight(), 0) / this.currentGame.getHeight();

			size = Math.min(height, width);
		}
		
		return Board.USE_CELL_SIZE_FLOOR ? (float) Math.floor(size) : size;
	}
	
	// Retourne le point du coin supérieur droit de la grille de manière à la centrer 
	// dans un panneau de jeu de dimensions variables
	private Point getGridOffset()
	{
		int x = Math.round((this.gamePanel.getWidth() - this.getGridScreenWidth()) / 2);
		int y = Math.round((this.gamePanel.getHeight() - this.getGridScreenHeight()) / 2);
		
		return new Point(Math.max(x, 0), Math.max(y, 0));
	}
	
	// Retourne la largeur de la grille à l'écran (en pixels)
	private int getGridScreenWidth()
	{
		return Math.max(Math.round(this.getCellSize() * this.currentGame.getWidth()), 1);
	}
	
	// Retourne la hauteur de la grille à l'écran (en pixels)
	private int getGridScreenHeight()
	{
		return Math.max(Math.round(this.getCellSize() * this.currentGame.getHeight()), 1);
	}

	/**
	 * Reçoit et traite les événements relatifs aux boutons, à la minuterie et aux menus.
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
				this.timerLabel.setText("Temps : " + String.format("%03d", this.elapsedTime));
			}
		}
		else if (evt.getActionCommand().equals("NEW_GAME"))
		{
			this.replay();
			this.repaint();
		}
		else if (evt.getActionCommand().equals("SCORES"))
		{
			// TODO : meilleurs scores
		}
		else if (evt.getActionCommand().equals("ABOUT"))
		{
			// TODO : À propos
		}
		else if (evt.getActionCommand().equals("HELP"))
		{
			// TODO : Aide
		}
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
			this.repaint();
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
		if (!this.currentGame.isOver())
		{
			Point gridOffset = this.getGridOffset();

			if (evt.getX() - gridOffset.x >= 0 && evt.getY() - gridOffset.y >= 0)
			{
				float cellSize = this.getCellSize();

				Point clickedCell = new Point(
						(int)((evt.getX() - gridOffset.x) / cellSize),
						(int)((evt.getY() - gridOffset.y) / cellSize));

				boolean hasChanged = false;

				switch (evt.getButton())
				{
					case MouseEvent.BUTTON1: // Bouton de gauche
					{
						int nbCellsShown = this.currentGame.getNbCellsShown();

						hasChanged = this.currentGame.showCell(clickedCell.x, clickedCell.y);
						
						// Premier clic gauche sur une cellule non dévoilée et sans drapeau?
						if (hasChanged && nbCellsShown == 0)
						{
							this.timer.restart();
							winGame(this.currentGame);
						}
						
						break;
					}
					case MouseEvent.BUTTON3: // Bouton de droite
					{
						hasChanged = this.currentGame.changeCellState(clickedCell.x, clickedCell.y);
						break;
					}
				}

				if (hasChanged)
				{
					this.flagsLabel.setText("Mines : " + (this.currentGame.getMineAmount() - this.currentGame.getNbCellsFlagged()));
					this.repaint();
				}

				if (this.currentGame.isWon())
				{
					if (Board.smileyWon != null)
					{
						this.newGameButton.setIcon(new ImageIcon(Board.smileyWon));
						this.newGameButton.setText("");
					}
					else
					{
						this.newGameButton.setIcon(null);
						this.newGameButton.setText("8¬)");
					}

					JOptionPane.showMessageDialog(
							this, 
							"Vous avez gagné !", 
							"Bravo !", 
							JOptionPane.PLAIN_MESSAGE);
					
					String[] difficulty = new String[] {"beginner", "intermediate", "expert"};
					
					String bestTime = bestTimes.getData("/best_times/level[@name='" + difficulty[this.currentGame.getCurrentLevel()] + "']/time");
					if (this.elapsedTime > Integer.parseInt(bestTime))
					{
						// Demander le nouveau record.
					}
					
					this.showScoresDialog();
				}
				else if (this.currentGame.isLost())
				{
					if (Board.smileyLost != null)
					{
						this.newGameButton.setIcon(new ImageIcon(Board.smileyLost));
						this.newGameButton.setText("");
					}
					else
					{
						this.newGameButton.setIcon(null);
						this.newGameButton.setText(":¬(");
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