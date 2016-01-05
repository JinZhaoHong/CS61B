import java.util.Observable;
import java.util.Queue;
import java.util.LinkedList;
/** 
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields: 
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze; 

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;     
    }

    /** Conducts a breadth first search of the maze starting at vertex x. */
    private void bfs(int x) {
        /* Your code here. */
        Queue<Integer> q = new LinkedList<Integer>();
        marked[x] = true;
        q.add(x);
        announce();

        while (!q.isEmpty()) {
            int v = q.poll();
            if (v == t) {
                targetFound = true;
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.add(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
} 

