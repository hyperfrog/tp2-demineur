package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

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
	 * @param gameIsLost indique si la partie est perdue
	 */
	public void redraw(Graphics g, float size, boolean showMines, boolean gameIsLost)
	{
		if (g != null)
		{
			int hintX = Math.round(size * 0.35f);
			int hintY = Math.round(size * 0.75f);
			int fontSize = Math.round(size * 0.60f);
			int cellSize = Math.round(size);
			
			// Visible, mine
			if (this.isMine && 
					(this.getState().equals(CellState.SHOWN) || 
							(showMines && !this.getState().equals(CellState.FLAGGED))))
			{
				this.drawMine(g, size);
			}
			// Visible, mais pas mine
			else if (!this.isMine && this.getState().equals(CellState.SHOWN))
			{
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, cellSize, cellSize);

				switch (this.getAdjacentMines())
				{
					case 1: g.setColor(new Color(106, 133, 165)); break;
					case 2: g.setColor(new Color(0,   200, 0  )); break;
					case 3: g.setColor(new Color(255, 0,   0  )); break;
					case 4: g.setColor(new Color(0,   0,   160)); break;
					case 5: g.setColor(new Color(136, 0,   61 )); break;
					case 6: g.setColor(new Color(112, 178, 146)); break;
					case 7: g.setColor(new Color(171, 31,  26 )); break;
					case 8: g.setColor(new Color(128, 0,   0  )); break;
				}

				g.setFont(new Font(null, Font.BOLD, fontSize));
				g.drawString("" + this.getAdjacentMines(), hintX, hintY);
			}
			// HIDDEN, FLAGGED ou DUBIOUS
			else if (!this.getState().equals(CellState.SHOWN)) 
			{
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, cellSize, cellSize);
				
				if (this.getState().equals(CellState.DUBIOUS))
				{
					// Dessine un ?
					g.setColor(Color.WHITE);
					g.setFont(new Font(null, Font.BOLD, fontSize));
					g.drawString("?", hintX, hintY);
				}
				else if (this.getState().equals(CellState.FLAGGED))
				{
					if (showMines)
					{
						// Dessine une mine (avec ou sans X)
						this.drawMine(g, size);
					}
					
					if (!showMines || (this.isMine && (gameIsLost || showMines)))
					{

						// Dessine un drapeau
						Graphics2D g2d = (Graphics2D) g;
						g2d.setStroke(new BasicStroke(3));
						g2d.setColor(Color.WHITE);
						
						g2d.drawLine(Math.round(size * 0.3f), Math.round(size * 0.8f), Math.round(size * 0.7f), Math.round(size * 0.8f));
						g2d.drawLine(Math.round(size * 0.5f), Math.round(size * 0.2f), Math.round(size * 0.5f), Math.round(size * 0.8f));

						Polygon pol = new Polygon();
						pol.addPoint(Math.round(size * 0.5f), Math.round(size * 0.2f));
						pol.addPoint(Math.round(size * 0.2f), Math.round(size * 0.4f));
						pol.addPoint(Math.round(size * 0.5f), Math.round(size * 0.6f));

						g2d.setColor(new Color(200, 0, 0));
						g2d.fillPolygon(pol);

					}
				}	
			}
		}
	}
	
	private void drawMine(Graphics g, float size)
	{
		int minePos = Math.round(size * 0.25f);
		int mineSize = Math.round(size * 0.50f);
		int cellSize = Math.round(size);

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, cellSize, cellSize);
		
		g.setColor(this.isMine && this.state == CellState.SHOWN ? new Color(200, 0, 0) : Color.BLACK);
		g.fillOval(minePos, minePos, mineSize, mineSize);
		
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
