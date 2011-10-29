package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import util.BaseMatrix;

/**
 * La classe Matrix impl�mente la matrice du jeu du d�mineur.
 * Elle est compos�e de cellules du type Cell.
 * 
 * @author Alexandre Tremblay
 * @author Christian Lesage
 * 
 * @See appDemineur.model.Cell
 *
 */

public class Matrix extends BaseMatrix
{
	/**
	 * Construit une matrice de la largeur et la hauteur sp�cifi�es
	 * et y ajoute le nombre de mines pass� en param�tre dans des cellules
	 * choisies au hasard.
	 * 
	 * @param width largeur de la matrice (en cases)
	 * @param height hauteur de la matrice (en cases)
	 * @param mineAmount nombre de mines � mettre dans la matrice
	 */
	public Matrix(int width, int height, int mineAmount)
	{
		super(width, height);
		this.populate();
		this.addMines(mineAmount);
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
	 * @param cellSize taille d'une case en pixels
	 * @param showMines 
	 * si vrai, les mines sont montr�es sans autre condition; 
	 * si faux, elles ne sont montr�es que si la case est r�v�l�e 
	 */
	public void redraw(Graphics g, float cellSize, boolean showMines)
	{
		if (g != null)
		{
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
								Math.round(cellSize), 
								Math.round(cellSize));

						// Dessine la cellule
						c.redraw(g2, cellSize, showMines);
					}
				}
			}

			// Dessine un quadrillage 

			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(3));
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
