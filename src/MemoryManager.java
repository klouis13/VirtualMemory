/**
 * Class MemoryManager manages the physical memory in my virtual
 * memory simulation. It handles page faults and maintains
 * statistics about memory access.
 *
 * @author David M. Hansen
 * @version 1.5
 */

abstract class MemoryManager
{
   // Class-wide constant defining how many physical pages we have
   static protected final int NUM_PHYSICAL_MEMORY_FRAMES = 4;

   // I represent physical memory as an array of PCBs - the process
   // that owns that memory will be in that array slot. A null object
   // means the slot is currently free.
   protected PCB _physicalMemory[] = new PCB[NUM_PHYSICAL_MEMORY_FRAMES];

   // memCounter keeps track of a count for each memory frame. Its
   // meaning depends on the page-replacement algorithm being used
   protected int _memCounter[] = new int[NUM_PHYSICAL_MEMORY_FRAMES];

   // Two counters to track the number of page faults and total number
   // of memory references
   protected int _pageFaults;
   protected int _memoryReferences;

   public MemoryManager()
   {
      // This is probably redundant since "null" is the default
      // value for an unitialized object and we could have just
      // set the other counters to 0 when we declared them below,
      // but I prefer to be safe and explicit.

      for (int i = 0; i < NUM_PHYSICAL_MEMORY_FRAMES; i++)
      {
         // Set the physical memory pages to refer to a null object
         // indicating that they are initially "free"
         _physicalMemory[i] = (PCB) null;
         _memCounter[i] = 0;
      }

      // Set our global page fault and memory-reference counters to 0
      _pageFaults = 0;
      _memoryReferences = 0;

   } // MemoryManager


   /**
    * Marks any memory pages owned by the given process as being
    * free and prints out information
    *
    * @param process the PCB leaving the simulation
    */
   public void freePages(PCB process)
   {
      // A process has left the system. Search the set of
      // physical pages and if this process owned a page,
      // set that page to "null" to indicate that it is
      // a free page

      // Set any physical page owned by this process to null and reset
      // the counter 
      for (int i = 0; i < NUM_PHYSICAL_MEMORY_FRAMES; i++)
      {
         if (_physicalMemory[i] == process)
         {
            _physicalMemory[i] = null;
            _memCounter[i] = 0;
         }
      }

   } // freePages 


   /**
    * Finds a physical memory page to give to the requesting process.
    * We also keep track of the number of page faults.
    *
    * @param process the PCB requesting a page of memory
    * @return the number of the physical page replaced
    */
   public abstract int handlePageFault(PCB process);

   /**
    * Simulates a physical page being referenced by a process.
    * This allows the MemoryManager to keep track of the total
    * number of page references.
    *
    * @param pageNum the physical page being referenced
    */
   public abstract void touchPage(int pageNum);


   /**
    * Searches physical memory to find a free page or a currently
    * occupied page to replace.
    *
    * @return an int specifying the number of the physical page to replace
    */
   protected int findVictim()
   {
      // Initially assume the victim page is page 0
      int victimPage = 0;
      // If we find a free page, we'll set this flag to true and quit
      // looking any further
      boolean foundFree = false;

      // Search physical memory. If we find a free frame, that's
      // our victim. Otherwise our victim will be the frame with
      // the highest _memCounter value.
      for (int i = 0; i < NUM_PHYSICAL_MEMORY_FRAMES && !foundFree; i++)
      {
         if (_physicalMemory[i] == null)
         {
            // Found a free frame - that's our victim!
            foundFree = true;
            victimPage = i;
         }
         else if (_memCounter[i] > _memCounter[victimPage])
         {
            // This page has a higher count than our current victim,
            // it's the new victim
            victimPage = i;
         }

      } // for

      return victimPage;

   } // findVictim


   /**
    * Print overall statistics about the simulation including the
    * total number of memory references, page faults, and the page
    * fault ratio.
    */
   public void printStatistics()
   {
      // Iterate over the physical memory and see if the page is
      // free or owned by some process
      for (int i = 0; i < NUM_PHYSICAL_MEMORY_FRAMES; i++)
      {
         if (_physicalMemory[i] == null) // Not owned by any process
         {
            System.out.println("Page " + i + " is free");
         }
         else // Owned by a procewss
         {
            System.out.println(
                  "Page " + i + " is owned by process # " + _physicalMemory[i]
                        .getID());
         }
      } // for

      // How many page faults were there and what was the page fault ratio
      System.out.println(
            "\n\n" + _pageFaults + " page faults out of " + _memoryReferences
                  + " total memory references for a page fault ratio of "
                  + (int) (((float) _pageFaults / (float) _memoryReferences)
                  * 100) + "%");

   } // printStatistics

} // MemoryManager 
