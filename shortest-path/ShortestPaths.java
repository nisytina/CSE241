import java.util.ArrayList;

//
// SHORTESTPATHS.JAVA
// Compute shortest paths in a graph.
//
// Your constructor should compute the actual shortest paths and
// maintain all the information needed to reconstruct them.  The
// returnPath() function should use this information to return the
// appropriate path of edge ID's from the start to the given end.
//
// Note that the start and end ID's should be mapped to vertices using
// the graph's get() function.
//
// You can ignore the input and startTime arguments to the constructor
// unless you are doing the extra credit.
//

class ShortestPaths {

	//
	// constructor
	//


	public ShortestPaths(Multigraph G, int startId, 
			Input input, int startTime) 
	{
		// your code here

		P = new PriorityQueue<Vertex>();
        
		//record the extraction result by extracMin()
		result = new ArrayList<Handle<Vertex>>();
		//record handle inserted in the priority queue
		handle = new ArrayList<Handle<Vertex>>();

		
		//initialize the handles and Pqueue
		int n = G.nVertices();  	
		for(int i = 0;i<n;++i){

			Handle<Vertex> ins= P.insert(Integer.MAX_VALUE, G.get(i));
			ins.setId(G.get(i).id());

			handle.add(ins);
		}

		//set start vertex
		P.decreaseKey(handle.get(startId), 0);
		
		//while Pqueue is not empty
		while(P.isEmpty()!=true){
           
			//extractMin and store it in result
			Vertex u = P.extractMin();   		
			Handle<Vertex> gethan = handle.get(u.id());
			result.add(gethan);
            
			//no more vertices can be reached from s 
			if(gethan.key==Integer.MAX_VALUE){
				break;
			}

			Vertex.EdgeIterator ed = u.adj();
            
			//keep going if there are more adjacent vertices
			while(ed.hasNext()){
				
				Edge e = ed.next();
				Vertex v = e.to();
				Handle<Vertex> gethan1 = handle.get(v.id());
                
				//the key value can be decreased, update handle information
				if(P.decreaseKey(gethan1,gethan.key+e.weight())){
					gethan1.key = gethan.key+e.weight();
					gethan1.parent = gethan;   
					gethan1.edge_id = e.id();

				}

			}

		}

	}


	//
	// returnPath()
	// Return an array containing a list of edge ID's forming
	// a shortest path from the start vertex to the specified
	// end vertex.
	//
	public int [] returnPath(int endId) 
	{ 
		// your code here
        
		//if start = end, return empty array
		if(endId == result.get(0).getId()){
			int []a = new int[1];
			return a;
		}

		ArrayList<Handle<Vertex>> ret = new ArrayList<Handle<Vertex>>();
		Handle<Vertex> u = handle.get(endId);
  
		Handle<Vertex> iter = u;
		//add the end vertex
		ret.add(u);
		
		//traverse back to add vertex's parent to the path
		while(iter.getId()!=result.get(0).getId()){
			if(iter.parent!=null){
				iter = iter.parent;
				ret.add(iter);

			}  		

		}
        
		//copy in a reverse order
		int size = ret.size()-1;
		int []a = new int[size];
		for(int i =size-1 ; i>=0;--i){
			a[size-i-1] = ret.get(i).edge_id;
		}



		return a;
	}

	private ArrayList<Handle<Vertex>> result;
	private PriorityQueue<Vertex> P;
	private ArrayList<Handle<Vertex>> handle;

}
