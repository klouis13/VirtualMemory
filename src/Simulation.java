/**
 *
 * Reads logical addresses from an input file and simulates virtual 
 * memory page-replacement algorithms
 *
 * @author David M. Hansen
 * @version 1.5
 *
 **/
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class Simulation
{

   // Constants defining how big pages are and how many virtual pages we allow. 
   // These constants are used by other classes who need to know the 
   // constraints of the simulation
   public static final int NUM_VIRTUAL_PAGES = 128;  // 128 virtual pages
   public static final int PAGE_SIZE = 32;           // 32 bytes per page


   /**
     * The main body of the simulation. Opens and reads a file of
     * memory addresses and simulates virtual memory references.
     *
     * @param args[] array of arguments - args[0] is the name of of the
     * input file. Optional args[1] indicates the replacement algorithm to use, 
     * the default is FIFO unless "LRU" is given
     */
   public static void main(String args[]) throws IOException
   {
      // An address that is used to signal the end of a process
      final int END_OF_PROCESS = -1;

      // At runtime we'll figure out what sort of MemoryManager to use -
      // we have different sublcasses with different replacement mechanisms
      MemoryManager memoryManager;

      // Initialize the HashMap we'll use to store and look up PCB 
      // objects as they enter and leave the simulation.  We'll use
      // the process number as the key when looking up processes in the processTable
      HashMap<Integer, PCB> processTable = new HashMap<Integer, PCB>();

      int processID;       // ID of the current process
      PCB process;         // Process Control Block for the process
      int logicalAddress;  // Logical address of the next memory reference
      boolean writeFlag;   // Flag indicating whether reference is a read or write
      Scanner inputFile;   // Scanner over the input file



      // Make sure we got enough arguments
      if (args.length == 0)
      {
         System.err.println("ERROR: Insufficient arguments\n\tUsage: java Simulation <inputFilename> [LRU/FIFO]");
         System.exit(1);
      }


      // Attempt to open the input file, just let the exception be
      // thrown if there are any problems
      inputFile = new Scanner(new File(args[0]));

      // Initialize the memory manager
      // Choose a memory manager depending on the argument given - if
      // they gave us "LRU" use the LRU manager, otherwise FIFO
      if ((args.length > 1) && (args[1].equals("LRU")))
      {
         memoryManager = new LRUMemoryManager();
      }
      else
      {
         memoryManager = new FIFOMemoryManager();
      }


      // Keep reading from the file until it's empty
      while (inputFile.hasNextInt())
      {
         // First parameter is the process number
         processID = inputFile.nextInt();
         // Second parameter is the logical Address
         logicalAddress = inputFile.nextInt();
         // Third parameter is the read/write flag that's represented as
         // a 1/0 in the input file; turn it into a boolean flag
         writeFlag = (inputFile.nextInt() == 1);


         // See if this process already exists in the simulation
         // Note: since the HashMap uses Object keys we must
         // create a temporary, bogus Integer object from our
         // process ID to use as a key
         process = processTable.get(processID);
         if (process == null)
         {
            // Process not yet in the simulation, create it and
            // add it to the processTable using the process ID as the key
            process = new PCB(processID);
            processTable.put(process.getID(), process);
         }
         // process now refers to a valid Process object


         // If the address is End-of-Process delete this 
         // process from the simulation, otherwise give the process
         // the address and let it pretend to access memory
         if (logicalAddress == END_OF_PROCESS)
         {
            // Remove this process from the processTable
            processTable.remove(process.getID());

            // Tell the memoryManager to free any physical pages 
            // assigned to this process
            memoryManager.freePages(process);
         }
         else // Valid logical address
         {
            // Tell the process to deal with this address
            process.handleAddress(memoryManager, logicalAddress, writeFlag);
         }

      } // while

      // Close the input file
      inputFile.close();

      // Print the PCB's page table
      for (PCB proc : processTable.values())
      {
         System.out.println(proc);
      }

      // Tell the memoryManager to print stats
      memoryManager.printStatistics();

   } // main

} // Simulation
