/**
 * 
 */
package appDemineur.model.test;

import junit.framework.Assert;

import org.junit.Test;

import appDemineur.model.BestTimes;
import appDemineur.model.Game;

/**
 * @author Christian
 *
 */
public class BestTimesTest
{

	private String[] players = new String[Game.LEVELS.length];
	private String[] times = new String[Game.LEVELS.length];
	
	// Mémorise le contenu du fichier des meilleurs temps pour pouvoir le restaurer après un test d'écriture
	private void backupFile()
	{
		BestTimes bt = new BestTimes();
		
		for (int i = 0; i < Game.LEVELS.length; i++)
		{
			players[i] = bt.getPlayer(i);
			times[i] = bt.getTime(i);
		}
	}
	
	// Restaure le contenu du fichier des meilleurs temps 
	private void restoreFile()
	{
		BestTimes bt = new BestTimes();
		
		for (int i = 0; i < Game.LEVELS.length; i++)
		{
			bt.setPlayer(i, players[i]);
			bt.setTime(i, times[i]);
		}
		bt.write();
	}
	
	/**
	 * Test method for {@link appDemineur.model.BestTimes#BestTimes()}.
	 */
	@Test
	public void testBestTimes()
	{
		BestTimes bt = new BestTimes();
		Assert.assertNotNull(bt);
	}

	/**
	 * Test method for {@link appDemineur.model.BestTimes#getTime(int)}.
	 */
	@Test
	public void testGetTime()
	{
		this.backupFile();
		
		// Cas 1 : vérifie que l'état initial correspond à celui sauvegardé 
		BestTimes bt = new BestTimes();
		bt.setTime(0, "10");
		bt.setTime(1, "20");
		bt.setTime(2, "30");
		bt.write();
		
		// En recréant l'objet, le fichier est lu
		bt = new BestTimes();
		Assert.assertEquals("10", bt.getTime(0));
		Assert.assertEquals("20", bt.getTime(1));
		Assert.assertEquals("30", bt.getTime(2));
		
		this.restoreFile();
		
		// Autres cas : on effectue les même tests que pour setTime.
		this.testSetTime();
	}

	/**
	 * Test method for {@link appDemineur.model.BestTimes#setTime(int, java.lang.String)}.
	 */
	@Test
	public void testSetTime()
	{
		BestTimes bt = new BestTimes();
		
		// Cas valide 1 : le niveau est > 0 et < 2
		bt.setTime(1, "10");
		Assert.assertEquals("10", bt.getTime(1));
		
		// Cas valide 2 : cas limite : le niveau est 0 
		bt.setTime(0, "10");
		Assert.assertEquals("10", bt.getTime(0));

		// Cas valide 2 : cas limite : le niveau est 2 
		bt.setTime(2, "10");
		Assert.assertEquals("10", bt.getTime(2));

		// Cas invalide 1 : le niveau est < 0 
		bt.setTime(-1, "10");
		Assert.assertNull(bt.getTime(-1));

		// Cas invalide 2 : le niveau est > 2 
		bt.setTime(3, "10");
		Assert.assertNull(bt.getTime(3));
	}

	/**
	 * Test method for {@link appDemineur.model.BestTimes#getPlayer(int)}.
	 */
	@Test
	public void testGetPlayer()
	{
		this.backupFile();
		
		// Cas 1 : vérifie que l'état initial correspond à celui sauvegardé 
		BestTimes bt = new BestTimes();
		bt.setPlayer(0, "Joe");
		bt.setPlayer(1, "Jack");
		bt.setPlayer(2, "Jenny");
		bt.write();
		
		// En recréant l'objet, le fichier est lu
		bt = new BestTimes();
		Assert.assertEquals("Joe", bt.getPlayer(0));
		Assert.assertEquals("Jack", bt.getPlayer(1));
		Assert.assertEquals("Jenny", bt.getPlayer(2));
		
		this.restoreFile();
		
		// Autres cas : on effectue les même tests que pour setPlayer.
		this.testSetPlayer();
	}

	/**
	 * Test method for {@link appDemineur.model.BestTimes#setPlayer(int, java.lang.String)}.
	 */
	@Test
	public void testSetPlayer()
	{
		BestTimes bt = new BestTimes();
		
		// Cas valide 1 : le niveau est > 0 et < 2
		bt.setPlayer(1, "10");
		Assert.assertEquals("10", bt.getPlayer(1));
		
		// Cas valide 2 : cas limite : le niveau est 0 
		bt.setPlayer(0, "10");
		Assert.assertEquals("10", bt.getPlayer(0));

		// Cas valide 2 : cas limite : le niveau est 2 
		bt.setPlayer(2, "10");
		Assert.assertEquals("10", bt.getPlayer(2));

		// Cas invalide 1 : le niveau est < 0 
		bt.setPlayer(-1, "10");
		Assert.assertNull(bt.getPlayer(-1));

		// Cas invalide 2 : le niveau est > 2 
		bt.setPlayer(3, "10");
		Assert.assertNull(bt.getPlayer(3));
	}

	/**
	 * Test method for {@link appDemineur.model.BestTimes#write()}.
	 */
	@Test
	public void testWrite()
	{
		this.backupFile();
		
		// Cas 1 : vérifie que l'état initial correspond à celui sauvegardé 
		BestTimes bt = new BestTimes();
		bt.setTime(0, "10");
		bt.setTime(1, "20");
		bt.setTime(2, "30");
		bt.setPlayer(0, "Joe");
		bt.setPlayer(1, "Jack");
		bt.setPlayer(2, "Jenny");
		bt.write();
		
		// En recréant l'objet, le fichier est lu
		bt = new BestTimes();
		Assert.assertEquals("10", bt.getTime(0));
		Assert.assertEquals("20", bt.getTime(1));
		Assert.assertEquals("30", bt.getTime(2));
		Assert.assertEquals("Joe", bt.getPlayer(0));
		Assert.assertEquals("Jack", bt.getPlayer(1));
		Assert.assertEquals("Jenny", bt.getPlayer(2));
		
		this.restoreFile();
	}

}
