/** AListPrinterLauncher
 *  @author Josh Hug
 */

public class AListPrinterLauncher {
    public static void main(String[] args) {
        AList L  = new AList();
        L.insertBack(5);
        L.insertBack(10);
        L.insertBack(-3);
        
        AList.ListPrinter lp = L.new ListPrinter();
        lp.printNext(); // 5
        lp.printNext(); // 10
        lp.printNext(); // -3 
        lp.printNext();  
    }
} 