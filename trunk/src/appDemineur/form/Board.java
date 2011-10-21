package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import appDemineur.model.Cell;
import appDemineur.model.Game;

public class Board extends JPanel implements ActionListener, MouseListener
{
	public static final int GRID_SIZE = 16;
	public static final int GRID_MINE = 40;
	
	private Game currentGame;
	private JPanel buttonPanel;
	private JPanel gamePanel;
	private JButton bNewGrid;
	
	public Board()
	{
		super();
		
		this.currentGame = new Game(Board.GRID_SIZE, Board.GRID_SIZE, Board.GRID_MINE);
		
		this.buttonPanel = new JPanel();
		this.gamePanel = new JPanel();
		
		this.setLayout(new BorderLayout());
        this.add(gamePanel, BorderLayout.CENTER);
        this.gamePanel.setBackground(Color.LIGHT_GRAY);
		
		this.bNewGrid = new JButton("New Grid");
		this.bNewGrid.setActionCommand("NEW_GRID");
		this.bNewGrid.addActionListener(this);
		
		this.buttonPanel.add(this.bNewGrid);
		this.buttonPanel.setBackground(Color.WHITE);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.setPreferredSize(new Dimension(Board.GRID_SIZE * Cell.CELL_SIZE, (Board.GRID_SIZE * Cell.CELL_SIZE) + 45));
		this.addMouseListener(this);
	}
	
	public void redraw()
	{
		Graphics g = this.gamePanel.getGraphics();
		Image bufferImg = this.createImage(this.gamePanel.getWidth(), this.gamePanel.getHeight());
	    Graphics buffer = bufferImg.getGraphics();
		
		if (g != null)
		{
			buffer.setClip(0, 0, this.gamePanel.getWidth(), this.gamePanel.getHeight());
			this.currentGame.redraw(buffer);
			
			g.drawImage(bufferImg, 0, 0, this);
		}
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("NEW_GRID"))
		{
			this.currentGame.newGame(Board.GRID_SIZE, Board.GRID_SIZE, Board.GRID_MINE);
			this.redraw();
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if (this.currentGame.changeCellState(e.getX() / Cell.CELL_SIZE, e.getY() / Cell.CELL_SIZE))
			this.redraw();
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}