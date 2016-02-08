import java.util.LinkedList;

public class EventNode {
	
	public double y;
    public EventNode [] prev;
    public EventNode [] next;
    public int h;
    public int eventCounter ;
	public LinkedList<Event> eventDes;
	
	//constructor
	
	public EventNode(){
		
	}
	
	public EventNode(Event e,int pillorH) {
		y = e.year;
		h = pillorH;
		eventDes = new LinkedList<Event>();
		eventDes.addFirst(e);
		prev = new EventNode[h];
		next = new EventNode[h];
		eventCounter = 1;
				
	}
	
	void addEvent(Event e){
		eventDes.addFirst(e);
		eventCounter++;
	}
	
	Event getEvent(int i){
		Event g = eventDes.get(i);
		return g;
	}
	
	EventNode getPrev(){
		EventNode p = prev[0];
		return p;
		
	}
	
	
	
	
	
	
	

}
