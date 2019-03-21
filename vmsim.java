/* jem319
 * Virtual Memory simulator
 * CS1550 project 3
 */
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class vmsim {
  public static void main(String[]args){
    int numFrames = 0;
    int refresh = 0;
    String algorithm = "";
    String traceFile = "";

    int totalMem;   // Total number of memory accesses
    int pageFaults; // Total number of page faults
    int writes;     // Total number of writes to disk

    // TODO: Get program Arguments
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
        String addr = line.substring(2, 12);
        String cycles = line.substring(13);

        System.out.println("Mode "+mode+" addr "+addr+" cycles "+cycles);
      }
    }catch(IOException e){
      e.printStackTrace();
    }

    // Run Selected Algorithm
    if(algorithm.equals("opt"))           opt();
    else if(algorithm.equals("fifo"))     fifo();
    else if(algorithm.equals("aging"))    aging();
    else{
      System.out.println("Improper algorithm! Usage -a <opt|fifo|aging>.");
    }

    // TODO: Print to Screen
  }

  // When a page is evicted if it's initial instruction was a 'store', write to disk.

  // TODO: Opt
  /* As you add to the table, search for the next instance of it in the program
   * and add to a hashtable mapping address --> "number of accesses in the future"
   * Start at 0. Each increment moves further into the accesses. If it hits the
   * end, then it has "infinite time."
   */
   public static void opt(){
   }

  // TODO: FIFO
  /*
   * Self Explanatory...
   */
   public static void fifo(){
   }

  // TODO: Aging
  /*
   * Relearn...
   */
   public static void aging(){
   }

}
