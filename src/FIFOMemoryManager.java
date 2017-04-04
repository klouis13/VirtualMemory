/**
 * Class FIFOMemoryManager manages the physical memory in my virtual
 * memory simulation using a FIFO paging algorithm.
 *
 * @author
 * @see MemoryManager
 */

class FIFOMemoryManager extends MemoryManager
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
      // Declare Variables
      boolean foundSlot = false;
      int physicalPageNum;
      int firstInIndex = 0;

      // Cycle through physical memory trying to find an open spot, if no spot
      // is open then the memory in the longest is then replaced
      for (physicalPageNum = 0; physicalPageNum < _physicalMemory.length
            && !foundSlot; physicalPageNum++)
      {
         // Check for an empty memory slot
         if (_physicalMemory[physicalPageNum] == null)
         {
            // Replace that empty slot with the process
            _physicalMemory[physicalPageNum] = process;
            _memCounter[physicalPageNum] = _processOrderIn++;
            foundSlot = true;
         }
         else
         // An empty slot wasn't found so increase the page fault counter
         {
            _pageFaults++;
            System.out.printf("PAGE-FAULT: Process %d given page %d\n",
                  process.getID(), physicalPageNum);
         }
      }
      if (!foundSlot)
      {
         // Cycle through the memory counter array comparing the value of the
         // memory references with the following value
         for (physicalPageNum = 0;
              physicalPageNum < _memCounter.length - 1; physicalPageNum++)
         {
            // Compare the memory reference values and store the index of the
            // lower
            if (_memCounter[physicalPageNum] < _memCounter[physicalPageNum + 1])
            {
               firstInIndex = physicalPageNum;
            }
         }

         // Replace i with the correct index to replace
         physicalPageNum = firstInIndex;

         // Replace the memory in the process
         _physicalMemory[physicalPageNum] = process;
         _memCounter[physicalPageNum] = _processOrderIn++;
      }

      return physicalPageNum;

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

      // Doesn't need to do anything ... ?

   } // touchPage 

} // FIFOMemoryManager
