package util.test;

import org.junit.Assert;
import org.junit.Test;

import util.BaseMatrix;

/**
 * La classe de test JUnit 4 pour la classe BaseMatrix.java
 * 
 * @author Alexandre Tremblay
 * @author Christian Lesage
 *
 */

public class BaseMatrixTest
{
	/**
	 * 	Méthode de test pour {@link util.BaseMatrix#BaseMatrix()}
	 */
	@Test
	public void testBaseMatrix()
	{
		BaseMatrix bm = new BaseMatrix(3, 4);
		Assert.assertNotNull(bm);
		Assert.assertEquals(3, bm.getWidth());
		Assert.assertEquals(4, bm.getHeight());
	}
	
	/**
	 * Méthode de test pour {@link util.BaseMatrix#getElement(int, int)}.
	 */
	@Test
	public void testGetElement()
	{
		BaseMatrix bm = new BaseMatrix(3, 4);
		Object obj1 = new Object();
		Object obj2 = new Object();
		Object obj3 = new Object();
		Object obj4 = new Object();
		Object obj5 = new Object();
		
		bm.setElement(0, 0, obj1);
		bm.setElement(0, 3, obj2);
		bm.setElement(2, 0, obj3);
		bm.setElement(2, 3, obj4);
		bm.setElement(1, 1, obj5);
		
		// Cas valide 1 : La position demandée en x se situe entre 0 et la largeur et en y se situe entre 0 et la hauteur 
		Assert.assertEquals(obj5, bm.getElement(1, 1));

		// Cas limite 1 : La position demandée en x et y est 0, 0
		Assert.assertEquals(obj1, bm.getElement(0, 0));
		
		// Cas limite 2 : La position demandée est 0 en x et la hauteur - 1 en y
		Assert.assertEquals(obj2, bm.getElement(0, 3));
		
		// Cas limite 3 : La position demandée est 0 en y et la largeur - 1 en x
		Assert.assertEquals(obj3, bm.getElement(2, 0));
		
		// Cas limite 4 : La position demandée est la largeur - 1 en x et la hauteur - 1 en y
		Assert.assertEquals(obj4, bm.getElement(2, 3));
		
		// Cas invalide 1 : La position demandée est négative en x et y
		Assert.assertNull(bm.getElement(-1, -1));
		
		// Cas invalide 2 : La position demandée est négative en x, mais positive en y
		Assert.assertNull(bm.getElement(-1, 1));
		
		// Cas invalide 3 : La position demandée est négative en y, mais positive en x
		Assert.assertNull(bm.getElement(1, -1));
		
		// Cas invalide 4 : La position demandée en x et y est supérieur à la largeur et la hauteur respectivement
		Assert.assertNull(bm.getElement(4, 4));
		
		// Cas invalide 5 : La position demandée en x est supérieur à la largeur, mais inférieur à la hauteur et supérieur à 0 en y.
		Assert.assertNull(bm.getElement(4, 2));
		
		// Cas invalide 6 : La position demandée en y est supérieur à la hauteur, mais inférieur à la largeur et supérieur à 0 en x.
		Assert.assertNull(bm.getElement(2, 4));
	}
	
