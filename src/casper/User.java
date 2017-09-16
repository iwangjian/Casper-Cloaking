package casper;

public class User {
	private int Uid; //用户id
	private double[] location = new double[2]; //用户位置
	private int k; //k为匿名用户人数
	private double Amin; //Amin为最小的匿名区域面积
	public User(){
		
	}
	public User(int uid, double x, double y)
	{
		this.Uid = uid;
		this.location[0] = x;
		this.location[1] = y;
	}
	public User(int uid, double x, double y, int k, double Amin)
	{
		this.Uid = uid;
		this.location[0] = x;
		this.location[1] = y;
		this.k = k;
		this.Amin = Amin;
	}
	public double[] getLocation()
	{
		return this.location;
	}
	public void setProfile(int k, double Amin)
	{
		this.k = k;
		this.Amin = Amin;
	}
	public int getUid()
	{
		return this.Uid;
	}
	public double getK()
	{
		return this.k;
	}
	public double getAmin()
	{
		return this.Amin;
	}
}
