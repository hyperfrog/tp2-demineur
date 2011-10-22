package appDemineur.model;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import appDemineur.form.Board;
import appDemineur.model.Cell.CellState;

public class Game extends JPanel
{
	private Matrix matrix;
	
	public Game(int width, int height, int mineAmount)
	{
		this.matrix = new Matrix(width, height, mineAmount);
	}
	
	public void redraw(Graphics g, float cellSize)
	{
		if (g != null)
		{	
			for (int i = 0; i < this.matrix.getWidth(); i++)
			{
				for (int j = 0; j < this.matrix.getHeight(); j++)
				{
					if (this.matrix.getElement(i, j) != null)
					{
						Graphics g2 = g.create(
								Math.round(i * cellSize), 
								Math.round(j * cellSize), 
								Math.round(cellSize), 
								Math.round(cellSize));

						Cell c = this.matrix.getElement(i, j);
						if (c != null)
						{
							c.redraw(g2, cellSize);
						}
					}
				}
			}
	
			// Dessine un quadrillage 
			
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setStroke(new BasicStroke(3));
			
			for (int i = 0; i <= this.matrix.getWidth(); i++)
			{
				int x = Math.round(i * cellSize);
				
				g2d.drawLine(x, 0, x, Math.round(this.matrix.getHeight() * cellSize));
			}
			
			for (int i = 0; i <= this.matrix.getHeight(); i++)
			{
				int y = Math.round(i * cellSize);
				
				g2d.drawLine(0, y, Math.round(this.matrix.getWidth() * cellSize), y);
			}
		}
	}
	
	// Work in progress ...
	public boolean changeCellState(int x, int y, CellState state)
	{
		boolean succeed = false;
		
		Cell c = this.matrix.getElement(x, y);
		
		if (c != null)
		{
			switch (state)
			{
			case SHOWN:
				if (c.isMine())
				{
					// Partie perdue.
				}
				else if (!c.getState().equals(CellState.SHOWN) && !c.getState().equals(CellState.FLAGGED))
				{
					this.showCell(x, y, 8);
					succeed = true;
				}
				break;
				
			case FLAGGED:
			case DUBIOUS:
			case HIDDEN:
				if (!c.getState().equals(CellState.SHOWN))
				{
					c.setState(state);
					succeed = true;
				}
				break;
			}
		}
		
		return succeed;
	}
	
	// Work in progress ... à refaire...
	private void showCell(int x, int y, int dir)
	{
		Cell c = this.matrix.getElement(x, y);

		if (c != null)
		{
			if (c.getAdjacentMines() == 0 && !c.getState().equals(CellState.SHOWN))
			{
				switch (dir)
				{
					case 0:
						this.showCell(x - 1, y - 1, 0);
						this.showCell(x - 1, y    , 3);
						this.showCell(x    , y - 1, 1);
						break;
					case 1:
						this.showCell(x - 1, y - 1, 1);
						this.showCell(x    , y - 1, 1);
						this.showCell(x + 1, y - 1, 1);
						break;
					case 2:
						this.showCell(x + 1, y - 1, 2);
						this.showCell(x + 1, y    , 4);
						this.showCell(x    , y - 1, 1);
						break;
					case 3:
						this.showCell(x - 1, y + 1, 3);
						this.showCell(x - 1, y    , 3);
						this.showCell(x - 1, y - 1, 3);
						break;
					case 4:
						this.showCell(x + 1, y + 1, 4);
						this.showCell(x + 1, y    , 4);
						this.showCell(x + 1, y - 1, 4);
						break;
					case 5:
						this.showCell(x - 1, y + 1, 5);
						this.showCell(x - 1, y    , 3);
						this.showCell(x    , y + 1, 6);
						break;
					case 6:
						this.showCell(x - 1, y + 1, 6);
						this.showCell(x    , y + 1, 6);
						this.showCell(x + 1, y + 1, 6);
						break;
					case 7:
						this.showCell(x + 1, y + 1, 7);
						this.showCell(x + 1, y    , 4);
						this.showCell(x    , y + 1, 6);
						break;
					case 8:
						this.showCell(x - 1, y - 1, 0);
						this.showCell(x    , y - 1, 1);
						this.showCell(x + 1, y - 1, 2);
						this.showCell(x - 1, y    , 3);
						this.showCell(x + 1, y    , 4);
						this.showCell(x - 1, y + 1, 5);
						this.showCell(x    , y + 1, 6);
						this.showCell(x + 1, y + 1, 7);
						break;
				}
			}
			
			c.setState(CellState.SHOWN);
		}
	}
	
}
