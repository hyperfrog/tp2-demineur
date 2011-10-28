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
	 * 	Méthode de test pour {@link appDemineur.model.Game#Game(int)}
	 */
	@Test
	public void testGame()
	{
		// Cas valide 1 : Le niveau est situé entre 0 et 2.
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
		
		// Cas invalide 1 : Le niveau est inférieur à 0
		g = new Game(-1);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
		
		// Cas invalide 2 : Le niveau est supérieur à 2
		g = new Game(3);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
	}
	
	/**
	 *  Méthode de test pour {@link appDemineur.model.Game#changeCellState(int, int, boolean)}
	 */
	@Test
	public void testChangeCellState()
	{
		int nbFlagsBefore = 0;
		
		// Cas valide 1 : La position est valide, l'état de la cellule est HIDDEN et on veut la découvrir.
		Game g = new Game(0);
		Assert.assertTrue(g.changeCellState(3, 3, true));
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas valide 2 : La position est valide et l'état de la cellule est DUBIOUS et on veut la découvrir.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		g.changeCellState(3, 3, false);
		Assert.assertTrue(g.changeCellState(3, 3, true));
		
		// Cas valide 3 : La position est valide, l'état de la cellule est HIDDEN et on veut la changer pour FLAGGED.
		g = new Game(0);
		nbFlagsBefore = g.getNbFlags();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbFlags());
		// Pour vérifier qu'on est bien à l'état FLAGGED, on essaie de découvrir la case et on doit avoir Faux en retour.
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas valide 4 : La position est valide, l'état de la cellule est FLAGGED et on veut la changer pour DUBIOUS.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		nbFlagsBefore = g.getNbFlags();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore - 1, g.getNbFlags());
		// Pour vérifier qu'on est bien à l'état DUBIOUS, on essaie de découvrir la case et on doit avoir Vrai en retour.
		Assert.assertTrue(g.changeCellState(3, 3, true));
		
		// Cas valide 5 : La position est valide, l'état de la cellule est DUBIOUS et on veut la changer pour HIDDEN.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		g.changeCellState(3, 3, false);
		nbFlagsBefore = g.getNbFlags();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore, g.getNbFlags());
		// Pour vérifier qu'on est revenu à l'état HIDDEN, on essaie de découvrir la case et on doit avoir Vrai en retour.
		Assert.assertTrue(g.changeCellState(3, 3, true));
		
		// Cas limite 1 : La position est (0, 0) et la cellule n'est pas null donc on peut changer son état.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(0, 0, false));
		
		// Cas limite 2 : La position est (largeur - 1, hauteur - 1) et la cellule n'est pas null donc on peut changer son état.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(8, 8, false));
		
		// Cas limite 3 : La position est (largeur - 1, 0) et la cellule n'est pas null donc on peut changer son état.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(8, 0, false));
		
		// Cas limite 4 : La position est (0, hauteur - 1) et la cellule n'est pas null donc on peut changer son état.
		g = new Game(0);
		Assert.assertTrue(g.changeCellState(0, 8, false));
		
		// Cas invalide 1 : La position est valide et l'état de la cellule est FLAGGED et on veut la décourvrir.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas invalide 2 : La position est valide, l'état de la cellule est SHOWN et on veut la découvrir.
		g = new Game(0);
		g.changeCellState(3, 3, true);
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas invalide 3 : La position est inférieur à 0 en x et y.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(-1, -1, false));
		
		// Cas invalide 4 : La position en x est valide et inférieur à 0 en y.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(1, -1, false));
		
		// Cas invalide 5 : La position en y est valide et inférieur à 0 en x.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(-1, 1, false));
		
		// Cas invalide 6 : La position en x est supérieur à la largeur - 1 de la grille et la position en y est supérieur
		// 					à la hauteur - 1 de la grille.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(9, 9, false));

		// Cas invalide 7 : La position en x est supérieur à la largeur - 1 de la grille et la position en y est valide.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(9, 1, false));
		
		// Cas invalide 8 : La position en y est supérieur à la hauteur - 1 de la grille et la position en x est valide
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(1, 9, false));
	}
	
	/**
	 * 	Méthode de test pour {@link appDemineur.model.Game#isOver()}
	 */
	@Test
	public void testIsOver()
	{
		Game g = new Game(0);
		
		// Cas 1 : La partie vient de demarrer donc la partie n'est pas terminé.
		Assert.assertFalse(g.isOver());
		
		// Cas 2 : Le joueur à cliqué sur une mine alors la partie est terminé.
		// On parcours la matrice au complet
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < g.getHeight(); j++)
			{
				// On découvre toute les cellules pout dévoiler une mine et donc mettre fin à la partie.
				g.changeCellState(i, j, true);
			}
		}
		
		Assert.assertTrue(g.isOver());
	}
	
	/**
	 * 	Méthode de test pour {@link appDemineur.model.Game#getNbFlags()}
	 */
	@Test
	public void testGetNbFlags()
	{
		Game g = new Game(0);
		
		// Cas 1 : La partie vient d'être créé donc aucun drapeau n'a été utilisé.
		Assert.assertEquals(0, g.getNbFlags());
		
		// Cas 2 : La partie est en cours et le joueur n'a pas utilisé tous les drapeaux.
		g = new Game(0);
		g.changeCellState(0, 0, false);
		g.changeCellState(0, 1, false);
		g.changeCellState(1, 0, false);
		Assert.assertEquals(3, g.getNbFlags());
		
		// Cas 3 : La partie est en cours et le joueur a utilisé un nombre de drapeau supérieur au nombre
		// 		   de mines cachées sur la grille.
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
	 * 	Méthode de test pour {@link appDemineur.model.Game#getWidth()}
	 */
	@Test
	public void testGetWidth()
	{
		// Cas 1 : On créer une partie en utilisant le niveau débutant donc la largeur devrait être de 9.
		Game g = new Game(0);
		Assert.assertEquals(9, g.getWidth());

		// Cas 2 : On créer une partie en utilisant le niveau intermédiaire donc la largeur devrait être de 16.		
		g = new Game(1);
		Assert.assertEquals(16, g.getWidth());
		
		// Cas 3 : On créer une partie en utilisant le niveau expert donc la largeur devrait être de 30.
		g = new Game(2);
		Assert.assertEquals(30, g.getWidth());
	}
	
	/**
	 * 	Méthode de test pour {@link appDemineur.model.Game#getHeight()}
	 */
	@Test
	public void testGetHeight()
	{
		// Cas 1 : On créer une partie en utilisant le niveau débutant donc la hauteur devrait être de 9.
		Game g = new Game(0);
		Assert.assertEquals(9, g.getHeight());

		// Cas 2 : On créer une partie en utilisant le niveau intermédiaire donc la hauteur devrait être de 16.		
		g = new Game(1);
		Assert.assertEquals(16, g.getHeight());
		
		// Cas 3 : On créer une partie en utilisant le niveau expert donc la hauteur devrait être de 16.
		g = new Game(2);
		Assert.assertEquals(16, g.getHeight());
	}
	
	/**
	 * 	Méthode de test pour {@link appDemineur.model.Game#getMineAmount()}
	 */
	@Test
	public void testGetMineAmount()
	{
		// Cas 1 : On créer une partie en utilisant le niveau débutant donc le nombre de mine devrait être de 10.
		Game g = new Game(0);
		Assert.assertEquals(10, g.getMineAmount());

		// Cas 2 : On créer une partie en utilisant le niveau intermédiaire donc le nombre de mine devrait être de 40.		
		g = new Game(1);
		Assert.assertEquals(40, g.getMineAmount());
		
		// Cas 3 : On créer une partie en utilisant le niveau expert donc le nombre de mine devrait être de 99.
		g = new Game(2);
		Assert.assertEquals(99, g.getMineAmount());
	}
}
