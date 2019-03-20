public class vmsim {
  public static void main(String[]args){
    int numFrames = 0;
    int refresh = 0;
    String algorithm = "";
    String traceFile = "";

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
    System.out.println("-n " + numFrames);
    System.out.println("-a " + algorithm);
    System.out.println("-r " + refresh);
    System.out.println(traceFile);
    // Put memory traces into array???
    // TODO: Select Algorithm
    // TODO: Run Selected Algorithm
    // TODO: Read File as you do ??? or see above note on array.
    // TODO: Print to Screen
  }

  // TODO: Opt
  /* As you add to the table, search for the next instance of it in the program
   * and add to a hashtable mapping address --> "number of accesses in the future"
   * Start at 0. Each increment moves further into the accesses. If it hits the
   * end, then it has "infinite time."
   */

  // TODO: FIFO
  /*
   * Self Explanatory...
   */
  // TODO: Aging
  /*
   * Relearn...
   */

}
