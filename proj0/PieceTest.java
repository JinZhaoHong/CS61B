import static org.junit.Assert.*;

import org.junit.Test;

public class PieceTest{
	@Test
	public void pieceTest(){
		Board b = new Board(true);
		Piece newPiece = new Piece(true, b, 1, 2, "shield");
		assertEquals(true, newPiece.isFire());
		assertEquals(1, newPiece.side());
		assertEquals(false, newPiece.isKing());
		assertEquals(false, newPiece.isBomb());
		assertEquals(true, newPiece.isShield());
		assertEquals(false, newPiece.hasCaptured());
	}

	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(PieceTest.class);
    }

}