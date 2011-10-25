package appDemineur.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class Cell
{
	public enum CellState
	{
		HIDDEN (0),
		FLAGGED (1),
		DUBIOUS (2),
		SHOWN (3);
		
		private static final Map<Integer, CellState> lookupMap = new HashMap<Integer, CellState>();
		
		public static CellState get(int id)
		{ 
			return lookupMap.get(id); 
		}
		
		static
		{
			for(CellState cs : EnumSet.allOf(CellState.class))
			{
				lookupMap.put(cs.getId(), cs);
			}
		}

		private int id;
		
		CellState(int id)
		{
			this.id = id;
		}
		
		int getId()
		{
			return this.id;
		}
	}
	
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
		int chrX = Math.round(size * 0.35f);
		int chrY = Math.round(size * 0.75f);
		int fontSize = Math.round(size * 0.60f);
		int cellSize = Math.round(size);
		int minePos = Math.round(size * 0.25f);
		int mineSize = Math.round(size * 0.50f);
		
		if (g != null)
		{
			if (!this.getState().equals(CellState.SHOWN))
			{
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, cellSize, cellSize);
				
				g.setColor(Color.WHITE);
				g.setFont(new Font(null, Font.BOLD, fontSize));
				
				if (this.getState().equals(CellState.DUBIOUS))
				{
					g.drawString("?", chrX, chrY);
				}
				else if (this.getState().equals(CellState.FLAGGED))
				{
					g.drawString("F", chrX, chrY);
				}
			}
			else if (this.getState().equals(CellState.SHOWN) && this.isMine())
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, cellSize, cellSize);
				
				g.setColor(Color.RED);
				g.fillOval(minePos, minePos, mineSize, mineSize);
			}
			else if (this.getState().equals(CellState.SHOWN) && !this.isMine())
			{
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, cellSize, cellSize);

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

				g.setFont(new Font(null, Font.BOLD, fontSize));
				g.drawString("" + this.getAdjacentMines(), chrX, chrY);
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
		return this.isMine() ? "*" : this.getAdjacentMines() + "";
	}
}
