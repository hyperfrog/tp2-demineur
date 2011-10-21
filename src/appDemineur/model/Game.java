package appDemineur.model;

import java.awt.Graphics;
import javax.swing.JPanel;

import appDemineur.form.Board;
import appDemineur.model.Cell.CellState;

public class Game extends JPanel
{
	private Matrix matrix;
	
	public Game(int width, int height, int mineAmount)
	{
		this.newGame(width, height, mineAmount);
	}
	
	public void newGame(int width, int height, int mineAmount)
	{
		this.matrix = new Matrix(width, height, mineAmount);
	}
	
	public void redraw(Graphics g, float size)
	{
		if (g != null)
		{	
			for (int i = 0; i < this.matrix.getWidth(); i++)
			{
				for (int j = 0; j < this.matrix.getHeight(); j++)
				{
					Graphics g2 = g.create(Math.round(i * size), Math.round(j * size), Math.round(size), Math.round(size));
					this.matrix.getElement(i, j).redraw(g2, size);
				}
			}
		}
	}
	
	public boolean changeCellState(int x, int y)
	{
		boolean succeed = false;
		
		if (this.matrix.getElement(x, y) != null)
		{
			if (this.matrix.getElement(x, y).isMine())
			{
				// Partie perdue.
			}
			else if (!this.matrix.getElement(x, y).getState().equals(CellState.SHOWN))
			{
				this.showCell(x, y, 8);
				succeed = true;
			}
		}
		
		return succeed;
	}
	
	private void showCell(int x, int y, int dir)
	{
		if (this.matrix.getElement(x, y) != null)
		{
			if (this.matrix.getElement(x, y).getAdjacentMines() == 0 && !this.matrix.getElement(x, y).getState().equals(CellState.SHOWN))
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
			
			this.matrix.getElement(x, y).setState(CellState.SHOWN);
		}
	}
	
}
