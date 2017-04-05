/**
 * Class LRUMemoryManager manages the physical memory in my virtual
 * memory simulation using an LRU paging algorithm.
 *
 * @author
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

      // Check that the page being replaced isn't empty
      if (_physicalMemory[replacedPage] != null)
      {
         // Increments the counters to keep track of statistics
         _pageFaults++;

         System.out
               .printf("PAGE-FAULT: Process %d given page %d\n", process.getID(),
                     replacedPage);
      }

      // Set the process to the victims page number
      _physicalMemory[replacedPage] = process;

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
      _memCounter[pageNum] = 0;

      incrementMemCount();

      _memoryReferences++;

   } // touchPage

   /**
    * Increment the time of each page in the memory
    */
   private void incrementMemCount()
   {
      for (int i = 0; i < _memCounter.length; i++)
      {
         _memCounter[i]++;
      }
   }

} // LRUMemoryManager
