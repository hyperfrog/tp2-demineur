package appDemineur.model.test;

import org.junit.Assert;
import org.junit.Test;

import appDemineur.model.Cell;
import appDemineur.model.Cell.CellState;

/**
 * La classe de test JUnit 4 pour la classe Cell.java
 * 
 * @author Alexandre Tremblay
 * @author Christian Lesage
 *
 */
public class CellTest
{

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#Cell()}.
	 */
	@Test
	public void testCell()
	{
		// Cas valide
		Cell c = new Cell();
		Assert.assertNotNull(c);
		Assert.assertFalse(c.isMine());
		Assert.assertEquals(0, c.getAdjacentMines());
		Assert.assertEquals(CellState.HIDDEN, c.getState());
	}

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#setAdjacentMines(int)}.
	 */
	@Test
	public void testSetAdjacentMines()
	{
		Cell c = new Cell();
		
		// Cas valide 1 : le nombre de mines est > 0 et < 8
		c.setAdjacentMines(3);
		Assert.assertEquals(3, c.getAdjacentMines());

		// Cas valide 2 : Cas limite : le nombre de mines est 0
		c.setAdjacentMines(0);
		Assert.assertEquals(0, c.getAdjacentMines());

		// Cas valide 3 : Cas limite : le nombre de mines est 8
		c.setAdjacentMines(8);
		Assert.assertEquals(8, c.getAdjacentMines());

		// Cas invalide 1 : le nombre de mines est > 8
		c.setAdjacentMines(3);
		c.setAdjacentMines(9);
		Assert.assertEquals(3, c.getAdjacentMines());

		// Cas invalide 2 : le nombre de mines est < 0
		c.setAdjacentMines(3);
		c.setAdjacentMines(-1);
		Assert.assertEquals(3, c.getAdjacentMines());
	}

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#getAdjacentMines()}.
	 */
	@Test
	public void testGetAdjacentMines()
	{
		// Cas 1 : La cellule vient d'être créée
		Cell c = new Cell();
		Assert.assertEquals(0, c.getAdjacentMines());
		
		// Autres cas : on teste les même cas que pour setAdjacentMines()
		this.testSetAdjacentMines();
	}

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#setState(appDemineur.model.Cell.CellState)}.
	 */
	@Test
	public void testSetState()
	{
		Cell c = new Cell();
		
		// Cas valide 1 : CellState.HIDDEN
		Assert.assertTrue(c.setState(CellState.HIDDEN));
		Assert.assertEquals(CellState.HIDDEN, c.getState());
		
		// Cas valide 2 : CellState.DUBIOUS
		Assert.assertTrue(c.setState(CellState.DUBIOUS));
		Assert.assertEquals(CellState.DUBIOUS, c.getState());
		
		// Cas valide 3 : CellState.FLAGGED
		Assert.assertTrue(c.setState(CellState.FLAGGED));
		Assert.assertEquals(CellState.FLAGGED, c.getState());

		// Cas valide 4 : CellState.SHOWN
		Assert.assertTrue(c.setState(CellState.SHOWN));
		Assert.assertEquals(CellState.SHOWN, c.getState());

		// Cas invalide : null
		c.setState(CellState.SHOWN);
		Assert.assertFalse(c.setState(null));
		Assert.assertEquals(CellState.SHOWN, c.getState());
	}

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#getState()}.
	 */
	@Test
	public void testGetState()
	{
		// Cas 1 : La cellule vient d'être créée
		Cell c = new Cell();
		Assert.assertEquals(CellState.HIDDEN, c.getState());
		
		// Autres cas : on teste les même cas que pour setState()
		this.testSetState();
	}

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#setAsMine(boolean)}.
	 */
	@Test
	public void testSetAsMine()
	{
		Cell c = new Cell();
		
		// Cas valide 1 : true
		c.setAsMine(true);
		Assert.assertTrue(c.isMine());
		
		// Cas valide 2 : false
		c.setAsMine(false);
		Assert.assertFalse(c.isMine());
	}

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#isMine()}.
	 */
	@Test
	public void testIsMine()
	{
		// Cas 1 : La cellule vient d'être créée
		Cell c = new Cell();
		Assert.assertFalse(c.isMine());
		
		// Autres cas : on teste les même cas que pour setState()
		this.testSetAsMine();
	}

	/**
	 * Méthode de test pour {@link appDemineur.model.Cell#toString()}.
	 */
	@Test
	public void testToString()
	{
		Cell c = new Cell();
		
		// Cas 1 : la cellule n'est pas une mine
		c.setAsMine(false);
		c.setAdjacentMines(3);
		Assert.assertEquals("3", c.toString());
		
		// Cas 1 : la cellule est une mine
		c.setAsMine(true);
		c.setAdjacentMines(3);
		Assert.assertEquals("*", c.toString());
	}

}
