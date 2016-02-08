
public class Node<T> {
	
	public int key;
    public T value;
    public Handle<T> h;
    
    
   
    
	public Node(int key, T value, Handle<T> h) {
		super();
		this.key = key;
		this.value = value;
		this.h = h;
	}
	
	public Node(int key, Handle<T> h) {
		super();
		this.key = key;
		this.h = h;
	}
	
	
	

}
