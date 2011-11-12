package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * La classe Cell modélise les cellules (cases) du jeu du démineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */

public class Cell
{
	/**
	 * L'énumération CellState représente les états possibles d'une cellule, soit :
	 *  HIDDEN -> le contenu de la cellule n'est pas visible
	 *  FLAGGED -> la cellule est marquée par un drapeau (comme contenant une mine)
	 *  DUBIOUS -> la cellule est marquée par un point d'interrogation (comme contenant peut-être une mine)
	 *  SHOWN -> le contenu de la cellule est visible
	 * 
	 * @author Christian Lesage
	 * @author Alexandre Tremblay
	 *
	 */
	
	// Les 4 états possibles d'une cellule
	public enum CellState { HIDDEN,	FLAGGED, DUBIOUS, SHOWN; }
	
	// Indique si la cellule contient une mine
	private boolean isMine;
	
	// Le nombre de mines adjacentes à la cellule
	private int adjacentMines;
	
	// État de la cellule
	private CellState state;
	
	// Images utilisées par la classe pour dessiner les cellules
	private static BufferedImage flagImage = null;
	private static BufferedImage mineImage = null;
	private static BufferedImage explodedMineImage = null;
	
	// Initialisation des images
	static
	{
		try
		{
			Cell.flagImage = ImageIO.read(Cell.class.getResource("../../flag.png"));
			Cell.mineImage = ImageIO.read(Cell.class.getResource("../../mine.png"));
			Cell.explodedMineImage = ImageIO.read(Cell.class.getResource("../../mine_exploded.png"));
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Incapable de trouver une ou plusieurs image(s) de la classe Cell.");
		}
	}
	
	/**
	 * Construit une cellule. La cellule créée ne contient pas de mine, 
	 * n'a aucune mine adjacente et se trouve dans l'état CellState.HIDDEN. 
	 */
	public Cell()
	{
		this.isMine = false;
		this.adjacentMines = 0;
		this.state = CellState.HIDDEN;
	}
	
	/**
	 * Dessine la cellule dans le Graphics spécifié
	 * 
	 * @param g Graphics dans lequel la cellule doit se dessiner
	 * @param cellSize taille d'une cellule en pixels
	 * @param showMines 
	 * si vrai, les cases minées sont montrées à moins d'être correctement marquées d'un drapeau,
	 * et une mine avec un X s'affiche dans les cases incorrectement marquées d'un drapeau; 
	 * 
	 * si faux, une mine n'est est montrée que si la cellule est dévoilée 
	 * ou si la partie est terminée et que la cellule n'est pas marquée d'un drapeau  
	 * 
	 * @param gameIsWon indique si la partie est gagnée
	 */
	public void redraw(Graphics g, int cellSize, boolean showMines, boolean gameIsWon)
	{
		if (g != null)
		{
			Graphics2D g2d = (Graphics2D) g;

			int hintX = Math.round(cellSize * 0.35f);
			int hintY = Math.round(cellSize * 0.75f);
			int fontSize = Math.round(cellSize * 0.60f);
			int shadowDistance = Math.round(cellSize * 0.03f);
			
			// Dessine le fond
			g2d.setColor(this.state == CellState.SHOWN ? Color.GRAY: Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, cellSize, cellSize);
			
			// Visible, mine
			if (this.isMine && (this.state == CellState.SHOWN || (showMines && this.state != CellState.FLAGGED)))
			{
				this.drawMine(g2d, cellSize);
			}
			
			// Visible, mais pas mine
			if (!this.isMine && this.state == CellState.SHOWN)
			{
				if (this.getAdjacentMines() > 0)
				{
					g2d.setFont(new Font(null, Font.BOLD, fontSize));
					
					switch (this.getAdjacentMines())
					{
						case 1: g2d.setColor(new Color(150, 150, 240)); break;
						case 2: g2d.setColor(new Color(0,   200, 0  )); break;
						case 3: g2d.setColor(new Color(255, 0,   0  )); break;
						case 4: g2d.setColor(new Color(0,   0,   160)); break;
						case 5: g2d.setColor(new Color(136, 0,   61 )); break;
						case 6: g2d.setColor(new Color(112, 178, 146)); break;
						case 7: g2d.setColor(new Color(171, 31,  26 )); break;
						case 8: g2d.setColor(new Color(128, 0,   0  )); break;
					}
					
					// Dessine l'indice
					this.drawStringWithShadow(g2d, "" + this.getAdjacentMines(), hintX, hintY, shadowDistance, null, null);
				}
			}
			else if (this.state == CellState.DUBIOUS)
			{
				// Dessine un ?
				g2d.setFont(new Font(null, Font.BOLD, fontSize));
				this.drawStringWithShadow(g2d, "?", hintX, hintY, shadowDistance, Color.WHITE, null);
			}
			else if (this.state == CellState.FLAGGED)
			{
				// Pour imiter le comportement du démineur de Windows 7, les mines marquées 
				// d'un drapeau ne sont pas affichées à la fin d'une partie gagnée
				if (showMines && !gameIsWon)  
				{
					// Dessine une mine (avec ou sans X)
					this.drawMine(g2d, cellSize);
				}

				if (!showMines || this.isMine)
				{
					// Dessine un drapeau
					drawFlag(g2d, cellSize);
				}
			}
			
			this.draw3dEffect(g2d, cellSize, this.state == CellState.SHOWN);
		}
	}
	
