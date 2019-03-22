/* jem319
 * Virtual Memory simulator
 * CS1550 project 3
 */
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

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

    String[] ret = new String[3];
    // Run Selected Algorithm
    if(algorithm.equals("opt"))           ret = opt(numFrames, traces);
    else if(algorithm.equals("fifo"))     ret = fifo(numFrames, traces);
    else if(algorithm.equals("aging"))    ret = aging(numFrames, traces, refresh);
    else{
      System.out.println("Usage ./vmsim -n <numFrames> -a <opt|fifo|aging> -r <refresh> traceFile");
    }

    // Print to Screen
    System.out.println("Algorithm:\t"+algorithm.toUpperCase());
    System.out.println("Number of Frames:\t"+numFrames);
    System.out.println("Total memory accesses:\t"+ret[0]);
    System.out.println("Total page faults:\t"+ret[1]);
    System.out.println("Total writes to disk:\t"+ret[2]);
  }

  /*****************************************************************************
   ********************************** OPT **************************************
   *****************************************************************************/
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

  /*****************************************************************************
   *********************************** FIFO ************************************
   *****************************************************************************/
   public static String[] fifo(int numFrames, ArrayList<String[]> traces){
     int totalMem = 0;
     int pageFaults = 0;
     int writes = 0;
     // "Queues" add to end remove from front and shift down one
     //Should keep track of current end of list
     int[] memFrames = new int[numFrames];
     String[] memOps = new String[numFrames];
     int tail = 0; //End of queue
     boolean inTable = false;

     for(String[] access : traces){
         totalMem++;
         String mode = access[0];
         int page;
         long temp = Long.parseLong(access[1], 16);
         int cycles = Integer.parseInt(access[2]);
         temp = temp >>> 12; // Isolate the page number from the page offset
         page = (int) temp;
         for(int i = 0; i < tail; i++){
           if (memFrames[i] == page)
             inTable = true;
           else
             inTable = false;
         }
         if (!inTable){ // Not in table
           pageFaults++;
           if(tail == memFrames.length){// Need to evict a page
             memFrames[0] = 0; //Evict Page
             if (memOps[0].equals("s"))
               writes++; //Write to disk if it was a store instruction
             memOps[0] = "";
             //Shift Queues down one to make room at end
             int[] tempFrames = new int[numFrames];
             String[] tempOps = new String[numFrames];
             System.arraycopy(memFrames, 1, tempFrames, 0, tail - 1);
             memFrames = tempFrames;
             System.arraycopy(memOps, 1, tempOps, 0, tail - 1);
             memOps = tempOps;
             tail--;
           }
           //Add page to table
           memFrames[tail] = page;
           memOps[tail] = mode;
           tail++;
         } else { // Page already in table
           int i;
           for(i = 0; i < tail; i++){
             if (memFrames[i] == page)
               break;
           }
           if(mode.equals("s"))
             memOps[i] = "s";
         }
     }
     String[] ret = {""+totalMem, ""+pageFaults, ""+writes};
     return ret;
   }

  /*****************************************************************************
   *********************************** Aging ***********************************
   *****************************************************************************/
   public static String[] aging(int numFrames, ArrayList<String[]> traces, int refresh){
     int totalMem = 0;
     int pageFaults = 0;
     int writes = 0;

     int ref = 0x00000080; // 10000000 in binary
     //Parallel Arrays
     int[] memFrames = new int[numFrames];
     String[] memOps = new String[numFrames];
     int[] counters = new int[numFrames];
     boolean[] referenced = new boolean[numFrames];

     for(String[] access : traces){
       totalMem++;
       String mode = access[0];
       int page;
       long temp = Long.parseLong(access[1], 16);
       int cycles = Integer.parseInt(access[2]);
       int cycleCount = 0;
       temp = temp >>> 12; // Isolate the page number from the page offset
       page = (int) temp;
       int indexOfPage = -1;

        //Update internal clocks
        cycleCount += cycles;
        if(cycleCount >= refresh){
          for(int i = 0; i < counters.length; i++){
            counters[i] = counters[i] >>> 1;
            //if the page was accessed, put a 1 in the MSB
            if(referenced[i] == true)
              counters[i] = counters[i] | ref;
          }
        }

       for (int i = 0; i < memFrames.length; i++){
         if(memFrames[i] == page){
           indexOfPage = i;
           break;
         }
       }

       if(indexOfPage == -1){ // If not in table
         /* Add page to table
          * set counter = ref; (10000000)
          * referenced = false
          */
          pageFaults++;
          boolean full = true;
          for (int i = 0; i < memFrames.length; i++){
            if(memFrames[i] == 0){
              full = false;
              indexOfPage = i;
              break;
            }
          }
          if (full){ //If the table is full
            indexOfPage = 0; //the page to evict and later fill in.
            for (int i = 0; i < memFrames.length; i++){
              if(counters[i] < counters[indexOfPage])
                indexOfPage = i;
              else if (counters[i] == counters[indexOfPage] && memFrames[i] < memFrames[indexOfPage])
                indexOfPage = i;
            }
            // Evict page
            if(memOps[indexOfPage].equals("s"))
              writes++;
            memFrames[indexOfPage] = 0;
            memOps[indexOfPage] = "";
            counters[indexOfPage] = 0;
            referenced[indexOfPage] = false;
          }
          // Add page to table
          memFrames[indexOfPage] = page;
          memOps[indexOfPage] = mode;
          counters[indexOfPage] = ref;
          referenced[indexOfPage] = false;
       } else { // Else (if in table)
         /* referenced = true;
          * if 's' set to 's'
          */
          referenced[indexOfPage] = true;
          if(mode.equals("s"))
            memOps[indexOfPage] = "s";
       }
     }

     String[] ret = {""+totalMem, ""+pageFaults, ""+writes};
     return ret;
   }
}
