package appDemineur.model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import appDemineur.model.Cell.CellState;

/**
 * La classe Game implémente le jeu du démineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Game
{
	// Classe définissant les propriétés d'un niveau de difficulté
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
	
	// Les trois niveaux de difficulté du jeu
	public static final Level[] LEVELS = new Level[] { 
		new Level(new Dimension(9, 9), 10), 
		new Level(new Dimension(16, 16), 40), 
		new Level(new Dimension(30, 16), 99)
		};

	private int level;
	
	// Matrice contenant la grille de jeu
	private Matrix matrix;
	
	// Indique si la partie est perdue
	private boolean isLost;
	
	// Indique si la partie est gagnée
	private boolean isWon;
	
	// Nombre de cases marquées d'un drapeau
	private int nbFlags;
	
	// Nombre de cases non minées dévoilées
	private int nbNonMineCellsShown;
	
	/**
	 * Construit une partie avec le niveau de difficulté spécifié.
	 * 
	 * @param level niveau de difficulté de la partie; doit être 0, 1 ou 2, sinon 0 est utilisé
	 */
	public Game(int level)
	{
		this.level = (level < 0 || level > Game.LEVELS.length - 1) ? 0 : level;		
		
		this.matrix = new Matrix(
				Game.LEVELS[this.level].dim.width, 
				Game.LEVELS[this.level].dim.height, 
				Game.LEVELS[this.level].mineAmount);
		
		this.isLost = false;
		this.isWon = false;
		this.nbFlags = 0;
		this.nbNonMineCellsShown = 0;
	}

	/**
	 * Redessine la partie
	 * 
	 * @param g Graphics dans lequel la partie doit se dessiner
	 * @param cellSize taille d'une case en pixels
	 * @param showMines 
	 * si vrai, les mines sont montrées sans autre condition; 
	 * si faux, elles ne sont montrées que si la case est révélée 
	 */
	public void redraw(Graphics g, float cellSize, boolean showMines)
	{
		this.matrix.redraw(g, cellSize, showMines);
	}	
	
	/**
	 * Change l'état d'une case non dévoilée. N'as pas d'effet si la case est déjà dévoilée.  
	 * 
	 * @param x coordonnée x de la case dont on souhaite changer l'état
	 * @param y coordonnée y de la case dont on souhaite changer l'état
	 * 
	 * @param show 
	 * si vrai, dévoile la case si elle n'est pas marquée d'un drapeau; 
	 * si faux, alterne entre le drapeau, le point d'interrogation et l'état non marqué.
	 * 
	 * @return vrai si la cellule a changé d'état, faux sinon
	 */
	public boolean changeCellState(int x, int y, boolean show)
	{
		boolean changed = false;
		
		Cell c = this.matrix.getElement(x, y);
		
		// Si la case n'est pas dévoilée
		if (c != null && !c.getState().equals(CellState.SHOWN))
		{
			// Si on souhaite la dévoiler
			if (show)
			{
                // Si elle n'est pas marquée d'un drapeau
                if (!c.getState().equals(CellState.FLAGGED))
                {
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
	
	// Fonction qui dévoile une cellule et, si elle n'a pas de mines adjacentes, 
	// toutes les cellules voisines avec ou sans mines adjacentes, et ce, 
	// récursivement jusqu'à ce qu'à ce que les cellules frontières soit dévoilées, 
	// c'est-à-dire les cellules ayant des mines adjacentes.
	//
	// Si l'état d'une cellule est SHOWN ou FLAGGED, cet état n'est 
	// pas modifié, donc les cellules FLAGGED ne sont pas dévoilées.
	//
	// La méthode prend les coordonnées x et y de la cellule à dévoiler 
	// ainsi qu'une liste vide de cellules (ou une valeur null),
	// laquelle sert aux appels récursifs de la fonction.
	private void showCell(int x, int y, List<Cell> visitedCells)
	{
		Cell curCell = this.matrix.getElement(x, y);
		
		if (curCell != null 
				&& !curCell.getState().equals(CellState.SHOWN)
				&& !curCell.getState().equals(CellState.FLAGGED))
		{
			// Change l'état de la cellule courante
			curCell.setState(CellState.SHOWN);
			
			if (curCell.isMine())
			{
				this.isLost = true;
			}
			else
			{
				this.isWon = ++this.nbNonMineCellsShown == this.getWidth() * this.getHeight() - this.getMineAmount();

				// Si aucune cellule adjacente ne contient de mine 
				if (curCell.getAdjacentMines() == 0)
				{
					// Au besoin, construit une liste de cellules à ne pas revérifier plus tard
					if (visitedCells == null)
					{
						visitedCells = new ArrayList<Cell>();
					}

					// Construit une liste de cellules à vérifier maintenant
					List<Point> cellsToVisit = new ArrayList<Point>();

					// Parcourt les 9 cellules incluant la cellule courante et les cellules adjacentes
					for (int r = y - 1 ; r <= y + 1; r++)
					{
						for (int c = x - 1 ; c <= x + 1; c++)
						{
							Cell adjCell = this.matrix.getElement(c, r);

							// Si la cellule n'a pas encore été vérifiée
							if (adjCell != null && !visitedCells.contains(adjCell))
							{
								// Si ce n'est pas la cellule courante
								if  (!(r == y && c == x))
								{
									// Ajoute la cellule dans la liste des cellules à vérifier maintenant
									cellsToVisit.add(new Point(c, r));
								}
								// Ajoute la cellule dans la liste des cellules à ne pas revérifier plus tard
								visitedCells.add(adjCell);
							}
						}
					}

					// Pour chacune des cellules à vérifier maintenant
					for (Point p : cellsToVisit)
					{
						this.showCell(p.x, p.y, visitedCells);
					}
				}
			}
		}
	}
	
	/**
	 * Vérifie si la partie est gagnée.
	 * 
	 * Une partie est gagnée quand toutes les cases non minées sont découvertes.
	 * 
	 * @return vrai si la partie est gagnée, sinon faux.
	 */
	public boolean isWon()
	{
		return this.isWon;
	}
	
	/**
	 * Vérifie si la partie est perdue.
	 * 
	 * Une partie est perdue quand une des mines a été découverte. 
	 * 
	 * @return vrai si la partie est perdue, sinon faux.
	 */
	public boolean isLost()
	{
		return this.isLost;
	}
	
	/**
	 * Vérifie si la partie est terminée.
	 * 
	 * Une partie est terminée quand elle est gagnée ou perdue. 
	 * 
	 * @return vrai si la partie est terminée, sinon faux.
	 */
	public boolean isOver()
	{
		return this.isWon || this.isLost;
	}
	
	/**
	 * Retourn le nombre de cases marquées d'un drapeau
	 * @return le nombre de cases marquées d'un drapeau
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
