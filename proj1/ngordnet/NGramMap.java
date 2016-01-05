package ngordnet;

import java.util.TreeMap;
import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;
import java.util.TreeSet;
import edu.princeton.cs.introcs.In;



public class NGramMap {
    //data structure to store information from the wordsFilename,
    //eg: airport     2007    175702  32788
    //format will be key = "airport,2007"; value = 175702;
    private TreeMap<String, Integer> yearlyRecordDictionary;

    //data structure to store information from the countsFilename.
    //eg: 1505,32059,231,1
    //format will be key = 1505; value = 32059;
    private TreeMap<Integer, Long> timeSeriesDictionary;





    //Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
    public NGramMap(String wordsFilename, String countsFilename) {
        yearlyRecordDictionary = new  TreeMap<String, Integer>();
        timeSeriesDictionary = new TreeMap<Integer, Long>();
        In inWordsFile = new In(wordsFilename);
        In inCountsFile = new In(countsFilename);

        while (inWordsFile.hasNextLine()) { //read wordsFilename into wordsFile
            String line = inWordsFile.readLine(); 
            String[] words = line.split("\\s+");


            String keys = words[0] + "," + words[1];

            Integer value = Integer.parseInt(words[2]);


            yearlyRecordDictionary.put(keys, value);
        }

        while (inCountsFile.hasNextLine()) { //read countsFilename into countsFile
            String line = inCountsFile.readLine();
            String[] numbers = line.split(",");
            Integer key = Integer.parseInt(numbers[0]);
            Long value = Long.parseLong(numbers[1]);
            timeSeriesDictionary.put(key, value);
        }


    }


    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        String newYear = Integer.toString(year);
        String keys = word + "," + newYear;
        System.out.println(keys);
        if (!yearlyRecordDictionary.containsKey(keys)) {
            return 0;
        }
        if (yearlyRecordDictionary.get(keys) == null) {
            return 0;
        }
        return yearlyRecordDictionary.get(keys);


    }

    //Returns a defensive copy of the YearlyRecord of WORD.
    //System.out.println(ngm.countInYear("quantity", 1736)); 
    // should print 139
    //YearlyRecord yr = ngm.getRecord(1736);
    //System.out.println(yr.count("quantity")); 
    public YearlyRecord getRecord(int year) {
        Set<String> keys = yearlyRecordDictionary.keySet();
        YearlyRecord yr = new YearlyRecord();
        for (String s : keys) {
            String[] sList = s.split(",");
            int yeartag = Integer.parseInt(sList[1]);
            if (yeartag == year) {
                String keyForYr = sList[0];
                Integer valForYr = yearlyRecordDictionary.get(s);
                yr.put(keyForYr, valForYr);
            }
        }

        return yr;

    }

    //TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
    //System.out.println(countHistory.get(1736)); 
    // should print 139
    //Provides a defensive copy of the history of WORD.
    public TimeSeries<Integer> countHistory(String word) {
        Set<String> keys = yearlyRecordDictionary.keySet();
        TimeSeries<Integer> ts = new TimeSeries<Integer>();
        for (String s : keys) {
            String[] sList = s.split(",");
            String wordtag = sList[0];
            if (wordtag.equals(word)) {
                Integer keyForTs = Integer.parseInt(sList[1]);
                Integer valForTs = yearlyRecordDictionary.get(s);
                ts.put(keyForTs, valForTs);
            }
        }

        return ts;
    }


    //Provides the history of WORD between STARTYEAR and ENDYEAR.
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> original = countHistory(word);
        TimeSeries<Integer> ts = new TimeSeries<Integer>(original, startYear, endYear);
        return ts;

    }

    //Returns the total number of words recorded in all volumes.
    public TimeSeries<Long> totalCountHistory() {
        TimeSeries<Long> ts = new TimeSeries();
        Set<Integer> keys = timeSeriesDictionary.keySet();
        for (Integer k : keys) {
            Long value = timeSeriesDictionary.get(k);
            ts.put(k, value);
        }

        return ts;

    }


    //Provides the relative frequency of WORD.
    //(a.k.a. normalized count) of the given word for all time. 
    //For example there were 186,706 words across all volumes in 1575, 
    //and thus weightHistory will be countHistory / 186706.
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Integer> countH = countHistory(word);
        TimeSeries<Long> totalHistory = totalCountHistory();
        TimeSeries<Double> weightHistoryTs = new TimeSeries<Double>();

        Set<Integer> yearKeys = countH.keySet();

        for (Integer i : yearKeys) {
            Integer wordFrequency = countH.get(i);
            Long overallFrequency = totalHistory.get(i);
            Double division = ((double) wordFrequency) / ((double) overallFrequency);
            weightHistoryTs.put(i, division);

        }

        return weightHistoryTs;




    }


    //Provides the relative frequency of WORD between STARTYEAR and ENDYEAR.
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> countH = countHistory(word, startYear, endYear);
        TimeSeries<Long> totalHistory = totalCountHistory();
        TimeSeries<Double> weightHistoryTs = new TimeSeries<Double>();

        Set<Integer> yearKeys = countH.keySet();


        for (Integer i : yearKeys) {
            Integer wordFrequency = countH.get(i);
            Long overallFrequency = totalHistory.get(i);
            Double division = ((double) wordFrequency) / ((double) overallFrequency);
            weightHistoryTs.put(i, division);

        }


        return weightHistoryTs;



    }

    //Returns the summed relative frequency of all WORDS.
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> summedweightHistoryTs = new TimeSeries<Double>();
        ArrayList<TimeSeries> al = new ArrayList<TimeSeries>(); 

        for (String s : words) { //put the weightedhistory of each word into the arraylist
            TimeSeries<Double> wh = weightHistory(s);
            al.add(wh);
        }

        Set<Integer> totalKeySet = new TreeSet(); // the key set for the summedWeightHistory
        for (TimeSeries ts : al) {
            Set<Integer> subKeySet = ts.keySet(); //the key set for each words
            for (Integer i : subKeySet) {
                totalKeySet.add(i);
            }
        } //finish constructing the totalkeyset

        for (Integer k : totalKeySet) { //put key and value into the summedWeightHistory
            double sum = 0.0;
            for (TimeSeries ts: al) {
                if (ts.containsKey(k)) { //test whether a word has a frequency in a specific year
                    sum += (double) ts.get(k); //add up the frequencies.
                }
            }
            summedweightHistoryTs.put(k, sum);

        }

        return summedweightHistoryTs;



    }


    //Provides the summed relative frequency of all WORDS between STARTYEAR and ENDYEAR.
    public TimeSeries<Double> summedWeightHistory(Collection<String> words,
        int startYear, int endYear) {
        TimeSeries<Double> summedweightHistoryTs = new TimeSeries<Double>();
        ArrayList<TimeSeries> al = new ArrayList<TimeSeries>(); 

        for (String s : words) { //put the weightedhistory of each word into the arraylist
            TimeSeries<Double> wh = weightHistory(s, startYear, endYear);
            al.add(wh);
        }

        Set<Integer> totalKeySet = new TreeSet(); // the key set for the summedWeightHistory
        for (TimeSeries ts : al) {
            Set<Integer> subKeySet = ts.keySet(); //the key set for each words
            for (Integer i : subKeySet) {
                totalKeySet.add(i);
            }
        } //finish constructing the totalkeyset

        for (Integer k : totalKeySet) { //put key and value into the summedWeightHistory
            double sum = 0.0;
            for (TimeSeries ts: al) {
                if (ts.containsKey(k)) { //test whether a word has a frequency in a specific year
                    sum += (double) ts.get(k); //add up the frequencies.
                }
            }
            summedweightHistoryTs.put(k, sum);

        }

        return summedweightHistoryTs;

    }

    //Provides processed history of all words between STARTYEAR and ENDYEAR as processed by YRP.
    public synchronized TimeSeries<Double> processedHistory(int startYear, 
        int endYear, YearlyRecordProcessor yrp) {
        TimeSeries<Double> processedhistory = new TimeSeries<Double>();
        Set<Integer> keys = timeSeriesDictionary.keySet();

        for (Integer i : keys) {
            if (i >= startYear && i <= endYear) {
                YearlyRecord yr = getRecord(i);
                double value = yrp.process(yr);
                processedhistory.put(i, value);
            }            
        }

        return processedhistory;
    }

    //Provides processed history of all words ever as processed by YRP.
    public synchronized TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> processedhistory1 = new TimeSeries<Double>();
        Set<Integer> keys = timeSeriesDictionary.keySet();

        for (Integer i : keys) {
            YearlyRecord yr = getRecord(i);
            double value = yrp.process(yr);
            processedhistory1.put(i, value);
        }

        return processedhistory1; 
    } 


}
