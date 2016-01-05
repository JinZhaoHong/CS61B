// zhaohong Jin//
public class Piece{
	
	private boolean isFire;
	private boolean isKing = false;
	private boolean hasCaptured = false;
	private Board b;
	private int x;
	private int y;
	private String type;

	public Piece (boolean isFire, Board b, int x, int y, String type){
		/* Constructor for a piece. isFire determines whether the piece is a fire or water piece. 
		b represents the Board that the piece is on (this will be only be used for the move() method). 
		(x, y) is the position to initialize it at. type is a string representing the type of the Piece. 
		It should be either "pawn", "bomb", or "shield". 
		(Don’t worry about bounds checking). The new piece should not be a king.
		*/
		this.isFire = isFire;
		this.b = b;
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public boolean isFire(){
		/* Returns whether or not the piece is a fire piece. */
		return this.isFire;
	}

	public int side(){
		/* Returns 0 if the piece is a fire piece, or 1 if the piece is a water piece. */
		if (this.isFire == true){
			return 1;
		}
		return 0;
	}

	public boolean isKing(){
		/* Returns whether or not the piece has been crowned.*/

		return this.isKing;

	}

	public boolean isBomb(){
		/* Returns whether or not the piece is a Bomb Piece. */
		if (this.type == "bomb"){
			return true;
		}
		return false;
	}

	public boolean isShield(){
		/* Returns whether or not the piece is a Shield Piece. */
		if (this.type == "shield"){
			return true;
		}
		return false;
	}


	private void explode(int x, int y){
		//kill anything that is not a shield during the explode.//
		//check the four corner around the bomb piece. If there is a 
		//piece that is not a shield, then kill it. 
		int x1 = x + 1;
		int y1 = y + 1;
		Piece p1 = b.pieceAt(x1, y1);
		if ((p1 != null) && (p1.type != "shield")){
			p1.x = x1;
			p1.y = y1;
			Piece p11 = b.remove(x1, y1);
		}


		int x2 = x - 1;
		int y2 = y + 1;
		Piece p2 = b.pieceAt(x2, y2);
		if ((p2 != null) && (p2.type != "shield")){
			p2.x = x2;
			p2.y = y2;
			Piece p22 = b.remove(x2, y2);
		}

		int x3 = x - 1;
		int y3 = y - 1;
		Piece p3 = b.pieceAt(x3, y3);
		if ((p3 != null) && (p3.type != "shield")){
			p3.x = x3;
			p3.y = y3;
			Piece p33 = b.remove(x3, y3);
		}

		int x4 = x + 1;
		int y4 = y - 1;
		Piece p4 = b.pieceAt(x4, y4);
		if ((p4 != null) && (p4.type != "shield")){
			p4.x = x4;
			p4.y = y4;
			Piece p44 = b.remove(x4, y4);
		}
	}

	public void move(int x, int y){
		/* Assumes this Piece's movement from its current position to (x, y) is valid.
		 Moves the piece to (x, y), capturing any intermediate piece if applicable. 
		 This will be a difficult method to write.*/
		int dx =(int) (0.5 * (x - this.x));
		int dy =(int) (0.5 * (y - this.y));



		if ((Math.abs(x - this.x)==2) && (Math.abs(y - this.y)==2) && (hasCaptured==false)){
			// this is a capture movemwnt
					Piece p = b.pieceAt(this.x + dx, this.y + dy);
					if (p != null){
						p.x = this.x + dx;
						p.y = this.y + dy;
						Piece removed1 = b.remove(this.x + dx, this.y + dy);
						//kill the piece that is on the way//
						b.place(this, x, y);
						this.x = x;
						this.y = y;
						this.hasCaptured = true;
						//b.hasMoved = true;
						if (this.type == "bomb"){
							//any special effect for the bomb//
							this.explode(x, y);
							Piece removed2 = b.remove(x, y);
							return;
						}
						if (isFire() && (this.y==7)){
							this.isKing=true;
						}

						if((isFire()!= true) && (this.y==0)){
							this.isKing=true;
						}
						return;
					}
				}
				
		b.place(this, x, y);
		this.x = x;
		this.y = y;
		if (isFire() && (this.y==7)){
			this.isKing=true;
		}

		if((isFire()!= true) && (this.y==0)){
			this.isKing=true;

		}
		//b.hasMoved = true;
	}

	public boolean hasCaptured(){
		/*Returns whether or not this Piece has captured another piece this turn.*/
		return this.hasCaptured;
	}

	public void doneCapturing(){
	/* Called at the end of each turn on the Piece that moved. 
	Makes sure the piece's hasCaptured() value returns to false.*/
		this.hasCaptured = false;
	}

	







}