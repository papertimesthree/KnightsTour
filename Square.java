// Square
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

public class Square {
	// instance (class) variable
	private static final int WH = 50;
	private int row, col, moveNumber;
	private Color clr;
	private BufferedImage horse;
	private SquareState state;
	public int getRow() { return this.row; }
	public int getCol() { return this.col; }
	public void setMoveNumber(int n) {
		this.moveNumber = n;
	}
	public SquareState getSquareState() {
		return this.state;
	}
	public void setSquareState(SquareState state) {
		this.state = state;
	}

	public String toString() {
		//		String s = 
		//			"(r=" + this.row + 
		//			",c=" + this.col + 
		//			",state=" + this.state + ")";
		String s = String.format("%3s","-");
		if (this.state == SquareState.NEXT_MOVE) {
			s = String.format("%3s","N");
		}
		if (this.state == SquareState.OCCUPIED) {
			s = String.format("%3s","O");
		}
		if (this.state == SquareState.POTENTIAL_MOVE) {
			s = String.format("%3s","P");
		}
		if (this.state == SquareState.VISITED) {
			s = String.format("%3s",this.moveNumber);
		}
		return s;
	}

	public Square(int r, int c) {
		this.moveNumber = 0;
		this.row = r;
		this.col = c;
		this.clr = (r + c) % 2 == 0 ? 
				Color.WHITE : Color.BLACK;
		this.horse = ChessBoard.getHorse(this.clr);
		this.state = SquareState.UNVISITED;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Square) {
			Square sq = (Square)obj;
			return this.row == sq.row && this.col == sq.col;
		}
		return false;
	}

	public void display(Graphics g) {
		int left = WH * this.col;
		int top = WH * this.row;

		g.setColor(this.clr); // OCCUPIED,VISITED,UNVISITED
		if (this.state == SquareState.POTENTIAL_MOVE) {
			g.setColor(Color.RED);
		}
		else if (this.state == SquareState.NEXT_MOVE) {
			g.setColor(Color.YELLOW);
		}

		g.fillRect(left, top, WH, WH);
		g.setColor(Color.BLACK);
		g.drawRect(left, top, WH, WH);

		if (this.state == SquareState.OCCUPIED) {
			int x = left + (WH - this.horse.getWidth()) / 2;
			int y = top + (WH - this.horse.getHeight()) / 2;
			g.drawImage(this.horse, x, y, null );
		}

		if (this.state == SquareState.VISITED) {
			g.setColor( Color.WHITE );
			if (this.clr == Color.WHITE) {
				g.setColor( Color.BLACK );
			}
			String str = "" + this.moveNumber;
			Font font = new Font("Times New Roman", Font.BOLD, 24);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			Rectangle2D rect = fm.getStringBounds(str, g);
			int x = left + (WH - (int)rect.getWidth() ) / 2;
			int y = top + fm.getAscent() + (WH - (int)rect.getHeight() ) / 2;
			g.drawString(str, x, y);
		}
	}
}