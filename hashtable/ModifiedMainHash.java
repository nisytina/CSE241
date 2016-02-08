
public class ModifiedMainHash {
	    
	    public static void main(String args[])
	    {
		int matchLength   = 0;
		String corpusSeq  = null;
		String patternSeq = null;
		String maskSeq    = null;
		
		if (args.length < 3)
		    {
			System.out.println("Syntax: Main <match length> " +
					   "<corpus file> <pattern file> " +
					   "[ <mask file> ]");
			return;
		    }
		else
		    {
			matchLength = Integer.parseInt(args[0]);
			corpusSeq   = SeqReader.readSeq(args[1]);
			patternSeq  = SeqReader.readSeq(args[2]);
			
			System.out.println("M = " + matchLength);
			System.out.println("CORPUS: " + corpusSeq.length() + " bases");
			System.out.println("PATTERN: " + patternSeq.length()+" bases");
			
			if (args.length > 3)
			    {
				maskSeq = SeqReader.readSeq(args[3]);
				System.out.println("MASK: "+maskSeq.length()+" bases");
			    }
		    }
		
		PartOneStringTable table = createTable(patternSeq, matchLength);
		
		if (maskSeq != null)
		    maskTable(table, maskSeq, matchLength);
		
		findMatches(table, corpusSeq, matchLength);
	    }
	    
	    
	    // Create a new StringTable containing all substrings of the pattern
	    // sequence.
	    // 
	    public static PartOneStringTable createTable(String patternSeq, int matchLength)
	    {
	    	PartOneStringTable table = new PartOneStringTable(patternSeq.length());
		
		for (int j = 0; j < patternSeq.length() - matchLength + 1; j++)
		    {
			String key = patternSeq.substring(j, j + matchLength);
			
			Record rec = table.find(key);
			
			if (rec == null) // not found -- need new Record
			    {
				rec = new Record(key);
				if (!table.insert(rec))
				    System.out.println("Error (insert): hash table is full!\n");
			    }
			
			rec.positions.add(new Integer(j));
		    }
		
		return table;
	    }
	    
	    
	    // Remove all substrings in the mask sequence from a StringTable.
	    // 
	    public static void maskTable(PartOneStringTable table, String maskSeq,
				  int matchLength)
	    {
		for (int j = 0; j < maskSeq.length() - matchLength + 1; j++)
		    {
			String key = maskSeq.substring(j, j + matchLength);
			
			Record rec = table.find(key);
			if (rec != null)
			    table.remove(rec);
		    }
	    }
	    
	    
	    // Find and print all matches between the corpus sequence and any
	    // string in a StringTable.
	    //
	    public static void findMatches(PartOneStringTable table, String corpusSeq,
					   int matchLength)
	    {
		for (int j = 0; j < corpusSeq.length() - matchLength + 1; j++)
		    {
			String key = corpusSeq.substring(j, j + matchLength);
			
			Record rec = table.find(key);
			if (rec != null)
			    {
				for (int k = 0; k < rec.positions.size(); k++)
				    {
					System.out.println(j + " " + 
							   rec.positions.get(k) +" "+ 
							   key);
				    }
			    }
		    }
	    }
	}

