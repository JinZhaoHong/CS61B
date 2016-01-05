package ngordnet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Collection;
import java.util.*;

public class TestNGramMap{

	@Test
	public void testNGComprehensive(){
		NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", 
                                    "./ngrams/total_counts.csv");


        System.out.println(ngm.countInYear("quantity", 1736)); // should print 139

        assertEquals(139, ngm.countInYear("quantity", 1736));

        YearlyRecord yr = ngm.getRecord(1736);
        System.out.println(yr.count("quantity")); // should print 139

        assertEquals(139, yr.count("quantity"));

        TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
        System.out.println(countHistory.get(1736)); // should print 139

        assertEquals(139, countHistory.get(1736), 0.01);

        TimeSeries<Long> totalCountHistory = ngm.totalCountHistory();
        System.out.println(totalCountHistory.get(1736)); // should print 8049773

        assertEquals(8049773, totalCountHistory.get(1736), 0.01);
        
        TimeSeries<Double> weightHistory = ngm.weightHistory("quantity");
        System.out.println(weightHistory.get(1736));  // should print roughly 1.7267E-5

        assertEquals(1.7267E-5, weightHistory.get(1736), 0.000001);
    
        System.out.println((double) countHistory.get(1736) 
                           / (double) totalCountHistory.get(1736)); 

        ArrayList<String> words = new ArrayList<String>();
        words.add("quantity");
        words.add("quality");        

        TimeSeries<Double> sum = ngm.summedWeightHistory(words);
        System.out.println(sum.get(1736)); // should print roughly 3.875E-5

        assertEquals(3.875E-5, sum.get(1736), 0.000001);
	}


	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestNGramMap.class);
    }
}