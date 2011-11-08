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
 * La classe Game implémente la logique du jeu du démineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Game extends BaseMatrix
{
	// Classe définissant les propriétés d'un niveau de difficulté
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
	
	// Les trois niveaux de difficulté du jeu
	private static final Level[] LEVELS = new Level[] { 
		new Level(new Dimension(9, 9), 10), 	// Débutant : 9 x 9 avec 10 mines
		new Level(new Dimension(16, 16), 40), 	// Intermédiaire : 16 x 16 avec 40 mines
		new Level(new Dimension(30, 16), 99)	// Expert : 30 x 16 avec 99 mines
		};

	private int level;
	
	// Indique si la partie est perdue
	private boolean isLost;
	
	// Indique si la partie est gagnée
	private boolean isWon;
	
	// Nombre de cases marquées d'un drapeau
	private int nbCellsFlagged;
	
	// Nombre de cases non minées dévoilées
	private int nbCellsShown;
	
	// Indique si des mines ont été placées dans la grille
//	private boolean containsMines;
	
	/**
	 * Construit une partie avec le niveau de difficulté spécifié.
	 * 
	 * @param level niveau de difficulté de la partie; doit être 0, 1 ou 2, sinon 0 est utilisé
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
		
		// Définit l'état de départ 
		this.isLost = false;
		this.isWon = false;
		this.nbCellsFlagged = 0;
		this.nbCellsShown = 0;
//		this.containsMines = false;
	}
	
	/*
	 * Remplit la matrice avec le nombre de mines déterminé par le niveau de difficulté, 
	 * celles-ci étant distribuées au hasard, et détermine, pour chaque cellule, 
	 * le nombre de mines adjacentes.
	 * 
	 * La méthode garantit que la cellule aux coordonnées définies par cellToExclude
	 * ne contiendra pas de mine. 
	 *  
	 * @param cellToExclude coordonnées de la cellule ne devant pas contenir de mine
	 * 
	 * @return vrai si l'opération a réussi, faux sinon
	 *  
	 */
	private boolean addMines(Point cellToExclude)
	{
		boolean success = true;
		
		// «Chapeau» duquel on va tirer des cellules 
		ArrayList<Point> eligibleCells = new ArrayList<Point>();
	
		// Parcourt toute la matrice
		for (int i = 0; i < this.getWidth(); i++)
		{
			for (int j = 0; j < this.getHeight(); j++)
			{
				Point cellCoords = new Point(i, j);
				
				// Ajoute dans le chapeau la coordonnée de chaque cellule qui n'est pas 
				// la cellule à exclure 
				if (cellToExclude == null || !cellToExclude.equals(cellCoords))
				{
					eligibleCells.add(cellCoords);
				}
			}
		}
		
		if (eligibleCells.size() >= Game.LEVELS[this.level].mineAmount)
		{
			// Tire le nombre approprié de cellules du chapeau
			for (int i = 0; i < Game.LEVELS[this.level].mineAmount; i++)
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
						// S'il y a vraiment une cellule à ces coordonnées
						if (cell != null)
						{
							// Si ce n'est pas la cellule courante
							if (!(r == p.y && c == p.x))
							{
								// Incrémente le compteur de mines adjacentes
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
	 * Change l'état d'une case non dévoilée en alternant entre FLAGGED, DUBIOUS et HIDDEN.
	 * N'a pas d'effet si la case est dévoilée (état = SHOWN).  
	 * 
	 * @param x coordonnée x de la case dont on souhaite changer l'état
	 * @param y coordonnée y de la case dont on souhaite changer l'état
	 * 
	 * @return vrai si la cellule a changé d'état, faux sinon
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
	 * Dévoile une cellule et, si elle n'a pas de mines adjacentes, 
	 * toutes les cellules voisines avec ou sans mines adjacentes, et ce, 
	 * récursivement jusqu'à ce qu'à ce que soient dévoilées les cellules frontières, 
	 * c'est-à-dire les cellules ayant des mines adjacentes.
	 * 
	 * Si l'état d'une cellule est SHOWN ou FLAGGED, cet état n'est 
	 * pas modifié, donc les cellules FLAGGED ne sont pas dévoilées.
	 * 
	 * La méthode prend comme paramètres les coordonnées x et y de la cellule à dévoiler 
	 * 
	 * @param x coordonnée x de la case dont on souhaite changer l'état
	 * @param y coordonnée y de la case dont on souhaite changer l'état
	 * 
	 * @return vrai si la cellule a changé d'état, faux sinon
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
	
	// Dévoile une cellule et, si elle n'a pas de mines adjacentes, 
	// toutes les cellules voisines avec ou sans mines adjacentes, et ce, 
	// récursivement jusqu'à ce qu'à ce que soient dévoilées les cellules frontières, 
	// c'est-à-dire les cellules ayant des mines adjacentes.
	private void showCell(int x, int y, List<Cell> visitedCells)
	{
		Cell curCell = this.getElement(x, y);
		
		if (curCell != null 
				&& !curCell.getState().equals(CellState.SHOWN)
				&& !curCell.getState().equals(CellState.FLAGGED))
		{
			// Ajoute les mines si c'est la première cellule à être dévoilée  
			if (this.nbCellsShown == 0)
			{
				this.addMines(new Point(x, y));
			}
			
			// Change l'état de la cellule courante
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
							Cell adjCell = this.getElement(c, r);

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
	 * Retourne le nombre de cases marquées d'un drapeau
	 * @return le nombre de cases marquées d'un drapeau
	 */
	public int getNbCellsFlagged()
	{
		return this.nbCellsFlagged;
	}
	
	/**
	 * Retourne le nombre de mines associé au niveau de difficulté
	 * de la partie courante 
	 * 
	 * @return nombre de mines associé au niveau de difficulté de la partie courante
	 */
	public int getMineAmount()
	{
		return Game.LEVELS[this.level].mineAmount;
	}
	
	
	// TODO : Méthode de test
	/**
	 * Retourne le nombre de cases non minées dévoilées.
	 * 
	 * @return nombre de cases non minées dévoilées
	 */
	public int getNbCellsShown()
	{
		return this.nbCellsShown;
	}
	
	// TODO : Méthode de test
	/**
	 * Retourne le niveau de la partie en cours.
	 * 
	 * @return le niveau de la partie en cours
	 */
	public int getCurrentLevel()
	{
		return this.level;
	}

	/**
	 * Retourne la cellule à la position (x, y) spécifiée.
	 * 
	 * @param x		Colonne de la cellule
	 * @param y		Rangée de la cellule
	 * @return		La cellule à la position spécifiée ou null si l'objet à cette position n'est pas une cellule
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
	 * Dessine la matrice dans le Graphics spécifié
	 * 
	 * @param g Graphics dans lequel la matrice doit se dessiner
	 * @param cellSize taille d'une case en pixels
	 * @param showMines 
	 * si vrai, les mines sont montrées sans autre condition; 
	 * si faux, elles ne sont montrées que si la case est révélée 
	 */
	public void redraw(Graphics g, float cellSize, boolean showMines)
	{
		if (g != null)
		{
			int roundedCellSize = Math.round(cellSize);
			
			// Pour chaque colonne i
			for (int i = 0; i < this.getWidth(); i++)
			{
				// Pour chaque rangée j
				for (int j = 0; j < this.getHeight(); j++)
				{
					// Obtient la cellule en (i, j)
					Cell c = this.getElement(i, j);
					
					if (c != null)
					{
						// Créé un Graphics pour la cellule
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

			// Si nécessaire, dessine un quadrillage pour remplir les espaces entre les cellules
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
