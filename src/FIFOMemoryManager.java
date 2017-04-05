/**
 * Class FIFOMemoryManager manages the physical memory in my virtual
 * memory simulation using a FIFO paging algorithm.
 *
 * @author
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
      // Increment to the next page to start
      _replacedPage++;

      // Check if the physical memory is empty at the next slot
      if (_physicalMemory[_replacedPage % NUM_PHYSICAL_MEMORY_FRAMES] != null)
      {
         // A page fault occured
         _pageFaults++;

         // Print out the proccess ID an the page that was given to it
         System.out
               .printf("PAGE-FAULT: Process %d given page %d\n", process.getID(),
                     _replacedPage % NUM_PHYSICAL_MEMORY_FRAMES);
      }

      // Put the process in the current page, counts from 0 to the
      // NUM_PHYSICAL_MEMORY_FRAMES
      _physicalMemory[_replacedPage % NUM_PHYSICAL_MEMORY_FRAMES] = process;

      return _replacedPage % NUM_PHYSICAL_MEMORY_FRAMES;

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
