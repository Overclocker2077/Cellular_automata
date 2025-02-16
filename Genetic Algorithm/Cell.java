public class Cell {
    public int x;
    public int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) {
        Cell c = (Cell) obj;
        if (x == c.x && y == c.y) 
            return true;
        return false;
    }

    public int hashCode() {  
        return 31 * x + y;  // custom hash code 
    }
}
