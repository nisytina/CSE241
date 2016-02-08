public class StringTable {

	//
	// Create an empty table big enough to hold maxSize records.
	//
	
	
	//exp: record the number of times of doubling
	//inserted: record the number of Records inserted in the hash table
	
	private Record[] table;
	private int exp ;
	private int inserted;
	
	//overload constructors:
	//the first is the one that takes one parameter as size, to fulfill part 2, 
	//the initial size of hash table should be 2, ignoring the parameter passed by the main.
	
	public StringTable(int size) 
	{		 
	  table = new Record[2];
	}
	//default constructor
	public StringTable() 
	{		 
	  table = new Record[2];
	  inserted = 0;
	  exp = 1;
	}


	//
	// Insert a Record r into the table.  Return true if
	// successful, false if the table is full.  You shouldn't ever
	// get two insertions with the same key value, but you may
	// simply return false if this happens.
	//
	public boolean insert(Record r) 
	{ 

		boolean returnCode = true;
		//Compute the hash value
		int hashSKey = toHashKey(r.key);
		
		//store hash value to Record r, to make searching and comparison 
		//more efficient afterwards
		r.toHashValue = hashSKey;
		
		//use hash value to compute slot index si
		int si = baseHash(hashSKey)%table.length;
		
		//compute load factor
		double loadFactor = 1.0*inserted / table.length;
	
		//b0 checks if slot is deleted
		//b1 checks if slot is empty
		boolean b0 = false;
		boolean b1 = table[si]==null;
		
		if (!b1){
			b0 = table[si].toHashValue == -1;
			
		}			
		//if the slot si is empty and (loadfactor is less than 1/4 or exp is 31),
		//we can insert Record in it
		if((b0 || b1)&&(exp == 31 || loadFactor<=0.25)){
			table[si] = r;
			inserted++;

		}
		
		//if load factor is larger than 1/4, extend the table
		//if exp is 31. then the table can't be extended but still can insert Records in it 
		//until it is full.
		
		else if(exp!= 31 && loadFactor>0.25){
			int code = 0;
			
			//continue extend until the load factor is less than 1/4
			while(loadFactor>0.25){
				code = doubling(table);
				loadFactor = 1.0*inserted / table.length;
			}	
			
			//if extension succeeds, insert the Records into the updated hash table
			if (code == 0){
				insert(r); 
			}
			//otherwise, return not extensible
			else{
				returnCode = false;
			}
		}
		else{
			//counter counts number of slots checked
			int counter = 0;	
			
			// b2 checks if slot is not deleted
			// b3 checks if the hash function goes through every slot
			
			boolean b2 = table[si].toHashValue!=-1;
			boolean b3 = counter<table.length;
			
			//while the slot is not empty and not deleted and didn't go through every slot,
			//keep searching by adding step hash function value
			
			while(!b1&&b2&&b3){
				 //if the slot has a Record that is the same as the passed one,
				 // ignore the passed Record, return false.
				if(table[si].toHashValue == hashSKey && table[si].key.equals(r.key) ){
					returnCode = false;
					break;
				}
				
				si = (si+stepHash(hashSKey))%(table.length);
				counter++;
				
				//update the checking condition.
			     b1 = table[si]==null;
			     
			     if (!b1){
			    	 b2 = table[si].toHashValue!=-1;
					 b3 = counter<table.length;
			     }
				 
			}
			
			// when jumping out of the loop, 
			//first  possibility is we find an empty or deleted slot,
			//second is we find a repeated value,
			//third is the table is full.
			
			
			if (counter<table.length && returnCode){
				//if it is the first case, we insert the Record
				table[si] = r;
				inserted++;
			}
			else{
				//if the table is full, return false.
				returnCode = false;
			}
						
		}
		return returnCode; 
	}


	//
	// Delete a Record r from the table.  Note that you'll have to
	// find the record first unless you keep some extra information
	// in the Record structure.
	//
	public void remove(Record r) 
	{
		Record f = find(r.key);
		if (f != null){
			r.delete();
			inserted--;
		}

	}


	//
	// Find a record with a key matching the input.  Return the
	// record if it exists, or null if no matching record is found.
	//
	public Record find(String key) 
	{
		int hashSKey = toHashKey(key);
		int si = baseHash(hashSKey)%table.length;

		int counter = 0;
		//if the hash value is same, check the key string
		if (table[si]!=null){
			if (table[si].toHashValue==hashSKey && table[si].key.equals(key)){	
				return table[si];
				
			}
			else{
				
				boolean b1 = table[si]!=null;
				boolean b2 = false, b3 =false , b4 = false;
				if(b1){
					b2 = table[si].toHashValue!=hashSKey;
					b3 = !table[si].key.equals(key);
					b4 = counter<=table.length;
				}
								
				//keep looking if: 
				//a) the table is not empty and the Records are not the same
				//b) the slots haven't been all covered 
				
				while(b1&&(b2||(!b2&&b3))&&b4){
					si = (si+stepHash(hashSKey))%(table.length);
					counter++;
					
					b1 = table[si]!=null;
					if(b1){
						b2 = table[si].toHashValue!=hashSKey;
						b3 = !table[si].key.equals(key);
						b4 = counter<=table.length;
					}
									
				}

				// if the loop terminates, check the cause
				
				if (counter<table.length){
					//a) find an empty slot
					if(table[si] == null){
						return null;
					}
					//find the key
					else {
						return table[si];
					}
				}
				//cover all slots without finding the key
				else {
					return null;
				}
			}
		}

		else{
			return null;

		}

	}


	///////////////////////////////////////////////////////////////////////


	// Convert a String key into an integer that serves as input to hash
	// functions.  This mapping is based on the idea of a linear-congruential
	// pesudorandom number generator, in which successive values r_i are 
	// generated by computing
	//    r_i = ( A * r_(i-1) + B ) mod M
	// A is a large prime number, while B is a small increment thrown in
	// so that we don't just compute successive powers of A mod M.
	//
	// We modify the above generator by perturbing each r_i, adding in
	// the ith character of the string and its offset, to alter the
	// pseudorandom sequence.
	//
	int toHashKey(String s)
	{
		int A = 1952786893;
		int B = 367257;
		int v = B;

		for (int j = 0; j < s.length(); j++)
		{
			char c = s.charAt(j);
			v = A * (v + (int) c + j) + B;
		}

		if (v < 0) v = -v;
		return v;
	}

	int baseHash(int hashKey)
	{
		// Fill in your own hash function here
		
		//choose irrational number as A1 to avoid repeated bit pattern
		double A1 = (Math.sqrt(5)-1)/2;
		
		double f =hashKey*A1-Math.floor(hashKey*A1);
		double m = f*table.length;
		int h1 = (int)(Math.floor(m))+1;

		return h1;
	}

	int stepHash(int hashKey)
	{
		// Fill in your own hash function here
		
		//choose irrational number as A2 to avoid repeated bit pattern
		double A2 = Math.sqrt(2)/2;
		
		double f =hashKey*A2-Math.floor(hashKey*A2);
		double m = f*table.length;
		int h2 = (int)(Math.floor(m));
		//because table.length is 2^a, make h2 an odd so as to be relative prime to table length
		if(h2%2 == 0){
			h2 += 1;
		}

		return h2;  	

	}
	
	
	//The method is implemented internally, so we can define it as private
	//Basically, pass the hash table itself
	
	private int doubling(Record[] t){
		
		//check if the size is greater than 2^31, if it is and we continue doubling, there will 
		//be integer overflow.
		
		//return 0 means doubling succeeds, and 1 means fails,
				
		if (exp < 31){
			
			//create a new hash table with array of twice size than the original
			
			StringTable newStringTable = new StringTable();
			Record[] t1 = new Record[2*table.length];
			newStringTable.table = t1;
			
			//rehash from the original hash table
			for(int i = 0;i<t.length;i++){
				if(t[i] != null){
					newStringTable.insert(t[i]);
				}				
			}
			
			//update the original hash table
			table = newStringTable.table;
			//exp stores the power of 2, which also means number of times we do doubling 
			exp++;
			return 0;
		}
		
		else{
			return 1;
		}		
	}
}
