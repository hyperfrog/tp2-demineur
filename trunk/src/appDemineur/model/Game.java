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
	
	// Work in progress ...
	public boolean changeCellState(int x, int y, CellState state)
	{
		boolean succeed = false;
		
		Cell c = this.matrix.getElement(x, y);
		
		if (c != null)
		{
			switch (state)
			{
			case SHOWN:
				if (c.isMine())
				{
					this.isOver = true;
				}
				else if (!c.getState().equals(CellState.SHOWN) 
						&& !c.getState().equals(CellState.FLAGGED))
				{
//					List<Point> pos = new ArrayList<Point>();
//					this.showCell(x, y, pos);
					this.showCell(x, y, new ArrayList<Cell>());
					succeed = true;
				}
				break;
				
			case FLAGGED:
			case DUBIOUS:
			case HIDDEN:
				if (!c.getState().equals(CellState.SHOWN))
				{
					c.setState(state);
					succeed = true;
				}
				break;
			}
		}
		
		return succeed;
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
	//
//	private void showCell(int x, int y, List<Point> oldPos)
//	{
//		Cell cl = this.matrix.getElement(x, y);
//		
//		if (cl != null 
//				&& !cl.getState().equals(CellState.SHOWN)
//				&& !cl.getState().equals(CellState.FLAGGED))
//		{
//			if (cl.getAdjacentMines() == 0)
//			{
//				List<Point> possiblePos = new ArrayList<Point>();
//				
//				for (int r = y - 1 ; r <= y + 1; r++)
//				{
//					for (int c = x - 1 ; c <= x + 1; c++)
//					{
//						Cell t = this.matrix.getElement(c, r);
//						
//						if (t != null && !(r == y && c == x))
//						{
//							boolean found = false;
//								
//							if (oldPos.size() > 0)
//							{
//								for (int i = 0; i < oldPos.size(); i++)
//								{
//									if (oldPos.equals(new Point(c, r)))
//									{
//										found = true;
//									}
//								}
//							}
//							
//							if (!found)
//							{
//								possiblePos.add(new Point(c, r));
//							}
//						}
//					}
//				}
//				
//				for (int i = 0; i < possiblePos.size(); i++)
//				{
//					this.showCell(possiblePos.get(i).x, possiblePos.get(i).y, possiblePos);
//				}
//			}
//				
//			cl.setState(CellState.SHOWN);
//		}
//	}

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
				List<Point> cellsToVisit = new ArrayList<Point>();
				
				for (int r = y - 1 ; r <= y + 1; r++)
				{
					for (int c = x - 1 ; c <= x + 1; c++)
					{
						Cell adjCell = this.matrix.getElement(c, r);
						
						if (adjCell != null)
						{
							if (!visitedCells.contains(adjCell))
							{
								if  (!(r == y && c == x))
								{
									cellsToVisit.add(new Point(c, r));
								}
								
								visitedCells.add(adjCell);
							}
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
}
