import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest{


	@Test
	public void boardTestingPlace() {
		Board b = new Board(false);
		Piece test1 = b.pieceAt(0, 0);
		Piece test2 = b.pieceAt(0, 1);
		Piece test3 = b.pieceAt(5, 1);

		assertEquals(false, (test1.isShield() && test1.isBomb()));
		assertEquals(null, test2);
		assertEquals(true, test3.isShield());
		assertEquals(true, test3.isFire());
		
		Piece removed = b.remove(0, 0);
		Piece mystery = b.pieceAt(0 ,0);
		assertEquals(false, (removed.isShield() && removed.isBomb()));
		assertEquals(true, removed.isFire());
		assertEquals(null, mystery);



		Piece removed1 = b.remove(1 , 1);
		assertEquals(true, removed1.isShield());
		assertEquals(null, b.pieceAt(1, 1));
	
	}


	@Test
	public void boardTestingRemove(){
		Board b = new Board(false);

		Piece test1 = b.pieceAt(0,0);
		assertEquals(true, test1.isFire());
		b.remove(0,0);
		assertEquals(null, b.pieceAt(0,0));
	}

	@Test
	public void boardTestingExplosion(){
		Board b = new Board(false);

		Piece bombfire1 = b.pieceAt(4, 2);
		Piece bombwater1 = b.pieceAt(1, 5);

		b.place(bombwater1, 3, 3);
		System.out.println("piece at 3, 3: " + b.pieceAt(3, 3));
		assertEquals(null, b.pieceAt(1, 5));
		b.select(4, 2);

		b.select(2, 4);
		System.out.println(b.pieceAt(3, 3));


		assertEquals(null, b.pieceAt(2, 3));
		assertEquals(null, b.pieceAt(3, 3));
		assertEquals(null, b.pieceAt(3, 5));

	}




	@Test
	public void boardTestingComprehemsive (){
		Board b = new Board(false);
		Piece test1 = b.pieceAt(0,2);
		System.out.println(test1);
		assertEquals(false, b.canEndTurn());

		//* player 0 */
		assertEquals(true, b.canSelect(0 , 2));
		
		b.select(0,2);

		assertEquals(true, test1.isBomb());
		assertEquals(true, b.canSelect(1, 3));
		
		b.select(1, 3);
		System.out.println("Player 0 just moved");
		Piece mystery = b.pieceAt(1, 3);

		System.out.println(test1);
		System.out.println(mystery);
		assertEquals(test1, mystery);


		assertEquals(true, b.canEndTurn());
		b.endTurn();

		//* player 1//
		assertEquals(true, b.canSelect(1, 5));
		Piece test2 = b.pieceAt(1, 5);
		assertEquals(false, b.canEndTurn());
		assertEquals(true, test2.isBomb());
		b.select(1, 5);

		assertEquals(true, b.canSelect(0, 4));

		b.select(0, 4);
		System.out.println("Player 1 just moved");

		Piece mystery2 = b.pieceAt(0, 4);
		assertEquals(mystery2, test2);


		assertEquals(true, b.canEndTurn());
		b.endTurn();

		//play0 again. yo!!!//

		Piece test3 = b.pieceAt(1, 3);
		assertEquals(test3, test1);
		assertEquals(true, test3.isBomb());
		assertEquals(true, b.canSelect(1, 3));
		Piece test4 = b.pieceAt(2, 2);
		b.select(2, 2);
		b.select(3, 3);

		b.endTurn();

		// player 1 again . ha!!!//

		test2.move(2, 2);

		b.endTurn();


	}


	@Test
	public void gameSimulation1(){
		Board b = new Board(false);
		Piece p1 = b.pieceAt(2, 2);
		b.place(p1, 3, 3);
		Piece p2 = b.pieceAt(3, 3);
		assertEquals(p1, p2);

	}


	@Test 
	public void gameTestMulticapture(){
		Board b = new Board(true);

		Piece p1 = new Piece(true, b, 2, 2, "pawn");
		Piece p2 = new Piece(false, b, 3, 3, "pawn");
		Piece p3 = new Piece(false, b, 3, 5, "pawn");

		b.place(p1, 2, 2);
		b.place(p2, 3, 3);
		b.place(p3, 3, 5);

		assertEquals(true, b.canSelect(2, 2));
		b.select(2, 2);

		assertEquals(true, b.canSelect(4, 4));
		b.select(4, 4);


		assertEquals(null, b.pieceAt(3, 3));
		assertEquals(p1, b.pieceAt(4, 4));

		assertEquals(true, b.canSelect(2, 6));
		b.select(2, 6);

		assertEquals(null, b.pieceAt(3, 5));
		assertEquals(p1, b.pieceAt(2, 6));

	}


	@Test
	public void gameTestEndTurn(){
		Board b = new Board(false);
		assertEquals(true, b.canSelect(0, 0));
		assertEquals(true, b.canSelect(2, 2));
		assertEquals(false, b.canSelect(3, 3));
		assertEquals(false, b.canEndTurn());

		Piece bomb = b.pieceAt(2, 2);
		b.select(2, 2);
		assertEquals(false, b.canEndTurn());

		b.select(3,3);
		assertEquals(true, b.canEndTurn());
		assertEquals(false, b.canSelect(0, 0));
		assertEquals(false, b.canSelect(7,7));
		
		b.endTurn();

		Piece bombWater = b.pieceAt(3, 5);
		assertEquals(false, b.canEndTurn());
		b.select(3, 5);
		assertEquals(false, b.canEndTurn());

		b.select(4, 4);
		assertEquals(true, b.canEndTurn());


	}

	@Test
	public void gameTestKing(){
		Board b = new Board(true);

		Piece a = new Piece(true, b, 2, 6, "shield" );
		Piece c = new Piece(false, b, 5, 1, "shield");
		Piece d = new Piece(true, b, 4, 6, "bomb");
		Piece e = new Piece(false, b, 1, 1, "bomb");

		b.place(a, 2, 6);
		b.place(c, 5, 1);
		b.place(d, 4, 6);
		b.place(e, 1, 1);

		assertEquals(false, a.isKing());
		assertEquals(false, c.isKing());
		assertEquals(false, d.isKing());
		assertEquals(false, e.isKing());

		assertEquals(true, b.canSelect(2, 6));
		b.select(2, 6);
		b.select(1, 7);

		assertEquals(true, a.isKing());
		assertEquals(false, c.isKing());
		assertEquals(false, d.isKing());
		assertEquals(false, e.isKing());

		assertEquals(true, b.canEndTurn());
		b.endTurn();



		assertEquals(true, a.isKing());
		assertEquals(true, b.canSelect(5, 1));
		b.select(5, 1);
		assertEquals(true, b.canSelect(6, 0));
		b.select(6, 0);

		assertEquals(true, a.isKing());
		assertEquals(true, c.isKing());
		assertEquals(true, b.canEndTurn());
		b.endTurn();

		assertEquals(true, a.isKing());
		assertEquals(true, c.isKing());
		assertEquals(true, b.canSelect(1, 7));
		b.select(1,7);
		assertEquals(true, b.canSelect(2, 6));
		b.select(2, 6);
		assertEquals(true, a.isKing());
		assertEquals(true, c.isKing());



	}



	@Test
	public void gameTestMulticapture2(){
		Board b = new Board(true);

		Piece a = new Piece(true, b, 5, 1, "shield" );
		Piece c = new Piece(false, b, 4, 2, "shield");
		Piece d = new Piece(false, b, 4, 4, "bomb");
		Piece e = new Piece(false, b, 4, 6, "bomb");

		b.place(a, 5, 1);
		b.place(c, 4, 2);
		b.place(d, 4, 4);
		b.place(e, 4, 6);





		assertEquals(false, b.canEndTurn());
		assertEquals(true, b.canSelect(5, 1));

		b.select(5, 1);


		assertEquals(true, b.canSelect(3, 3));
		b.select(3, 3);


		assertEquals(true, b.canEndTurn());

		assertEquals(true, b.canSelect(5, 5));
		b.select(5, 5);


		assertEquals(true, b.canEndTurn());

		assertEquals(true, b.canSelect(3, 7));
		b.select(3, 7);


		assertEquals(true, b.canEndTurn());
		b.endTurn();


	}









	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(BoardTest.class);
    }


}
