/* jem319
 * Virtual Memory simulator
 * CS1550 project 3
 */
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Arrays;

public class vmsim {
  public static void main(String[]args){
    int numFrames = 0;
    int refresh = 0;
    String algorithm = "";
    String traceFile = "";

    int totalMem;   // Total number of memory accesses
    int pageFaults; // Total number of page faults
    int writes;     // Total number of writes to disk

    // Get program Arguments
    for(int i = 0; i < args.length; i++){
      if(args[i].equals("-n"))
        numFrames = Integer.parseInt(args[++i]);
      else if(args[i].equals("-a"))
        algorithm = args[++i];
      else if(args[i].equals("-r"))
        refresh = Integer.parseInt(args[++i]);
      else
        traceFile = args[i];
    }

    // Put memory traces into array???
    ArrayList<String[]> traces = new ArrayList<String[]>();
    try{
      String line;
      BufferedReader br = new BufferedReader(new FileReader(traceFile));
      while((line = br.readLine()) != null){
        String mode = line.substring(0, 1);
        String addr = line.substring(4, 12);
        String cycles = line.substring(13);

        String[] access = {mode, addr, cycles};

        traces.add(access);
      }
    }catch(IOException e){
      e.printStackTrace();
    }

    // Run Selected Algorithm
    if(algorithm.equals("opt"))           opt(numFrames, traces);
    else if(algorithm.equals("fifo"))     fifo(numFrames, traces);
    else if(algorithm.equals("aging"))    aging(numFrames, traces);
    else{
      System.out.println("Usage ./vmsim -n <numFrames> -a <opt|fifo|aging> -r <refresh> traceFile");
    }

    // TODO: Print to Screen
  }

  // TODO: Opt
  /* As you add to the table, search for the next instance of it in the program
   * and add to a hashtable mapping address --> "number of accesses in the future"
   * Start at 0. Each increment moves further into the accesses. If it hits the
   * end, then it has "infinite time."
   */
   public static String[] opt(int numFrames, ArrayList<String[]> traces){
     int totalMem = 0;
     int pageFaults = 0;
     int writes = 0;
     HashMap<Integer,Integer> optTracker = new HashMap<Integer,Integer>();
     /* Parallel Arrays matching address acesses and if it has been stored its operation (s/l)
      * {3ae38, 2900a, 2252f, 223ff}
      * {  l,     s,     s,     l  } */
     int[] memFrames = new int[numFrames];
     String[] memOps = new String[numFrames];
     int framesUsed = 0;

     for(String[] access : traces){
       totalMem++;
       String mode = access[0];
       int page;

       long temp = Long.parseLong(access[1], 16);
       int cycles = Integer.parseInt(access[2]);
       temp = temp >>> 12; // Isolate the page number from the page offset
       page = (int) temp;

       // Is it in page table?
       if(optTracker.get(page) == null){ //Page not in table
         pageFaults++;
         if(framesUsed == numFrames){ // Page must be evicted
           // Evict a Page
           int i;
           int addr = memFrames[0]; //The address to be evicted
           for(i = 0; i < memFrames.length; i++){
             if(optTracker.get(memFrames[i]) > optTracker.get(addr))
               addr = memFrames[i];
           }
           for(i = 0; i < memFrames.length; i++){ //Get index
             if(memFrames[i] == addr)
               break;
           }
           memFrames[i] = 0;
           optTracker.remove(addr);
           // If store, writes++
           if(memOps[i].equals("s"))
             writes++;
           memOps[i] = "";
           framesUsed--;
         }
         //Actually add the page to the table
         framesUsed++;
         int i;
         for(i = 0; i < memFrames.length; i++){ //Get index
           if(memFrames[i] == 0)
             break;
         }
         // Add a new page to memFrames
         memFrames[i] = page;
         // Add mode to memOps
         memOps[i] = mode;
       } else { // Page already in table
         if (mode.equals("s")){
           int i;
           for(i = 0; i < memFrames.length; i++){ //Get index
             if(memFrames[i] == page){
               memOps[i] = "s";
               break;
             }
           }
         }
       }
       // Iterate through rest of list and find next access
       int count = 0;
       for(int i = totalMem; i < traces.size(); i++){
         count++;
         // Add to hashtable along with next access
         if( (int)((Long.parseLong(traces.get(i)[1], 16) >>> 12) ) == page)
           break;
       }
       optTracker.put(page, count);
     }
     String[] ret = {""+totalMem, ""+pageFaults, ""+writes};
     return ret;
   }

  // TODO: FIFO
  /*
   * First in First out.
   * Make a simple queue of max size numFrames
   */
   public static String[] fifo(int numFrames, ArrayList<String[]> traces){
     int totalMem = 0;
     int pageFaults = 0;
     int writes = 0;
     // "Queues" add to end remove from front and shift down one
     //Should keep track of current end of list
     int[] memFrames = new int[numFrames];
     String[] memOps = new String[numFrames];
     for(String[] access : traces){
       //totalMem++;
       // If not in table:
       /* pagefaults++;
        * Check: need to evict?
          * remove front of list
          * if 's' -> writes++;
          * shift both arrays down 1;
        * Add new one to end of table;
        */
        //If In table
        /* if 's' -> change to s
         */
     }

     String[] ret = {""+totalMem, ""+pageFaults, ""+writes};
     return ret;
   }

  // TODO: Aging
  /*
   * Relearn...
   */
   public static String[] aging(int numFrames, ArrayList<String[]> traces){
     int totalMem = 0;
     int pageFaults = 0;
     int writes = 0;
     String[] ret = {""+totalMem, ""+pageFaults, ""+writes};
     return ret;
   }
}
