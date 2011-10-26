package appDemineur.model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import appDemineur.model.Cell.CellState;

public class Game //extends JPanel
{
	// Classe d�finissant les propri�t�s d'un niveau de difficult�
	private static class Level
	{
		public final Dimension dim;
		public final int mineAmount;
		
		public Level(Dimension dim, int mineAmount)
		{
			this.dim = dim;
			this.mineAmount = mineAmount;
		}
	}
	
	// Les trois niveaux de difficult� du jeu
	private static final Level[] LEVELS = new Level[] { 
		new Level(new Dimension(9, 9), 10), 
		new Level(new Dimension(16, 16), 40), 
		new Level(new Dimension(30, 16), 99)
		};

	private int level;
	
	// Matrice contenant la grille de jeu
	private Matrix matrix;
	
	// Indique si la partie est termin�e
	private boolean isOver;
	
	//
	private int nbFlags;
	
	/**
	 * 
	 * 
	 * @param width
	 * @param height
	 * @param mineAmount
	 */
//	public Game(int width, int height, int mineAmount)
//	{
//		this.matrix = new Matrix(width, height, mineAmount);
//		
//		this.nbFlags = 0;
//	}

	/**
	 * @param level
	 */
	public Game(int level)
	{
		this.level = (level < 0 || level > 2) ? 0 : level;
		
		this.matrix = new Matrix(
				Game.LEVELS[level].dim.width, 
				Game.LEVELS[level].dim.height, 
				Game.LEVELS[level].mineAmount);
		
		this.nbFlags = 0;
	}

	/**
	 * Redessine la partie
	 * 
	 * @param g Graphics dans lequel dessiner la partie
	 * @param cellSize taille d'une cellule en pixels 
	 */
	public void redraw(Graphics g, float cellSize)
	{
		this.matrix.redraw(g, cellSize);
	}	
	
	/**
	 * 
	 * 
	 * @param x
	 * @param y
	 * @param show
	 * @return
	 */
	public boolean changeCellState(int x, int y, boolean show)
	{
		boolean changed = false;
		
		Cell c = this.matrix.getElement(x, y);
		
		if (c != null && !c.getState().equals(CellState.SHOWN))
		{
			if (show)
			{
				if (!c.getState().equals(CellState.FLAGGED))
				{
					if (c.isMine())
					{
						System.out.println("Perdu!");
						c.setState(CellState.SHOWN);

						this.isOver = true;
					}

					this.showCell(x, y, null);
					changed = true;
				}
			}
			else // Alterne entre FLAGGED, DUBIOUS et HIDDEN
			{
				CellState newState = null;
				
				switch(c.getState())
				{
					case HIDDEN:
						newState = CellState.FLAGGED;
						this.nbFlags++;
						break;
					case FLAGGED:
						newState = CellState.DUBIOUS;
						this.nbFlags--;
						break;
					case DUBIOUS:
						newState = CellState.HIDDEN;
						break;
				}
				
				changed = c.setState(newState);
			}
		}
		
		return changed;
	}
	
	// Fonction qui affiche une cellule et, si elle n'a pas de mines adjacentes, 
	// toutes les cellules voisines n'ayant pas non plus de mines adjacentes, 
	// et ce, jusqu'� ce qu'� ce soient r�v�l�es les cellules fronti�res, 
	// c'est-�-dire celles qui ont des mines adjacentes.
	//
	// Si l'�tat de la cellule est SHOWN ou FLAGGED, alors cet �tat n'est 
	// pas modifi�.
	//
	// La m�thode prend les coordonn�es x et y de la cellule � afficher 
	// ainsi qu'une liste vide de cellules (ou une valeur null). 
	// Ce dernier param�tre sert aux appels r�cursifs.
	private void showCell(int x, int y, List<Cell> visitedCells)
	{
		Cell curCell = this.matrix.getElement(x, y);
		
		if (curCell != null 
				&& !curCell.getState().equals(CellState.SHOWN)
				&& !curCell.getState().equals(CellState.FLAGGED))
		{
			// Change l'�tat de la cellule courante
			curCell.setState(CellState.SHOWN);

			// Si aucune cellule adjacente ne contient de mine 
			if (curCell.getAdjacentMines() == 0)
			{
				// Au besoin, construit une liste de cellules � ne pas rev�rifier plus tard
				if (visitedCells == null)
				{
					visitedCells = new ArrayList<Cell>();
				}
				
				// Construit une liste de cellules � v�rifier maintenant
				List<Point> cellsToVisit = new ArrayList<Point>();
				
				// Parcourt les 9 cellules incluant la cellule courante et les cellules adjacentes
				for (int r = y - 1 ; r <= y + 1; r++)
				{
					for (int c = x - 1 ; c <= x + 1; c++)
					{
						Cell adjCell = this.matrix.getElement(c, r);
						
						// Si la cellule n'a pas encore �t� v�rifi�e
						if (adjCell != null && !visitedCells.contains(adjCell))
						{
							// Si ce n'est pas la cellule courante
							if  (!(r == y && c == x))
							{
								// Ajoute la cellule dans la liste des cellules � v�rifier maintenant
								cellsToVisit.add(new Point(c, r));
							}
							// Ajoute la cellule dans la liste des cellules � ne pas rev�rifier plus tard
							visitedCells.add(adjCell);
						}
					}
				}
				
				// Pour chacune des cellules � v�rifier maintenant
				for (Point p : cellsToVisit)
				{
					this.showCell(p.x, p.y, visitedCells);
				}
			}
		}
	}
	
	/**
	 * V�rifie si la partie est termin�e.
	 * 
	 * Une partie est termin�e quand une des mines a �t� d�couverte 
	 * ou que toutes cases ayant des mines sont dans l'�tat FLAGGED.
	 * 
	 * @return vrai si la partie est termin�e, sinon faux.
	 */
	public boolean isOver()
	{
		return this.isOver;
	}
	
	public int getNbFlags()
	{
		return this.nbFlags;
	}
	
	public int getWidth()
	{
		return Game.LEVELS[this.level].dim.width;
	}
	
	public int getHeight()
	{
		return Game.LEVELS[this.level].dim.height;
	}
	
	public int getMineAmount()
	{
		return Game.LEVELS[this.level].mineAmount;
	}
}
