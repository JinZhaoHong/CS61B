package ngordnet;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

public class YearlyRecord {
    //data structure
    private HashMap<String, Integer> yearlyrecord;

    //rank
    private TreeMap<String, Integer> rankMap;

    private boolean store;





    //Creates a new empty YearlyRecord
    public YearlyRecord() {
        yearlyrecord = new HashMap<String, Integer>();

    }


    //Creates a YearlyRecord using the given data.
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        yearlyrecord = new HashMap<String, Integer>();
        Set<String> key = otherCountMap.keySet();
        for (String s : key) {
            Integer val = otherCountMap.get(s);
            put(s, val);
        }
    }



    //Returns the number of times WORD appeared in this year.
    public int count(String word) {
        return (int) yearlyrecord.get(word);
    }


    //Returns all counts in ascending order of count.
    public Collection<Number> counts() {
        ArrayList<Integer> orderOfCount = new ArrayList<Integer>();
        Collection<Integer> counts = yearlyrecord.values();

        for (Integer i : counts) { 
            orderOfCount.add(i);
        }
        Collections.sort(orderOfCount);

        ArrayList<Number> returnArray = new ArrayList<Number>();
        for (Integer i : orderOfCount) {
            returnArray.add(i);
        }

        Collection<Number> returnCollection = returnArray;

        return returnCollection;

    }


    //Records that WORD occurred COUNT times in this year.
    public void put(String word, int count) {
        store = false;
        yearlyrecord.put(word, count);
        
    }


    //helper method
    private int calculateRank(String word) {
        //construct the rankMap
        ArrayList<Integer> orderOfCount = new ArrayList<Integer>();

        Collection<Integer> values = yearlyrecord.values();

        for (Integer i : values) { 
            orderOfCount.add(i);
        }

        Collections.sort(orderOfCount); //first sort the values of the yearly record 

        //debug
        /* for (Integer i : orderOfCount) {
            System.out.println(i);
        } */

        int rankCount = orderOfCount.size();
        int value = yearlyrecord.get(word);
        for (int i : orderOfCount) {
            if (i == value) {
                return rankCount;
            }
            rankCount -= 1;
        }
        return rankCount;
    }



    //Returns rank of WORD.
    public int rank(String word) {
        if (!store) { //inspiration from the Discussion worksheet.
            //rebuild rankMap everytime a new word gets put in;
            store = true;
            rankMap = new TreeMap<String, Integer>();
            Set<String> key = yearlyrecord.keySet();

            //build the rank map;
            for (String s : key) {
                int rank = calculateRank(s);
                rankMap.put(s, rank);
            }

        }
        return rankMap.get(word);
    }



    //Returns the number of words recorded this year.
    public int size() {
        int count = 0;
        Set<String> years = yearlyrecord.keySet();
        for (String s : years) {
            count += 1;
        }
        return count;



    }


    //Returns all words in ascending order of count.
    public Collection<String> words() {
        ArrayList<String> toBeSorted = new ArrayList<String>(); //store the String
        ArrayList<Integer> orderOfCount = new ArrayList<Integer>(); 

        Collection<Integer> counts = yearlyrecord.values(); //a collection of all values
        Set<String> years = yearlyrecord.keySet(); // a set of all keys

        for (Integer i : counts) { 
            orderOfCount.add(i);
        }
        Collections.sort(orderOfCount); //sort the orderOfCount in ascending order

        for (Integer i : orderOfCount) { //finr each string based on the ascending order 
            for (String s : years) {
                Integer value = yearlyrecord.get(s);
                if (i == value) {
                    toBeSorted.add(s);
                }
            }
        }

        Collection<String> returnCollection = toBeSorted;

        return returnCollection;
    }



}
