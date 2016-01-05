import java.util.Comparator;

public class RadiusComparator implements Comparator<Planet> {
	public RadiusComparator(){

	}

	public int compare(Planet a, Planet b) {
		int radiusA = (int) a.getRadius();
		int radiusB = (int) b.getRadius();

		return radiusB-radiusA;
	}

}