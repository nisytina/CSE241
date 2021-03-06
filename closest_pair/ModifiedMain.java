//
// MAIN.JAVA
// Main driver code for CSE 241 Closest Pair Lab.
//
// WARNING: ANY CHANGES YOU MAKE TO THIS FILE WILL BE DISCARDED BY THE
// AUTO-GRADER!  Make sure your code works with the unmodified
// original driver before you turn it in.  (You may wish to modify
// your local copy to do the timing experiments requested by the lab.)

import java.util.*;

public class ModifiedMain{

	static final long seed = 87654321;

	public static void main(String args[])
	{


		PRNG prng = new PRNG(seed); // seed random number generator
		XYPoint points [];
		int nPoints = 0;
		String fileName;

		if (args.length >= 1)
		{
			fileName = args[0];
		}
		else
		{
			System.out.println("Syntax: Main [ <filename> | @<numPoints> ]");
			return;
		}


		// A filename argument of the form '@x', where x is a non-negative
		// integer, allocates x random points.  Any other argument is
		// assumed to be a file from which points are read.

		if (fileName.charAt(0) != '@')
		{
			points = PointReader.readXYPoints(fileName);
			nPoints = points.length;
		}
		else
		{
			nPoints = Integer.parseInt(fileName.substring(1));
			points = null;
		}

		if (nPoints < 2)
		{
			System.err.println("ERROR: need at least two points");
			return;
		}

		// Generate a set of points if one was not read.
		// When timing, call genPointsAtRandom() as shown
		// each time you want a new set of points.
		


		if (points == null){
			points = genPointsAtRandom(nPoints, prng);
			int iterrN = 100;
			
			double minDC = Double.POSITIVE_INFINITY;
			double maxDC = 0;
			double aveDC = 0;
			double sumDC = 0;
			double DDc = 0;
			long elapsedTimeDC =0;
			
			double minN = Double.POSITIVE_INFINITY;
			double maxN = 0;
			double aveN = 0;
			double sumN = 0;
			double DN = 0;
			long elapsedTimeN =0;
							
			for(int i = 0; i<iterrN;++i){
				
				// run the DC algorithm
				{
					XComparator lessThanX = new XComparator();
					YComparator lessThanY = new YComparator();

					/////////////////////////////////////////////////////////////////
					// DC CLOSEST-PAIR ALGORITHM STARTS HERE
					
					Date startTime = new Date();

					// Ensure sorting precondition for divide-and-conquer CP
					// algorithm.  NB: you should *not* have to call sort() in
					// your own code!

					// The algorithm expects two arrays containing the same points.
					XYPoint pointsByX [] = Arrays.copyOf(points, points.length);
					XYPoint pointsByY [] = Arrays.copyOf(points, points.length);

					Arrays.sort(pointsByX, lessThanX); // sort by x-coord
					Arrays.sort(pointsByY, lessThanY); // sort by y-coord

					ClosestPairDC.findClosestPair(pointsByX, pointsByY, true);

					Date endTime = new Date();

					// DC CLOSEST-PAIR ALGORITHM ENDS HERE
					/////////////////////////////////////////////////////////////////

					elapsedTimeDC = endTime.getTime() - startTime.getTime();
					if (elapsedTimeDC < minDC){
						minDC = elapsedTimeDC;
					}
					if (elapsedTimeDC>maxDC){
						maxDC = elapsedTimeDC;
					}
					sumDC += elapsedTimeDC;
					DDc +=elapsedTimeDC*elapsedTimeDC;

					}

		// run the naive algorithm
		{
			Date startTime = new Date();

			ClosestPairNaive.findClosestPair(points, true);

			Date endTime = new Date();

			elapsedTimeN = endTime.getTime() - startTime.getTime();
			
			if (elapsedTimeN < minN){
				minN = elapsedTimeN;
			}
			if (elapsedTimeN>maxN){
				maxN = elapsedTimeN;
			}
			sumN += elapsedTimeN;
			DN +=elapsedTimeN*elapsedTimeN;

		
		}
			}
			System.out.println("For n = " + points.length + 
					", the min elapsed time is " +
					minN + " milliseconds." + "the max elapsed time is " +
					maxN + " milliseconds." + "the average elapsed time is " +
					sumN/iterrN + " milliseconds."+ "the average D^2 elapsed time is " +
					DN/iterrN + " milliseconds.");
			System.out.println("");
			
			System.out.println("For n = " + points.length + 
					", the min elapsed time is " +
					minDC + " milliseconds." + "the max elapsed time is " +
					maxDC + " milliseconds." + "the average elapsed time is " +
					sumDC/iterrN + " milliseconds."+ "the average D^2 elapsed time is " +
					DDc/iterrN + " milliseconds.");
			System.out.println("");
	}
	}

	//
	// genPointsAtRandom()
	// Generate an array of specified size containing
	// points with coordinates chosen at random, using
	// the specified random sequence generator.
	//

	static XYPoint[] genPointsAtRandom(int nPoints, 
			PRNG prng) 
	{
		XYPoint points[] = new XYPoint [nPoints];

		double x = 0.0;
		double y = 0.0;

		double step = Math.sqrt(nPoints);

		for (int j = 0; j < nPoints; j++) 
		{
			// jitter next point's X coordinate
			x += 10000.0 * (prng.nextDouble() - 0.5);

			// move the Y coordinate a random amount up,
			// while keeping it within limits [0 .. nPoints)
			y = (y + step * prng.nextDouble()) % nPoints;

			points[j] = new XYPoint((int) Math.round(x), 
					(int) Math.round(y));
		}

		return points;
	}

}
