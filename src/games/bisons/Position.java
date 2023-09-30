package games.bisons;

public class Position {

    public int x; //Coord x
    public int y; //Coord y

    /**
     * Construit une position à partir des coordonnées x et y
     * ======================================================
     * @param nx, coord x
     * @param ny, coord y
     */
    public Position(int nx, int ny){
        x = nx;
        y = ny;
    }

    /**
     * Regarde si deux positions sont égales
     * =====================================
     * @param other
     * @return
     */
    public boolean equal(Position other){
        return this.x == other.x && this.y == other.y;
    }

    public void set(int nx, int ny){
        this.x = nx;
        this.y = ny;
    }

}
