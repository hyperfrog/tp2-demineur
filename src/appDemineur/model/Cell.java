package appDemineur.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Cell
{
	public enum CellState { HIDDEN, SHOWN, DUBIOUS, FLAGGED }
	
	private boolean isMine;
	private int adjacentMines;
	private CellState state;
	
	public Cell()
	{
		this.isMine = false;
		this.adjacentMines = 0;
		this.state = CellState.HIDDEN;
	}
	
	public void redraw(Graphics g, float size)
	{
		if (g != null)
		{
			if (!this.getState().equals(CellState.SHOWN))
			{
				g.setColor(Color.GRAY);
				g.fill3DRect(0, 0, Math.round(size), Math.round(size), true);
				
				g.setColor(Color.WHITE);
				g.setFont(new Font(null, Font.BOLD, Math.round((12 * size) / 20)));
				
				if (this.getState().equals(CellState.DUBIOUS))
				{
					g.drawString("?", (int) ((7 * size) / 20), (int) ((15 * size) / 20));
				}
				else if (this.getState().equals(CellState.FLAGGED))
				{
					g.drawString("F", (int) ((7 * size) / 20), (int) ((15 * size) / 20));
				}
			}
			else if (this.getState().equals(CellState.SHOWN) && !this.isMine())
			{
				g.setColor(Color.WHITE);
				g.fill3DRect(0, 0, Math.round(size), Math.round(size), true);
				
				switch (this.getAdjacentMines())
				{
					case 1: g.setColor(new Color(106, 133, 165)); break;
					case 2: g.setColor(new Color(0,   200, 0  )); break;
					case 3: g.setColor(new Color(255, 0,   0  )); break;
					case 4: g.setColor(new Color(0,   0,   160)); break;
					case 5: g.setColor(new Color(136, 0,   61 )); break;
					case 6: g.setColor(new Color(112, 178, 146)); break;
					case 7: g.setColor(new Color(171, 31,  26 )); break;
					case 8: g.setColor(new Color(128, 0,   0  )); break;
				}
				
				g.setFont(new Font(null, Font.BOLD, Math.round((12 * size) / 20)));
				g.drawString("" + this.getAdjacentMines(), (int) ((7 * size) / 20), (int) ((15 * size) / 20));
			}
			else if (this.getState().equals(CellState.SHOWN) && this.isMine())
			{
				g.setColor(Color.BLACK);
				g.fill3DRect(0, 0, Math.round(size), Math.round(size), true);
				g.setColor(Color.RED);
				g.fillOval(5, 5, 10, 10);
			}
		}
	}
	
	public void setAdjacentMines(int n)
	{
		this.adjacentMines = n;
	}
	
	public int getAdjacentMines()
	{
		return this.adjacentMines;
	}
	
	public boolean setState(CellState newState)
	{
		boolean succeed = false;
		
		if (newState != null)
		{
			this.state = newState;
			succeed = true;
		}
			
		return succeed;
	}
	
	public CellState getState()
	{
		return this.state;
	}
	
	public void setAsMine(boolean isMine)
	{
		this.isMine = isMine;
	}
	
	public boolean isMine()
	{
		return this.isMine;
	}
	
	public String toString()
	{
		String s = new String("");
		
		if (this.isMine())
			s = "*";
		else
			s = this.getAdjacentMines() + "";
		
		return s;
	}
}
