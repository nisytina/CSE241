import java.util.ArrayList;

//
// PRIORITYQUEUE.JAVA
// A priority queue class supporting sundry operations needed for
// Dijkstra's algorithm.
//

class PriorityQueue<T> {
	
	public ArrayList<Node<T>> pq;
  
    // constructor
    //
	public PriorityQueue()
    {
    	pq = new ArrayList<Node<T>>();  	
    	//first cell is not used
    	Node<T> init = new Node<T>(-1,null,null);
    	pq.add(init);
    }
    
    // Return true iff the queue is empty.
    //
    public boolean isEmpty()
    {
    boolean b = (pq.size() == 1);
	return b;
    }
    
    // Insert a pair (key, value) into the queue, and return
    // a Handle to this pair so that we can find it later in
    // constant time.
    //
    
	Handle<T> insert(int key, T value)
    {
    	//allocate new node to insert
    	Handle<T> newh = new Handle<T>(key,value);
    	Node<T> n = new Node<T>(key,value,newh);
    	pq.add(n);
    	int newIndex = pq.size()-1;
    	newh.index = newIndex;
    	  
    	if( newIndex == 1){
    		newh.index = 1;    		
        	return newh;
    	}
    	
    	
    	int parentIndex = (newIndex/2); 
    	int i = newIndex;
    	Node<T> iter = n;    	
    	Node<T> parent = pq.get(parentIndex);
    	
    	newh = bubbleUp(iter,parent,i);
	
    	return newh;
    	 		
    	}
    
public void swap(Node<T> a,Node<T> b){
    	
    	int temp = a.key;
		a.key = b.key;
		b.key = temp;
		
		T temp2 = (T) a.value;
		a.value = b.value;
		b.value = temp2;
		
		Handle<T> temp3 = a.h;
		a.h = b.h;
		b.h = temp3;
				
		int i  = a.h.index;
		a.h.index = b.h.index;
		b.h.index = i;
			
    	
    }
    	
   
    // Return the smallest key in the queue.
    //
    public int min()
    {
    	
    		int minKey = pq.get(1).key;
    		
    
	return minKey;
    }
    
    // Extract the (key, value) pair associated with the smallest
    // key in the queue and return its "value" object.
    //
    public T extractMin()
    {
    	
    	Node<T> min = pq.get(1);
    	T minValue = min.value;
    	
    	if(pq.size() == 2){
    		min.h.remove = true;
    		pq.remove(min);
    		return minValue;
    	}
    	
    	if(pq.size() ==3){
    		if(min.key<pq.get(2).key){
    			swap(min,pq.get(2));
    		} 		
    		minValue = pq.get(2).value;
    		pq.get(2).h.remove = true;
    		pq.remove(pq.get(2));
    		
    		return minValue;
    	}
    	
    	int last = pq.size()-1;
    	Node<T> l = pq.get(last);
    	
    	swap(min,l);
    	
    	l.h.remove =true;
    	pq.remove(last); 
    	
    	if(pq.size() ==3){
    		if(min.key>pq.get(2).key){
    			swap(min,pq.get(2));
    		} 		
    		
    	}
    	
    	if(pq.size() >3){
    		
    		heapify(min,last);
    	}
    	     
	    return minValue;
	    
    	
    }
    
    //addditional helper function: bubble up
    public Handle<T> bubbleUp(Node<T> insert, Node<T> parent,int i){
            
    	    Node<T> iter = insert;
            while(iter.key<parent.key){
    		
    		swap(iter,parent);
    		
    		iter = parent;
    		int parentIndex = parent.h.index/2;
    		parent = pq.get(parentIndex);
    		 		
    	}
        insert = iter;
    	return insert.h;
       	
    }
    
    //additional helper function
   public Node<T> heapify( Node<T> min, int last){
	   
	   int i = min.h.index;
	   Node<T> aChild = pq.get(i* 2);
   	   Node<T> bChild = pq.get(i * 2 +1);
   	   Node<T> minC = minChild(aChild, bChild);
   	   Node<T> iter = min;
   	   //if not leaf
	   while(pq.indexOf(iter)<=last/2 && iter.key > minC.key )
   	{
		   
       		swap(iter,minC);  	
       		iter = minC;
       		//if not a leaf
       		if(pq.indexOf(iter)<last/2){
       			
       		aChild = pq.get(pq.indexOf(iter) * 2);
           	bChild = pq.get(pq.indexOf(iter) * 2 +1);
           	minC = minChild(aChild, bChild);
           	
       		}
       		last = pq.size()-1;
   	}  
	   
	   return iter;
	 
	   
   }
     
    public Node<T> minChild(Node<T> a, Node<T> b){
    	
    	if(a.key < b.key){
    		
    		return a;
    	}
    	return b;
    	  	
    }
    
    
    // Look at the (key, value) pair referenced by Handle h.
    // If that pair is no longer in the queue, or its key
    // is <= newkey, do nothing and return false.  Otherwise,
    // replace "key" by "newkey", fixup the queue, and return
    // true.
    //
    public boolean decreaseKey(Handle<T> h, int newkey)
    {
    	
    	 int i = h.index;
    	 
    	 if(h.remove == true ||i>=pq.size() || h.key <=newkey){ 		 
    		 return false;		 
    	 }
    	 
    	 else{
    		
    		 pq.get(i).key = newkey;
    		 h.key = newkey;
    		 int last = pq.size()-1;
    		 
    		 if(pq.size() ==3){
    	    		if(h.key>pq.get(2).key){
    	    			swap(pq.get(h.index),pq.get(2));
    	    		} 		
    	    		
    	    	}
    		 if(pq.size()>3){
    			 int parentIndex = h.index/2;
    			 if(h.key<pq.get(parentIndex).key){
					 bubbleUp(pq.get(i),pq.get(parentIndex),i);
					 
				 }
    			 else if(i<last/2){
    				 
    				 if((h.index*2+1) <= pq.size()){
    				 int cMinKey = minChild(pq.get(h.index*2),pq.get(h.index*2+1)).key;
    				
    				 if(h.key>cMinKey){
    					h.index =  heapify(pq.get(i),last).h.index;   	
    				 }  
    				 }
    			 }
    			 		 
    			 
    		 }
    		
  		 
    		 return true;
    		 
    	 }
	     
    }
    
    
    // Get the key of the (key, value) pair associated with a 
    // given Handle. (This result is undefined if the handle no longer
    // refers to a pair in the queue.)
    //
    public int handleGetKey(Handle<T> h)
    {
    	int key = h.key;
	    return key;
    }

    // Get the value object of the (key, value) pair associated with a 
    // given Handle. (This result is undefined if the handle no longer
    // refers to a pair in the queue.)
    //
    public T handleGetValue(Handle<T> h)
    {
    	T value = h.value;
	    return value;
    }
    
 
    
    
    
    
    // Print every element of the queue in the order in which it appears
    // in the implementation (i.e. the array representing the heap).
    public String toString()
    {
    	String s = "";
    	for(int i =1;i<pq.size();++i){
    		
    		int index = pq.get(i).h.index;
    		int num = pq.get(i).key;
    		T value = (T) pq.get(i).value;
    
    		s += "[" + index + "]: (" + num + ", " + value +")\n";
    	}
    	
	return s;
    }
}
