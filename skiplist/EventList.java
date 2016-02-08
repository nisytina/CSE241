//
// EVENTLIST.JAVA
// Skeleton code for your EventList collection type.
//
import java.util.*;

class EventList {

	Random randseq;
	public EventNodeOpt etail;
	public EventNodeOpt startNode;

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
	public EventList()
	{
		randseq = new Random(58234); // You may seed the PRNG however you like.

		//initilialize the tail
		etail = new EventNodeOpt(1);	
		etail.next[0] = null;
		etail.y = Double.POSITIVE_INFINITY;
		//initilialize the startNode, and link to tail
		startNode = new EventNodeOpt(1);
		startNode.next[0]=etail;

	}

	//
	// Add an Event to the list.
	//
	public void insert(Event e)
	{


		//if the list contains no event,add to the startNode
		if (startNode.eventCounter ==0){

			startNode.y = e.year;
			startNode.addEvent(e);    		

		}

		// if the list is not empty
		else{

			EventNodeOpt ins = find(e.year);
			//if find the year, add event
			if(ins.y == e.year){
				ins.addEvent(e);
			}
			//if the the new allocated node is before the startNode, update the startNode
			else if(ins.y>e.year){
				int pillarH = startNode.h;
				EventNodeOpt newN = new EventNodeOpt(e,pillarH);
				for(int i = 0;i<pillarH;i++){
					newN.next[i] = startNode;
				}

				startNode = newN;

			}
			// if not, allocate a node to insert in the middle of the list
			else{
				int pillarH = randomHeight();
				if(pillarH > startNode.h){
					//update the height of startNode
					int H = startNode.h;
					while(H<pillarH){
					H *=2;
					}
					startNode = replaceStart(startNode,H);
				}

				EventNodeOpt newN = new EventNodeOpt(e,pillarH);  			
				link(newN,pillarH);    

			}

		}

	}


	//
	// Remove all Events in the list with the specified year.
	//
	public void remove(int year)
	{

		if(year==startNode.y){

			//if the only Node is the startNode, then don't move startNode, 
			//just remove the events from the node, and zero out other features
			if(startNode.next[0] == etail){
				startNode.eventCounter = 0;
				startNode.eventDes = null;
				startNode.y = 0;

			}

			else{
				//unlink the startNode and update startNode
				EventNodeOpt newS = startNode.next[0];
				int l = startNode.h-1;
				do{
					unlink(startNode,l);	
					l--;

				}while(l>=0);
				startNode = newS;
			}

		}
		//if it is not the startNode, go through the skip list to unlink the node
		else {
			int l = startNode.h-1;
			EventNodeOpt iter = startNode;

			while(l>=0){
				EventNodeOpt nextN = iter.next[l];
				//if found, remove the level
				if(nextN.y == year){

					iter.next[l] = nextN.next[l];
					unlink(nextN,l);
					l--;
				}
				//keep going if next < year
				else if(nextN.y <year){
					iter = nextN;
					nextN = iter.next[l];
				}
				//next > year, go down
				else{	
					l--;
				}

			}
		}

	}

	//
	// Find all events with greatest year <= input year
	//
	public Event [] findMostRecent(int year)
	{

		//find the node that is <= year
		EventNodeOpt recent = find(year);

		//if the year is within the range stored in list
		if(recent.y <= year){
			int num = recent.eventCounter;
			//make an array to store Events
			Event [] array = new Event[num];
			for(int i = 0;i<num;++i){
				array[i] = recent.getEvent(i);
			}
			return array;
		}
		//if not within range, not found
		else{
			return null;
		}

	}


	//
	// Find all Events within the specific range of years (inclusive).
	//
	public Event [] findRange(int first, int last)
	{

		EventNodeOpt recent =  find(first);
		//
		int sum = 0;
		int step = 1;
		//whether find returned node is within search range
		int flag = 0;

		EventNodeOpt iter = new EventNodeOpt();
		
		//because find always return the node <= year,
		//make sure the iter node is within range
		
		if(recent.y == first){
			iter = recent;
		}
		else{
			//startNode is the returned iter node
			if(recent.y>first){
				//if the search range is out of list range, return null
				if(recent.y>last){
					return null;
				}
				//else iter starts from startNode
				else{
					iter = recent;
				}
			}
			//returned node is before first, so take one step to fall within search range
			else{
				//fall out of range
				if(recent.next[0].y > last){
					return null;
				}
				else{
					iter = recent.next[0];
					flag =1;
				}
				
			}

		}				

		//go through list to calculate array size and copy events into an array
		while(iter.y<=last){
			sum += iter.eventCounter;
			iter = iter.getNext();
			if(iter.y<=last){
				step++;
			}				
		}

		    //make array to store events
			Event [] array = new Event[sum];
			int i = sum -1;

			EventNodeOpt iter2 = new EventNodeOpt();
			if(flag == 0){
				iter2 = recent;
			}
			else{
				iter2 = recent.next[0];
			}

			//iter to store events, takes O(m_total)
			for(int s = 0;s<step;++s){

				for(int m = 0;m<iter2.eventCounter;m++){

					array[i] = iter2.getEvent(m);
					if(i>0){
						i--;
					}       			
				} 
				//move one step forward
				iter2 = iter2.getNext();

			}
			return array;
		}


	//find if year is in the list, return the node<=year if year>min, otherwise node is startNode
	EventNodeOpt find(int year){

		if(year==startNode.y){
			return startNode;
		}
		else {
			int l = startNode.h-1;
			EventNodeOpt iter = startNode;
			while(l>=0){
				EventNodeOpt nextN = iter.next[l];
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


	}

	//allocate new startNode
	EventNodeOpt replaceStart(EventNodeOpt startNode, int pillarH){

		EventNodeOpt newStart = new EventNodeOpt(pillarH);
		//link the above part to tail
		for(int i = pillarH -1; i>=startNode.h;i--){
			newStart.next[i] = etail;
		}

		//link the under part to the old startNode's next
		for(int i = startNode.h -1;i>=0;i--){
			newStart.next[i] = startNode.next[i];
		}

		//copy data into the newNode from old startNode
		newStart.y=startNode.y;
		newStart.h = pillarH;
		newStart.eventCounter = startNode.eventCounter;
		newStart.eventDes = startNode.eventDes; 	
		
		return newStart; 	

	}

	void link(EventNodeOpt e,int H){

		int l = startNode.h-1;
		EventNodeOpt iter = startNode;
		while(l>=0){
			EventNodeOpt nextN = iter.next[l];
			if(nextN.y<e.y){
				iter = nextN;
				nextN = iter.next[l];
			}
			else{
				if(l<H){
					iter.next[l] = e;
					e.next[l]=nextN;       				
				}
				l--;
			}

		}

	}

	void unlink(EventNodeOpt m,int level){
		m.next[level]=null;

	}
}

