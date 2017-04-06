/**
 * Class FIFOMemoryManager manages the physical memory in my virtual
 * memory simulation using a FIFO paging algorithm.
 *
 * @see MemoryManager
 */
class FIFOMemoryManager extends MemoryManager
{
   // Initialize to -1 so that it starts at 0 when incremented once.
   private int _replacedPage = -1;


   /**
    * Finds a physical memory page to give to the requesting process.
    * We also keep track of the number of page faults.
    *
    * @param process the PCB requesting a page of memory
    * @return an int that is the number of the physical page replaced
    */
   public int handlePageFault(PCB process)
   {
      // Initialize variables
      int replacedPage;

      // Increment to the next page to start
      _replacedPage++;

      // A page fault occured
      _pageFaults++;

      // Mod the counter with the number of physical pages to count from 0 to the
      // number of NUM_PHYSICAL_MEMORY_FRAMES - 1
      replacedPage = _replacedPage % NUM_PHYSICAL_MEMORY_FRAMES;

      // Make sure the page isn't empty
      if (_physicalMemory[replacedPage] != null)
      {
         // Invalidate the process that is being replaced
         _physicalMemory[replacedPage].invalidatePage(replacedPage);
      }

      // Put the process in the current page
      _physicalMemory[replacedPage] = process;

      // Print out the proccess ID and the page that was given to it
      System.out
            .printf("PAGE-FAULT: Process %d given page %d\n", process.getID(),
                  replacedPage);

      return replacedPage;

   } // handlePageFault


   /**
    * Simulates a physical page being referenced by a process.
    * This allows the MemoryManager to keep track of the total
    * number of page references. For FIFO we don't actually need to do
    * anything about the page being referenced
    *
    * @param pageNum the physical page being referenced
    */
   public void touchPage(int pageNum)
   {
      _memoryReferences++;

   } // touchPage 

} // FIFOMemoryManager
