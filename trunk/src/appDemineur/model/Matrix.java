package appDemineur.model;

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
	 * @param width
	 * @param height
	 * @param mineAmount
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
		// �Chapeau� duquel on va tirer des num�ros de cellule 
		ArrayList<Integer> allCells = new ArrayList<Integer>();
		
		// Ajoute chaque num�ro de cellule dans le chapeau
		for (int i = 0; i < this.getWidth() * this.getHeight(); i++)
		{
			allCells.add(i);
		}
		
		// Tire un nombre �amount� de cellules du chapeau
		for (int i = 0; i < amount; i++)
		{
			Random rand = new Random();

			int cellIdx = rand.nextInt(allCells.size());
			
			int cellNum = allCells.remove(cellIdx);
			
			int x = cellNum % this.getWidth();
			int y = cellNum / this.getHeight();
						
			// Parcourt les 9 cellules incluant la cellule courante et les cellules adjacentes
			for (int r = y - 1 ; r <= y + 1; r++)
			{
				for (int c = x - 1 ; c <= x + 1; c++)
				{
					Cell cell = this.getElement(c, r);
					// S'il y a vraiment une cellule � ces coordonn�es
					if (cell != null)
					{
						// Si ce n'est pas la cellule courante
						if (!(r == y && c == x))
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
	 * Retourne l'�l�ment de la matrice pr�sent � la position (x, y) donn�e.
	 * 
	 * @param x		Position en largeur.
	 * @param y		Position en hauteur
	 * @return		L'�l�ment � la position donn�e
	 */
	@Override
	public Cell getElement(int x, int y)
	{
		Object o = super.getElement(x, y);
		
		return o instanceof Cell ? (Cell)o : null;
	}
}
