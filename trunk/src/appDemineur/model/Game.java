package appDemineur.model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import appDemineur.model.Cell.CellState;

/**
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Game
{
	// Classe d�finissant les propri�t�s d'un niveau de difficult�
	public static class Level
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
	public static final Level[] LEVELS = new Level[] { 
		new Level(new Dimension(9, 9), 10), 
		new Level(new Dimension(16, 16), 40), 
		new Level(new Dimension(30, 16), 99)
		};

	private int level;
	
	// Matrice contenant la grille de jeu
	private Matrix matrix;
	
	// Indique si la partie est termin�e
	private boolean isOver;
	
	// Nombre de cases marqu�es d'un drapeau
	private int nbFlags;
	
	/**
	 * Construit une partie avec le niveau de difficult� sp�cifi�.
	 * 
	 * @param level niveau de difficult� de la partie; doit �tre 0, 1 ou 2, sinon 0 est utilis�
	 */
	public Game(int level)
	{
		this.level = (level < 0 || level > Game.LEVELS.length - 1) ? 0 : level;		
		
		this.matrix = new Matrix(
				Game.LEVELS[this.level].dim.width, 
				Game.LEVELS[this.level].dim.height, 
				Game.LEVELS[this.level].mineAmount);
		
		this.nbFlags = 0;
	}

	/**
	 * Redessine la partie
	 * 
	 * @param g Graphics dans lequel la partie doit se dessiner
	 * @param cellSize taille d'une case en pixels
	 * @param showMines 
	 * si vrai, les mines sont montr�es sans autre condition; 
	 * si faux, elles ne sont montr�es que si la case est r�v�l�e 
	 */
	public void redraw(Graphics g, float cellSize, boolean showMines)
	{
		this.matrix.redraw(g, cellSize, showMines);
	}	
	
	/**
	 * Change l'�tat d'une case non r�v�l�e. N'as pas d'effet si la case est d�j� r�v�l�e.  
	 * 
	 * @param x coordonn�e x de la case dont on souhaite changer l'�tat
	 * @param y coordonn�e y de la case dont on souhaite changer l'�tat
	 * 
	 * @param show 
	 * si vrai, r�v�le la case si elle n'est pas marqu�e d'un drapeau; 
	 * si faux, alterne entre le drapeau, le point d'interrogation et l'�tat non marqu�.
	 * 
	 * @return vrai si la cellule a chang� d'�tat, faux sinon
	 */
	public boolean changeCellState(int x, int y, boolean show)
	{
		boolean changed = false;
		
		Cell c = this.matrix.getElement(x, y);
		
		// Si la case n'est pas r�v�l�e
		if (c != null && !c.getState().equals(CellState.SHOWN))
		{
			// Si on souhaite la r�v�ler
			if (show)
			{
				// Si elle n'est pas marqu�e d'un drapeau
				if (!c.getState().equals(CellState.FLAGGED))
				{
					// Si c'est une mine, la partie est perdue
					if (c.isMine())
					{
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
	// ainsi qu'une liste vide de cellules (ou une valeur null),
	// laquelle sert aux appels r�cursifs de la fonction.
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
	
	/**
	 * Retourn le nombre de cases marqu�es d'un drapeau
	 * @return le nombre de cases marqu�es d'un drapeau
	 */
	public int getNbFlags()
	{
		return this.nbFlags;
	}
	
	/**
	 * Retourne la largeur de la matrice de jeu (en cases)
	 * @return la largeur de la matrice de jeu (en cases)
	 */
	public int getWidth()
	{
		return Game.LEVELS[this.level].dim.width;
	}
	
	/**
	 * Retourne la hauteur de la matrice de jeu (en cases)
	 * @return la hauteur de la matrice de jeu (en cases)
	 */
	public int getHeight()
	{
		return Game.LEVELS[this.level].dim.height;
	}
	
	/**
	 * Retourne le nombre de mines dans la partie
	 * @return le nombre de mines dans la partie
	 */
	public int getMineAmount()
	{
		return Game.LEVELS[this.level].mineAmount;
	}
}
