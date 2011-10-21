package appDemineur.form;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class AppFrame extends JFrame
{
	private static final Dimension INIT_SIZE = new Dimension(800, 600);
	private static final String INIT_TITLE = "Minesweeper";
	
	private Board mBoard; 
	
	public AppFrame()
	{
		super();
		
		this.setTitle(AppFrame.INIT_TITLE);
		this.setSize(AppFrame.INIT_SIZE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mBoard = new Board();
		this.getContentPane().add(this.mBoard);
		
		this.pack();
	}
	
	/**
	 * Redessine la fenêtre.
	 * Cette méthode doit être publique mais ne devrait pas être appelée directement.
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 * 
	 * @param g Graphics de la fenêtre
	 */
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		this.mBoard.redraw();		
	}
	
}
