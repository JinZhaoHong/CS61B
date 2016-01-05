/* Starter code for NgordnetUI (part 7 of the project). Rename this file and 
   remove this comment. */

package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;
import java.util.Set;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Zhaohong Jin
 */
public class NgordnetUI {
    private static int startDate;
    private static int endDate;
    private static NGramMap ngm;
    private static WordNet wn;
    public static void main(String[] args) { 
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");
        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        ngm = new NGramMap(wordFile, countFile);
        wn = new WordNet(synsetFile, hyponymFile);
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile 
                           + ", and " + hyponymFile + ".");
        System.out.println("\nFor tips on implementing NgordnetUI, see ExampleUI.java.");
        while (true) {
            System.out.print(">>> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            switch (command) {
                case "quit": 
                    return;
                case "help":
                    In in1 = new In("help.txt");
                    String helpStr = in1.readAll();
                    System.out.println(helpStr);
                    break;  
                case "range":
                    startDate = Integer.parseInt(tokens[0]); 
                    endDate = Integer.parseInt(tokens[1]);
                    break;
                case "count":
                    String word = tokens[0];
                    try {
                        int year = Integer.parseInt(tokens[1]);
                        System.out.println(ngm.countInYear(word, year));
                    } catch (NumberFormatException ex) { 
                        System.err.println("Illegal input");
                    }
                    break;
                case "hyponyms":
                    String word1 = tokens[0];
                    String returnString = "[ ";
                    Set<String> hyponymSet = wn.hyponyms(word1);
                    for (String s : hyponymSet) {
                        returnString = returnString + s + " ";
                    }
                    returnString = returnString + "]";
                    System.out.println(returnString);
                    break;
                case "history":
                    String [] word2 = tokens;
                    Plotter.plotAllWords(ngm, word2, startDate, endDate);
                    break;
                case "hypohist":
                    String [] words = tokens;
                    Plotter.plotCategoryWeights(ngm, wn, words, startDate, endDate);
                    break;
                case "wordlength":
                    YearlyRecordProcessor yrp = new WordLengthProcessor();
                    TimeSeries<Double> ts = ngm.processedHistory(startDate, endDate, yrp);
                    Plotter.plotTS(ts, "wordlength", "year", "yearlyRecord", "word");
                    break;
                case "zipf":
                    try {
                        int year1 = Integer.parseInt(tokens[0]);
                        Plotter.plotZipfsLaw(ngm, year1);
                    } catch (NumberFormatException ex) { 
                        System.err.println("Illegal input");
                    }
                    break;
                default:
                    System.out.println("Invalid command. Please type in help for support.");  
                    break;
            }
        }
    }
}
