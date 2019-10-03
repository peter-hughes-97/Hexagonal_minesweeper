import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class hexsweeper {
	
    public static final int MINE = 10;

    private static double POPULATION_CONSTANT = 2;

    int mines[][];
    
    public int mineNum;
		
  public hexsweeper() {
		initGame();
		createAndShowGUI();
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				new hexsweeper();
				}
				});
	}
  
	final static int empty = 0;
	final static int gridSize = 14; 
	final static int hexSize = 45;	
	final static int border = 15;  
	final static int screensize = hexSize * (gridSize + 1) + border*3; 
	int playerx = 0;
	int playery = 0;
	
	public JFrame  frame;

	int[][] board = new int[gridSize][gridSize];

	void initGame(){

		hexagon.setXYasVertex(false);

		hexagon.setHeight(hexSize); 
		hexagon.setBorders(border);

		for (int i=0;i<gridSize;i++) {
			for (int j=0;j<gridSize;j++) {
				board[i][j]=empty;
			}
		}
		
		board[0][0] = (int)'A';
		board[13][13] = -(int)'B';
	}
	

	private void createAndShowGUI()
	{
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
			setBackground(Color.WHITE);

			MyMouseListener ml = new MyMouseListener();            
			addMouseListener(ml);
			createMines();	
		}

		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g2);
			for (int i=0;i<gridSize;i++) {
				for (int j=0;j<gridSize;j++) {
					hexagon.drawHex(i,j,g2);
				}
			}
			for (int i=0;i<gridSize;i++) {
				for (int j=0;j<gridSize;j++) {					
					hexagon.fillHex(i,j,board[i][j],g2);
				}
			}
		}

		class MyMouseListener extends MouseAdapter	{	
			public void mouseClicked(MouseEvent e) { 
				int x = e.getX(); 
				int y = e.getY();
				Point p = new Point( hexagon.pxtoHex(e.getX(),e.getY()) );
				if (p.x < 0 || p.y < 0 || p.x >= gridSize || p.y >= gridSize) return;
				if (p.x == (playerx + 1) || p.x == (playerx - 1) || p.x == playerx){
					if (p.y == (playery + 1) || p.y == (playery - 1) || p.y == playery){
						if(p.x == gridSize-1 && p.y == gridSize-1){
							JOptionPane.showMessageDialog(
							frame, "You have won!", "Congratulations",
							JOptionPane.INFORMATION_MESSAGE
							);
						} else {
						playerx = p.x;
						playery = p.y;
						board[p.x][p.y] = (int)' ';
						repaint();
						}
					}
				}
			}
		} 
		public void createMines () {
			System.out.println("Creating Mines");
        //resetAllCells();

        final int mineCount = (int) POPULATION_CONSTANT * gridSize;
        final Random random    = new Random();

        // Map all (row, col) pairs to unique integers
        Set<Integer> positions = new HashSet<>(gridSize * gridSize);
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                positions.add(row * gridSize + col);
            }
        }
        // Initialize mines
        for (int index = 0; index < mineCount; index++) {
            int choice = random.nextInt(positions.size());
			//System.out.println(choice);
            int row = choice / gridSize;
            int col = choice % gridSize;
			if (row == 0 && col == 0) continue;
			mines = new int[row][col];
			board[row][col] = (int)'X';
			repaint();
            mineNum++;
            //Counter.setText(String.valueOf(mineNum));
            positions.remove(choice);
        }
		/*
        // Initialize neighbour counts
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (!cells[row][col].isAMine()) {
                    cells[row][col].updateNeighbourCount();
                }
            }
        }
        */
		}
	} // end of DrawingPanel class
}