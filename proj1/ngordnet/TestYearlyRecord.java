//Jin
package ngordnet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Collection;
import java.util.*;

public class TestYearlyRecord {

    @Test
    public void TestyearlyComprehensive() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95);
        yr.put("surrogate", 340);
        yr.put("merchantman", 181);
        System.out.println(yr.rank("surrogate")); // should print 1
        System.out.println(yr.rank("quayside")); // should print 3
        System.out.println(yr.size()); // should print 3

        Collection<String> words = yr.words();

        /*
         * The code below should print:
         * 
         * quayside appeared 95 times. merchantman appeared 181 times. surrogate
         * appeared 340 times.
         */
        for (String word : words) {
            System.out
                    .println(word + " appeared " + yr.count(word) + " times.");
        }

        /*
         * The code below should print the counts in ascending order:
         * 
         * 95 181 340
         */

        Collection<Number> counts = yr.counts();
        for (Number count : counts) {
            System.out.println(count);
        }

        HashMap<String, Integer> rawData = new HashMap<String, Integer>();
        rawData.put("berry", 1290);
        rawData.put("auscultating", 6);
        rawData.put("temporariness", 20);
        rawData.put("puppetry", 191);
        YearlyRecord yr2 = new YearlyRecord(rawData);
        System.out.println(yr2.rank("auscultating")); // should print 4
        assertEquals(4, yr2.rank("auscultating"));
        assertEquals(3, yr2.rank("temporariness"));

        yr2.put("asda", 100);
        System.out.println(" ");
        yr2.put("asdasd", 1000);
        assertEquals(6, yr2.rank("auscultating"));
        assertEquals(1, yr2.rank("berry"));
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestYearlyRecord.class);
    }

}
