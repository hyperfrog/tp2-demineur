package appDemineur.model.test;

import org.junit.Assert;
import org.junit.Test;

import appDemineur.model.*;
import appDemineur.model.Cell.CellState;

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
		Assert.assertEquals(1, g.getLevelNum());
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
		Assert.assertEquals(0, g.getLevelNum());
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
		Assert.assertEquals(2, g.getLevelNum());
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
		Assert.assertEquals(0, g.getLevelNum());
		Assert.assertFalse(g.isOver());
		Assert.assertFalse(g.isWon());
		Assert.assertFalse(g.isLost());
		
		// Cas invalide 2 : Le niveau est sup�rieur � 2
		g = new Game(3);
		Assert.assertNotNull(g);
		Assert.assertEquals(30, g.getWidth());
		Assert.assertEquals(16, g.getHeight());
		Assert.assertEquals(99, g.getMineAmount());
		Assert.assertEquals(0, this.mineOnGrid(g));
		Assert.assertEquals(0, g.getNbCellsFlagged());
		Assert.assertEquals(0, g.getNbCellsShown());
		Assert.assertEquals(2, g.getLevelNum());
		Assert.assertFalse(g.isOver());
		Assert.assertFalse(g.isWon());
		Assert.assertFalse(g.isLost());
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
	 *  M�thode de test pour {@link appDemineur.model.Game#changeCellState(int, int)}
	 */
	@Test
	public void testChangeCellStateIntInt()
	{
		Game g;
		int nbFlagsBefore = 0;
		
		// Cas valide 1 : La position est valide, l'�tat de la cellule est HIDDEN et on veut la changer pour FLAGGED.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(3, 3));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.FLAGGED, g.getElement(3, 3).getState());
		
		// Cas valide 2 : La position est valide, l'�tat de la cellule est FLAGGED et on veut la changer pour DUBIOUS.
		g = new Game(0);
		g.changeCellState(3, 3);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(3, 3));
		Assert.assertEquals(nbFlagsBefore - 1, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.DUBIOUS, g.getElement(3, 3).getState());
		
		// Cas valide 3 : La position est valide, l'�tat de la cellule est DUBIOUS et on veut la changer pour HIDDEN.
		g = new Game(0);
		g.changeCellState(3, 3);
		g.changeCellState(3, 3);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(3, 3));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.HIDDEN, g.getElement(3, 3).getState());
		
		// Cas limite 1 : La position est (0, 0) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(0, 0));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.FLAGGED, g.getElement(0, 0).getState());
		
		// Cas limite 2 : La position est (largeur - 1, hauteur - 1) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(8, 8));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.FLAGGED, g.getElement(8, 8).getState());
		
		// Cas limite 3 : La position est (largeur - 1, 0) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(8, 0));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.FLAGGED, g.getElement(8, 0).getState());
		
		// Cas limite 4 : La position est (0, hauteur - 1) et la cellule n'est pas null donc on peut changer son �tat.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertTrue(g.changeCellState(0, 8));
		Assert.assertEquals(nbFlagsBefore + 1, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.FLAGGED, g.getElement(0, 8).getState());
		
		// Cas invalide 1 : La position est inf�rieure � 0 en x et en y.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertFalse(g.changeCellState(-1, -1));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
		
		// Cas invalide 2 : La position en x est valide et inf�rieure � 0 en y.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertFalse(g.changeCellState(1, -1));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
		
		// Cas invalide 3 : La position en y est valide et inf�rieure � 0 en x.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertFalse(g.changeCellState(-1, 1));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
		
		// Cas invalide 4 : La position en x est sup�rieure ou �gale � la largeur de la grille et  
		// 					la position en y est sup�rieure ou �gale � la hauteur de la grille.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertFalse(g.changeCellState(9, 9));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());

		// Cas invalide 5 : La position en x est sup�rieure ou �gale � la largeur de la grille et la position en y est valide.
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertFalse(g.changeCellState(9, 1));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
		
		// Cas invalide 6 : La position en y est sup�rieure ou �gale � la hauteur de la grille et la position en x est valide
		g = new Game(0);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertFalse(g.changeCellState(1, 9));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
		
		// Cas invalide 7 : La position est valide, l'�tat de la cellule est SHOWN et on essaie de changer son �tat
		g = new Game(0);
		g.showCell(3, 3);
		nbFlagsBefore = g.getNbCellsFlagged();
		Assert.assertFalse(g.changeCellState(3, 3));
		Assert.assertEquals(nbFlagsBefore, g.getNbCellsFlagged());
		Assert.assertEquals(CellState.SHOWN, g.getElement(3, 3).getState());
	}

	/**
	 *  M�thode de test pour {@link appDemineur.model.Game#showCell(int, int)}
	 */
	@Test
	public void testShowCellIntInt()
	{
		Game g;
		int nbCellsShownBefore;
		
		// Cas valide 1 : La position est valide, l'�tat de la cellule est HIDDEN.
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertTrue(g.showCell(3, 3));
		Assert.assertTrue(nbCellsShownBefore < g.getNbCellsShown());
		
		// Cas valide 2 : La position est valide et l'�tat de la cellule est DUBIOUS.
		g = new Game(0);
		g.changeCellState(3, 3);
		g.changeCellState(3, 3);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertTrue(g.showCell(3, 3));
		Assert.assertTrue(nbCellsShownBefore < g.getNbCellsShown());
		
		// Cas valide 3 : Ajout des mines : La position est valide et le niveau est situ� entre 0 et 2.
		g = new Game(1);
		Assert.assertTrue(g.showCell(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(40, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas limite 1 : La position est (0, 0).
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertTrue(g.showCell(0, 0));
		Assert.assertTrue(nbCellsShownBefore < g.getNbCellsShown());
		
		// Cas limite 2 : La position est (largeur - 1, hauteur - 1).
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertTrue(g.showCell(8, 8));
		Assert.assertTrue(nbCellsShownBefore < g.getNbCellsShown());
		
		// Cas limite 3 : La position est (largeur - 1, 0).
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertTrue(g.showCell(8, 0));
		Assert.assertTrue(nbCellsShownBefore < g.getNbCellsShown());
		
		// Cas limite 4 : La position est (0, hauteur - 1).
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertTrue(g.showCell(0, 8));
		Assert.assertTrue(nbCellsShownBefore < g.getNbCellsShown());
		
		// Cas limite 5 : Ajout des mines : La position est valide et le niveau est 0
		g = new Game(0);
		Assert.assertTrue(g.showCell(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(10, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas limite 6 : Ajout des mines : La position est valide et le niveau est 2
		g = new Game(2);
		Assert.assertTrue(g.showCell(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(99, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas invalide 1 : La position est valide et l'�tat de la cellule est FLAGGED.
		g = new Game(0);
		g.changeCellState(3, 3);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(3, 3));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());
		
		// Cas invalide 2 : La position est valide, l'�tat de la cellule est SHOWN.
		g = new Game(0);
		g.showCell(3, 3);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(3, 3));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());
		
		// Cas invalide 3 : La position est inf�rieure � 0 en x et en y.
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(-1, -1));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());
		
		// Cas invalide 4 : La position en x est valide et inf�rieure � 0 en y.
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(1, -1));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());
		
		// Cas invalide 5 : La position en y est valide et inf�rieure � 0 en x.
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(-1, 1));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());
		
		// Cas invalide 6 : La position en x est sup�rieure ou �gale � la largeur de la grille et  
		// 					la position en y est sup�rieure ou �gale � la hauteur de la grille.
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(9, 9));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());

		// Cas invalide 7 : La position en x est sup�rieure ou �gale � la largeur de la grille et la position en y est valide.
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(9, 1));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());
		
		// Cas invalide 8 : La position en y est sup�rieure ou �gale � la hauteur de la grille et la position en x est valide
		g = new Game(0);
		nbCellsShownBefore = g.getNbCellsShown();
		Assert.assertFalse(g.showCell(1, 9));
		Assert.assertEquals(nbCellsShownBefore, g.getNbCellsShown());
		
		// Cas invalide 9 : Le niveau est inf�rieur � 0
		g = new Game(-1);
		Assert.assertTrue(g.showCell(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(10, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));
		
		// Cas invalide 10 : Le niveau est sup�rieur � 2
		g = new Game(3);
		Assert.assertTrue(g.showCell(0, 0));
		Assert.assertFalse(g.getElement(0, 0).isMine());
		Assert.assertEquals(99, this.mineOnGrid(g));
		Assert.assertTrue(this.checkAdjacentMines(g));		
	}

	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#isLost()}
	 */
	@Test
	public void testIsLost()
	{
		Game g = new Game(0);
		
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
		Game g;
		
		// Cas 1 : La partie vient de demarrer et n'est donc pas termin�e.
		g = new Game(0);
		Assert.assertFalse(g.isOver());
		
		// Cas 2 : Le joueur a perdu la partie.
		g = new Game(0);
		this.loseGame(g);
		Assert.assertTrue(g.isOver());

		// Cas 3 : Le joueur a gagn� la partie.
		g = new Game(0);
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
					g.showCell(i, j);
				}
			}
		}
	}
	
	// Perd une partie
	private void loseGame(Game g)
	{
		boolean lost = false;

		g.showCell(0, 0);
		
		// On parcours la matrice au complet
		for (int i = 0; !lost && i < g.getWidth(); i++)
		{
			for (int j = 0; !lost && j < g.getHeight(); j++)
			{
				// On d�couvre la premi�re mine trouv�e
				if (g.getElement(i, j).isMine())
				{
					g.showCell(i, j);
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
		g.changeCellState(0, 0);
		g.changeCellState(0, 1);
		g.changeCellState(1, 0);
		Assert.assertEquals(3, g.getNbCellsFlagged());
		
		// Cas 3 : La partie est en cours et le joueur a plac� un nombre de drapeau sup�rieur au nombre
		// 		   de mines cach�es dans la grille.
		g = new Game(0);
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < 2; j++)
			{
				// On pose un drapeau sur 18 cases.
				g.changeCellState(i, j);
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
	 * @see util.BaseMatrix#getElement(int, int)
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
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getNbCellsShown()}
	 */
	@Test
	public void testGetNbCellsShown()
	{
		// Cas 1 : La partie vient d'�tre cr��e, donc aucune cellule n'est d�voil�e.
		Game g = new Game(0);
		Assert.assertEquals(0, g.getNbCellsShown());
		
		// Cas 2 : La partie est en cours et le joueur n'a pas d�voil� toutes les cases non min�es.
		g = new Game(0);
		g.showCell(0, 0);
		Assert.assertTrue(g.getNbCellsShown() > 0);
		Assert.assertEquals(this.nbCellsShown(g), g.getNbCellsShown());
		
		// Cas 3 : La partie est gagn�e
		g = new Game(0);
		winGame(g);
		Assert.assertTrue(g.getNbCellsShown() > 0);
		Assert.assertEquals((g.getWidth() * g.getHeight()) - g.getMineAmount(), g.getNbCellsShown());
		
		// Cas 4 : La partie est perdue
		g = new Game(0);
		loseGame(g);
		Assert.assertTrue(g.getNbCellsShown() > 0);
		Assert.assertEquals(this.nbCellsShown(g), g.getNbCellsShown());
	}
	
	// Retourne le nombre de cellules non min�es qui sont d�voil�es
	private int nbCellsShown(Game g)
	{
		int cellCount = 0;
		
		for (int i = 0; i < g.getWidth(); i++)
		{
			for (int j = 0; j < g.getHeight(); j++)
			{
				// Si c'est une mine, on incr�mente le compteur de mines
				if (g.getElement(i, j).getState().equals(CellState.SHOWN) && !g.getElement(i, j).isMine())
				{
					cellCount++;
				}
			}
		}
		
		return cellCount;
	}
	
	/**
	 * 	M�thode de test pour {@link appDemineur.model.Game#getLevelNum()}
	 */
	@Test
	public void testGetLevelNum()
	{
		// Cas valide 1 : Le niveau est situ� entre 0 et 2.
		Game g = new Game(1);
		Assert.assertEquals(1, g.getLevelNum());
		
		// Cas limite 1 : Le niveau est 0
		g = new Game(0);
		Assert.assertEquals(0, g.getLevelNum());
		
		// Cas limite 2 : Le niveau est 2
		g = new Game(2);
		Assert.assertEquals(2, g.getLevelNum());
		
		// Cas invalide 1 : Le niveau est inf�rieur � 0
		g = new Game(-1);
		Assert.assertEquals(0, g.getLevelNum());
		
		// Cas invalide 2 : Le niveau est sup�rieur � 2
		g = new Game(3);
		Assert.assertEquals(2, g.getLevelNum());
	}
}
