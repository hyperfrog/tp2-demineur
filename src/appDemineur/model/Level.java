package appDemineur.model;

import java.awt.Dimension;

/**
 * Classe d�finissant les propri�t�s d'un niveau de difficult� du jeu du d�mineur
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */
public class Level
{
	// Dimensions de la matrice
	public final Dimension dim;
	// Nombre de mines dans la matrice
	public final int mineAmount;
	// Nom du niveau
	public final String name;
	// Nom du niveau pour l'affichage
	public final String displayName;
	
	/**
	 * Construit un niveau de difficult�.
	 * 
	 * @param dim Dimensions de la matrice
	 * @param mineAmount Nombre de mines dans la matrice
	 * @param name Nom du niveau
	 * @param displayName Nom du niveau pour l'affichage
	 */
	public Level(Dimension dim, int mineAmount, String name, String displayName)
	{
		this.dim = dim;
		this.mineAmount = mineAmount;
		this.name = name;
		this.displayName = displayName;
	}
}
