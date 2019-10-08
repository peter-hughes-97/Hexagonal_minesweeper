import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class hexsweeper {
	
    public static final int MINE = 10;

    private static double POPULATION_CONSTANT = 2;
	
	public Cell[] reusableStorage = new Cell[8];
    static Cell[][] cells;

    int mines[][];
    
    public int mineNum;
	
	final static int empty = 0;
	static int gridSize = 14; 
	final static int hexSize = 45;	
	final static int border = 15;  
	final static int screensize = hexSize * (gridSize + 1) + border*3; 
	int playerx = 0;
	int playery = 0;
	boolean runonce;
	
	public JFrame  frame;

	int[][] board = new int[gridSize][gridSize];
		
	public static void main(String[] args)
	{
		hexsweeper runVar = new hexsweeper();
        SwingUtilities.invokeLater(() -> runVar.run());
	}
	
	public void run() {
		new hexsweeper();
		initGame();
		createAndShowGUI();
		
	}

	void initGame(){

		hexagon.setXYasVertex(false);

		hexagon.setHeight(hexSize); 
		hexagon.setBorders(border);

		for (int i=0;i<gridSize;i++) {
			for (int j=0;j<gridSize;j++) {
				board[i][j]=empty;
			}
		}
	}

	private void createAndShowGUI()
	{
		this.gridSize = gridSize;
        cells = new Cell[gridSize][gridSize];
		DrawingPanel panel = new DrawingPanel();


		 try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) { }
		frame = new JFrame("HexSweeper");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		Container content = frame.getContentPane();
		content.add(panel);
		frame.setSize( (int)(screensize/1.075), screensize);
		frame.setResizable(false);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
	}


	class DrawingPanel extends JPanel
	{		

		public DrawingPanel()
		{	
			setBackground(new Color(169,169,169));

			MyMouseListener ml = new MyMouseListener();            
			addMouseListener(ml);
		}

		public void paintComponent(Graphics g)
		{
			System.out.println("paintComponent");
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g2);
			if(!runonce) {
				System.out.println("runonce");
				for (int row = 0; row < gridSize; row++) {
					for (int col = 0; col < gridSize; col++) {          
						cells[row][col] = new Cell(row, col);
						hexagon.drawHex(row,col,g2);
					}
				}
				createMines();
				runonce = true;
			}
			for (int i=0;i<gridSize;i++) {
					for (int j=0;j<gridSize;j++) {					
						hexagon.fillHex(i,j,board[i][j],g2);
					}
				}
		}
		
		class MyMouseListener extends MouseAdapter	{	
			public void mouseClicked(MouseEvent e) {
				System.out.println("MouseEvent");
				int x = e.getX(); 
				int y = e.getY();
				Point p = new Point( hexagon.pxtoHex(e.getX(),e.getY()) );
				if (p.x < 0 || p.y < 0 || p.x >= gridSize || p.y >= gridSize) return;
				if (p.x == (playerx + 1) || p.x == (playerx - 1) || p.x == playerx){
					if (p.y == (playery + 1) || p.y == (playery - 1) || p.y == playery){
						handleCell(playerx, playery);
						playerx = p.x;
						playery = p.y;
						Cell cell = cells[p.x][p.y];
						handleNewCell(playerx, playery);
						repaint();
						}
					}
			}
		}
		private void resetAllCells() {
			System.out.println("resetAllCells");
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				cells[row][col].reset();
			}
		}
		}
		
		public void reset() {
			System.out.println("reset");
			runonce = false;
			mineNum = 0;
			frame.dispose();
			playerx = 0;
			playery = 0;
			run();
		}
		public void createMines() {
			System.out.println("createMines");
			final int    mineCount = (int) POPULATION_CONSTANT * gridSize;
			final Random random    = new Random();
			Set<Integer> positions = new HashSet<>(gridSize * gridSize);
			for (int row = 0; row < gridSize; row++) {
				for (int col = 0; col < gridSize; col++) {
					positions.add(row * gridSize + col);
				}
			}
	
			// Initialize mines
			for (int index = 0; index < mineCount; index++) {
				int choice = random.nextInt(positions.size());
				int row    = choice / gridSize;
				int col    = choice % gridSize;
				if (row == 0 && col == 0) continue;
				if (row == 0 && col == 1) continue;
				if (row == 1 && col == 0) continue;
				if (row == gridSize && col == gridSize) continue;
				cells[row][col].setValue(MINE);
				repaint();
				System.out.println(col);
				mineNum++;
				positions.remove(choice);
			}
	
			// Initialize neighbour counts
			for (int row = 0; row < gridSize; row++) {
				for (int col = 0; col < gridSize; col++) {
					if (!cells[row][col].isAMine()) {
						cells[row][col].updateNeighbourCount();
					}
					board[row][col] = (int) ' ';
				}
			}
			handleCell(0, 1);
			handleCell(1, 0);
			board[0][0] = -(int)'A';
			board[13][13] = -(int)'B';
		}

   public void handleCell(int row, int col) {
	System.out.println("handleCell");
	int celltext = cells[row][col].getValue();
	System.out.println(celltext);
      if (cells[row][col].isAMine()) {
		System.out.println("cellmine");
         cells[row][col].setForeground(Color.RED);
         cells[row][col].reveal();
         JOptionPane.showMessageDialog(null, "Game Over");
		 reset();
         return;
      }
      if (cells[row][col].getValue() == 0) {
			board[row][col] = (int)'0';
			System.out.println("value0");
            Set<Cell> positions = new HashSet<>();
            positions.add(cells[row][col]);
            cascade(positions);
         } else {
			if (cells[row][col].getValue() == 1) {
				board[row][col] = (int)'1';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 2) {
				board[row][col] = (int)'2';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 3) {
				board[row][col] = (int)'3';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 4) {
				board[row][col] = (int)'4';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 5) {
				board[row][col] = (int)'5';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 6) {
				board[row][col] = (int)'6';
				cells[row][col].reveal();
			 }
		 }
    }
	public void handleNewCell(int row, int col) {
	System.out.println("handleCell");
	int celltext = cells[row][col].getValue();
	System.out.println(celltext);
      if (cells[row][col].isAMine()) {
		board[row][col] = -(int)'X';
		System.out.println("cellmine");
         cells[row][col].reveal();
         JOptionPane.showMessageDialog(null, "Game Over");
		 reset();
         return;
      } if (row == gridSize && col == gridSize) {
		JOptionPane.showMessageDialog(
            frame, "You have won!", "Congratulations",
            JOptionPane.INFORMATION_MESSAGE
            );
	  }
      if (cells[row][col].getValue() == 0) {
			board[row][col] = -(int)'0';
			System.out.println("value0");
            Set<Cell> positions = new HashSet<>();
            positions.add(cells[row][col]);
            cascade(positions);
         } else {
			if (cells[row][col].getValue() == 1) {
				board[row][col] = -(int)'1';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 2) {
				board[row][col] = -(int)'2';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 3) {
				board[row][col] = -(int)'3';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 4) {
				board[row][col] = -(int)'4';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 5) {
				board[row][col] = -(int)'5';
				cells[row][col].reveal();
			 } else if (cells[row][col].getValue() == 6) {
				board[row][col] = -(int)'6';
				cells[row][col].reveal();
			 }
		 }
    }

    private void cascade(Set<Cell> positionsToClear) {
		System.out.println("cascade");
        while (!positionsToClear.isEmpty()) {
            Cell cell = positionsToClear.iterator().next();
            positionsToClear.remove(cell);
            cell.reveal();

            cell.getNeighbours(reusableStorage);
            for (Cell neighbour : reusableStorage) {
                if (neighbour == null) {
                    break;
                }
                if (neighbour.getValue() == 0
                    && neighbour.isEnabled()) {
                    positionsToClear.add(neighbour);
                } else {
                    neighbour.reveal();
                }
            }
        }
    }
	}
	
}