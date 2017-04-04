/**
 *
 * Class FIFOMemoryManager manages the physical memory in my virtual
 * memory simulation using a FIFO paging algorithm. 
 *
 * @author 
 * @version 
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
      return 0;

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

   } // touchPage 


} // FIFOMemoryManager 
