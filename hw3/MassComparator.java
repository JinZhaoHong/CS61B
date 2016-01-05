import java.util.Comparator;

/**
 * MassComparator.java
 */

public class MassComparator implements Comparator<Planet> {

    public MassComparator() {
    }

    /** Returns the difference in mass as an int.
     *  Round after calculating the difference. */
    public int compare(Planet planet1, Planet planet2) {
        // REPLACE THIS LINE WITH YOUR SOLUTION
        int massPlanet1 = (int) planet1.getMass();
        int massPlanet2 = (int) planet2.getMass();
        return massPlanet2 - massPlanet1;
    }
}


