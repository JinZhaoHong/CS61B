
package ngordnet;

import java.util.Collection;


public class WordLengthProcessor implements YearlyRecordProcessor {
    
    //Returns some feature of a YearlyRecord as a double.
    public double process(YearlyRecord yearlyRecord) {
        double quotient = 0.0;
        long totalCount = 0;
        long totalLength = 0;

        Collection<String> names = yearlyRecord.words();
        Collection<Number> frequencies = yearlyRecord.counts();

        for (String s : names) {
            long l = s.length();
            long c = yearlyRecord.count(s);
            totalLength += (l * c);
            totalCount += c;
        }

        quotient = (double) totalLength / (double) totalCount;
        
        return quotient;

    }
}
