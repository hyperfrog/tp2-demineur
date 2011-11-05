package appDemineur.model.test;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

import appDemineur.model.*;

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
	public void testGameInt()
	{
		// Cas valide 1 : Le niveau est situ� entre 0 et 2.
		Game g = new Game(1);
		Assert.assertNotNull(g);
		Assert.assertEquals(16, g.getWidth());
		Assert.assertEquals(16, g.getHeight());
		Assert.assertEquals(40, g.getMineAmount());
		Assert.assertEquals(0, this.mineOnGrid(g));
		Assert.assertEquals(0, g.getNbCellsFlagged());
		Assert.assertEquals(0, g.getNbCellsShown());
		Assert.assertFalse(g.isOver());
		Assert.assertFalse(g.isWon());
		Assert.assertFalse(g.isLost());
		
		// Cas limite 1 : Le niveau est 0
		g = new Game(0);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
		Assert.assertEquals(0, this.mineOnGrid(g));
		Assert.assertEquals(0, g.getNbCellsFlagged());
		Assert.assertEquals(0, g.getNbCellsShown());
		Assert.assertFalse(g.isOver());
		Assert.assertFalse(g.isWon());
		Assert.assertFalse(g.isLost());
		
		// Cas limite 2 : Le niveau est 2
		g = new Game(2);
		Assert.assertNotNull(g);
		Assert.assertEquals(30, g.getWidth());
		Assert.assertEquals(16, g.getHeight());
		Assert.assertEquals(99, g.getMineAmount());
		Assert.assertEquals(0, this.mineOnGrid(g));
		Assert.assertEquals(0, g.getNbCellsFlagged());
		Assert.assertEquals(0, g.getNbCellsShown());
		Assert.assertFalse(g.isOver());
		Assert.assertFalse(g.isWon());
		Assert.assertFalse(g.isLost());
		
		// Cas invalide 1 : Le niveau est inf�rieur � 0
		g = new Game(-1);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
		Assert.assertEquals(0, this.mineOnGrid(g));
		Assert.assertEquals(0, g.getNbCellsFlagged());
		Assert.assertEquals(0, g.getNbCellsShown());
		Assert.assertFalse(g.isOver());
		Assert.assertFalse(g.isWon());
		Assert.assertFalse(g.isLost());
		
		// Cas invalide 2 : Le niveau est sup�rieur � 2
		g = new Game(3);
		Assert.assertNotNull(g);
		Assert.assertEquals(9, g.getWidth());
		Assert.assertEquals(9, g.getHeight());
		Assert.assertEquals(10, g.getMineAmount());
		Assert.assertEquals(0, this.mineOnGrid(g));
		Assert.assertEquals(0, g.getNbCellsFlagged());
		Assert.assertEquals(0, g.getNbCellsShown());
		Assert.assertFalse(g.isOver());
		Assert.assertFalse(g.isWon());
		Assert.assertFalse(g.isLost());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#start(Point)}
	 */
	@Test
	public void testStartPoint()
	{
		// Cas valide 1 : Le niveau est situ� entre 0 et 2.
		Game g = new Game(1);
		Assert.assertNotNull(g);
		g.start(new Point(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(40, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas limite 1 : Le niveau est 0
		g = new Game(0);
		Assert.assertNotNull(g);
		g.start(new Point(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(10, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas limite 2 : Le niveau est 2
		g = new Game(2);
		Assert.assertNotNull(g);
		g.start(new Point(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(99, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas invalide 1 : Le niveau est inf�rieur � 0
		g = new Game(-1);
		Assert.assertNotNull(g);
		g.start(new Point(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(10, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas invalide 2 : Le niveau est sup�rieur � 2
		g = new Game(3);
		Assert.assertNotNull(g);
		g.start(new Point(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(10, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
	}
	
	// V�rifie la validit� du nombre de mines adjacentes de chaque cellule
	private boolean checkAdjacentMines(Game g)
	{
		boolean countIsOk = true;
		
		for (int x = 0; x < g.getWidth() && countIsOk; x++)
		{
			for (int y = 0; y < g.getHeight() && countIsOk; y++)
			{
				int nbAdjacentMines = 0;
				
				// Parcourt les 9 cellules incluant la cellule courante et les cellules adjacentes
				for (int c = x - 1 ; c <= x + 1; c++)
				{
					for (int r = y - 1 ; r <= y + 1; r++)
					{
						Cell adjCell = g.getElement(c, r);
						
						if (!(c == x && r == y) && adjCell != null && adjCell.isMine())
						{
							nbAdjacentMines++;
						}
					}
				}
				
				countIsOk = nbAdjacentMines == g.getElement(x, y).getAdjacentMines();
				
				if (!countIsOk)
				{
					System.out.println(String.format("Nombre de mines adjacentes erron� en (%d, %d).", x, y));
				}
			}
		}
		
		return countIsOk;
	}
	
	// Retourne le nombre de mines pr�sentes sur la grille
	private int mineOnGrid(Game g)
	{
		int mineCount = 0;
		
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < g.getHeight(); j++)
			{
				// Si c'est une mine, on incr�mente le compteur de mines
				if (g.getElement(i, j).isMine())
				{
					mineCount++;
				}
			}
		}
		
		return mineCount;
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
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbCellsFlagged());
		// Pour v�rifier qu'on est bien � l'�tat FLAGGED, on essaie de d�couvrir la case et on doit avoir Faux en retour.
		Assert.assertFalse(g.changeCellState(3, 3, true));
		
		// Cas valide 4 : La position est valide, l'�tat de la cellule est FLAGGED et on veut la changer pour DUBIOUS.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore - 1, g.getNbCellsFlagged());
		// Pour v�rifier qu'on est bien � l'�tat DUBIOUS, on essaie de d�couvrir la case et on doit avoir Vrai en retour.
		Assert.assertTrue(g.changeCellState(3, 3, true));
		
		// Cas valide 5 : La position est valide, l'�tat de la cellule est DUBIOUS et on veut la changer pour HIDDEN.
		g = new Game(0);
		g.changeCellState(3, 3, false);
		g.changeCellState(3, 3, false);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(3, 3, false));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
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
		
		// Cas invalide 3 : La position est inf�rieure � 0 en x et en y.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(-1, -1, false));
		
		// Cas invalide 4 : La position en x est valide et inf�rieure � 0 en y.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(1, -1, false));
		
		// Cas invalide 5 : La position en y est valide et inf�rieure � 0 en x.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(-1, 1, false));
		
		// Cas invalide 6 : La position en x est sup�rieure ou �gale � la largeur de la grille et  
		// 					la position en y est sup�rieure ou �gale � la hauteur de la grille.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(9, 9, false));

		// Cas invalide 7 : La position en x est sup�rieure ou �gale � la largeur de la grille et la position en y est valide.
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(9, 1, false));
		
		// Cas invalide 8 : La position en y est sup�rieure ou �gale � la hauteur de la grille et la position en x est valide
		g = new Game(0);
		Assert.assertFalse(g.changeCellState(1, 9, false));
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#isLost()}
	 */
	@Test
	public void testIsLost()
	{
		Game g = new Game(0);
		g.start(new Point(0, 0));
		
		// Cas 1 : La partie vient de demarrer et n'est donc pas perdue.
		Assert.assertFalse(g.isOver());
		
		// Cas 2 : Le joueur a perdu la partie.
		this.loseGame(g);
		Assert.assertTrue(g.isLost());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#isWon()}
	 */
	@Test
	public void testIsWon()
	{
		Game g = new Game(0);
		g.start(new Point(0, 0));

		// Cas 1 : La partie vient de demarrer et n'est donc pas gagn�e.
		Assert.assertFalse(g.isOver());
		
		// Cas 2 : Le joueur a gagn� la partie.
		this.winGame(g);
		Assert.assertTrue(g.isWon());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#isOver()}
	 */
	@Test
	public void testIsOver()
	{
		Game g = new Game(0);
		g.start(new Point(0, 0));
		
		// Cas 1 : La partie vient de demarrer et n'est donc pas termin�e.
		Assert.assertFalse(g.isOver());
		
		// Cas 2 : Le joueur a perdu la partie.
		this.loseGame(g);
		Assert.assertTrue(g.isOver());

		// Cas 3 : Le joueur a gagn� la partie.
		this.winGame(g);
		Assert.assertTrue(g.isOver());
	}
	
	// Gagne une partie
	private void winGame(Game g)
	{
		// On parcours la matrice au complet
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < g.getHeight(); j++)
			{
				// On d�couvre toutes les cellules qui ne sont pas des mines
				if (!g.getElement(i, j).isMine())
				{
					g.changeCellState(i, j, true);
				}
			}
		}
	}
	
	// Perd une partie
	private void loseGame(Game g)
	{
		boolean lost = false;

		// On parcours la matrice au complet
		for (int i = 0; !lost && i < g.getWidth(); i++)
		{
			for (int j = 0; !lost && j < g.getHeight(); j++)
			{
				// On d�couvre la premi�re mine trouv�e
				if (g.getElement(i, j).isMine())
				{
					g.changeCellState(i, j, true);
					lost = true;
				}
			}
		}
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getNbCellsFlagged()}
	 */
	@Test
	public void testGetNbCellsFlagged()
	{
		Game g = new Game(0);
		
		// Cas 1 : La partie vient d'�tre cr��e, donc aucun drapeau n'a �t� plac�.
		Assert.assertEquals(0, g.getNbCellsFlagged());
		
		// Cas 2 : La partie est en cours et le joueur n'a pas plac� tous les drapeaux.
		g = new Game(0);
		g.changeCellState(0, 0, false);
		g.changeCellState(0, 1, false);
		g.changeCellState(1, 0, false);
		Assert.assertEquals(3, g.getNbCellsFlagged());
		
		// Cas 3 : La partie est en cours et le joueur a plac� un nombre de drapeau sup�rieur au nombre
		// 		   de mines cach�es dans la grille.
		g = new Game(0);
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < 2; j++)
			{
				// On pose un drapeau sur 18 cases.
				g.changeCellState(i, j, false);
			}
		}
		Assert.assertEquals(18, g.getNbCellsFlagged());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getWidth()}
	 */
	@Test
	public void testGetWidth()
	{
		// Cas 1 : On cr�e une partie de niveau d�butant, donc la largeur devrait �tre de 9.
		Game g = new Game(0);
		Assert.assertEquals(9, g.getWidth());

		// Cas 2 : On cr�e une partie de niveau interm�diaire, donc la largeur devrait �tre de 16.		
		g = new Game(1);
		Assert.assertEquals(16, g.getWidth());
		
		// Cas 3 : On cr�e une partie de niveau expert, donc la largeur devrait �tre de 30.
		g = new Game(2);
		Assert.assertEquals(30, g.getWidth());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getHeight()}
	 */
	@Test
	public void testGetHeight()
	{
		// Cas 1 : On cr�e une partie de niveau d�butant, donc la hauteur devrait �tre de 9.
		Game g = new Game(0);
		Assert.assertEquals(9, g.getHeight());

		// Cas 2 : On cr�e une partie de niveau interm�diaire, donc la hauteur devrait �tre de 16.		
		g = new Game(1);
		Assert.assertEquals(16, g.getHeight());
		
		// Cas 3 : On cr�e une partie de niveau expert, donc la hauteur devrait �tre de 16.
		g = new Game(2);
		Assert.assertEquals(16, g.getHeight());
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getMineAmount()}
	 */
	@Test
	public void testGetMineAmount()
	{
		// Cas 1 : On cr�e une partie de niveau d�butant, donc le nombre de mined devrait �tre de 10.
		Game g = new Game(0);
		Assert.assertEquals(10, g.getMineAmount());

		// Cas 2 : On cr�e une partie de niveau interm�diaire, donc le nombre de mines devrait �tre de 40.
		g = new Game(1);
		Assert.assertEquals(40, g.getMineAmount());
		
		// Cas 3 : On cr�e une partie de niveau expert, donc le nombre de mines devrait �tre de 99.
		g = new Game(2);
		Assert.assertEquals(99, g.getMineAmount());
	}
	
	/**
	 * M�thode de test pour {@link appDemineur.model.Game#getElement(int, int)}.
	 * 
	 * @see {@link util.BaseMatrix#getElement(int, int)}
	 */
	@Test
	public void testGetElementIntInt() 
	{
		Game g = new Game(0);
		
		//Cas valide 1 : La position demand�e en (x, y) est valide et l'objet ins�r� � cette position n'est pas null 
		// 				 et est une instance de Cell.
		Assert.assertNotNull(g.getElement(0, 0));
		Assert.assertTrue(g.getElement(0, 0) instanceof Cell);
		
		//Cas valide 2 : La position demand�e en (x, y) est valide et l'objet ins�r� � cette position n'est pas null 
		//				 mais n'est pas une instance de Cell.
		g.setElement(1, 1, "PasUneCellule");
		Assert.assertNull(g.getElement(1, 1));
		Assert.assertFalse(g.getElement(1, 1) instanceof Cell);
		
		//Cas invalide 1 : La position demand�e en (x, y) est valide et l'objet ins�r� � cette position est null 
		//				   et n'est pas une instance de Cell.
//		g.setElement(1, 2, null);
//		Assert.assertNull(g.getElement(1, 2));
//		Assert.assertFalse(g.getElement(1, 2) instanceof Cell);
	}
}
