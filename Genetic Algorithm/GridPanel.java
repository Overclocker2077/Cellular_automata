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
    private Integer[][] init_tissue;                            // initial configuration of cells 
    private int sample_size = 10;
    private Integer[][][] tissue_sample;
    private int tissue_size = 35;                     
    private int tissue_offset = 30;
    private int mutation_rate = 15;
    private Integer[][][] sample;                       // store all the tissues; which are to be mutated.  
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
        
        // Glider Gun
        init_tissue =  new Integer[][] {{1,5},{2,5},{1,6},{2,6},
        {11,5}, {11,6}, {11,7}, {12,4}, {12,8},{13,3},{13,9},{14,3},{14,9},
        {15,6}, {16,4}, {16,8}, {17,5}, {17,6}, {17,7}, {18,6},
        {21,3}, {21,4}, {21,5}, {22,3}, {22,4}, {22,5}, {23,2}, {23,6}, {25,1}, {25,2},
        {25,6}, {25,7}, {35,3}, {35,4}, {36,3}, {36,4}};  

        // Glider
        // init_tissue = new Integer[][] {
        //     {40,40}, {40,42},{41,41},{41,42},{42,41} 
        // };
        
        //init_tissue = random_tissue(tissue_size, tissue_offset);
        
        aliveSet = new HashSet<Cell>();
        setFocusable(true);
        
        // initialize
        for (Integer[] pos: init_tissue) {
            if (pos != null) {
                aliveSet.add(new Cell(pos[0] + tissue_offset, pos[1] + tissue_offset));
                System.out.println(pos[0] + " " + pos[1]);
            }
        }

        t = new Timer(50, new Listener());	   // 0 delay between frames
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

    // pre: size of tissue and location offset
    // post: return a random tissue structure (random cluster of cells)
    public Integer[][] random_tissue(int size, int offset) {
        Integer[][] random_tissue = new Integer[size][2]; 
        for (int i = 0; i < size; i++) {
            if (Math.random() > 0.5)
                random_tissue[i] = new Integer[] {(int)(Math.random() * mutation_rate + offset), 
                                                (int)(Math.random() * mutation_rate + offset)};
            else
                random_tissue[i] = null;
        }
        return random_tissue;
    } 

    // cross over reproduction
    // pre: two tissues with equal dimentions
    // post: swap second half of the tissues and return both in a 3d array
    public Integer[][][] cross_over(Integer[][] tissue_1, Integer[][] tissue_2) {
        
        for (int i = (int)(tissue_1.length/2); i < tissue_1.length; i++) {
            Integer[] temp = tissue_1[i];
            tissue_1[i] = tissue_2[i];
            tissue_2[i] = temp; 
        } 
        return new Integer[][][] {tissue_1, tissue_2};
    }

    public selection() {

    }

    // post: return the fitness of a pattern based on survival time and growth rate 
    public int fitness() {
        return 0;        
    }

    // pre: tissue
    // post: return randomly modified version of original tissues
    public Integer[][] mutate(Integer[][] tissue) {

    }
    
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
        g.drawString("Gen: " + generation_count, 10,10);
        
        
    }

    private class Listener implements ActionListener
    {
       public void actionPerformed(ActionEvent e)	//this is called for each timer iteration
       {
          if(aliveSet.size() != 0 && generation_count < 1000) // 
          {
            advance_generation();
            repaint();
          }
          else {
            // Use CrossOver function to reproduce with highest fitness

            // Use Mutate function 
            
            // reset 
          }
       }
    }
}
