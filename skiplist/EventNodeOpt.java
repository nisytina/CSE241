import java.util.LinkedList;

public class EventNodeOpt {

	public double y;
    public EventNodeOpt [] next;
    public int h;
    public int eventCounter;
	public LinkedList<Event> eventDes;
	
	//constructors
	//overload
	public EventNodeOpt(){
		
	}
	
	public EventNodeOpt(int pillarH){
		h = pillarH;
		next = new EventNodeOpt[h];
		eventDes = new LinkedList<Event>();
		
	}
	
	public EventNodeOpt(Event e,int pillarH) {
		y = e.year;
		h = pillarH;
		eventDes = new LinkedList<Event>();
		eventDes.addFirst(e);
		next = new EventNodeOpt[h];
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
	
	EventNodeOpt getNext(){
		EventNodeOpt p = next[0];
		return p;
		
	}
	
	
	
	
	
}
