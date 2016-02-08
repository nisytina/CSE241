
public class Result {
	public final static double INF = java.lang.Double.POSITIVE_INFINITY;
	
	private double minDis;
	private XYPoint p1 ;
	private XYPoint p2 ;
	
	public Result() {
		super();
		this.minDis = INF;
		this.p1 = new XYPoint();
		this.p2 = new XYPoint();
	}
	
	public static Result f = new Result();
	
	public double returnMin(){
		return f.minDis;
	}

	public XYPoint returnMinPoint1(){
		return f.p1;
	}
	
	public XYPoint returnMinPoint2(){
		return f.p2;
	}
	
	public void setMin(double dis){
		f.minDis = dis;
	}
	
	public void setMinPoint1(XYPoint a){
		f.p1=a;
	}
	
	public void setMinPoint2(XYPoint a){
		f.p2=a;
	}
	
}