	/**
	 * Méthode de test pour {@link util.BaseMatrix#setElement(int, int, java.lang.Object)}.
	 */
	@Test
	public void testSetElement()
	{
		BaseMatrix bm = new BaseMatrix(3, 4);

		// Cas valide 1 : La position demandée en x se situe entre 0 et la largeur et en y se situe entre 0 et la hauteur
		//				  et l'objet passé n'est pas null.
		Assert.assertTrue(bm.setElement(1, 1, 1));
		Assert.assertEquals(1, bm.getElement(1, 1));
		
		// Cas limite 1 : La position demandée en x et y est 0, 0 et l'objet passé n'est pas null.
		Assert.assertTrue(bm.setElement(0, 0, 2));
		Assert.assertEquals(2, bm.getElement(0, 0));
		
		// Cas limite 2 : La position demandée est 0 en x et la hauteur - 1 en y et l'objet passé n'est pas null.
		Assert.assertTrue(bm.setElement(0, 3, 3));
		Assert.assertEquals(3, bm.getElement(0, 3));
		
		// Cas limite 3 : La position demandée est 0 en y et la largeur - 1 en x et l'objet passé n'est pas null.
		Assert.assertTrue(bm.setElement(2, 0, 4));
		Assert.assertEquals(4, bm.getElement(2, 0));
		
		// Cas limite 4 : La position demandée est la largeur - 1 en x et la hauteur - 1 en y et l'objet passé n'est pas null.
		Assert.assertTrue(bm.setElement(2, 3, 5));
		Assert.assertEquals(5, bm.getElement(2, 3));
		
		// Cas invalide 1 : La position demandée est négative en x et y et l'objet passé n'est pas null.
		Assert.assertFalse(bm.setElement(-1, -1, 6));
		
		// Cas invalide 2 : La position demandée est négative en x, mais positive en y et l'objet passé n'est pas null.
		Assert.assertFalse(bm.setElement(-1, 1, 7));
		
		// Cas invalide 3 : La position demandée est négative en y, mais positive en x et l'objet passé n'est pas null.
		Assert.assertFalse(bm.setElement(1, -1, 8));
		
		// Cas invalide 4 : La position demandée en x et y est supérieur à la largeur et la hauteur respectivement
		// 					et l'objet passé n'est pas null.
		Assert.assertFalse(bm.setElement(4, 4, 9));
		
		// Cas invalide 5 : La position demandée en x est supérieur à la largeur, mais inférieur à la hauteur et supérieur à 0 en y
		//			et l'objet passé n'est pas null.
		Assert.assertFalse(bm.setElement(4, 2, 9));
		
		// Cas invalide 6 : La position demandée en y est supérieur à la hauteur, mais inférieur à la largeur et supérieur à 0 en x
		//			et l'objet passé n'est pas null.
		Assert.assertFalse(bm.setElement(2, 4, 10));
		
		// Cas invalide 7 : La position demandée en x se situe entre 0 et la largeur et en y se situe entre 0 et la hauteur
		//				  	et l'objet passé est null.
		Assert.assertFalse(bm.setElement(1, 1, null));
	}

	/**
	 * Méthode de test pour {@link util.BaseMatrix#toString()}.
	 */
	@Test
	public void testToString()
	{
		BaseMatrix bm = new BaseMatrix(3, 2);
		
		bm.setElement(0, 0, 1);
		bm.setElement(1, 0, 2);
		bm.setElement(2, 0, 3);
		
		bm.setElement(0, 1, 4);
		bm.setElement(1, 1, 5);
		bm.setElement(2, 1, 6);
		
		Assert.assertEquals("[ 1, 2, 3 ]\n[ 4, 5, 6 ]\n", bm.toString());
	}

	/**
	 * Méthode de test pour {@link util.BaseMatrix#getWidth()}.
	 */
	@Test
	public void testGetWidth()
	{
		// On créer une matrice avec une hauteur de 3
		BaseMatrix bm = new BaseMatrix(3, 4);
		Assert.assertEquals(3, bm.getWidth());
		
		// On créer une matrice avec une hauteur de 12
		bm = new BaseMatrix(12, 1);
		Assert.assertEquals(12, bm.getWidth());
	}

	/**
	 * Méthode de test pour {@link util.BaseMatrix#getHeight()}.
	 */
	@Test
	public void testGetHeight()
	{
		// On créer une matrice avec une largeur de 4
		BaseMatrix bm = new BaseMatrix(3, 4);
		Assert.assertEquals(4, bm.getHeight());
		
		// On créer une matrice avec une largeur de 1
		bm = new BaseMatrix(12, 1);
		Assert.assertEquals(1, bm.getHeight());
	}
}
