package casper;

public class GridInfo {
	public int Number; //该网格单元的用户数量
	public double Area; //该网格单元的面积
	public double[] Rect = new double[4]; //该网格的定位
	public GridInfo(){
		this.Number = 0;
		this.Area = 0;
	}
	public GridInfo(int Number, double Area)
	{
		this.Number = Number;
		this.Area = Area;
	}
	public void setUser(int number)
	{
		this.Number = number;
	}
	public void addUser(int number)
	{
		this.Number = this.Number + number;
	}
	public void removeUser()
	{
		this.Number = this.Number - 1;
	}
	public void setRect(double[] rect)
	{
		this.Rect = rect;
		this.Area = (rect[2]-rect[0])*(rect[3]-rect[1]);
	}
}
