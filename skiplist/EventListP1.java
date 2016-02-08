//
// EVENTLIST.JAVA
// Skeleton code for your EventList collection type.
//
import java.util.*;

class EventListP1 {
    
    Random randseq;
    public EventNode ehead;
    public EventNode etail;
    public EventNode startNode;
    public int MAXH;
      
    ////////////////////////////////////////////////////////////////////
    // Here's a suitable geometric random number generator for choosing
    // pillar heights.  We use Java's ability to generate random booleans
    // to simulate coin flips.
    ////////////////////////////////////////////////////////////////////
    
    int randomHeight()
    {
	int v = 1;
	while (randseq.nextBoolean()) { v++; }
	return v;
    }

    
    //
    // Constructor
    //
    public EventListP1()
    {
	randseq = new Random(58234); // You may seed the PRNG however you like.
	int h = 50; // head and tail height;
	//init the EventList's head and tail
	ehead = new EventNode();
	etail = new EventNode();	
	ehead.next = new EventNode[h];
	ehead.prev = null;
	ehead.y = Double.NEGATIVE_INFINITY;
	etail.prev = new EventNode[h];
	etail.next = null;
	etail.y = Double.POSITIVE_INFINITY;
	startNode = new EventNode();
	startNode =null;
	MAXH=0;
    }

    
    //
    // Add an Event to the list.
    //
    public void insert(Event e)
    {
    	
    	//TRIVIAL CASE:
    	//the list is empty,build a new Node
    	if (MAXH == 0){
    		int pillorH = randomHeight();
    		EventNode newNode = new EventNode(e,pillorH); 
    		
    		//link all levels to the head and tail
    		for(int i = 0; i < pillorH;++i){
    			
    			newNode.prev[i] = ehead;
    			newNode.next[i] = etail;
    			ehead.next[i]=newNode;
    			etail.prev[i] = newNode;
    			
    		}
    		//track the highest pillor;
    		//MAXH = pillorH;
    		startNode = newNode;
    		MAXH = startNode.h;
	 		
    	}
    	
    	// if the list is not empty
    	else{
    		
    		EventNode ins = find(e.year);
    		//if find the year, add event
    		if(ins.y == e.year){
    			ins.addEvent(e);
    		}
    		// if not, allocate a node to insert
    		else{
    			int pillorH = randomHeight();
    			EventNode newN = new EventNode(e,pillorH);
    			if(pillorH>MAXH){
    				for(int i = MAXH; i < pillorH;++i){ 	    			
    	    			newN.prev[i] = ehead;
    	    			newN.next[i] = etail;
    	    			ehead.next[i] = newN;
    	    			etail.prev[i] = newN;    	    			
    	    		}
    				MAXH = pillorH;
    				link(newN,pillorH);      			
        			startNode = newN;
    		  				
    			}
    			else{
    				link(newN,pillorH);    
    			}
    			  			
    		}
    		   	  		
    	}
	
    }

    
    //
    // Remove all Events in the list with the specified year.
    //
    public void remove(int year)
    {
    	int l = startNode.h-1;
    	//just the year I need
    	if(year == startNode.y){
    		//update the next startnode
    		while(l>=0 && startNode.prev[l] == ehead && startNode.next[l] == etail){
    			ehead.next[l] = etail;
    			etail.prev[l] = ehead;
    			startNode.prev[l]=null;
    			startNode.next[l]=null;
    			l--;			
    		}
    		if(l >= 0){
    			//if the 2nd maxh is before startnode
    			if(startNode.prev[l] != ehead){
    				EventNode newStartNode = startNode.prev[l];
    				while(l>=0){
    					unlink(startNode,l);
    					l--;  					
    				}
    				startNode = newStartNode;
    			}
    			else {
    				EventNode newStartNode = startNode.next[l];
    				while(l>=0){
    					unlink(startNode,l);
    					l--;  					
    				}
    				startNode = newStartNode;
    			}
    		}		
    		
    	}
    	// then find the year Node, then remove the node
    	else{
    		
    		EventNode removeN = find(year);
    		
    		if(removeN.y == year){
    			for(int i = 0; i < removeN.h;i++){
            		unlink(removeN,i);
            	}
        		
    		}
        	
    	}
    	
    	
    	
    		
    }
    
