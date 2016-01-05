package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

/** An implementation of a fierce blue killer.
 *  @author Josh Hug
 */
public class Clorus extends Creature {
    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;



    /** creates Clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** creates a Clorus with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    /** always return r = 34, g = 0, b = 231
     */
    public Color color() {
        r = 34;
        b = 231;
        g = 0;
        return color(r, g, b);
    }

    /** gain the creature's energy */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /** Clorus should lose 0.3 units of energy when moving. If you want to
     *  to avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
        this.energy -= 0.03;
    }


    /** Clorus gain 0.2 energy when staying due to photosynthesis. */
    public void stay() {
        this.energy -= 0.01;
    }

    /** Clorus and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Clorus.
     */
    public Clorus replicate() {
        Clorus c = new Clorus(energy/2);
        energy = energy/2;
        return c;
    }

    

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        //if there are not empty spaces, the Clorus should stay.
        if (empties.size() == 0){
            return new Action(Action.ActionType.STAY);
        }
        //if any plips are seen, the Clorus will ATTACK one of them randomly.
        if (plips.size() > 0){
            Direction moveDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, moveDir);
        }
        // If the Clorus has energy greater than or equal to one, it will replicate to a random empty square
        if (energy >= 1){
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, moveDir);
        }

        Direction moveDir = HugLifeUtils.randomEntry(empties);
        return new Action(Action.ActionType.MOVE, moveDir);
    }


        
    }