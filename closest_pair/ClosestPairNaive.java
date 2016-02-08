import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ClosestPairNaive {

	public final static double INF = java.lang.Double.POSITIVE_INFINITY;

	//
	// findClosestPair()
	//
	// Given a collection of nPoints points, find and ***print***
	//  * the closest pair of points
	//  * the distance between them
	// in the form "NAIVE (x1, y1) (x2, y2) distance"
	//

	// INPUTS:
	//  - points sorted in nondecreasing order by X coordinate
	//  - points sorted in nondecreasing order by Y coordinate
	//
	ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
	long time = threadMXBean.getCurrentThreadCpuTime();
	public static void findClosestPair(XYPoint points[], boolean print)
	{
		 int nPoints = points.length;

		//
		// Your code goes here!
		//
		double minDis = INF;
		XYPoint p1 = new XYPoint();
		XYPoint p2 = new XYPoint();
		double dis = 0;

		for (int i =0; i<nPoints-1;++i){
			for (int j = i+1;j<nPoints;++j){
				dis = points[i].dist(points[j]);
				if (dis < minDis){
					minDis = dis;
					p1 = points[i];
					p2 = points[j];

				}    		
			}
		}   	
        //print = false;
		if (print){
			System.out.println("NAIVE " + p1 + p2 + minDis);
		}
	}
}