    //
    // Find all events with greatest year <= input year
    //
    public Event [] findMostRecent(int year)
    {
    		
    	EventNode recent =  find(year);
    	
    	if(recent != ehead ){
    		
    		int num = recent.eventCounter;
        	Event [] array = new Event[num];
        	for(int i = 0;i<num;++i){
        		array[i] = recent.getEvent(i);
        	}
        	return array;
    	}
    	else{
    		return null;
    	}

    }
    
    
    //
    // Find all Events within the specific range of years (inclusive).
    //
    public Event [] findRange(int first, int last)
    {
    	
    	EventNode recent =  find(last);
    	
    		if(recent!= ehead){
    		int sum =0;
        	int step = 0 ;
        	
        	EventNode iter = recent;
        	if(iter.y<first){
        		return null;
        	}
        	
        	while(iter.y>=first){
        		sum += iter.eventCounter;
        		iter = iter.getPrev();
        		step++;
        	}
        	
        	Event [] array = new Event[sum];
        	
        	int i = sum - 1;
        	EventNode iter2 = recent;
        	
        	for(int s = 0;s<step;++s){
        		
        		for(int m = 0;m<iter2.eventCounter;m++){
        			
        			array[i] = iter2.getEvent(m);
        			if(i>0){
        				i--;
        			}       			
        		} 
        		
        		iter2 = iter2.getPrev();
        		
        	}
        	return array;
    		}
    		else{
    			return null;
    		}
    	 
    }
    
    //find if year is in the list
    EventNode find(int year){
    	
    	if(year==startNode.y){
    		return startNode;
    	}
    	else if(year > startNode.y){
    		int l = startNode.h-1;
        	EventNode iter = startNode;
        	while(l>=0){
        		EventNode nextN = iter.next[l];
        		if(nextN.y == year){
        			return nextN;
        		}
        		else if(nextN.y <year){
        			iter = nextN;
        			nextN = iter.next[l];
        		}
        		else{
        			l--;
        			
        		}
        		
        	}
        	return iter;
    	}
    	else{
    		
    		int l = startNode.h-1;
        	EventNode iter = startNode;
        	EventNode prevN = iter.prev[l];
        	while(l>=0){
        		
        		if(prevN.y == year){
        			return prevN;
        		}
        		else if(prevN.y >year){
        			iter = prevN;
        			prevN = iter.prev[l];
        		}
        		else{
        			l--;
        		
        		}
        		
        	}
        	iter = prevN;
        	return iter;
    	}
    	
    	
    }
    
    void link(EventNode e,int H){
    	if(e.y>startNode.y){
    		int l = startNode.h-1;
        	EventNode iter = startNode;
        	while(l>=0){
        		EventNode nextN = iter.next[l];
        		if(nextN.y<e.y){
        			iter = nextN;
        			nextN = iter.next[l];
        		}
        		else{
        			if(l<H){
        				iter.next[l] = e;
        				e.next[l]=nextN;
        				nextN.prev[l] = e;
        				e.prev[l]=iter;
        				
        			}
        			l--;
        		}
        		
        	}
    	}
    	else{
    		int l = startNode.h-1;
        	EventNode iter = startNode;
        	while(l>=0){
        		EventNode prevN = iter.prev[l];
        		if(prevN.y>e.y){
        			iter = prevN;
        			prevN = iter.prev[l];
        		}
        		else{
        			if(l<H){
        				iter.prev[l] = e;
        				e.prev[l]=prevN;
        				prevN.next[l] = e;
        				e.next[l]=iter;
        				
        			}
        			l--;
        		}
        		
        	}
    		
    	}
    	
    }
    
    void unlink(EventNode m,int level){
    	m.prev[level].next[level] = m.next[level];
		m.next[level].prev[level] = m.prev[level];
		m.prev[level] = null;
		m.next[level]=null;
    	   	
    }
}
