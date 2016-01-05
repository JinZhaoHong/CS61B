package ngordnet;
import java.util.TreeMap;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Set;
import java.util.ArrayList;
//JinZhaoHong

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    //Constructs a new empty TimeSeries.
    public TimeSeries() {
        super();
    }


    //Creates a copy of TS.
    public TimeSeries(TimeSeries<T> ts) {
        Set<Integer> keys = ts.keySet();
        for (Integer i : keys) {
            T value = ts.get(i);
            this.put(i, value);
        }
    }


    //Creates a copy of TS, but only between STARTYEAR and ENDYEAR, 
    //inclusive of both end points.
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {        
        Set<Integer> newKeys = ts.keySet(); //copy ts to this
        for (Integer i : newKeys) {
            if (i >= startYear && i <= endYear) {
                T value = ts.get(i);
                this.put(i, value);
            }
        } 
    }




    //Returns all data for this time series (in any order).
    public Collection<Number> data() {
        //return values(); //Returns a Collection view of the values contained in this map.
        Collection<Number> returnCollection = new ArrayList<Number>();
        Collection<Number> yearCollection = years();
        for (Number n : yearCollection) {
            Number value = get(n);
            returnCollection.add(value);
        }
        return returnCollection;
    }


    //Returns the quotient of this time series divided by the relevant value in ts.
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> returnTs = new TimeSeries();
        Set<Integer> keySet1 = this.keySet(); //set of all keys in this
        for (Integer i : keySet1) {
            if (!ts.containsKey(i)) {
                throw new IllegalArgumentException(" ts does not include all years of this ");
            }
            Number value1 = this.get(i);
            Number value2 = ts.get(i);
            Double finalValue = value1.doubleValue() / value2.doubleValue();
            returnTs.put(i, finalValue); 
        }
        return returnTs;

    }



    //Returns the sum of this time series with the given ts.
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> returnTs = new TimeSeries();
        Set<Integer> keySet1 = this.keySet(); //set of all keys in this
        Set<Integer> keySet2 = ts.keySet(); // set of all keys in ts


        for (Integer key : keySet1) { // put the rest of the keys into the new ts
            Number value =  this.get(key);
            returnTs.put(key, value.doubleValue());
        }

        for (Integer key : keySet2) {
            Number value =  ts.get(key);
            returnTs.put(key, value.doubleValue());
        }


        for (Integer key : keySet2) { //add all keys shared by both keyset
            if (this.containsKey(key)) {
                Number value1 =  this.get(key); //get the value of the corresponding key
                Number value2 =  ts.get(key);
                double finalValue =  value1.doubleValue() +  value2.doubleValue();
                returnTs.put(key, finalValue); 
            }
        }

        return returnTs;


    }

    //Returns all years for this time series (in any order).
    public Collection<Number> years() {
        //return keySet(); //Returns a Set view of the keys contained in this map.
        Collection<Number> returnCollection = new TreeSet();
        Set<Integer> yearSet = keySet();
        for (Integer i : yearSet) {
            returnCollection.add(i);
        }
        return returnCollection;
    }




}
