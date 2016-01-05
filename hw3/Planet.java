

public class Planet { 
    public double x; /** The Planet's current loaction at x axis    */
    public double y; /**  The Planet's current location at y axis   */   
    public double xVelocity;  /** The Planet's current velocity at x axis*/
    public double yVelocity;  
    public double mass;
    public double xNetForce;
    public double yNetForce;
    public double xAccel;
    public double yAccel;  
    public String img; 
    public double G = 6.67e-11;
    private double radius;
    public Planet (double x1, double y1, double xVelocity1, double yVelocity1, double mass1, double radius1, String img1){
        x = x1;
        y = y1;
        xVelocity = xVelocity1;
        yVelocity = yVelocity1;
        mass = mass1;
        img = img1;
        radius = radius1;

    }

    public double calcDistance (Planet p) {
        double distance = Math.sqrt((x - p.x)*(x - p.x) + (y - p.y)*(y - p.y)); 
        return distance;
    }

    public double calcPairwiseForce(Planet p){
        double r = this.calcDistance(p);
        double force = (G * mass * p.mass)/(r * r);
        return force;
            
    }

    public double calcPairwiseForceX(Planet p){
        double force = this.calcPairwiseForce(p);
        double r = this.calcDistance(p);
        double dx = p.x - x;
        double forceX = (force * dx) / r;
        return forceX;

    }

    public double calcPairwiseForceY(Planet p){
        double force = this.calcPairwiseForce(p);
        double r = this.calcDistance(p);
        double dy = p.y - y;
        double forceY = (force * dy) / r;
        return forceY;

    }

    public void setNetForce(Planet [] arrayP){
        for (Planet p : arrayP){
            if (this != p) {
                double forceX = this.calcPairwiseForceX(p);
                double forceY = this.calcPairwiseForceY(p);
                xNetForce = xNetForce + forceX;
                yNetForce = yNetForce + forceY;
            }
        }
    }

    public void draw (){
        StdDraw.picture(x,y, "images/" + img);
    }



    public void update(double dt){
        xAccel = xNetForce/mass;
        yAccel = yNetForce/mass;
        xVelocity = xVelocity + dt*xAccel;
        yVelocity = yVelocity + dt*yAccel;
        x = x + dt*xVelocity;
        y = y + dt*yVelocity;
        xNetForce = 0;
        yNetForce = 0;
    }

    public double getMass(){
        return mass;
    }

    public double getRadius(){
        return radius;
    }





}