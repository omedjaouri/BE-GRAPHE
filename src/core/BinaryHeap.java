//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( )  --> Return smallest item
// boolean isEmpty( )     --> Return true if empty; else false
// ******************ERRORS********************************
// Throws RuntimeException for findMin and deleteMin when empty
package core;
import java.util.* ;

/**
 * Implements a binary heap.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 * @author DLB
 */
public class BinaryHeap<E extends Comparable<E>> {

    private int currentSize; // Number of elements in heap

    // Java genericity does not work with arrays.
    // We have to use an ArrayList
    private ArrayList<E> array; // The heap array
    private HashMap<E,Integer> hash;

    /**
     * Construct the binary heap.
     */
    public BinaryHeap() {
        this.currentSize = 0;
        this.array = new ArrayList<E>() ;
        this.hash = new HashMap<E,Integer>();
    }

    // Constructor used for debug.
    private BinaryHeap(BinaryHeap<E> heap) {
    	this.currentSize = heap.currentSize ;
    	this.array = new ArrayList<E>(heap.array) ;
    	this.hash = new HashMap<E,Integer>();
    }

    // Sets an element in the array and update the values in the Hashmap
    private void arraySet(int index, E value) {
    	if (index == this.array.size()) {
    		this.array.add(value) ;
    	}
    	else {
    		this.array.set(index, value) ;
    	}
    	//Add element in the hashmap, using the element as the key an the index as the value.
    	this.hash.put(value, new Integer(index));
    }

    /**
     * Test if the heap is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() { return this.currentSize == 0; }
    
    /**
     * Returns size.
     * @return current size.
     */
    public int size() { return this.currentSize; }
        
    
    /**
     * Returns index of parent.
     */
    private int index_parent(int index) {
	return (index - 1) / 2 ;
    }

    /**
     * Returns index of left child.
     */
    private int index_left(int index) {
	return index * 2 + 1 ;
    }
    
    //Returns index of the right child
    private int index_right(int index){
    	return index * 2 + 2;
    }

    /**
     * Insert into the heap.
     * @param x the item to insert.
     */
    public void insert(E x) {
    	int index = this.currentSize++ ;
    	this.arraySet(index, x) ;
    	this.percolateUp(index) ;
    }

    /**
     * Internal method to percolate up in the heap.
     * @param index the index at which the percolate begins.
     */
    private void percolateUp(int index) {
	E x = this.array.get(index) ;

        for( ; index > 0 && x.compareTo(this.array.get(index_parent(index)) ) < 0; index = index_parent(index) ) {
        	E moving_val = this.array.get(index_parent(index)) ;
            this.arraySet(index, moving_val) ;
        }

        this.arraySet(index, x) ;
    }

    /**
     * Internal method to percolate down in the heap.
     * @param index the index at which the percolate begins.
     */
    private void percolateDown(int index) {
	int ileft = index_left(index) ;
	int iright = ileft + 1 ;

	if (ileft < this.currentSize) {
	    E current = this.array.get(index) ;
	    E left = this.array.get(ileft) ;
	    boolean hasRight = iright < this.currentSize ;
	    E right = (hasRight)?this.array.get(iright):null ;
	    
	    if (!hasRight || left.compareTo(right) < 0) {
		// Left is smaller
		if (left.compareTo(current) < 0) {
		    this.arraySet(index, left) ;
		    this.arraySet(ileft, current) ;
		    this.percolateDown( ileft ) ;
		}
	    }
	    else {
		// Right is smaller
		if (right.compareTo(current) < 0) {
		    this.arraySet(index, right) ;
		    this.arraySet(iright, current) ;
		    this.percolateDown( iright ) ;
		}		
	    }
	}
    }

    /**
     * Find the smallest item in the heap.
     * @return the smallest item.
     * @throws Exception if empty.
     */
    public E findMin( ) {
        if( isEmpty() )
            throw new RuntimeException( "Empty binary heap" );
        return this.array.get(0);
    }
    
    /**
     * Remove the smallest item from the heap.
     * @return the smallest item.
     * @throws Exception if empty.
     */
    public E deleteMin( ) {
        E minItem = findMin( );
	E lastItem = this.array.get(--this.currentSize) ;
        this.arraySet(0, lastItem) ;
        this.percolateDown( 0 );
        return minItem;
    }
    
