package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import appDemineur.model.Cell.CellState;

public class Game extends JPanel
{
	// Matrice contenant la grille de jeu
	private Matrix matrix;
	
	// Indique si la partie est termin�e
	private boolean isOver;
	
	private int nbFlags;
	
	/**
	 * 
	 * 
	 * @param width
	 * @param height
	 * @param mineAmount
	 */
	public Game(int width, int height, int mineAmount)
	{
		this.matrix = new Matrix(width, height, mineAmount);
		this.nbFlags = 0;
	}

	/**
	 * 
	 * 
	 * @param g
	 * @param size
	 * @param centerPoint
	 */
	public void redraw(Graphics g, float cellSize)
	{
		if (g != null)
		{       
			for (int i = 0; i < this.matrix.getWidth(); i++)
			{
				for (int j = 0; j < this.matrix.getHeight(); j++)
				{
					if (this.matrix.getElement(i, j) != null)
					{
						Graphics g2 = g.create(
								Math.round(i * cellSize), 
								Math.round(j * cellSize), 
								Math.round(cellSize), 
								Math.round(cellSize));

						Cell c = this.matrix.getElement(i, j);
						if (c != null)
						{
							c.redraw(g2, cellSize);
						}
					}
				}
			}

			// Dessine un quadrillage 

			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(3));

			for (int i = 0; i <= this.matrix.getWidth(); i++)
			{
				int x = Math.round(i * cellSize);

				g2d.drawLine(x, 0, x, Math.round(this.matrix.getHeight() * cellSize));
			}

			for (int i = 0; i <= this.matrix.getHeight(); i++)
			{
				int y = Math.round(i * cellSize);

				g2d.drawLine(0, y, Math.round(this.matrix.getWidth() * cellSize), y);
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @param x
	 * @param y
	 * @param newState
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
			else
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
	
	// Fonction r�cursive qui affiche toutes les cellules ayant un nombre de
	// voisins �gal � 0 jusqu'� ce que la cellule poss�de un nombre de voisins
	// diff�rent de 0. Si l'�tat de la cellules est FLAGGED alors elle n'est 
	// pas affect�.
	//
	// La m�thode prend la position � afficher ainsi qu'une liste des anciennes
	// cellules ayant �t� visit� par la m�thode auparavant. La m�thode ce rappelle
	// elle-m�me pour toutes les positions voisines qui ne faisaient pas parties
	// de la liste d'anciennes positions.
	private void showCell(int x, int y, List<Cell> visitedCells)
	{
		Cell curCell = this.matrix.getElement(x, y);
		
		if (curCell != null 
				&& !curCell.getState().equals(CellState.SHOWN)
				&& !curCell.getState().equals(CellState.FLAGGED))
		{
			curCell.setState(CellState.SHOWN);

			if (curCell.getAdjacentMines() == 0)
			{
				if (visitedCells == null)
				{
					visitedCells = new ArrayList<Cell>();
				}

				List<Point> cellsToVisit = new ArrayList<Point>();
				
				for (int r = y - 1 ; r <= y + 1; r++)
				{
					for (int c = x - 1 ; c <= x + 1; c++)
					{
						Cell adjCell = this.matrix.getElement(c, r);
						
						if (adjCell != null && !visitedCells.contains(adjCell))
						{
							if  (!(r == y && c == x))
							{
								cellsToVisit.add(new Point(c, r));
							}

							visitedCells.add(adjCell);
						}
					}
				}
				
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
}
