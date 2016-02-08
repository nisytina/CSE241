//import java.util.Arrays;

public class ClosestPairDC {

	public final static double INF = java.lang.Double.POSITIVE_INFINITY;

	//
	// findClosestPair()
	//
	// Given a collection of nPoints points, find and ***print***
	//  * the closest pair of points
	//  * the distance between them
	// in the form "DC (x1, y1) (x2, y2) distance"
	//

	// INPUTS:
	//  - points sorted in nondecreasing order by X coordinate
	//  - points sorted in nondecreasing order by Y coordinate
	//


	public static void findClosestPair(XYPoint pointsByX[], 
			XYPoint pointsByY[],
			boolean print)
	{
		int nPoints = pointsByX.length;
		//
		// Your code goes here!
		//	
		double dis=Result.f.returnMin();

		if (nPoints == 1){
			dis = INF;

		}
		else if (nPoints == 2){
			dis = pointsByX[0].dist(pointsByX[1]);
			if(dis<Result.f.returnMin()){
				Result.f.setMinPoint1(pointsByX[0]);
				Result.f.setMinPoint2(pointsByX[1]);
				Result.f.setMin(dis);
			}
		}
		else{
			int midP = (int)(nPoints/2+1)-1;

			XYPoint []XL = new XYPoint[midP+1];
			XYPoint []XR = new XYPoint[nPoints-midP-1];
			XYPoint []YL = new XYPoint[midP+1];
			XYPoint []YR = new XYPoint[nPoints-midP-1];

			for (int i = 0;i<=midP;i++){
				XL[i] = pointsByX[i];
			}
			for(int i=0,j = midP+1;j<=nPoints-1;i++,j++){
				XR[i] = pointsByX[j];

			}
			int l=0,r=0;

			for(int i=0;i<nPoints;i++){
				if(pointsByY[i].isLeftOf(XR[0])){
					YL[l]=pointsByY[i];
					l++;
				}
				else{
					YR[r]=pointsByY[i];
					r++;
				}
			}
			boolean notPrint = false;

			findClosestPair(XL, YL,notPrint);
			double disL=Result.f.returnMin();
			findClosestPair(XR, YR,notPrint);
			double disR=Result.f.returnMin();

			double minLR = min(disL, disR);
			Combine(pointsByY, pointsByX[midP],nPoints,minLR );

			//print = false;
			if(print){
				System.out.println("DC " + Result.f.returnMinPoint1() + Result.f.returnMinPoint2() + Result.f.returnMin());
			}
		}
	}

	public static void Combine(XYPoint[] pointsByY, XYPoint pointsByXmid,int nPoints, double minLR){

		int counter=0;
		XYPoint[] pointsYStrip = new XYPoint[nPoints];

		for(int i= 0,j=0;i<nPoints;i++)
		{
			if(-minLR<pointsByY[i].x-pointsByXmid.x&&pointsByY[i].x-pointsByXmid.x<minLR){
				pointsYStrip[j] = pointsByY[i];
				j++;
				counter++;
			}
		}

		for (int j=0;j<=counter-2;j++){
			for(int k = j+1;k<=counter-1&&pointsYStrip[k].y-pointsYStrip[j].y<minLR;k++){
				double dis = pointsYStrip[j].dist(pointsYStrip[k]);
				if(Result.f.returnMin()>dis){
					Result.f.setMinPoint1(pointsYStrip[j]);
					Result.f.setMinPoint2(pointsYStrip[k]);
					Result.f.setMin(dis);
				}
			}
		}   	  	
	}

	public static double min(double a,double b){
		double min =a;
		if(b<min){
			min = b;
		}
		return min;
	}




}
