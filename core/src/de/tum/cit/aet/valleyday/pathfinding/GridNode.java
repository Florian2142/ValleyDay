package de.tum.cit.aet.valleyday.pathfinding;



/**
 * Makes a GridNode for determining the neigbour steps of wildlife and Npcs
 * 
 * Important for finding the shortest path towards Crops or Player
 * 
 * 
 */

public class GridNode implements Comparable<GridNode> {

    private int x;
    private int y;
    private int moves;

    private int heuristics;

    private GridNode prev;

    public GridNode(int x, int y, int moves, int heuristics, GridNode prev) {
        this.x = x;
        this.y = y;
        this.moves = moves;
        this.heuristics = heuristics;
        this.prev = prev;
    }
     

    /**
     * Compares two gridnodes and their costs towards the shortest path
     */
    @Override
    public int compareTo(GridNode that) {

        /** fN = f(n) = previous costs + heuristics */

        int thisfN = (this.moves + this.heuristics);
        int thatfN = (that.moves + that.heuristics);

        if (thisfN < thatfN) {
            return -1;
        }
        else if (thisfN > thatfN) {
            return +1;
        }
        else {
            return 0;
        }
        
    }

  
    public boolean equals(GridNode that) {
        if (that == null) {return false;}
        if (!(that instanceof GridNode)) {
            return false;
        }
        if (that.getX() == this.getX() && that.getY() == this.getY()) {
            return true;
        }
        else {
            return false;
        }
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int getMoves() {
        return moves;
    }


    public int getHeuristics() {
        return heuristics;
    }


    public GridNode getPrev() {
        return prev;
    }



    


    
    
}
