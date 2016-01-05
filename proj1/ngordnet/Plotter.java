package ngordnet;

import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.ChartBuilder;

public class Plotter {


    //Makes a plot showing overlaid individual normalized count 
    //for every word in WORDS from STARTYEAR to ENDYEAR using NGM as a data source.
    public static void plotAllWords(NGramMap ngm, String[] words, 
        int startYear, int endYear) {
        String ylabel = "weight";
        String xlabel = "Years";
        int w = 1000;
        int h = 1000;
        Chart chart = new ChartBuilder().width
            (w).height(h).xAxisTitle(xlabel).yAxisTitle(ylabel).build();

        for (String s : words) {
            ArrayList<Number> xValues = new ArrayList<Number>();
            ArrayList<Number> yValues = new ArrayList<Number>();

            //find the weighted history for each of the word.
            TimeSeries<Double> weightHistory = ngm.weightHistory(s, startYear, endYear);

            Set<Integer> keys = weightHistory.keySet();
            Collection<Double> values = weightHistory.values();

            for (Integer i : keys) {
                xValues.add(i);
            }

            for (Double d : values) {
                yValues.add(d);
            }

            String title = "weight for word: " + s;
            String legend = s;

            chart.addSeries(legend, xValues, yValues);


        
        }

        // Show it
        new SwingWrapper(chart).displayChart();


    }



    //Creates overlaid category weight plots for each category label 
    //in CATEGORYLABELS from STARTYEAR to ENDYEAR using NGM and WN as data sources.
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, 
        String[] categoryLabels, int startYear, int endYear) {

        String ylabel = "weight";
        String xlabel = "Years";
        int w = 1000;
        int h = 1000;
        Chart chart = new ChartBuilder().width
            (w).height(h).xAxisTitle(xlabel).yAxisTitle(ylabel).build();

        for (String s : categoryLabels) {


            Set<String> hyponym = wn.hyponyms(s);

            for (String s1 : hyponym) {

                ArrayList<Number> xValues = new ArrayList<Number>();
                ArrayList<Number> yValues = new ArrayList<Number>();

                //find the weighted history for each of the word.
                TimeSeries<Double> weightHistory = ngm.weightHistory(s, startYear, endYear);

                Set<Integer> keys = weightHistory.keySet();
                Collection<Double> values = weightHistory.values();

                for (Integer i : keys) {
                    xValues.add(i);
                }

                for (Double d : values) {
                    yValues.add(d);
        
                }

                String legend = s;

                chart.addSeries(legend, xValues, yValues);
            }
        }


        // Show it
        new SwingWrapper(chart).displayChart();


    } 



    //Creates a plot of the total normalized count of every word that is a hyponym of 
    //CATEGORYLABEL from STARTYEAR to ENDYEAR using NGM and WN as data sources.
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, String categoryLabel, 
        int startYear, int endYear) {
        String ylabel = "weights";
        String xlabel = "Years";
        int w = 1000;
        int h = 1000;
        Chart chart = new ChartBuilder().width
            (w).height(h).xAxisTitle(xlabel).yAxisTitle(ylabel).build();
        Set<String> hyponym = wn.hyponyms(categoryLabel);

        for (String s : hyponym) {
            ArrayList<Number> xValues = new ArrayList<Number>();
            ArrayList<Number> yValues = new ArrayList<Number>();

            //find the weighted history for each of the word.
            TimeSeries<Double> weightHistory = ngm.weightHistory(s, startYear, endYear);

            Set<Integer> keys = weightHistory.keySet();
            Collection<Double> values = weightHistory.values();

            for (Integer i : keys) {
                xValues.add(i);
            }

            for (Double d : values) {
                yValues.add(d);
            }       


            String title = "weight for word: " + s;
            String legend = s;

            chart.addSeries(legend, xValues, yValues);


        
        }

    // Show it
        new SwingWrapper(chart).displayChart();




    }


    //Creates a plot of the absolute word counts for WORD from STARTYEAR to ENDYEAR, 
    //using NGM as a data source.
    public static void plotCountHistory(NGramMap ngm, String word, 
        int startYear, int endYear) {

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        TimeSeries<Integer> history  = ngm.countHistory(word, startYear, endYear);


        Set<Integer> keys = history.keySet();
        Collection<Integer> value  = history.values();


        for (Integer i : keys) {
            xValues.add(i);
        }

        for (Number n : value) {
            yValues.add(n);
        }

        String title = "plotCountHistory";
        String ylabel = "counts";
        String xlabel = "year";
        String legend = word;

        // Create Chart

        Chart chart = QuickChart.getChart(title, ylabel, xlabel, legend, xValues, yValues);
     
        // Show it
        new SwingWrapper(chart).displayChart();


    }

    //Creates a plot of the TimeSeries TS.
    public static void plotTS(TimeSeries<? extends Number> ts, String title, 
        String xlabel,  String ylabel, String legend) {

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        Set<Integer> keys = ts.keySet();
        Collection<? extends Number> value  = ts.values();

        for (Integer i : keys) {
            xValues.add(i);
        }

        for (Number n : value) {
            yValues.add(n);
        }

        // Create Chart

        Chart chart = QuickChart.getChart(title, ylabel, xlabel, legend, xValues, yValues);
     
            // Show it
        new SwingWrapper(chart).displayChart();


    }  

    //Creates a plot of the normalized weight counts for WORD from STARTYEAR to ENDYEAR,
    // using NGM as a data source.
    public static void plotWeightHistory(NGramMap ngm, String word, int startYear, 
        int endYear) {

        System.out.println(ngm);
        System.out.println(word);
        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        TimeSeries<Double> history  = ngm.weightHistory(word, startYear, endYear);


        Set<Integer> keys = history.keySet();
        System.out.println(keys.size());
        Collection<Double> value  = history.values();
        System.out.println(value.size());


        for (Integer i : keys) {

            xValues.add(i);
        }

        for (Number n : value) {

            yValues.add(n);
        }

        String title = "plotWeightHistory";
        String ylabel = "weights";
        String xlabel = "year";
        String legend = word;
 
        // Create Chart



        Chart chart = QuickChart.getChart(title, xlabel, ylabel, legend, xValues, yValues);
     
        // Show it
        new SwingWrapper(chart).displayChart();


    } 

        //Plots the normalized count of every word against the rank of every word on a log-log plot.
    public static void plotZipfsLaw(NGramMap ngm, int year) {
        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        YearlyRecord yr = ngm.getRecord(year);
        TimeSeries<Long> totalHistory = ngm.totalCountHistory();

        Collection<String> words = yr.words();

        for (String s : words) {
            int rk = yr.rank(s);
            xValues.add(rk);
            TimeSeries<Integer> countH = ngm.countHistory(s);
            Integer wordFrequency = countH.get(year);
            Long overallFrequency = totalHistory.get(year);
            Double division = ((double) wordFrequency) / ((double) overallFrequency);
            yValues.add(division);
        }
                
        String title = "plotZipfsLaw";
        String xlabel = "count";
        String ylabel = "weight";
        String legend = "weight/count";


        // Create Chart
        int w = 1000;
        int h = 1000;
        Chart chart = new ChartBuilder().width
            (w).height(h).xAxisTitle(xlabel).yAxisTitle(ylabel).build();
        chart.getStyleManager().setYAxisLogarithmic(true);
        chart.getStyleManager().setXAxisLogarithmic(true);
        chart.addSeries(legend, xValues, yValues);
     
        // Show it
        new SwingWrapper(chart).displayChart();

    }


}
