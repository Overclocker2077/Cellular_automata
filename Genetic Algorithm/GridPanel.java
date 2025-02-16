// import javax.swing.JPanel;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
// import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;

// Cellular automata simulation: Conways game of life
// If a cell is dead and has exactly 3 living neighbors, it comes alive 
// If a cell is alive and has 2 or 3 neighors, it stays alive
// Otherwise it dies 

public class GridPanel extends JPanel {
    //private boolean[][] grid;                           // grid false = dead; true = alive;
    private Integer[][] seed;                           // initial generation of cells 
    private int sample_size = 10;
    private Integer[][][] sample;                       // store all the seeds; which are to be mutated.  
    private int[][] neighbors = {{-1,1}, {0,1}, {1,1},  // store relative location of neighbors
                                        {-1,0},        {1,0},
                                        {-1,-1}, {0,-1}, {1,-1}
                                        };      

    // dead_neighbors_frequency keeps track of the number of neighbors dead cells have 
    private HashMap<Cell, Integer> dead_neighbors_frequency = new HashMap<Cell, Integer>();

    private int generation_count = 0;                   // num of gens that passed
    Set<Cell> aliveSet;        // store location of current living cells with O(1) lookup, so we don't have to iterate through the full grid

    // cell height and width
    private int c_width = 10;
    private int c_height = 10;

    private Timer t;	//used to set the speed of generation refresh
    
    public GridPanel() {
        //boolean[][] grid = new boolean[1000][1000];  // 1000 by 1000 grid false = dead; true = alive;
        // glider gun
        seed =  new Integer[][] {{1,5},{2,5},{1,6},{2,6},
        {11,5}, {11,6}, {11,7}, {12,4}, {12,8},{13,3},{13,9},{14,3},{14,9},
        {15,6}, {16,4}, {16,8}, {17,5}, {17,6}, {17,7}, {18,6},
        {21,3}, {21,4}, {21,5}, {22,3}, {22,4}, {22,5}, {23,2}, {23,6}, {25,1}, {25,2},
        {25,6}, {25,7}, {35,3}, {35,4}, {36,3}, {36,4}};  
        
        // seed = new Integer[][] {
        //     {40,40}, {40,42},{41,41},{41,42},{42,41} 
        // };

        aliveSet = new HashSet<Cell>();
        setFocusable(true);
        
        // initialize
        for (Integer[] pos: seed) {
            aliveSet.add(new Cell(pos[0], pos[1]));
        }

        t = new Timer(40, new Listener());	   // 0 delay between frames
        t.start(); 
    }
    
    public int count_neighbors(Cell pos) {
        int count = 0;
        for (int[] neighbor: neighbors) {
            Cell c = new Cell(pos.x + neighbor[0], pos.y + neighbor[1]);
        
            // O(1) Lookup
            if (aliveSet.contains(c)) {
                count++;
            }
            else {
                // update 
                dead_neighbors_frequency.putIfAbsent(c, 0);
                dead_neighbors_frequency.replace(c, dead_neighbors_frequency.get(c)+1);
            }
        }
        return count;
    }

    public void advance_generation() {
        generation_count++;
        // If a cell is dead and has exactly 3 living neighbors, it comes alive 
        // If a cell is alive and has 2 or 3 neighors, it stays alive
        // Otherwise it dies 
        Iterator<Cell> cells_iter = aliveSet.iterator();
        Set<Cell> new_aliveSet = new HashSet<Cell>();
        while (cells_iter.hasNext()){
            Cell cell = cells_iter.next();
            int n = count_neighbors(cell);
            
            if (n == 2 || n == 3)
                new_aliveSet.add(cell);
        }
        aliveSet = new_aliveSet;
        // dead cells with a frequency of exactly 3 become alive 
        Set<Cell> s = dead_neighbors_frequency.keySet();
        for (Cell c:s) {
            if (dead_neighbors_frequency.get(c) == 3) 
                aliveSet.add(c);
        }
        dead_neighbors_frequency.clear();
    }

    public int[][] random_seed() {
        return null;
    } 

    // public void initialize() {
    //     for (int[] pos: seed) {
    //         alive.add((ArrayList<Integer>) Arrays.asList(pos[0], pos[1]));
    //     }
    // }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        // draw living cells
        if (aliveSet != null) {
            for (Cell cell : aliveSet) {
                g.drawRect(cell.x*c_width, cell.y*c_height, c_width, c_height);
                g.fillRect(cell.x*c_width, cell.y*c_height, c_width, c_height);
            }
        }
    }

    // post: return the fitness of a pattern based on survival time and growth rate 
    public int fitness() {
        return 0;        
    }

    private class Listener implements ActionListener
    {
       public void actionPerformed(ActionEvent e)	//this is called for each timer iteration
       {
          if(true) // TODO: Add a terminating case; MAX Fitness
          {
            advance_generation();
            repaint();
          }
       }
    }
}
