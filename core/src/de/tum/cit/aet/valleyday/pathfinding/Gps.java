package de.tum.cit.aet.valleyday.pathfinding;

import de.tum.cit.aet.valleyday.map.GameMap;
import java.util.*;


/**
 * Implements a GPS class for enemies and NPC which gives back 
 * the shortest path according to the A*-Algorithm 
 * 
 * Implementation via the minimum PQ, meaning ways with lowest costs will always swim to the top.
 * This A*-Search also has the critical optimization of not paying attention to already visited neighbors and will not add them
 * -> Make it severly more efficient.
 * 
 *
 */
public class Gps {
    // Static because spawning a million GPS instances would be peak chaos.
    public static List<GridNode> findPath(GridNode start, GridNode goal, GameMap map) {

        // If you handed me nothing, I will return nothing. Fair trade.
        if (start == null || goal == null) {return null;}

        if (!map.inBound(start.getX(), start.getY()) || !map.inBound(goal.getX(), goal.getY())) {
            // Out of bounds? Not today, map goblins.
            return null;
        }

        PriorityQueue<GridNode> pq = new PriorityQueue<>();

        // Fixed shift indices for neighbors: the four horsemen of "no diagonals".
        int[][] neigbors = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        // Keep track of where we've been so we don't do the map equivalent of pacing.
        boolean[][] visited = new boolean[map.getWidth() + 1][map.getHeight() + 1];

        // Pre-declare because Java likes its variables like it likes coffee: upfront.
        int x;
        int y;

        int heuristics;

        int offsetX;
        int offsetY;

        
        GridNode current = start;

        // Mark the start as "been there, done that".
        visited[start.getX()][start.getY()] = true;

        
        pq.add(current); // Add the starting node to the buffet line.


        GridNode neigbour;


        while (!pq.isEmpty()) {

            current = pq.remove();

            if (current.equals(goal)) {
                // We made it! Cue the victory music and the path reconstruction montage.
                List<GridNode> path = new LinkedList<>();
                solutionList(path, current);

                if (path.size() > 0) {
                    // Drop the start node because the caller already knows where they are.
                    path.remove(0); 
                }
                return path;
            }

            

            // Build the neighbor board and toss them into the priority queue like a reality show.
            for (int i = 0; i < 4; i++) {

                x = current.getX();
                y = current.getY();

                // Compute neighbor coordinates: up/down/left/right, no fancy footwork.
                offsetX = x + neigbors[i][0];
                offsetY = y + neigbors[i][1];

                if (map.inBound(offsetX, offsetY) && visited[offsetX][offsetY] == false && map.isWalkable(offsetX, offsetY))  {
                    
                    // Heuristics: Manhattan distance, aka "taxicab, not teleport".
                    heuristics = heuristics(offsetX, offsetY, goal);
                    // Make the new node with one more move and a little optimism.
                    neigbour = new GridNode(offsetX, offsetY, current.getMoves() + 1, heuristics, current);
                    // Add it to the PQ and let it swim to the top if it's worthy -> Maybe its destiny, who knows.
                    pq.add(neigbour);

                    visited[offsetX][offsetY] = true; // Been there, planted a flag.
                }
            }
        }

        // If we get here, the goal is apparently in another castle.
        return null;

        



    }
    /**
     * Calculates the heuristics or costs.
     * 
     * @param currTile the current node
     * @param goal     the goal node
     * @return         The calculated costs for the A*-search
     */
    private static int heuristics(int x, int y, GridNode goal) {
        // Manhattan distance: because diagonals are cheating.
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
            // Walk backwards in time like a responsible time traveler.
            solutionList(list, solutionNode.getPrev());
        }
       
        
        // Add on the way back out so the path is in the right order.
        list.add(solutionNode);
        return list;
        
    

    }



    

    
}
