package appDemineur.model;

import java.awt.Dimension;

/**
 * Classe définissant les propriétés d'un niveau de difficulté du jeu du démineur
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
	 * Construit un niveau de difficulté.
	 * 
	 * @param dim Dimensions de la matrice
	 * @param mineAmount Nombre de mines dans la matrice
	 * @param name Nom du niveau
	 * @param displayName Nom du niveau pour l'affichage
	 */
	public Level(Dimension dim, int mineAmount, String name, String displayName)
	{
		// Matrice d'au moins 1 x 1
		dim.width = Math.max(1, dim.width);
		dim.height = Math.max(1, dim.height);
		this.dim = dim;
		
		// Nombre de mines : au moins 0 et au plus (largeur * hauteur) - 1  
		this.mineAmount = Math.max(0, Math.min(this.dim.width * this.dim.height - 1, mineAmount));

		this.name = (name == null || name.length() == 0) ? "unnamed_level" : name;
		this.displayName = (displayName == null || displayName.length() == 0) ? "Niveau ?" : displayName;
	}
}
