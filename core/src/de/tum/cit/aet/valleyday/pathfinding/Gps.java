package de.tum.cit.aet.valleyday.pathfinding;

import de.tum.cit.aet.valleyday.map.GameMap;
import java.util.*;


/**
 * Implements a GPS class for enemies and NPC which gives back 
 * the shortest path according to the A*-Algorithm 
 * 
 * Implementation via the minimum PQ
 *
 */
public class Gps {
    // make a static method as we dont want to have million instances
    public static List<GridNode> findPath(GridNode start, GridNode goal, GameMap map) {

        if (start == null || goal == null) {return null;}

        if (!map.inBound(start.getX(), start.getY()) || !map.inBound(goal.getX(), goal.getY())) {
            return null;
        }

        PriorityQueue<GridNode> pq = new PriorityQueue<>();

        // have fixed shift indices for neigbors
        int[][] neigbors = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        // Make a boolean array for all places previously visit to avoid going in circles
        boolean[][] visited = new boolean[map.getWidth() + 1][map.getHeight() + 1];

        int x;
        int y;

        int heuristics;

        int offsetX;
        int offsetY;

        
        GridNode current = start;

        visited[start.getX()][start.getY()] = true;

        
        pq.add(current); // Add the starting node


        GridNode neigbour;


        while (!pq.isEmpty()) {

            current = pq.remove();

            if (current.equals(goal)) {
                List<GridNode> path = new LinkedList<>();
                solutionList(path, current);

                if (path.size() > 0) {
                    path.remove(0); 
                }
                return path;
            }

            

            /** make the neighbour board and put on PQ */
            for (int i = 0; i < 4; i++) {

                x = current.getX();
                y = current.getY();

                // Offset given the coordinates of neighbours
                offsetX = x + neigbors[i][0];
                offsetY = y + neigbors[i][1];

                if (map.inBound(offsetX, offsetY) && visited[offsetX][offsetY] == false && map.isWalkable(offsetX, offsetY))  {
                    
                    // Calculate heuristics
                    heuristics = heuristics(offsetX, offsetY, goal);
                    // Make the new node 
                    neigbour = new GridNode(offsetX, offsetY, current.getMoves() + 1, heuristics, current);
                    // add it to the PQ and let it swim
                    pq.add(neigbour);

                    visited[offsetX][offsetY] = true; // visited this tile already
                }
            }
        }

        return null;

        



    }
    /**
     * Calculates the heuristics or costs
     * 
     * @param currTile the current node
     * @param goal     the goal node
     * @return         The calculated costs for the A*-search
     */
    private static int heuristics(int x, int y, GridNode goal) {
        return Math.abs(x - goal.getX()) + Math.abs(y - goal.getY());
    } 

    /**
     * Recursive inorder traversal function to retrieve the Solution list. 
     * Goes to the first node via the prev nodes and adds them sequentially to the list
     * @param list
     * @param solutionNode
     * @return
     */
    private static List<GridNode> solutionList(List<GridNode> list, GridNode solutionNode) {

        if (solutionNode.getPrev() != null) {
            solutionList(list, solutionNode.getPrev());
        }
       
        
        list.add(solutionNode);
        return list;
        
    

    }



    

    
}
