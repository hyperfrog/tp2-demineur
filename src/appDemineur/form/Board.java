package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import appDemineur.model.Game;

/**
 * La classe Board impl�ment l'interface du jeu du D�mineur.
 * Elle g�re la plupart des �v�nements de l'interface utilisateur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Board extends JPanel implements ActionListener, MouseListener, ItemListener
{
	// Objet de la partie courante
	private Game currentGame = null;
	
	// Panneau dans lequel la partie est dessin�e
	private DrawingPanel gamePanel;
	
	// Bouton pour cr�er une nouvelle partie
	private JButton newGameButton;
	
	// Panneau contenant les contr�les
	private JPanel controlPanel;
	
	// Libell� pour la minuterie
	private JLabel timerLabel;
	
	// Libell� pour le nombre de cases marqu�es d'un drapeau
	private JLabel flagsLabel;
	
	// Minuterie d�clench�e une fois par seconde
	private Timer timer;
	
	// Temps �coul� pour la partie en cours en secondes
	private int elapsedTime;
	
	// Objet parent
	private AppFrame parent = null;
	
	/**
	 * Construit un plateau de jeu.
	 * Le plateau d'une partie initiale est d'une dimension de 16x16
	 * et contient 40 mines.
	 * 
	 * @param parent Objet parent du panneau, doit �tre du type AppFrame
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
		
		this.newGameButton.setText(":)");
		this.newGameButton.setActionCommand("NEW_GAME");
		
		this.timerLabel.setHorizontalAlignment(JLabel.CENTER);
		this.flagsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		this.controlPanel.setLayout(new GridLayout(0, 3));
		this.controlPanel.setBackground(new Color(235, 235, 235));
		this.controlPanel.setBorder(LineBorder.createBlackLineBorder());
		//this.buttonPanel.setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(10, 10, 10, 10)));
		
		this.controlPanel.add(this.timerLabel);
		this.controlPanel.add(this.newGameButton);
		this.controlPanel.add(this.flagsLabel);
		
		this.add(this.gamePanel, BorderLayout.CENTER);
		this.add(this.controlPanel, BorderLayout.PAGE_START);
		
		// Sp�cifie les �couteurs pour le panneau de jeu et le bouton
		this.gamePanel.addMouseListener(this);
		this.newGameButton.addActionListener(this);
		
		// Fixe le d�lai de la minuterie � 1 seconde et sp�cifie son �couteur
		this.timer = new Timer(1000, this);
		this.timer.setActionCommand("TICK");
		
		// Replay se charge de cr�er une nouvelle partie 
		this.replay();
	}
	
	// Cette classe pourrait impl�menter le mouse listener
	/**
	 * Cette classe ne sert qu'� fournir un acc�s au paintComponent() 
	 * du panneau de jeu, qui est un enfant du panneau de type Board.
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
		 * @param parent Objet parent du panneau, doit �tre du type Board
		 */
		public DrawingPanel(Board parent)
		{
			this.parent = parent;
			this.setBackground(Color.WHITE);
		}

		/**
		 * Redessine le plateau de jeu.
		 * Vous ne devriez pas avoir � appeler cette m�thode directement.
		 * @param g Graphics dans lequel le panneau doit se dessiner
		 * 
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			if (g != null && this.parent != null && this.parent instanceof Board)
			{
				BufferedImage image = new BufferedImage(
						this.parent.getGridScreenWidth(), 
						this.parent.getGridScreenHeight(), 
						BufferedImage.TYPE_INT_ARGB);
				
				Graphics g2 = image.getGraphics();

				Point gridOffset = this.parent.getGridOffset();
				
				this.parent.currentGame.redraw(g2, this.parent.getCellSize(), this.parent.parent.getCheatMode());
				g.drawImage(image, gridOffset.x, gridOffset.y, null);
			}
		}
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
			this.currentGame = new Game(this.parent.getNextGameLevel());
			
			this.timerLabel.setText("Temps : 0");
			this.flagsLabel.setText("Mines : " + this.currentGame.getMineAmount());
			
			this.elapsedTime = 0;
			this.timer.restart();
		}
	}
	
	// Retourne la plus grande taille de cellule possible en fonction des dimensions du � gamePanel �.
	private float getCellSize()
	{
		float size = 0;
		
		if (this.currentGame.getWidth() > 0 && this.currentGame.getHeight() > 0)
		{
			float width = (float) Math.max(this.gamePanel.getWidth(), 0) / this.currentGame.getWidth();
			float height = (float) Math.max(this.gamePanel.getHeight(), 0) / this.currentGame.getHeight();

			size = Math.min(height, width);
		}
		
		return size;
	}
	
	// Retourne le point du coin sup�rieur droit de la grille de mani�re � la centrer 
	// dans un panneau de jeu de dimension variable
	private Point getGridOffset()
	{
		int x = Math.round((this.gamePanel.getWidth() - this.getGridScreenWidth()) / 2);
		int y = Math.round((this.gamePanel.getHeight() - this.getGridScreenHeight()) / 2);
		
		return new Point(Math.max(x, 0), Math.max(y, 0));
	}
	
	// Retourne la largeur de la grille � l'�cran (en pixels)
	private int getGridScreenWidth()
	{
		return Math.max(Math.round(this.getCellSize() * this.currentGame.getWidth()), 1);
	}
	
	// Retourne la hauteur de la grille � l'�cran (en pixels)
	private int getGridScreenHeight()
	{
		return Math.max(Math.round(this.getCellSize() * this.currentGame.getHeight()), 1);
	}

	/**
	 * Re�oit et traite les �v�nements relatifs aux boutons, � la minuterie et aux menus.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * @param evt �v�nement d�clencheur
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
			this.repaint();
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
	 * M�thode appel�e quand l'�tat d'un �l�ment de menu change.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @param evt �v�nement d�clencheur
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
						this.repaint();
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