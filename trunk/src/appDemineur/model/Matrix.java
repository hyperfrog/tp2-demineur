package appDemineur.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * La classe Matrix ...
 * 
 * @author Alexandre Tremblay
 * @author Christian Lesage
 *
 */

public class Matrix
{
	// Contient la matrice courante.
	private Cell[][] m;
	
	// Largeur de la matrice courante.
	private int width;
	
	// Hauteur de la matrice courante.
	private int height;
	
	/**
	 * Construit une nouvelle matrice de la largeur et la hauteur passée en paramètre.
	 * 
	 * @param width Largeur de la matrice
	 * @param height Hauteur de la matrice
	 */
	public Matrix(int width, int height)
	{
		this(width, height, 40);
	}
	
	/**
	 * Construit une nouvelle matrice de la largeur et la hauteur passée en paramètre
	 * en ajoutant le nombre de mines passée en paramètre à l'intérieur de la matrice.
	 * 
	 * @param width
	 * @param height
	 * @param mineAmount
	 */
	public Matrix(int width, int height, int mineAmount)
	{
		this.set(new Cell[width][height]);
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
	
	private void addMines(int amount)
	{
		// «Chapeau» duquel on va tirer des numéros de cellule 
		ArrayList<Integer> allCells = new ArrayList<Integer>();
		
		// Ajoute chaque numéro de cellule dans le chapeau
		for(int i = 0; i < this.width * this.height; i++)
		{
			allCells.add(i);
		}
		
		// Tire un nombre «amount» de cellules du chapeau
		for(int i = 0; i < amount; i++)
		{
			Random rand = new Random();

			int cellIdx = rand.nextInt(allCells.size());
			
			int cellNum = allCells.remove(cellIdx);
			
			int x = cellNum % this.width;
			int y = cellNum / this.height;
						
			// On place une mine dans cette cellule
			this.getElement(x, y).setAsMine(true);
			
			// On incrémente le compteur de mines adjacentes de chacune des mines adjacentes
			for (int r = y - 1 ; r <= y + 1; r++)
			{
				for (int c = x - 1 ; c <= x + 1; c++)
				{
					// Si ce n'est pas la cellule courante
					if (!(r == y && c == x))
					{
						Cell cell = this.getElement(c, r);
						// S'il y a vraiment une cellule à ces coordonnées
						if (cell != null)
						{
							// Incrémente le compteur de mines adjacentes
							cell.setAdjacentMines(cell.getAdjacentMines() + 1);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Remplace la matrice courante par une nouvelle matrice passée en paramètre.
	 * Celle-ci ne doit pas être null.
	 * 
	 * @param newM		Nouvelle matrice
	 */
	public void set(Cell[][] newM)
	{
		if (newM != null && newM.length != 0)
		{
			this.setWidth(newM[0].length);
			this.setHeight(newM.length);
			this.m = newM;
		}
	}
	
	/**
	 * Retourne l'élément de la matrice présent à la position (x, y) donnée.
	 * 
	 * @param x		Position en largeur.
	 * @param y		Position en hauteur
	 * @return		L'élément à la position donnée
	 */
	public Cell getElement(int x, int y)
	{
		Cell s = null;
		
		if ((x < this.getWidth() && x >= 0) && (y < this.getHeight() && y >= 0))
			s = this.m[x][y];
			
		return s;
	}
	
	/**
	 * Remplace l'élément à la position (x, y) donnée par l'élément passée
	 * en paramètre. Celui-ci ne doit pas être null et la position demandée
	 * doit se trouver à l'intérieur de la matrice.
	 * 
	 * @param x Position en largeur
	 * @param y	Position en hauteur
	 * @param o	Nouvel élément
	 * 
	 * @return succeed Retourne true si l'opération s'est produite avec succès, sinon false
	 */
	public boolean setElement(int x, int y, Cell s)
	{
		boolean succeed = false;
		
		if (s != null && (x < this.getWidth() && x >= 0) && (y < this.getHeight() && y >= 0))
		{
			this.m[x][y] = s;
			succeed = true;
		}
		
		return succeed;
	}
	
	/**
	 * Retourne une représentation visuelle de la matrice.
	 * 
	 * @return s	Représentation visuelle de la matrice.
	 */
	public String toString()
	{
		String s = new String("");
		
		for (int i = 0; i < this.getHeight(); i++)
		{
			s += "[ ";
			
			for (int j = 0; j < this.getWidth(); j++)
			{
				s += this.getElement(j, i).toString();
				
				if (j < this.getWidth() - 1)
					s += ", ";
			}
			
			s += " ]\n";
		}
		
		return s;
	}
	
	/**
	 * Retourne la largeur de la matrice.
	 * 
	 * @return		La largeur de la matrice.
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 * Remplace la largeur courante par celle passée en paramètre.
	 * 
	 * @param w		La nouvelle largeur de la matrice.
	 */
	public void setWidth(int w)
	{
		this.width = w;
	}
	
	/**
	 * Retourne la hauteur de la matrice.
	 * 
	 * @return		La hauteur de la matrice.
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	/**
	 * Remplace la hauteur courante par celle passée en paramètre.
	 * 
	 * @param h		La nouvelle hauteur de la matrice.
	 */
	public void setHeight(int h)
	{
		this.height = h;
	}
}
