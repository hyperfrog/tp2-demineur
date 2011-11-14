/**
 * 
 */
package appDemineur.model.test;

import static org.junit.Assert.*;

import java.awt.Dimension;

import junit.framework.Assert;

import org.junit.Test;

import appDemineur.model.Level;

/**
 * @author Christian
 *
 */
public class LevelTest
{

	/**
	 * Test method for {@link appDemineur.model.Level#Level(java.awt.Dimension, int, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testLevel()
	{
		Level l;
		
		// Cas valide 1 : les dimensions, le nombre de mines et les noms sont corrects
		l = new Level(new Dimension(8, 9), 1, "Beginner", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas valide 2 : cas limite : la largeur est 1
		l = new Level(new Dimension(1, 9), 1, "Beginner", "Débutant");
		Assert.assertEquals(1, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas valide 3 : cas limite : la hauteur est 1
		l = new Level(new Dimension(8, 1), 1, "Beginner", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(1, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas valide 4 : cas limite : le nombre de mines est 0
		l = new Level(new Dimension(8, 9), 0, "Beginner", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(0, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas valide 5 : cas limite : le nombre de mines est largeur * hauteur - 1
		l = new Level(new Dimension(8, 9), 8 * 9 - 1, "Beginner", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(8 * 9 - 1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas invalide 1 : la largeur est 0
		l = new Level(new Dimension(-1, 9), 1, "Beginner", "Débutant");
		Assert.assertEquals(1, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas invalide 2 : la hauteur est 0
		l = new Level(new Dimension(8, 0), 1, "Beginner", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(1, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas invalide 3 : le nombre de mines est -1
		l = new Level(new Dimension(8, 9), -1, "Beginner", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(0, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas invalide 4 : le nombre de mines est largeur * hauteur
		l = new Level(new Dimension(8, 9), 8 * 9, "Beginner", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(8 * 9 - 1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Débutant", l.displayName);

		// Cas invalide 5 : name est null
		l = new Level(new Dimension(8, 9), 1, null, "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("unnamed_level", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas invalide 6 : name est vide
		l = new Level(new Dimension(8, 9), 1, "", "Débutant");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("unnamed_level", l.name);
		Assert.assertEquals("Débutant", l.displayName);
		
		// Cas invalide 7 : displayName est null
		l = new Level(new Dimension(8, 9), 1, "Beginner", null);
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Niveau ?", l.displayName);
		
		// Cas invalide 8 : displayName est vide
		l = new Level(new Dimension(8, 9), 1, "Beginner", "");
		Assert.assertEquals(8, l.dim.width);
		Assert.assertEquals(9, l.dim.height);
		Assert.assertEquals(1, l.mineAmount);
		Assert.assertEquals("Beginner", l.name);
		Assert.assertEquals("Niveau ?", l.displayName);
	}

}
