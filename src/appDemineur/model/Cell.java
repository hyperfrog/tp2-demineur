package appDemineur.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
	public enum CellState { HIDDEN,	FLAGGED, DUBIOUS, SHOWN; }
	
	// Indique si la cellule contient une mine
	private boolean isMine;
	
	// Le nombre de mines adjacentes à la cellule
	private int adjacentMines;
	
	// État de la cellule
	private CellState state;
	
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
	 * si vrai, une mine sont montrée sans autre condition; 
	 * si faux, une mine n'est montrée que si la cellule est révélée 
	 */
	public void redraw(Graphics g, float size, boolean showMines)
	{
		if (g != null)
		{
			int chrX = Math.round(size * 0.35f);
			int chrY = Math.round(size * 0.75f);
			int fontSize = Math.round(size * 0.60f);
			int cellSize = Math.round(size);
			int minePos = Math.round(size * 0.25f);
			int mineSize = Math.round(size * 0.50f);
			
			// Visible, mine
			if ((this.getState().equals(CellState.SHOWN) || showMines) && this.isMine())
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, cellSize, cellSize);
				
				g.setColor(Color.RED);
				g.fillOval(minePos, minePos, mineSize, mineSize);
			}
			// Pas visible, mine
			else if (this.getState().equals(CellState.SHOWN) && !this.isMine())
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
				g.drawString("" + this.getAdjacentMines(), chrX, chrY);
			}
			// HIDDEN, FLAGGED ou DUBIOUS
			else if (!this.getState().equals(CellState.SHOWN)) 
			{
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, cellSize, cellSize);
				
				g.setColor(Color.WHITE);
				g.setFont(new Font(null, Font.BOLD, fontSize));
				
				if (this.getState().equals(CellState.DUBIOUS))
				{
					g.drawString("?", chrX, chrY);
				}
				else if (this.getState().equals(CellState.FLAGGED))
				{
					g.setColor(new Color(200, 0, 0));
					g.drawString("¶", chrX, chrY);
				}
			}
		}
	}
	
	/**
	 * Fixe le nombre de mines adjacentes à la cellule si le paramètre est valide,
	 * sinon, aucun changement n'est effectué.
	 * 
	 * @param n nombre de mines adjacentes à la cellule; doit être compris dans [0, 8] 
	 */
	public void setAdjacentMines(int n)
	{
		if (n >=0 && n <= 8)
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