	// Dessine une mine
	private void drawMine(Graphics2D g2d, int cellSize)
	{
		BufferedImage img = this.state == CellState.SHOWN ? Cell.explodedMineImage : Cell.mineImage;
		
		if (img != null)
		{
			g2d.drawImage(img, 0, 0, cellSize, cellSize, null);
		}
		else
		{
			int minePos = Math.round(cellSize * 0.25f);
			int mineSize = Math.round(cellSize * 0.50f);

			g2d.setColor(this.state == CellState.SHOWN ? new Color(200, 0, 0) : Color.BLACK);
			g2d.fillOval(minePos, minePos, mineSize, mineSize);
		}
		
		if (!this.isMine)
		{
			g2d.setStroke(new BasicStroke(4));
			g2d.setColor(new Color(200, 0, 0));
			
			g2d.drawLine(0, 0, cellSize - 1, cellSize - 1);
			g2d.drawLine(cellSize - 1, 0 , 0, cellSize - 1);
		}
	}
	
	// Dessine un drapeau
	private void drawFlag(Graphics2D g2d, int cellSize)
	{
		if (Cell.flagImage != null)
		{
			g2d.drawImage(Cell.flagImage, 0, 0, cellSize, cellSize, null);
		}
		else
		{
			g2d.setStroke(new BasicStroke(3));
			g2d.setColor(Color.WHITE);

			g2d.drawLine(Math.round(cellSize * 0.3f), Math.round(cellSize * 0.8f), Math.round(cellSize * 0.7f), Math.round(cellSize * 0.8f));
			g2d.drawLine(Math.round(cellSize * 0.5f), Math.round(cellSize * 0.2f), Math.round(cellSize * 0.5f), Math.round(cellSize * 0.8f));

			Polygon pol = new Polygon();
			pol.addPoint(Math.round(cellSize * 0.5f), Math.round(cellSize * 0.2f));
			pol.addPoint(Math.round(cellSize * 0.2f), Math.round(cellSize * 0.4f));
			pol.addPoint(Math.round(cellSize * 0.5f), Math.round(cellSize * 0.6f));

			g2d.setColor(new Color(200, 0, 0));
			g2d.fillPolygon(pol);
		}		
	}
	
	// Dessine les effets 3D sur la grille de jeu
	private void draw3dEffect(Graphics2D g2d, int cellSize, boolean isPushed)
	{
		g2d.setStroke(new BasicStroke(6));
		
		g2d.setColor(isPushed ? Color.GRAY : Color.GRAY);
		g2d.drawLine(0, cellSize, cellSize, cellSize);
		g2d.drawLine(cellSize, 0, cellSize, cellSize);		

		g2d.setColor(isPushed ? Color.DARK_GRAY : Color.WHITE);
		g2d.drawLine(0, 0, cellSize - 1, 0);
		g2d.drawLine(0, 0, 0, cellSize - 1);

		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, cellSize - 1, cellSize - 1);
	}
	
	// Dessine un texte avec une ombre selon la distance définit par « shadowDistance ».
	private void drawStringWithShadow(Graphics2D g2d, String str, int x, int y, int shadowDistance, Color textColor, Color shadowColor)
	{
		if (textColor == null)
		{
			textColor = g2d.getColor();
		}
		
		if (shadowColor == null)
		{
			shadowColor = new Color(40, 40, 40);
		}
		
		g2d.setColor(shadowColor);
		g2d.drawString(str, x + shadowDistance, y + shadowDistance);
		g2d.setColor(textColor);
		g2d.drawString(str, x, y);		
	}
	
	/**
	 * Fixe le nombre de mines adjacentes à la cellule si le paramètre est valide,
	 * sinon, aucun changement n'est effectué.
	 * 
	 * @param n nombre de mines adjacentes à la cellule; doit être compris dans [0, 8] 
	 */
	public void setAdjacentMines(int n)
	{
		if (n >= 0 && n <= 8)
		{
			this.adjacentMines = n;
		}
	}
	
	/**
	 * Retourne le nombre de mines adjacentes à la cellule.
	 * 
	 * @return le nombre de mines adjacentes à la cellule
	 */
	public int getAdjacentMines()
	{
		return this.adjacentMines;
	}
	
	/**
	 * Fixe l'état de la cellule.
	 * 
	 * @param newState le nouvel état de la cellule; ne doit pas être null
	 * @return vrai si l'état a pu être fixé, faux sinon.
	 */
	public boolean setState(CellState newState)
	{
		boolean succeed = false;
		
		if (newState != null)
		{
			this.state = newState;
			succeed = true;
		}
			
		return succeed;
	}
	
	/**
	 * Retourne l'état de la cellule.
	 * 
	 * @return l'état de la cellule
	 */
	public CellState getState()
	{
		return this.state;
	}
	
	/**
	 * Marque la cellule comme contenant ou non une mine. 
	 * 
	 * @param isMine si vrai, la cellule contient une mine; si faux, elle n'en contient pas. 
	 */
	public void setAsMine(boolean isMine)
	{
		this.isMine = isMine;
	}
	
	/**
	 * Retourne un indicateur à l'effet que la cellule contient ou non une mine.
	 * 
	 * @return vrai si la cellule contient une mine; faux si elle n'en contient pas.
	 */
	public boolean isMine()
	{
		return this.isMine;
	}

	/** 
	 * Retourne une représentation textuelle de la cellule.
	 * Une mine est représentée par «*» et tout autre cellule par le nombre de mines adjacentes. 
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.isMine() ? "*" : this.getAdjacentMines() + "";
	}
}
