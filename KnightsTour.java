// KnightsTour
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KnightsTour extends JFrame 
implements ActionListener, MouseListener {
	private ChessBoard chessBoard;
	private JButton nextMoveButton;
	private JButton runButton;
	private JButton resetButton;
	private JButton prevMoveButton;
	private javax.swing.Timer timer;

	public KnightsTour() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null); // centers in screen
		this.setSize(430,500); // window size
		this.setTitle("Knight's Tour");

		Container contentPane = this.getContentPane();
		contentPane.setLayout(null);
		this.chessBoard = new ChessBoard();
		this.chessBoard.addMouseListener(this);
		contentPane.add(this.chessBoard);

		this.nextMoveButton = new JButton("Next Move");
		this.nextMoveButton.setBounds(6, 420, 93, 24);
		this.nextMoveButton.addActionListener(this);
		this.nextMoveButton.setEnabled(false);
		contentPane.add(this.nextMoveButton);

		this.prevMoveButton = new JButton("Prev Move");
		this.prevMoveButton.setBounds(306, 420, 93, 24);
		this.prevMoveButton.addActionListener(this);
		this.prevMoveButton.setEnabled(false);
		contentPane.add(this.prevMoveButton);

		this.runButton = new JButton("Run");
		this.runButton.setBounds(106, 420, 93, 24);
		this.runButton.addActionListener(this);
		this.runButton.setEnabled(false);
		contentPane.add(this.runButton);

		this.resetButton = new JButton("Reset");
		this.resetButton.setBounds(206, 420, 93, 24);
		this.resetButton.addActionListener(this);
		this.resetButton.setEnabled(false);
		contentPane.add(this.resetButton);

		this.timer = new javax.swing.Timer(10, this);

		this.setVisible(true); // at the very end of the constructor
	}

	public void stopTimer() {
		this.timer.stop();
	}

	public void startTimer() {
		this.timer.start();
	}

	public static void main(String[] args) {
		new KnightsTour();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point pt = e.getPoint();
		System.out.print("mouseClicked: ");
		System.out.println("(" + pt.x + "," + pt.y + ")");		
		this.chessBoard.firstMove(pt);
		this.nextMoveButton.setEnabled(true);
		this.prevMoveButton.setEnabled(true);
		this.runButton.setEnabled(true);
		this.resetButton.setEnabled(true);

	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.timer) {
			boolean gameOver = this.chessBoard.nextMove();
			if (gameOver) {
				this.stopTimer();
				this.nextMoveButton.setEnabled(false);
				this.runButton.setEnabled(false);
			}
		}
		else {
			String sCommand = e.getActionCommand();
			if (sCommand.equalsIgnoreCase("Next Move")) {
				boolean gameOver = this.chessBoard.nextMove();
				if (gameOver) {
					this.nextMoveButton.setEnabled(false);
					this.runButton.setEnabled(false);
				}
			}
			if (sCommand.equalsIgnoreCase("Run")) {
				this.startTimer();
				this.runButton.setText("Pause");
			}
			if (sCommand.equalsIgnoreCase("Pause")) {
				this.stopTimer();
				this.runButton.setText("Run");
			}
			if (sCommand.equalsIgnoreCase("Reset")) {
				this.stopTimer();
				this.runButton.setText("Run");
				this.nextMoveButton.setEnabled(false);
				this.runButton.setEnabled(false);
				this.resetButton.setEnabled(false);
				this.prevMoveButton.setEnabled(false);
				
				this.chessBoard.resetMoveHistory();
				this.chessBoard.initializeBoard();
				this.chessBoard.repaint();
			}
			if (sCommand.equalsIgnoreCase("Prev Move")) {
				if (this.chessBoard.getMoveNumber() == 0) {
					this.prevMoveButton.setEnabled(false);
				}
				if (this.chessBoard.getMoveNumber() == 1) {
					this.stopTimer();
					this.runButton.setText("Run");
					this.nextMoveButton.setEnabled(false);
					this.runButton.setEnabled(false);
					this.resetButton.setEnabled(false);
					
					// clear the moveHistory because the first move is saved
					this.chessBoard.resetMoveHistory();
					this.chessBoard.initializeBoard();
					this.chessBoard.repaint();
				}
				if (this.chessBoard.getMoveNumber() >= 2) {
					this.stopTimer();
					this.chessBoard.prevMove();
				}
			}
		}
	}

} // class KnightsTour
