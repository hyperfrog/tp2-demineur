/**
 * 
 */
package util;

/**
 * La classe BaseMatrix impl�mente une matrice d'�l�ments de type Object.
 * 
 * @author Alexandre Tremblay
 * @author Christian Lesage
 *
 */

public class BaseMatrix
{
	// Contient la matrice courante.
	private Object[][] m;
	
	// Largeur de la matrice courante.
	private int width;
	
	// Hauteur de la matrice courante.
	private int height;
	
	/**
	 * Construit une matrice de la largeur et la hauteur sp�cifi�es
	 * en ajoutant le nombre de mines pass�e en param�tre � l'int�rieur de la matrice.
	 * 
	 * @param width
	 * @param height
	 * @param mineAmount
	 */
	public BaseMatrix(int width, int height)
	{
		
		Object[][] newM = new Object[width][height];
		
		this.width = newM[0].length;
		this.height = newM.length;
		this.m = newM;
		
//		this.populate();
	}

	/**
	 * Retourne l'�l�ment de la matrice pr�sent � la position (x, y) donn�e.
	 * 
	 * @param x		Position en largeur.
	 * @param y		Position en hauteur
	 * @return		L'�l�ment � la position donn�e
	 */
	public Object getElement(int x, int y)
	{
		Object o = null;
		
		if ((x < this.getWidth() && x >= 0) && (y < this.getHeight() && y >= 0))
		{
			o = this.m[x][y];
		}
			
		return o;
	}
	
	/**
	 * Remplace l'�l�ment � la position (x, y) donn�e par l'�l�ment pass�
	 * en param�tre. Celui-ci ne doit pas �tre null et la position demand�e
	 * doit se trouver � l'int�rieur de la matrice.
	 * 
	 * @param x Position en largeur
	 * @param y	Position en hauteur
	 * @param o	Nouvel �l�ment
	 * 
	 * @return succeed Retourne true si l'op�ration s'est produite avec succ�s, sinon false
	 */
	public boolean setElement(int x, int y, Object o)
	{
		boolean succeed = false;
		
		if (o != null && (x < this.getWidth() && x >= 0) && (y < this.getHeight() && y >= 0))
		{
			this.m[x][y] = o;
			succeed = true;
		}
		
		return succeed;
	}
	
	/**
	 * Retourne une repr�sentation visuelle de la matrice.
	 * 
	 * @return s	Repr�sentation visuelle de la matrice.
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
				{
					s += ", ";
				}
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
	 * Retourne la hauteur de la matrice.
	 * 
	 * @return		La hauteur de la matrice.
	 */
	public int getHeight()
	{
		return this.height;
	}
}
