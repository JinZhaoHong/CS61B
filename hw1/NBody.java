class NBody{
	
	public static void main (String args[]){
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		In in = new In(filename);
		int numOfPlanets = in.readInt();
		double radiusOfUniverse = in.readDouble();

		Planet [] pList = new Planet[numOfPlanets];

		for (int i = 0; i < numOfPlanets; i++){
			Planet newPlanet = NBody.getPlanet(in);
			pList[i] = newPlanet;
		}

		StdDraw.setScale(-radiusOfUniverse, radiusOfUniverse);
		StdDraw.picture(0.0, 0.0, "images/starfield.jpg");

		for (Planet p : pList){
			StdDraw.picture(p.x, p.y, "images/" + p.img);
		}

		double time = 0;
		while (time<=T){
			for (Planet p : pList){
				p.setNetForce(pList);
			}
			for (Planet p : pList){
				p.update(dt);
			}
			StdDraw.picture(0.0, 0.0, "images/starfield.jpg");
			for (Planet p : pList){
			StdDraw.picture(p.x, p.y, "images/" + p.img);
			}
			StdDraw.show(10);
			time += dt;
		}
		StdAudio.play("audio/2001.mid");

		StdOut.printf("%d\n", numOfPlanets);
		StdOut.printf("%.2e\n", radiusOfUniverse);
		for (int i = 0; i < numOfPlanets; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                   pList[i].x, pList[i].y, pList[i].xVelocity, pList[i].yVelocity, pList[i].mass, pList[i].img);
		}

	}

	public static Planet getPlanet (In in){
		double x1 = in.readDouble();
		double y1 = in.readDouble();
		double xVelocity1 = in.readDouble();
		double yVelocity1 = in.readDouble();
		double mass1 = in.readDouble();
		String img1 = in.readString();

		Planet p = new Planet(x1, y1, xVelocity1, yVelocity1, mass1, img1);
		
		return p;

	}
}