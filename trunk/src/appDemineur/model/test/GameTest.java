package appDemineur.model.test;

import org.junit.Assert;
import org.junit.Test;

import appDemineur.model.Game;

/**
 * La classe de test JUnit 4 pour la classe Game.java
 * 
 * @author Alexandre Tremblay
 * @author Christian Lesage
 *
 */

public class GameTest
{
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#Game(int)}
	 */
	@Test
	public void testGame()
	{
		// Cas valide 1 : Le niveau est situ� entre 0 et 2.
		Game g = new Game(1);
		Assert.assertNotNull(g);
		Assert.assertEquals(16, g.getWidth());
		Assert.assertEquals(16, g.getHeight());
		Assert.assertEquals(40, g.getMineAmount());
		Assert.assertEquals(0, g.getNbFlags());
		Assert.assertFalse(g.isOver());
		
		// Cas limite 1 : Le niveau est 0
		g = new Game(0);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
		Assert.assertEquals(0, g.getNbFlags());
		Assert.assertFalse(g.isOver());
		
		// Cas limite 2 : Le niveau est 2
		g = new Game(2);
		Assert.assertNotNull(g);
		Assert.assertEquals(30, g.getWidth());
		Assert.assertEquals(16, g.getHeight());
		Assert.assertEquals(99, g.getMineAmount());
		Assert.assertEquals(0, g.getNbFlags());
		Assert.assertFalse(g.isOver());
		
		// Cas invalide 1 : Le niveau est inf�rieur � 0
		g = new Game(-1);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
		
		// Cas invalide 2 : Le niveau est sup�rieur � 2
		g = new Game(3);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
	}
	
	/**
	 *  M�thode de test pour {@link appDemineur.model.Game#changeCellState(int, int, boolean)}
	 */
	@Test
	public void testChangeCellState()
	{
		int nbFlagsBefore = 0;
		
		// Cas valide 1 : La position est valide, l'�tat de la cellule est HIDDEN et on veut la d�couvrir.
		Game g = new Game(0);
		Assert.assertTrue(g.changeCellState(3, 3, true));
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas valide 2 : La position est valide et l'�tat de la cellule est DUBIOUS et on veut la d�couvrir.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		g.changeCellState(3, 3, false);
		Assert.assertTrue(g.changeCellState(3, 3, true));
		
		// Cas valide 3 : La position est valide, l'�tat de la cellule est HIDDEN et on veut la changer pour FLAGGED.
		g = new Game(0);
		nbFlagsBefore = g.getNbFlags();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbFlags());
		// Pour v�rifier qu'on est bien � l'�tat FLAGGED, on essaie de d�couvrir la case et on doit avoir Faux en retour.
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas valide 4 : La position est valide, l'�tat de la cellule est FLAGGED et on veut la changer pour DUBIOUS.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		nbFlagsBefore = g.getNbFlags();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore - 1, g.getNbFlags());
		// Pour v�rifier qu'on est bien � l'�tat DUBIOUS, on essaie de d�couvrir la case et on doit avoir Vrai en retour.
		Assert.assertTrue(g.changeCellState(3, 3, true));
		
