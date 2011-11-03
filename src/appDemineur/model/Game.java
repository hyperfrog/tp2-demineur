package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import appDemineur.model.Cell.CellState;
import util.BaseMatrix;

/**
 * La classe Game impl�mente le jeu du d�mineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Game extends BaseMatrix
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
	
	// Indique si la partie est perdue
	private boolean isLost;
	
	// Indique si la partie est gagn�e
	private boolean isWon;
	
	// Nombre de cases marqu�es d'un drapeau
	private int nbFlags;
	
	// Nombre de cases non min�es d�voil�es
	private int nbNonMineCellsShown;
	
	/**
	 * Construit une partie avec le niveau de difficult� sp�cifi�.
	 * 
	 * @param level niveau de difficult� de la partie; doit �tre 0, 1 ou 2, sinon 0 est utilis�
	 */
	public Game(int level)
	{
		super();

		this.level = (level < 0 || level > Game.LEVELS.length - 1) ? 0 : level;		
		
		this.redim(Game.LEVELS[this.level].dim.width, Game.LEVELS[this.level].dim.height);
		this.populate();
		this.addMines(Game.LEVELS[this.level].mineAmount);
		
		this.isLost = false;
		this.isWon = false;
		this.nbFlags = 0;
		this.nbNonMineCellsShown = 0;
	}

	/**
	 * Change l'�tat d'une case non d�voil�e. N'as pas d'effet si la case est d�j� d�voil�e.  
	 * 
	 * @param x coordonn�e x de la case dont on souhaite changer l'�tat
	 * @param y coordonn�e y de la case dont on souhaite changer l'�tat
	 * 
	 * @param show 
	 * si vrai, d�voile la case si elle n'est pas marqu�e d'un drapeau; 
	 * si faux, alterne entre le drapeau, le point d'interrogation et l'�tat non marqu�.
	 * 
	 * @return vrai si la cellule a chang� d'�tat, faux sinon
	 */
	public boolean changeCellState(int x, int y, boolean show)
	{
		boolean changed = false;
		
		Cell c = this.getElement(x, y);
		
		if (c != null && !c.getState().equals(CellState.SHOWN))
		{
			// Si on souhaite la d�voiler
			if (show)
			{
                // Si elle n'est pas marqu�e d'un drapeau
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
	
	// Fonction qui d�voile une cellule et, si elle n'a pas de mines adjacentes, 
	// toutes les cellules voisines avec ou sans mines adjacentes, et ce, 
	// r�cursivement jusqu'� ce qu'� ce que les cellules fronti�res soit d�voil�es, 
	// c'est-�-dire les cellules ayant des mines adjacentes.
	//
	// Si l'�tat d'une cellule est SHOWN ou FLAGGED, cet �tat n'est 
	// pas modifi�, donc les cellules FLAGGED ne sont pas d�voil�es.
	//
	// La m�thode prend les coordonn�es x et y de la cellule � d�voiler 
	// ainsi qu'une liste vide de cellules (ou une valeur null),
	// laquelle sert aux appels r�cursifs de la fonction.
	private void showCell(int x, int y, List<Cell> visitedCells)
	{
		Cell curCell = this.getElement(x, y);
		
		if (curCell != null 
				&& !curCell.getState().equals(CellState.SHOWN)
				&& !curCell.getState().equals(CellState.FLAGGED))
		{
			// Change l'�tat de la cellule courante
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
							Cell adjCell = this.getElement(c, r);

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
	}
	
	/**
	 * V�rifie si la partie est gagn�e.
	 * 
	 * Une partie est gagn�e quand toutes les cases non min�es sont d�couvertes.
	 * 
	 * @return vrai si la partie est gagn�e, sinon faux.
	 */
	public boolean isWon()
	{
		return this.isWon;
	}
	
	/**
	 * V�rifie si la partie est perdue.
	 * 
	 * Une partie est perdue quand une des mines a �t� d�couverte. 
	 * 
	 * @return vrai si la partie est perdue, sinon faux.
	 */
	public boolean isLost()
	{
		return this.isLost;
	}
	
	/**
	 * V�rifie si la partie est termin�e.
	 * 
	 * Une partie est termin�e quand elle est gagn�e ou perdue. 
	 * 
	 * @return vrai si la partie est termin�e, sinon faux.
	 */
	public boolean isOver()
	{
		return this.isWon || this.isLost;
	}
	
	/**
	 * Retourne le nombre de cases marqu�es d'un drapeau
	 * @return le nombre de cases marqu�es d'un drapeau
	 */
	public int getNbFlags()
	{
		return this.nbFlags;
	}
	
	/**
	 * Retourne le nombre de mines dans la partie
	 * @return le nombre de mines dans la partie
	 */
	public int getMineAmount()
	{
		return Game.LEVELS[this.level].mineAmount;
	}
	
	// Remplit la matrice de cellules
	private void populate()
	{
		for (int i = 0; i < this.getWidth(); i++)
		{
			for (int j = 0; j < this.getHeight(); j++)
			{
				this.setElement(i, j, new Cell());
			}
		}
	}
	
	// Remplit la matrice avec un nombre �amount� de mines
	// et d�termine, pour chaque cellule, le nombre de mines adjacentes
	private void addMines(int amount)
	{
		// �Chapeau� duquel on va tirer des cellules 
		ArrayList<Point> allCells = new ArrayList<Point>();
	
		// Ajoute la coordonn�e de chaque cellule dans le chapeau
		for (int i = 0; i < this.getWidth(); i++)
		{
			for (int j = 0; j < this.getHeight(); j++)
			{
				allCells.add(new Point(i, j));
			}
		}
		
		// Tire un nombre �amount� de cellules du chapeau
		for (int i = 0; i < amount; i++)
		{
			Random rand = new Random();

			int cellIdx = rand.nextInt(allCells.size());
			
			Point p = allCells.remove(cellIdx);
			
			// Parcourt les 9 cellules incluant la cellule courante et les cellules adjacentes
			for (int r = p.y - 1 ; r <= p.y + 1; r++)
			{
				for (int c = p.x - 1 ; c <= p.x + 1; c++)
				{
					Cell cell = this.getElement(c, r);
					// S'il y a vraiment une cellule � ces coordonn�es
					if (cell != null)
					{
						// Si ce n'est pas la cellule courante
						if (!(r == p.y && c == p.x))
						{
							// Incr�mente le compteur de mines adjacentes
							cell.setAdjacentMines(cell.getAdjacentMines() + 1);
						}
						else // On place une mine dans cette cellule
						{
							cell.setAsMine(true);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Dessine la matrice dans le Graphics sp�cifi�
	 * 
	 * @param g Graphics dans lequel la matrice doit se dessiner
	 * @param cellSize taille d'une case en pixels (nombre rationnel pour faciliter le �scaling�)
	 * @param showMines 
	 * si vrai, les mines sont montr�es sans autre condition; 
	 * si faux, elles ne sont montr�es que si la case est r�v�l�e 
	 */
	public void redraw(Graphics g, float cellSize, boolean showMines)
	{
		if (g != null)
		{
			int intCellSize = Math.round(cellSize);
			
			// Pour chaque colonne i
			for (int i = 0; i < this.getWidth(); i++)
			{
				// Pour chaque rang�e j
				for (int j = 0; j < this.getHeight(); j++)
				{
					// Obtient la cellule en (i, j)
					Cell c = this.getElement(i, j);
					
					if (c != null)
					{
						// Cr�� un Graphics pour la cellule
						Graphics g2 = g.create(
								Math.round(i * cellSize), 
								Math.round(j * cellSize), 
								intCellSize, 
								intCellSize);

						// Dessine la cellule
						c.redraw(g2, intCellSize, showMines, this.isWon);
					}
				}
			}

			// Dessine un quadrillage 

			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(Color.BLACK);

			for (int i = 0; i <= this.getWidth(); i++)
			{
				int x = Math.round(i * cellSize);

				g2d.drawLine(x, 0, x, Math.round(this.getHeight() * cellSize));
			}

			for (int i = 0; i <= this.getHeight(); i++)
			{
				int y = Math.round(i * cellSize);

				g2d.drawLine(0, y, Math.round(this.getWidth() * cellSize), y);
			}
		}
	}

	/**
	 * Retourne la cellule de la matrice � la position (x, y) sp�cifi�e.
	 * 
	 * @param x		Colonne de la cellule
	 * @param y		Rang�e de la cellule
	 * @return		La cellule � la position sp�cifi�e ou null si l'objet � cette position n'est pas une cellule
	 */
	@Override
	public Cell getElement(int x, int y)
	{
		Object o = super.getElement(x, y);
		
		return o instanceof Cell ? (Cell)o : null;
	}

}
