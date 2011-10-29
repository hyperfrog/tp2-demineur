package appDemineur.model.test;

import org.junit.Assert;
import org.junit.Test;

import appDemineur.model.Cell;
import appDemineur.model.Matrix;

/**
 * Classe de test JUnit 4 pour la classe Matrix.java
 * 
 * @author Alexandre Tremblay
 * @author Christian Lesage
 *
 */

public class MatrixTest
{
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Matrix#Matrix(int, int, int)}
	 */
	@Test
	public void testMatrix()
	{
		Matrix m = new Matrix(10, 10, 30);
		Assert.assertNotNull(m);
		
		int mineCount = 0;
		
		// On parcours la matrice au complet
		for (int i = 0; i < m.getWidth(); i++)
		{
			for (int j = 0; j < m.getHeight(); j++)
			{
				Object o = m.getElement(i, j);
				
				// On v�rifie que l'objet est bien une cellule et non null.
				Assert.assertNotNull(o);
				Assert.assertTrue(o instanceof Cell);
				
				// Si c'est une mine, on incr�mente le compteur de mines
				if (o != null && o instanceof Cell && ((Cell) o).isMine())
				{
					mineCount++;
				}
			}
		}
		
		// On v�rifie que le nombre de mines ins�r�es correspond avec celui pass� en param�tre au constructeur.
		Assert.assertEquals(30, mineCount);
	}

	/**
	 * M�thode de test pour {@link appDemineur.model.Matrix#getElement(int, int)}.
	 * 
	 * @see {@link util.BaseMatrix#getElement(int, int)}
	 */
	@Test
	public void testGetElementIntInt() 
	{
		Matrix m = new Matrix(3, 2, 2);
		
		//Cas valide 1 : La position demand�e en (x, y) est valide et l'objet ins�r� � cette position n'est pas null 
		// 				 et est une instance de Cell.
		Assert.assertNotNull(m.getElement(0, 0));
		Assert.assertTrue(m.getElement(0, 0) instanceof Cell);
		
		//Cas valide 2 : La position demand�e en (x, y) est valide et l'objet ins�r� � cette position n'est pas null 
		//				 mais n'est pas une instance de Cell.
		m.setElement(1, 1, "PasUneCellule");
		Assert.assertNull(m.getElement(1, 1));
		Assert.assertFalse(m.getElement(1, 1) instanceof Cell);
		
		//Cas valide 3 : La position demand�e en (x, y) est valide et l'objet ins�r� � cette position est null 
		//				 et n'est pas une instance de Cell.
		m.setElement(1, 2, null);
		Assert.assertNull(m.getElement(1, 2));
		Assert.assertFalse(m.getElement(1, 2) instanceof Cell);
	}
	
	
}