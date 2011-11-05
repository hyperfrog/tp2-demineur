package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import appDemineur.model.Cell.CellState;
import util.BaseMatrix;

/**
 * La classe Game impl�mente la logique du jeu du d�mineur.
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
	private int nbCellsFlagged;
	
	// Nombre de cases non min�es d�voil�es
	private int nbCellsShown;
	
	/**
	 * Construit une partie avec le niveau de difficult� sp�cifi�.
	 * 
	 * @param level niveau de difficult� de la partie; doit �tre 0, 1 ou 2, sinon 0 est utilis�
	 */
	public Game(int level)
	{
		super();

		// Fixe le niveau
		this.level = (level < 0 || level > Game.LEVELS.length - 1) ? 0 : level;		
		
		// Redimensionne la matrice sous-jacente
		this.redim(Game.LEVELS[this.level].dim.width, Game.LEVELS[this.level].dim.height);
		
		// Remplit la matrice de cellules
		this.populate();
		
//		this.addMines(Game.LEVELS[this.level].mineAmount, null);
		
		// D�finit l'�tat de d�part 
		this.isLost = false;
		this.isWon = false;
		this.nbCellsFlagged = 0;
		this.nbCellsShown = 0;
	}
	
	/**
	 * Remplit la matrice avec un nombre de mines d�termin� par le niveau de difficult�, 
	 * celles-ci �tant distribu�es au hasard, et d�termine, pour chaque cellule, 
	 * le nombre de mines adjacentes.
	 * 
	 * La m�thode garantit que la cellule aux coordonn�es d�finies par cellToExclude
	 * ne contiendra pas de mine.
	 * 
	 * @param cellToExclude coordonn�es de la cellule ne devant pas contenir de mine 
	 */
	public void start(Point cellToExclude)
	{
		this.addMines(Game.LEVELS[this.level].mineAmount, cellToExclude);
	}
	
	// TODO : � flusher... M�thode laiss�e en place seulement pour les tests.
	/**
	 * Change l'�tat d'une case non d�voil�e. N'a pas d'effet si la case est d�j� d�voil�e.  
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
						this.nbCellsFlagged++;
						break;
					case FLAGGED:
						newState = CellState.DUBIOUS;
						this.nbCellsFlagged--;
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
	
	// TODO : Adapter les tests.
	/**
	 * Change l'�tat d'une case non d�voil�e en alternant entre FLAGGED, DUBIOUS et HIDDEN.
	 * N'a pas d'effet si la case est d�voil�e.  
	 * 
	 * @param x coordonn�e x de la case dont on souhaite changer l'�tat
	 * @param y coordonn�e y de la case dont on souhaite changer l'�tat
	 * 
	 * @return vrai si la cellule a chang� d'�tat, faux sinon
	 */
	public boolean changeCellState(int x, int y)
	{
		boolean changed = false;
		
		Cell c = this.getElement(x, y);
		
		if (c != null && c.getState() != CellState.SHOWN)
		{
			// Alterne entre FLAGGED, DUBIOUS et HIDDEN
			CellState newState = null;

			switch(c.getState())
			{
				case HIDDEN:
					newState = CellState.FLAGGED;
					this.nbCellsFlagged++;
					break;
				case FLAGGED:
					newState = CellState.DUBIOUS;
					this.nbCellsFlagged--;
					break;
				case DUBIOUS:
					newState = CellState.HIDDEN;
					break;
			}

			changed = c.setState(newState);
		}
		
		return changed;
	}
	
	// TODO : Adapter les tests.
	/**
	 * D�voile une cellule et, si elle n'a pas de mines adjacentes, 
	 * toutes les cellules voisines avec ou sans mines adjacentes, et ce, 
	 * r�cursivement jusqu'� ce qu'� ce que soient d�voil�es les cellules fronti�res, 
	 * c'est-�-dire les cellules ayant des mines adjacentes.
	 * 
	 * Si l'�tat d'une cellule est SHOWN ou FLAGGED, cet �tat n'est 
	 * pas modifi�, donc les cellules FLAGGED ne sont pas d�voil�es.
	 * 
	 * La m�thode prend comme param�tres les coordonn�es x et y de la cellule � d�voiler 
	 * 
	 * @param x coordonn�e x de la case dont on souhaite changer l'�tat
	 * @param y coordonn�e y de la case dont on souhaite changer l'�tat
	 * 
	 * @return vrai si la cellule a chang� d'�tat, faux sinon
	 */
	public boolean showCell(int x, int y)
	{
		boolean changed = false;
		
		Cell c = this.getElement(x, y);
		
		if (c != null)
		{
			CellState oldState = c.getState();
			this.showCell(x, y, null);
			changed = c.getState() != oldState;
		}

		return changed;
	}
	
	// D�voile une cellule et, si elle n'a pas de mines adjacentes, 
	// toutes les cellules voisines avec ou sans mines adjacentes, et ce, 
	// r�cursivement jusqu'� ce qu'� ce que soient d�voil�es les cellules fronti�res, 
	// c'est-�-dire les cellules ayant des mines adjacentes.
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
				this.isWon = ++this.nbCellsShown == this.getWidth() * this.getHeight() - this.getMineAmount();

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
	public int getNbCellsFlagged()
	{
		return this.nbCellsFlagged;
	}
	
	/**
	 * Retourne le nombre de mines dans la partie
	 * @return le nombre de mines dans la partie
	 */
	public int getMineAmount()
	{
		return Game.LEVELS[this.level].mineAmount;
	}
	
	
	// TODO : M�thode de test
	/**
	 * Retourne le nombre de cases non min�es d�voil�es.
	 * 
	 * @return nombre de cases non min�es d�voil�es
	 */
	public int getNbCellsShown()
	{
		return nbCellsShown;
	}

	/**
	 * Retourne la cellule � la position (x, y) sp�cifi�e.
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
	
	// Remplit la matrice avec un nombre de mines d�termin� par �amount�, 
	// celles-ci �tant distribu�es au hasard, et d�termine, pour chaque cellule, 
	// le nombre de mines adjacentes.
	//
	// La m�thode garantit que la cellule aux coordonn�es d�finies par cellToExclude
	// ne contiendra pas de mine.
	private void addMines(int amount, Point cellToExclude)
	{
		// �Chapeau� duquel on va tirer des cellules 
		ArrayList<Point> allCells = new ArrayList<Point>();
	
		// Ajoute la coordonn�e de chaque cellule dans le chapeau
		for (int i = 0; i < this.getWidth(); i++)
		{
			for (int j = 0; j < this.getHeight(); j++)
			{
				if (cellToExclude == null || !cellToExclude.equals(new Point(i, j))) 
				{
					allCells.add(new Point(i, j));
				}
			}
		}
		
		// Tire un nombre �amount� de cellules du chapeau
		for (int i = 0; i < amount && i < this.getWidth() * this.getHeight() - 1; i++)
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
	 * @param cellSize taille d'une case en pixels
	 * @param showMines 
	 * si vrai, les mines sont montr�es sans autre condition; 
	 * si faux, elles ne sont montr�es que si la case est r�v�l�e 
	 */
	public void redraw(Graphics g, float cellSize, boolean showMines)
	{
		if (g != null)
		{
			int roundedCellSize = Math.round(cellSize);
			
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
								roundedCellSize, 
								roundedCellSize);

						// Dessine la cellule
						c.redraw(g2, roundedCellSize, showMines, this.isWon);
					}
				}
			}

			// Si n�cessaire, dessine un quadrillage pour remplir les espaces entre les cellules
			// Se produit quand cellSize a une partie fractionnaire < 0,5 
			if (roundedCellSize - cellSize < 0)
			{
				Graphics2D g2d = (Graphics2D) g;
				g2d.setStroke(new BasicStroke(2));
				g2d.setColor(Color.DARK_GRAY);

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
	}
}
