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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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

public class Board extends JPanel implements ActionListener, MouseListener, FocusListener
{
	// Si vrai, la taille des cellules est fixée au plus grand entier inférieur à la taille possible
	// Si faux, la taille des cellules comporte une partie fractionnaire
	private static final boolean USE_CELL_SIZE_FLOOR = true;
	
	// Objet de la partie courante
	private Game game = null;
	
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
	
	// Minuterie déclenchée une fois par seconde
	private Timer timer;
	
	// Temps écoulé pour la partie en cours en secondes
	private int elapsedTime;
	
	// Objet parent
	private AppFrame parent = null;
	
	// Indique que la minuterie a été démarrée
	private boolean timerStarted;
	
	// Images utilisées pour le bouton newGameButton et la boîte de dialogue À propos
	private static BufferedImage smileyWon = null;
	private static BufferedImage smileyNormal = null;
	private static BufferedImage smileyLost = null;
//	private static BufferedImage aboutLogo = null;
	
	// Initialisation des images
	static
	{
		try
		{
			Board.smileyWon = ImageIO.read(Board.class.getResource("../../smiley_won.png"));
			Board.smileyNormal = ImageIO.read(Board.class.getResource("../../smiley_normal.png"));
			Board.smileyLost = ImageIO.read(Board.class.getResource("../../smiley_lost.png"));
//			Board.aboutLogo = ImageIO.read(Board.class.getResource("../../demineur_logo.png"));
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

        // Initialise les composantes
		this.gamePanel = new DrawingPanel(this);
		this.controlPanel = new JPanel();
		this.newGameButton = new JButton();
		this.timerLabel = new JLabel();
		this.flagsLabel = new JLabel();
		
		this.setLayout(new BorderLayout());
		
		this.newGameButton.setActionCommand("NEW_GAME");
		this.newGameButton.setPreferredSize(new Dimension(60, 60));
		this.newGameButton.setToolTipText("Cliquez ici pour commencer une nouvelle partie.");
		this.newGameButton.setContentAreaFilled(false);
		this.newGameButton.setFocusable(false);
		
		this.timerLabel.setHorizontalAlignment(JLabel.CENTER);
		this.timerLabel.setFont(new Font(null, Font.BOLD, 20));
		this.flagsLabel.setHorizontalAlignment(JLabel.CENTER);
		this.flagsLabel.setFont(new Font(null, Font.BOLD, 20));
		
		this.controlPanel.setLayout(new GridLayout(0, 3));
		this.controlPanel.setBorder(LineBorder.createBlackLineBorder());
		
		JPanel newGameButtonPanel = new JPanel();
		newGameButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		newGameButtonPanel.add(this.newGameButton);
		
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
	
	/**
	 * Réinitialise la partie
	 */
	public void replay()
	{
		int response = 0;
		
		// Demande une confirmation si une partie est en cours
		if (this.game != null && !this.game.isOver() && this.game.getNbCellsShown() > 0)
		{
			response = JOptionPane.showConfirmDialog(this, 
					"Êtes-vous sûr de vouloir commencer une nouvelle partie ? \nLa partie en cours n'est pas terminée.",
					"Confirmation", JOptionPane.YES_NO_OPTION);
		}
		// Si le joueur est certain de vouloir recommencer 
		if (response == 0)
		{
			// Crée une nouvelle partie
			this.game = new Game(((AppMenu)this.parent.getJMenuBar()).getNextGameLevel());
			
			this.flagsLabel.setText("Mines : " + this.game.getMineAmount());
			
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
			this.timer.stop();
			this.timerStarted = false;
			this.timerLabel.setText("Temps : " + String.format("%03d", this.elapsedTime));
			
		}
	}
	
	// Gagne une partie (option «sale tricheur»)
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
				
				this.parent.game.redraw(
						g2, 
						this.parent.getCellSize(), 
						this.parent.game.isOver() || ((AppMenu)this.parent.parent.getJMenuBar()).getCheatMode());
				
				g.drawImage(image, gridOffset.x, gridOffset.y, null);
			}
		}
	}
	
	// Retourne la plus grande taille de cellule possible en fonction des dimensions du « gamePanel ».
	private float getCellSize()
	{
		float size = 0;
		
		if (this.game.getWidth() > 0 && this.game.getHeight() > 0)
		{
			float width = (float) Math.max(this.gamePanel.getWidth(), 0) / this.game.getWidth();
			float height = (float) Math.max(this.gamePanel.getHeight(), 0) / this.game.getHeight();

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
		return Math.max(Math.round(this.getCellSize() * this.game.getWidth()), 1);
	}
	
	// Retourne la hauteur de la grille à l'écran (en pixels)
	private int getGridScreenHeight()
	{
		return Math.max(Math.round(this.getCellSize() * this.game.getHeight()), 1);
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
			if (!this.game.isOver())
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
		if (!this.game.isOver())
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
					case MouseEvent.BUTTON2: // Bouton du centre 
					{
						int nbCellsShown = this.game.getNbCellsShown();

						hasChanged = this.game.showCell(clickedCell.x, clickedCell.y);
						
						// Premier clic gauche sur une cellule non dévoilée et sans drapeau?
						if (hasChanged && nbCellsShown == 0)
						{
							this.timer.restart();
							this.timerStarted = true;
						}

						// Sale tricheur !
						if (evt.getButton() == MouseEvent.BUTTON2)
						{
							this.winGame(this.game);
						}
						
						break;
					}
					case MouseEvent.BUTTON3: // Bouton de droite
					{
						hasChanged = this.game.changeCellState(clickedCell.x, clickedCell.y);
						break;
					}
				}

				if (hasChanged)
				{
					this.flagsLabel.setText("Mines : " + (this.game.getMineAmount() - this.game.getNbCellsFlagged()));
					this.repaint();
				}

				if (this.game.isWon())
				{
					this.doGameWonStuff();
				}
				else if (this.game.isLost())
				{
					this.doGameLostStuff();
				}
			}
		}
	}
	
	// Choses à faire quand une partie est gagnée
	private void doGameWonStuff()
	{
		this.timer.stop();
		
		// Changer l'icône du bouton newGameButton
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
		
		// Récupérer le meilleur temps pour le niveau de difficulté de la partie
		
		BestTimes bestTimes = new BestTimes();

		int bestTimeForLevel;
		
		try
		{
			bestTimeForLevel = Integer.parseInt(bestTimes.getTime(this.game.getLevelNum()));
		}
		catch (NumberFormatException e)
		{
			bestTimeForLevel = Integer.MAX_VALUE;
		}
		
		// Ajout du nouveau temps seulement s'il est plus bas et que la partie courante n'est pas
		// en mode «tricher»
		if (this.elapsedTime < bestTimeForLevel && !((AppMenu)this.parent.getJMenuBar()).getCheatMode())
		{
			// Demande le nom du joueur
			String playerName = "";
			
			while (playerName.length() == 0)
			{
				playerName = JOptionPane.showInputDialog(
						this, 
						"Entrez votre nom de joueur : ", 
						"Nouveau record", 
						JOptionPane.DEFAULT_OPTION);
				
				// Si le joueur a appuyé sur « Cancel », on met son nom de joueur à « Anonyme »
				if (playerName == null)
				{
					playerName = "Anonyme";
					break;
				}
			} 
			
			// Inscrit le nouveau record
			bestTimes.setPlayer(this.game.getLevelNum(), playerName);
			bestTimes.setTime(this.game.getLevelNum(), "" + this.elapsedTime);
			
			// Écrit le fichier des meilleurs temps
			bestTimes.write();
			
			// Montre les meilleurs temps
			((AppMenu)this.parent.getJMenuBar()).showScoresDialog();
		}
		else
		{
			// Féliciter l'utilisateur
			JOptionPane.showMessageDialog(
					this, 
					"Vous avez gagné !", 
					"Bravo !", 
					JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	// Choses à faire quand une partie est perdu
	private void doGameLostStuff()
	{
		this.timer.stop();

		// Changer l'icône du bouton newGameButton
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
	
	/**
	 * Reçoit et traite les événements relatifs au gain de focus.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	@Override
	public void focusGained(FocusEvent evt)
	{
		if (this.timerStarted && !this.game.isOver())
		{
			this.timer.start();
		}
	}
	
	/**
	 * Reçoit et traite les événements relatifs à la perte de focus.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 * 
	 * @param evt événement déclencheur
	 */
	@Override
	public void focusLost(FocusEvent evt)
	{
		this.timer.stop();
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
}