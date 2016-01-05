public class Board {

    public static final int SIZE = 8;
    // You can call this variable by Board.SIZE.

    private Piece[][] pieces;
    private boolean isFireTurn;

    public Board() {
        pieces = new Piece[SIZE][SIZE];
        isFireTurn = true;
    }

    /** Makes a custom Board. Not a completely safe operation because you could do
    * some bad stuff here, but this is for the purposes of testing out hash
    * codes so let's forgive the author. 
    */
    public Board(Piece[][] pieces) {
        this.pieces = pieces;
        isFireTurn = true;
    }

    @Override
    public boolean equals(Object o) {
        // YOUR CODE HERE
        if (o instanceof Board) {
            // YOUR CODE HERE
            Board other = (Board) o;
            return this.hashCode() == other.hashCode(); 
        }
        return false;
    }

    @Override
    public int hashCode() {
         // YOUR CODE HERE
        int hash = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (pieces[i][j] != null) {
                    hash = hash + i + j + pieces[i][j].hashCode();
                }
            }
        }
        return hash;
    }
}
