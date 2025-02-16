import java.util.*;
import javax.swing.JFrame;

class mainDriver {
    public static void main(String[] args) {
        // Cellular automata simulation: Conways game of life
        // If a cell is dead and has exactly 3 living neighbors, it comes alive 
        // If a cell is alive and has 2 or 3 neighors, it stays alive
        // Otherwise it dies 
        JFrame frame = new JFrame("Window");
        frame.setSize(400, 300); // Set the size of the frame
        frame.setVisible(true); // Make the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridPanel panel = new GridPanel();
        frame.setContentPane(panel); 
        
    }

}