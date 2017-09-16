package casper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import quadTree.*;

public class CasperCloaking {
	
	public static final int LX = 0,LY = 1,RX = 2,RY = 3;
	private int layer;
	private final int MAXNUM = 1000000;
	/*
	 * 初始化layer
	 */
	public void initial(QuadTree tree)
	{
		layer = tree.maxDepth;
	}
	/*
	 * Casper匿名算法函数
	 */
	public double[] Cloaking(QuadTree tree, int k, double Amin, int cid)
	{
		GridInfo gridInfo = HashTable.getGridInfo(cid);
		if(gridInfo.Number >= k && gridInfo.Area >= Amin)
		{
			return gridInfo.Rect;
		}
		else if(cid == 0)
		{
			System.out.println("No such cloaking area!");
			return null;
		}
		
		double[] currentRect = gridInfo.Rect;
		double width = currentRect[RX] - currentRect[LX];
		double height = currentRect[RY] - currentRect[LY];
		//记录候选的k值
		int k1=MAXNUM, k2=MAXNUM, k3=MAXNUM, k4=MAXNUM;
		double[] rect1=null, rect2=null, rect3=null, rect4=null;
		//判断X轴方向相邻单元格
		if(currentRect[LX]-width >= 0)
		{
			double[] current = new double[4];
			current[LX] = currentRect[LX];
			current[LY] = currentRect[LY];
			current[RX] = currentRect[RX];
			current[RY] = currentRect[RY];
			current[LX] = current[LX]-width;
			current[RX] = current[LX]+width;
			Vector<Integer> vectorCid = new Vector<Integer>();
			tree.search(current, vectorCid);
			int cidH = vectorCid.elementAt(vectorCid.size()-1);
			GridInfo gridHInfo;
			if(HashTable.gridHashTable.containsKey(cidH))
			{
				gridHInfo = HashTable.getGridInfo(cidH);
			}
			else
			{
				gridHInfo = new GridInfo();
			}
			if((k1=gridInfo.Number+gridHInfo.Number) >= k && gridInfo.Area+gridHInfo.Area >= Amin)
			{
				current[RX] = current[RX]+width;
				rect1 = current;
			}
		}
		if(currentRect[RX]+width <= tree.root.rect[RX])
		{	
			double[] current = new double[4];
			current[LX] = currentRect[LX];
			current[LY] = currentRect[LY];
			current[RX] = currentRect[RX];
			current[RY] = currentRect[RY];
			current[LX] = current[LX]+width;
			current[RX] = current[LX]+width;
			Vector<Integer> vectorCid = new Vector<Integer>();
			tree.search(current, vectorCid);
			int cidH = vectorCid.elementAt(vectorCid.size()-1);
			GridInfo gridHInfo;
			if(HashTable.gridHashTable.containsKey(cidH))
			{
				gridHInfo = HashTable.getGridInfo(cidH);
			}
			else
			{
				gridHInfo = new GridInfo();
			}
			if((k2=gridInfo.Number+gridHInfo.Number) >= k && gridInfo.Area+gridHInfo.Area >= Amin)
			{
				current[LX] = current[LX]-width;
				rect2 = current;
			}
		}
		//判断Y轴方向相邻单元格
		if(currentRect[LY]-height >= 0)
		{	
			double[] current = new double[4];
			current[LX] = currentRect[LX];
			current[LY] = currentRect[LY];
			current[RX] = currentRect[RX];
			current[RY] = currentRect[RY];
			current[LY] = current[LY]-height;
			current[RY] = current[LY]+height;
			Vector<Integer> vectorCid = new Vector<Integer>();
			tree.search(current, vectorCid);
			int cidV = vectorCid.elementAt(vectorCid.size()-1);
			GridInfo gridVInfo;
			if(HashTable.gridHashTable.containsKey(cidV))
			{
				gridVInfo = HashTable.getGridInfo(cidV);
			}
			else
			{
				gridVInfo = new GridInfo();
			}
			if((k3=gridInfo.Number+gridVInfo.Number) >= k && gridInfo.Area+gridVInfo.Area >= Amin)
			{
				current[RY] = current[RY]+height;
				rect3 = current;
			}
		}
		if(currentRect[RY]+height <= tree.root.rect[RY])
		{
			double[] current = new double[4];
			current[LX] = currentRect[LX];
			current[LY] = currentRect[LY];
			current[RX] = currentRect[RX];
			current[RY] = currentRect[RY];
			current[LY] = current[LY]+height;
			current[RY] = current[LY]+height;
			Vector<Integer> vectorCid = new Vector<Integer>();
			tree.search(current, vectorCid);
			int cidV = vectorCid.elementAt(vectorCid.size()-1);
			GridInfo gridVInfo;
			if(HashTable.gridHashTable.containsKey(cidV))
			{
				gridVInfo = HashTable.getGridInfo(cidV);
			}
			else
			{
				gridVInfo = new GridInfo();
			}
			if((k4=gridInfo.Number+gridVInfo.Number) >= k && gridInfo.Area+gridVInfo.Area >= Amin)
			{
				current[LY] = current[LY]-height;
				rect4 = current;
			}
		}
		//判断并返回最接近k值的那个区域
		HashMap<Integer,double[]> mp = new HashMap<Integer,double[]>();
		if(rect1 != null) {
			mp.put(k1, rect1);
		}
		if(rect2 != null) {
			mp.put(k2, rect2);
		}
		if(rect3 != null) {
			mp.put(k3, rect3);
		}
		if(rect4 != null) {
			mp.put(k4, rect4);
		}
		int[] ks = {k1,k2,k3,k4};
		Arrays.sort(ks);
		for(int i=0; i<4; i++)
		{
			if(mp.containsKey(ks[i]))
				return mp.get(ks[i]);
		}
		
		//判断父层单元格
		layer = layer - 1;
		int parentCid = HashTable.getParentCid(tree, layer, cid);
		if(parentCid > 0)
		{
			return Cloaking(tree, k, Amin, parentCid);
		}
		
		return	Cloaking(tree, k, Amin, 0);		
	}
	/*
	 * 辅助函数，计算最小值
	 */
	public int getMin(int[] ary)
	{
		int min = 0;
		for(int i=1; i<ary.length; i++)
		{
			if(ary[i] < ary[min])
				min = i;
		}
		return ary[min];
	}
}
