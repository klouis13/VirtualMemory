/**
 *
 * Class PCB represents a process in my virtual memory simulation.
 * The PCB has a page-table and a process number and handles all memory
 * references.
 *
 * @author David M. Hansen
 * @version 1.5
 */

public class PCB
{
 

   /**
    * Creates a new PCB with the given process number
    *
    * @param processID the ID number of the process
    */
   public PCB(int processID)
   {
      // Set our process ID
      p_id = processID;
      // Create our page-table
      p_pageTable = new PageTable(this);
   }

 
   /**
    * Returns the number of the PCB
    *
    * @return the process ID number of this PCB
    */
   public int getID()
   {
      return p_id;
   } // getID


   /**
    * Main method called by the simulation to simulate accessing
    * a given logical address.
    *
    * @param memoryManager the object that manages memory
    * @param address the logical address to access
    * @param write if true, indicates a write operation 
    */
   public void handleAddress(MemoryManager memoryManager, int address, boolean write)
   {
      // Have our page-table translate the logical address to a physical page 
      // number - tell it whether this was a read or write so it can
      // remember. And ask the memoryManager to touch that page for us 
      memoryManager.touchPage(p_pageTable.translateAddress(memoryManager, address, write));
   } // handleAddress



   /**
    * Tells the page-table to mark a page as invalid
    *
    * @param page the physical page that is no longer valid
    * @returns boolean indicating whether the page is dirty or not
    */
   public boolean invalidatePage(int page)
   {
      // We've had a page taken away from us.  Have the page-table invalidate 
      // this physical page
      return p_pageTable.invalidatePage(page);
   } // invalidatePage 



   /**
    * @return process ID and page-table
    */
   public String toString()
   {
      return "Process # " + p_id + " Memory Map\n"+p_pageTable;
   } // toString



   // PCB knows its number and has a page-table for mapping memory
   private int p_id;
   private PageTable p_pageTable;



   /**
    *
    * Class PageTable is an inner class that holds the process-specific map
    * from logical memory to physical memory.
    *
    * @author David M. Hansen 
    * @version 1.5
    */
   class PageTable
   {


      /**
       * Create a new PageTable
       *
       * @param myProc a reference back to the PCB that holds this
       * PageTable
       */
      public PageTable(PCB myProcess)
      {
         // Set our reference to the owning process
         p_myProcess = myProcess;

         // Initially, all our pages are invalid and not dirty. We don't need to
         // initialize the logicalMap here because we'll never access an invalid
         // page.
         for(int i=0; i<Simulation.NUM_VIRTUAL_PAGES; i++)
         {
            p_valid[i] = false;
            p_dirty[i] = false;
         }
      } // PageTable


      /**
       * Converts the address to a logical page number and looks up the
       * physical page number. If the translation is not valid, the
       * PageTable requests the memoryManager to handle a page fault.
       *
       * @param memoryManager the object that manages memory
       * @param address the logical address to access
       * @param write if true, indicates a write operation
       * @return the physical page number
       */
      public int translateAddress(MemoryManager memoryManager, int address, boolean write)
      {
         // The logical page number is the address divided by the page size
         int logicalPage = address/Simulation.PAGE_SIZE;

         // Find the mapping to a physical page - we test validity below
         int physicalPage = p_logicalMap[logicalPage];

         // See if the physical page we mapped to is valid for us
         if (!p_valid[logicalPage])
         {
            // Nope - ask the memoryManager to fault a page in for us and
            // tell us what physical page he's assigned to us
            physicalPage = memoryManager.handlePageFault(p_myProcess);

            // Mark that logical page as valid and set it to refer to the
            // physical page the memoryManager just gave us
            p_valid[logicalPage] = true;
            p_logicalMap[logicalPage] = physicalPage;
         }

         // If this was a write, remember that
         if (write)
         {
            p_dirty[logicalPage] = true;
         }

         return physicalPage;

      } // translateAddress 



      /**
       * Searches the memory map for the given physical page and
       * marks that logical page as invalid
       *
       * @param page the physical page that is no longer valid
       * @returns boolean true if the page invalidated is p_dirty
       * 
       */
      public boolean invalidatePage(int page)
      {
         boolean isDirty = false;

         // Find this physical page in our memory map and mark it
         // as invalid for us
         for(int i=0; i<Simulation.NUM_VIRTUAL_PAGES; i++)
         {
            if (p_logicalMap[i] == page)
            {
               p_valid[i] = false;
               isDirty = p_dirty[i];
            }
         }

         return isDirty;

      } // invalidatePage 



      /**
       * @return string representation of pageTable that shows the logicalMap for 
       * pages that are currently valid
       */
      public String toString()
      {
         StringBuffer stringRep = new StringBuffer();

         // If the page is marked as "valid", include it, otherwise
         // ignore it
         for(int i=0; i<Simulation.NUM_VIRTUAL_PAGES; i++)
         {
            if (p_valid[i])
            {
               stringRep.append("Logical # ").append(i).append(" = physical #").
                  append(p_logicalMap[i]).append("\n");
            }
         }

         return stringRep.toString();
      } // toString


      // PageTable holds a memory map that maps logical pages to physical pages
      private int p_logicalMap[] = new int[Simulation.NUM_VIRTUAL_PAGES];

      // PageTable keeps track of whether the mapping is valid or not
      private boolean p_valid[] = new boolean[Simulation.NUM_VIRTUAL_PAGES];

      // PageTable keeps track of whether the page is dirty or not
      private boolean p_dirty[] = new boolean[Simulation.NUM_VIRTUAL_PAGES];

      // PageTable knows what process owns it
      private PCB p_myProcess;

   } // class PageTable


} // class PCB