    /**
     * Prints the heap
     */
    public void print() {
	System.out.println() ;
	System.out.println("========  HEAP  (size = " + this.currentSize + ")  ========") ;
	System.out.println() ;

	for (int i = 0 ; i < this.currentSize ; i++) {
	    System.out.println(this.array.get(i).toString()) ;
	}

	System.out.println() ;
	System.out.println("--------  End of heap  --------") ;
	System.out.println() ;
    }

    /**
     * Prints the elements of the heap according to their respective order.
     */
    public void printSorted() {

	BinaryHeap<E> copy = new BinaryHeap<E>(this) ;

	System.out.println() ;
	System.out.println("========  Sorted HEAP  (size = " + this.currentSize + ")  ========") ;
	System.out.println() ;

	while (!copy.isEmpty()) {
	    System.out.println(copy.deleteMin()) ;
	}

	System.out.println() ;
	System.out.println("--------  End of heap  --------") ;
	System.out.println() ;
	}


    
    // Test program : compare with the reference implementation PriorityQueue.
    public static void main(String [] args) {
        BinaryHeap<Integer> heap = new BinaryHeap<Integer>() ;
	PriorityQueue<Integer> queue = new PriorityQueue<Integer>() ;

	int count = 0 ;
	int blocksize = 10000 ;

	System.out.println("Interrupt to stop the test.") ;
	
	while (true) {

	    // Insert up to blocksize elements
	    int nb_insert = (int)(Math.random() * (blocksize + 1)) ;
	    
	    for (int i = 0 ; i < nb_insert ; i++) {
		Integer obj = new Integer(i) ;
		heap.insert(obj) ;
		queue.add(obj) ;
	    }

	    // Remove up to blocksize elements
	    int nb_remove = (int)(Math.random() * blocksize * 1.1) ;
	    
	    if (nb_remove > queue.size()) {
		nb_remove = queue.size() ;
	    }

	    for (int i = 0 ; i < nb_remove ; i++) {

		int removed1 = queue.poll().intValue() ;
		int removed2 = heap.deleteMin().intValue() ;
		
		if (removed1 != removed2) {
		    System.out.println("Ouch : expected " + removed1 + "  .. but got " + removed2) ;
		    System.exit(1) ;
		}
	    }

	    if (heap.size() != queue.size()) {
		    System.out.println("Ouch : heap size = " + heap.size() + "  queue size = " + queue.size() ) ;
		    System.exit(1) ;
		}

	    count += nb_remove ;
	    
	    if (count > 1000000) {
		System.out.println("" + count + " items successfully compared. Heap size : " + heap.size()) ;
		count = 0 ;
	    }
	}
    }
    
    //Return 0 if root
    //Else return index of other child

    private int findOtherChild(int index){
    	int parent = index_parent(index);
    	int left_child = index_left(parent);
    	int right_child = index_right(parent);
    	
    	if(left_child != 0 && left_child !=index){
    		return left_child;
    	}
    	else if(right_child != 0 && right_child !=index){
    		return right_child;
    	}
    	else{
    		return 0;
    	}
    	
    }
    
    
    //Reorder the heap
    private void heapify(int index){
    	
    	int left = index_left(index);
    	int right = index_right(index);
    	
    	int max = index;
    	int length = this.array.size();
    	
    	//If left value exists and is larger, max = left
    	if(left < length && array.get(left).compareTo(array.get(max)) < 0){
    		max = left;
    	}
    	//If right is bigger than max and left, max = right
    	if(right < length && array.get(right).compareTo(array.get(max)) < 0){
    		max = right;
    	}
    	//If the max is the index, the heap is in order, else swap the elements of the heap
    	if(max != index){
    		//Swap
    		E temp = this.array.get(index);
    		arraySet(index, this.array.get(max));
    		arraySet(max, temp);
    		
    		//Recursive call with the new index
    		heapify(max);
    	}
    	
    }
    
    //Change the value of the element and perform Heapify to restore heap order
    //Use HashMap to make it easier to search for elements
	public void update(E element) {
		//find the element to update and update
		int index = hash.get(element);
		this.percolateUp(index);
		this.percolateDown(index);
		//arraySet(index, element);
		
		//Reorder the heap
		/*for(int i = (this.array.size()/2 - 1); i >= 0; i-- ){
			heapify(i);
		}*/
		
		
	}
}

