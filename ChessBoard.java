// Chessboard
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class ChessBoard extends JPanel {
	private int moveNumber;
	private Square[][] squares;
	private int[][] accessibilityTable;
	private ArrayList<Square> moveHistory;
	private static BufferedImage whiteHorse;
	private static BufferedImage blackHorse;
	private final int LEFT_MARGIN = 6; 
	private final int TOP_MARGIN = 6; 

	public ChessBoard() {

		this.setDoubleBuffered(true);
		this.setBounds(LEFT_MARGIN,TOP_MARGIN,400+1,400+1);
		ChessBoard.loadImages();
		this.squares = new Square[8][8];
		this.initializeBoard();
		this.accessibilityTable = createAccessibilityTable();
		this.moveHistory = new ArrayList<Square>();
	}
	
	public int getMoveNumber() {
		return this.moveNumber;
	}
	
	public void resetMoveHistory() {
		this.moveHistory.clear();
	}

	public void initializeBoard() {
		this.moveNumber = 0;
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				this.squares[r][c] = new Square(r,c);
			}
		}
	}

	public void firstMove(Point pt) {
		if (this.moveNumber > 0) return;
		int row = pt.y / 50;
		int col = pt.x / 50;
		if (row > 7) row = 7;
		if (col > 7) col = 7;
		Square sqClicked = this.squares[row][col];
		sqClicked.setSquareState(SquareState.OCCUPIED);
		this.moveNumber = 1;
		sqClicked.setMoveNumber(this.moveNumber);
		this.setAvailableMoves(sqClicked);
		this.moveHistory.add(sqClicked);
		this.repaint();
	}

	public Square adjustBoardState() {
		for (Square[] row : this.squares) {
			for (Square sq : row) {
				if (sq.getSquareState() == SquareState.OCCUPIED) {
					sq.setSquareState(SquareState.VISITED);
				}
				if (sq.getSquareState() == SquareState.POTENTIAL_MOVE) {
					sq.setSquareState(SquareState.UNVISITED);
				}
			}
		}

		for (Square[] row : this.squares) {
			for (Square sq : row) {
				if (sq.getSquareState() == SquareState.NEXT_MOVE) {
					sq.setSquareState(SquareState.OCCUPIED);
					return sq;
				}
			}
		}
		return null;
	}

	public boolean nextMove() {
		boolean gameOver = true;
		this.moveNumber++;
		Square sqOccupied = this.adjustBoardState();
		sqOccupied.setMoveNumber(this.moveNumber);
		if (this.moveNumber < 64) {
			this.setAvailableMoves(sqOccupied);
			gameOver = false;
		}
		if (!this.moveHistory.contains(sqOccupied)) {
			this.moveHistory.add(sqOccupied);
		}
		this.repaint();
		return gameOver;
	}

	public void prevMove() {
			this.moveNumber--;
			Square prev = this.moveHistory.get(this.moveNumber-1);
			prev.setSquareState(SquareState.OCCUPIED);
			// reset the states of the current squares
			for (Square[] row : this.squares) {
				for (Square sq: row) {
					if (sq.getSquareState() == SquareState.POTENTIAL_MOVE) {
						sq.setSquareState(SquareState.UNVISITED);
					}
					if (sq.getSquareState() == SquareState.NEXT_MOVE) {
						sq.setSquareState(SquareState.UNVISITED);
					}
				}  
			}

			this.setAvailableMoves(prev);

			this.repaint();
		
	}

	public int[][] createAccessibilityTable() {
		int[][] table = new int[8][8];
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				table[r][c] = 
						this.getAllPossibleValidMoves(r, c).size();
			}
		}
		return table;
	}

	public String boardState() {
		String s = "";
		for (Square[] row : this.squares) {
			for (Square sq : row) {
				s = s + sq;
			}
			s += "\n";
		}
		return s;
	}

	public static void loadImages() {
		try {
			File fW = new File("images\\WhiteHorse.png");
			ChessBoard.whiteHorse = ImageIO.read(fW);
			File fB = new File("images\\BlackHorse.png");
			ChessBoard.blackHorse = ImageIO.read(fB);
		}

		catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	public static BufferedImage getHorse(Color clr) {
		if (clr == Color.BLACK) {
			return ChessBoard.whiteHorse;
		}
		return ChessBoard.blackHorse;
	}

	public void paintComponent(Graphics g) {
		this.drawBoard(g);
	}

	public void drawBoard(Graphics g) {
		for (Square[] sqArr : this.squares) {
			for (Square sq : sqArr) {
				sq.display(g);
			}
		}
	}

	public boolean isValidSquare(int row, int col) {
		return 0 <= row && row < 8 && 0 <= col && col < 8; 
	}

	public ArrayList<Square> getAllPossibleValidMoves(int row, int col) {
		int[] horz = { -2, -2, -1, -1, +1, +1, +2, +2 };
		int[] vert = { -1, +1, -2, +2, -2, +2, -1, +1 };
		ArrayList<Square> squares = new ArrayList<Square>();
		for (int i=0; i<horz.length; i++) {
			int c = col + horz[i];
			int r = row + vert[i];
			if (isValidSquare(r,c)) {
				squares.add( this.squares[r][c] );
			}
		}
		return squares;
	}

	// Remove squares that have been visited
	public ArrayList<Square> getAvailableMoves(int row, int col) {
		ArrayList<Square> squares = this.getAllPossibleValidMoves(row, col);
		for (int i=squares.size()-1; i>=0; i--) {
			Square sq = squares.get(i);
			if (sq.getSquareState() == SquareState.VISITED) {
				squares.remove(i);
			}
		}
		return squares;
	}

	public void getNextMove(ArrayList<Square> availMoves) {
		Square sqNext = availMoves.get(0);
		int smallest = 9;
		for (Square sq : availMoves) {
			int r = sq.getRow();
			int c = sq.getCol();
			int nMoves = this.getAvailableMoves(r, c).size();
			if (nMoves < smallest) {
				sqNext = sq;
				smallest = nMoves;
			} 
			else if (nMoves == smallest) {
				int rNext = sqNext.getRow();
				int cNext = sqNext.getCol();
				int rP = sq.getRow();
				int cP = sq.getCol();
				if (this.accessibilityTable[rP][cP] < this.accessibilityTable[rNext][cNext])
				{
					sqNext = sq;
				}
			}
		}
		sqNext.setSquareState(SquareState.NEXT_MOVE);
	}

	public void setAvailableMoves(Square sq) {
		ArrayList<Square> availMoves = 
				this.getAvailableMoves(sq.getRow(), sq.getCol());

		for (Square avail : availMoves) {
			Square sqP = this.squares[avail.getRow()][avail.getCol()];
			sqP.setSquareState(SquareState.POTENTIAL_MOVE);
		}

		this.getNextMove(availMoves);
	}
}