		// Cas valide 5 : La position est valide, l'�tat de la cellule est DUBIOUS et on veut la changer pour HIDDEN.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		g.changeCellState(3, 3, false);
		nbFlagsBefore = g.getNbFlags();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore, g.getNbFlags());
		// Pour v�rifier qu'on est revenu � l'�tat HIDDEN, on essaie de d�couvrir la case et on doit avoir Vrai en retour.
		Assert.assertTrue(g.changeCellState(3, 3, true));
		
		// Cas limite 1 : La position est (0, 0) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(0, 0, false));
		
		// Cas limite 2 : La position est (largeur - 1, hauteur - 1) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(8, 8, false));
		
		// Cas limite 3 : La position est (largeur - 1, 0) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(8, 0, false));
		
		// Cas limite 4 : La position est (0, hauteur - 1) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(0, 8, false));
		
		// Cas invalide 1 : La position est valide et l'�tat de la cellule est FLAGGED et on veut la d�courvrir.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas invalide 2 : La position est valide, l'�tat de la cellule est SHOWN et on veut la d�couvrir.
		g = new Game(0);
		g.changeCellState(3, 3, true);
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas invalide 3 : La position est inf�rieur � 0 en x et y.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(-1, -1, false));
		
		// Cas invalide 4 : La position en x est valide et inf�rieur � 0 en y.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(1, -1, false));
		
		// Cas invalide 5 : La position en y est valide et inf�rieur � 0 en x.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(-1, 1, false));
		
		// Cas invalide 6 : La position en x est sup�rieur � la largeur - 1 de la grille et la position en y est sup�rieur
		// 					� la hauteur - 1 de la grille.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(9, 9, false));

		// Cas invalide 7 : La position en x est sup�rieur � la largeur - 1 de la grille et la position en y est valide.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(9, 1, false));
		
		// Cas invalide 8 : La position en y est sup�rieur � la hauteur - 1 de la grille et la position en x est valide
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(1, 9, false));
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#isOver()}
	 */
	@Test
	public void testIsOver()
	{
		Game g = new Game(0);
		
		// Cas 1 : La partie vient de demarrer donc la partie n'est pas termin�.
		Assert.assertFalse(g.isOver());
		
		// Cas 2 : Le joueur � cliqu� sur une mine alors la partie est termin�.
		// On parcours la matrice au complet
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < g.getHeight(); j++)
			{
				// On d�couvre toute les cellules pout d�voiler une mine et donc mettre fin � la partie.
				g.changeCellState(i, j, true);
			}
		}
		
		Assert.assertTrue(g.isOver());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getNbFlags()}
	 */
	@Test
	public void testGetNbFlags()
	{
		Game g = new Game(0);
		
		// Cas 1 : La partie vient d'�tre cr�� donc aucun drapeau n'a �t� utilis�.
		Assert.assertEquals(0, g.getNbFlags());
		
		// Cas 2 : La partie est en cours et le joueur n'a pas utilis� tous les drapeaux.
		g = new Game(0);
		g.changeCellState(0, 0, false);
		g.changeCellState(0, 1, false);
		g.changeCellState(1, 0, false);
		Assert.assertEquals(3, g.getNbFlags());
		
		// Cas 3 : La partie est en cours et le joueur a utilis� un nombre de drapeau sup�rieur au nombre
		// 		   de mines cach�es sur la grille.
		g = new Game(0);
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < 2; j++)
			{
				// On pose un drapeau sur 18 cases.
				g.changeCellState(i, j, false);
			}
		}
		Assert.assertEquals(18, g.getNbFlags());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getWidth()}
	 */
	@Test
	public void testGetWidth()
	{
		// Cas 1 : On cr�er une partie en utilisant le niveau d�butant donc la largeur devrait �tre de 9.
		Game g = new Game(0);
		Assert.assertEquals(9, g.getWidth());

		// Cas 2 : On cr�er une partie en utilisant le niveau interm�diaire donc la largeur devrait �tre de 16.		
		g = new Game(1);
		Assert.assertEquals(16, g.getWidth());
		
		// Cas 3 : On cr�er une partie en utilisant le niveau expert donc la largeur devrait �tre de 30.
		g = new Game(2);
		Assert.assertEquals(30, g.getWidth());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getHeight()}
	 */
	@Test
	public void testGetHeight()
	{
		// Cas 1 : On cr�er une partie en utilisant le niveau d�butant donc la hauteur devrait �tre de 9.
		Game g = new Game(0);
		Assert.assertEquals(9, g.getHeight());

		// Cas 2 : On cr�er une partie en utilisant le niveau interm�diaire donc la hauteur devrait �tre de 16.		
		g = new Game(1);
		Assert.assertEquals(16, g.getHeight());
		
		// Cas 3 : On cr�er une partie en utilisant le niveau expert donc la hauteur devrait �tre de 16.
		g = new Game(2);
		Assert.assertEquals(16, g.getHeight());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getMineAmount()}
	 */
	@Test
	public void testGetMineAmount()
	{
		// Cas 1 : On cr�er une partie en utilisant le niveau d�butant donc le nombre de mine devrait �tre de 10.
		Game g = new Game(0);
		Assert.assertEquals(10, g.getMineAmount());

		// Cas 2 : On cr�er une partie en utilisant le niveau interm�diaire donc le nombre de mine devrait �tre de 40.		
		g = new Game(1);
		Assert.assertEquals(40, g.getMineAmount());
		
		// Cas 3 : On cr�er une partie en utilisant le niveau expert donc le nombre de mine devrait �tre de 99.
		g = new Game(2);
		Assert.assertEquals(99, g.getMineAmount());
	}
}
