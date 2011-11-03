package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * La classe Cell mod�lise les cellules (cases) du jeu du d�mineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Cell
{
	/**
	 * L'�num�ration CellState repr�sente les �tats possibles d'une cellule, soit :
	 *  HIDDEN -> le contenu de la cellule n'est pas visible
	 *  FLAGGED -> la cellule est marqu�e par un drapeau (comme contenant une mine)
	 *  DUBIOUS -> la cellule est marqu�e par un point d'interrogation (comme contenant peut-�tre une mine)
	 *  SHOWN -> le contenu de la cellule est visible
	 * 
	 * @author Christian Lesage
	 * @author Alexandre Tremblay
	 *
	 */
	public enum CellState { HIDDEN,	FLAGGED, DUBIOUS, SHOWN; }
	
	// Indique si la cellule contient une mine
	private boolean isMine;
	
	// Le nombre de mines adjacentes � la cellule
	private int adjacentMines;
	
	// �tat de la cellule
	private CellState state;
	
	// Images utilis�es par la classe pour dessiner les cellules
	private static BufferedImage flagImage = null;
	private static BufferedImage mineImage = null;
	private static BufferedImage explodedMineImage = null;
	
	// Initialisation des images
	static
	{
		try
		{
			Cell.flagImage = ImageIO.read(Cell.class.getResource("../../drapeau.png"));
			Cell.mineImage = ImageIO.read(Cell.class.getResource("../../mine.png"));
			Cell.explodedMineImage = ImageIO.read(Cell.class.getResource("../../mine_explose.png"));
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}	
	}
	
	/**
	 * Construit une cellule. La cellule cr��e ne contient pas de mine, 
	 * n'a aucune mine adjacente et se trouve dans l'�tat CellState.HIDDEN. 
	 */
	public Cell()
	{
		this.isMine = false;
		this.adjacentMines = 0;
		this.state = CellState.HIDDEN;
	}
	
	/**
	 * Dessine la cellule dans le Graphics sp�cifi�
	 * 
	 * @param g Graphics dans lequel la cellule doit se dessiner
	 * @param cellSize taille d'une cellule en pixels
	 * @param showMines 
	 * si vrai, les cases min�es sont montr�es � moins d'�tre correctement marqu�es d'un drapeau,
	 * et une mine avec un X s'affiche dans les cases incorrectement marqu�es d'un drapeau; 
	 * 
	 * si faux, une mine n'est est montr�e que si la cellule est d�voil�e 
	 * ou si la partie est termin�e et que la cellule n'est pas marqu�e d'un drapeau  
	 * 
	 * @param gameIsWon indique si la partie est gagn�e
	 */
	public void redraw(Graphics g, int cellSize, boolean showMines, boolean gameIsWon)
	{
		if (g != null)
		{
			Graphics2D g2d = (Graphics2D) g;

			int hintX = Math.round(cellSize * 0.35f);
			int hintY = Math.round(cellSize * 0.75f);
			int fontSize = Math.round(cellSize * 0.60f);
			
			// Dessine le fond
			g2d.setColor(this.state == CellState.SHOWN ? Color.GRAY: Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, cellSize, cellSize);
			
			// Visible, mine
			if (this.isMine && 
					(this.state == CellState.SHOWN || 
							(showMines && !(this.state == CellState.FLAGGED))))
			{
				this.drawMine(g2d, cellSize);
			}
			// Visible, mais pas mine
			else if (!this.isMine && this.state == CellState.SHOWN)
			{
				if (this.getAdjacentMines() > 0)
				{
					g2d.setFont(new Font(null, Font.BOLD, fontSize));
					
					// Dessine l'�ombre� de l'indice
					g2d.setColor(new Color(40, 40, 40));
					g2d.drawString("" + this.getAdjacentMines(), hintX + 2, hintY + 2);
					
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
					g2d.drawString("" + this.getAdjacentMines(), hintX, hintY);
				}
			}
			// HIDDEN, FLAGGED ou DUBIOUS
			else if (this.state == CellState.DUBIOUS)
			{
				// Dessine un ?
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font(null, Font.BOLD, fontSize));
				g2d.drawString("?", hintX, hintY);
			}
			else if (this.state == CellState.FLAGGED)
			{
				// Pour imiter le comportement du d�mineur de Windows 7, les mines marqu�es 
				// d'un drapeau ne sont pas affich�es � la fin d'une partie gagn�e
				
				if (showMines && !gameIsWon)  
				{
					// Dessine une mine (avec ou sans X)
					this.drawMine(g2d, cellSize);
				}

				if (!showMines || this.isMine)
				{
					// Dessine un drapeau
					
					//						Graphics2D g2d = (Graphics2D) g;
					//						g2d.setStroke(new BasicStroke(3));
					//						g2d.setColor(Color.WHITE);
					//						
					//						g2d.drawLine(Math.round(size * 0.3f), Math.round(size * 0.8f), Math.round(size * 0.7f), Math.round(size * 0.8f));
					//						g2d.drawLine(Math.round(size * 0.5f), Math.round(size * 0.2f), Math.round(size * 0.5f), Math.round(size * 0.8f));
					//
					//						Polygon pol = new Polygon();
					//						pol.addPoint(Math.round(size * 0.5f), Math.round(size * 0.2f));
					//						pol.addPoint(Math.round(size * 0.2f), Math.round(size * 0.4f));
					//						pol.addPoint(Math.round(size * 0.5f), Math.round(size * 0.6f));
					//
					//						g2d.setColor(new Color(200, 0, 0));
					//						g2d.fillPolygon(pol);

					g2d.drawImage(Cell.flagImage, 0, 0, cellSize, cellSize, null);
				}
			}
			
			this.draw3dEffect(g2d, cellSize, this.state == CellState.SHOWN);
		}
	}
	
	private void drawMine(Graphics g, int cellSize)
	{
//		int minePos = Math.round(cellSize * 0.25f);
//		int mineSize = Math.round(cellSize * 0.50f);

//		g.setColor(this.state == CellState.SHOWN ? new Color(200, 0, 0) : Color.BLACK);
//		g.fillOval(minePos, minePos, mineSize, mineSize);
		
		BufferedImage img = this.state == CellState.SHOWN ? Cell.explodedMineImage : Cell.mineImage;
		
		g.drawImage(img, 0, 0, cellSize, cellSize, null);
		
		if (!this.isMine)
		{
			Rectangle r = g.getClipBounds();
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(3));
			g2d.setColor(new Color(200, 0, 0));
			
			g2d.drawLine(0, 0, r.width - 1, r.height - 1);
			g2d.drawLine(r.width - 1, 0 , 0, r.height - 1);
		}
	}
	
	private void draw3dEffect(Graphics2D g2d, int cellSize, boolean isPushed)
	{
		g2d.setStroke(new BasicStroke(6));
		
		g2d.setColor(isPushed ? Color.DARK_GRAY : Color.WHITE);
		g2d.drawLine(0, 0, cellSize - 1, 0);
		g2d.drawLine(0, 0, 0, cellSize - 1);

		g2d.setColor(isPushed ? Color.LIGHT_GRAY : Color.GRAY);
		g2d.drawLine(0, cellSize, cellSize, cellSize);
		g2d.drawLine(cellSize, 0, cellSize, cellSize);		
	}
	
	/**
	 * Fixe le nombre de mines adjacentes � la cellule si le param�tre est valide,
	 * sinon, aucun changement n'est effectu�.
	 * 
	 * @param n nombre de mines adjacentes � la cellule; doit �tre compris dans [0, 8] 
	 */
	public void setAdjacentMines(int n)
	{
		if (n >=0 && n <= 8)
		{
			this.adjacentMines = n;
		}
	}
	
	/**
	 * Retourne le nombre de mines adjacentes � la cellule.
	 * 
	 * @return le nombre de mines adjacentes � la cellule
	 */
	public int getAdjacentMines()
	{
		return this.adjacentMines;
	}
	
	/**
	 * Fixe l'�tat de la cellule.
	 * 
	 * @param newState le nouvel �tat de la cellule; ne doit pas �tre null
	 * @return vrai si l'�tat a pu �tre fix�, faux sinon.
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
	 * Retourne l'�tat de la cellule.
	 * 
	 * @return l'�tat de la cellule
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
	 * Retourne un indicateur � l'effet que la cellule contient ou non une mine.
	 * 
	 * @return vrai si la cellule contient une mine; faux si elle n'en contient pas.
	 */
	public boolean isMine()
	{
		return this.isMine;
	}

	/** 
	 * Retourne une repr�sentation textuelle de la cellule.
	 * Une mine est repr�sent�e par �*� et tout autre cellule par le nombre de mines adjacentes. 
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.isMine() ? "*" : this.getAdjacentMines() + "";
	}
}
