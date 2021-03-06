/**
 * Class LRUMemoryManager manages the physical memory in my virtual
 * memory simulation using an LRU paging algorithm.
 *
 * @see MemoryManager
 */
class LRUMemoryManager extends MemoryManager
{

   /**
    * Finds a physical memory page to give to the requesting process.
    * We also keep track of the number of page faults.
    *
    * @param process the PCB requesting a page of memory
    * @return an int that is the number of the physical page replaced
    */
   public int handlePageFault(PCB process)
   {
      // Declare constants
      int replacedPage = findVictim();

      // Make sure the page isn't empty
      if (_physicalMemory[replacedPage] != null)
      {
         // Invalidate the process that is being replaced
         _physicalMemory[replacedPage].invalidatePage(replacedPage);
      }

      // Increments the counters to keep track of statistics
      _pageFaults++;

      // Set the process to the victims page number
      _physicalMemory[replacedPage] = process;

      // Print out the proccess ID an the page that was given to it
      System.out
            .printf("PAGE-FAULT: Process %d given page %d\n", process.getID(),
                  replacedPage);

      return replacedPage;

   } // handlePageFault


   /**
    * Simulates a physical page being referenced by a process.
    * This allows the MemoryManager to keep track of the total
    * number of page references.
    *
    * @param pageNum the physical page being referenced
    */
   public void touchPage(int pageNum)
   {
      // Reset the time for the page that was touched
      _memCounter[pageNum] = 0;

      // Increment the time for all the pages
      for (int i = 0; i < _memCounter.length; i++)
      {
         _memCounter[i]++;
      }

      // Increase the number of references
      _memoryReferences++;

   } // touchPage

} // LRUMemoryManager
