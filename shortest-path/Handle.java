public class Handle<T> {
	
	
	public int key;
	public T value;
	private int id;
	public int index;
	public boolean remove;
	public Handle<T> parent;
	public int edge_id;
	
	public Handle() {
	
	}
	
	public Handle(int key, T value) {
		super();
		this.key = key;
		this.value = value;
		remove = false;
		
	}
	
	public Handle(int key, T value,int index,int id) {
		super();
		this.key = key;
		this.value = value;
		this.index = index;
		this.id = id;
		remove = false;
		
	}
	
	public void setId(int id){
		
		this.id = id;
	}
	
public int getId(){
		
		return this.id;
	}
	
	
	
	
}
