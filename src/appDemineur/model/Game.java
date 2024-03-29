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
	// Les trois niveaux de difficult� du jeu
	public static final Level[] LEVELS = new Level[] { 
		// D�butant : 9 x 9 avec 10 mines
		new Level(new Dimension(9, 9), 10, "Beginner", "D�butant"),
		// Interm�diaire : 16 x 16 avec 40 mines
		new Level(new Dimension(16, 16), 40, "Intermediate", "Interm�diaire"), 	
		// Expert : 30 x 16 avec 99 mines
		new Level(new Dimension(30, 16), 99, "Expert", "Expert")	
		};

	// Indique le niveau de la partie courante
	private int levelNum;
	
	// Indique si la partie est perdue
	private boolean isLost;
	
	// Indique si la partie est gagn�e
	private boolean isWon;
	
	// Nombre de cases marqu�es d'un drapeau
	private int nbCellsFlagged;
	
	// Nombre de cases non min�es qui sont d�voil�es
	private int nbCellsShown;
	
	/**
	 * Construit une partie avec le niveau de difficult� sp�cifi�.
	 * 
	 * @param levelNum niveau de difficult� de la partie; si < 0, 0 est utilis� et si > 2, 2 est utilis�
	 */
	public Game(int levelNum)
	{
		super();

		// Fixe le niveau
		this.levelNum = this.validateLevelNum(levelNum); 
		
		// Redimensionne la matrice sous-jacente
		this.redim(Game.LEVELS[this.levelNum].dim.width, Game.LEVELS[this.levelNum].dim.height);
		
		// Remplit la matrice de cellules
		this.populate();
		
		// D�finit l'�tat de d�part 
		this.isLost = false;
		this.isWon = false;
		this.nbCellsFlagged = 0;
		this.nbCellsShown = 0;
	}
	
	// Valide que le niveau se situe entre 0 et le nombre de niveaux
	private int validateLevelNum(int levelNum)
	{
		return Math.max(0, Math.min(Game.LEVELS.length - 1, levelNum));
	}
	
	/*
	 * Remplit la matrice avec le nombre de mines d�termin� par le niveau de difficult�, 
	 * celles-ci �tant distribu�es au hasard, et d�termine, pour chaque cellule, 
	 * le nombre de mines adjacentes.
	 * 
	 * La m�thode garantit que la cellule aux coordonn�es d�finies par cellToExclude
	 * ne contiendra pas de mine. 
	 *  
	 * @param cellToExclude coordonn�es de la cellule ne devant pas contenir de mine
	 * 
	 * @return vrai si l'op�ration a r�ussi, faux sinon
	 *  
	 */
	private boolean addMines(Point cellToExclude)
	{
		boolean success = true;
		
		// �Chapeau� duquel on va tirer des cellules 
		ArrayList<Point> eligibleCells = new ArrayList<Point>();
	
		// Parcourt toute la matrice
		for (int i = 0; i < this.getWidth(); i++)
		{
			for (int j = 0; j < this.getHeight(); j++)
			{
				Point cellCoords = new Point(i, j);
				
				// Ajoute dans le chapeau la coordonn�e de chaque cellule qui n'est pas 
				// la cellule � exclure 
				if (cellToExclude == null || !cellToExclude.equals(cellCoords))
				{
					eligibleCells.add(cellCoords);
				}
			}
		}
		
		if (eligibleCells.size() >= Game.LEVELS[this.levelNum].mineAmount)
		{
			// Tire le nombre appropri� de cellules du chapeau
			for (int i = 0; i < Game.LEVELS[this.levelNum].mineAmount; i++)
			{
				Random rand = new Random();

				int cellIdx = rand.nextInt(eligibleCells.size());
				
				Point p = eligibleCells.remove(cellIdx);
				
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
		else
		{
			success = false;
		}
		
		return success;
	}
	
	/**
	 * Change l'�tat d'une case non d�voil�e en alternant entre FLAGGED, DUBIOUS et HIDDEN.
	 * N'a pas d'effet si la case est d�voil�e (�tat = SHOWN).  
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
			// Ajoute les mines si c'est la premi�re cellule � �tre d�voil�e  
			if (this.nbCellsShown == 0)
			{
				this.addMines(new Point(x, y));
			}
			
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
	 * Retourne le nombre de mines associ� au niveau de difficult�
	 * de la partie courante 
	 * 
	 * @return nombre de mines associ� au niveau de difficult� de la partie courante
	 */
	public int getMineAmount()
	{
		return Game.LEVELS[this.levelNum].mineAmount;
	}
	
	/**
	 * Retourne le nombre de cases non min�es d�voil�es.
	 * 
	 * @return nombre de cases non min�es d�voil�es
	 */
	public int getNbCellsShown()
	{
		return this.nbCellsShown;
	}
	
	/**
	 * Retourne le niveau de la partie en cours.
	 * 
	 * @return le niveau de la partie en cours
	 */
	public int getLevelNum()
	{
		return this.levelNum;
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
