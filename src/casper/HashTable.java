package casper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import quadTree.*;

public class HashTable {
	//网格单元哈希表
	public static HashMap<Integer,GridInfo> gridHashTable = new HashMap<Integer,GridInfo>();
	//用户与网格对应关系的哈希表
	public static HashMap<Integer,Integer> userHashTable = new HashMap<Integer,Integer>();
	//获取网格单元属性信息
	public static GridInfo getGridInfo(Integer cid)
	{
		return gridHashTable.get(cid);
	}
	//网格单元信息存入哈希表
	public static void putGridInfo(Integer cid, GridInfo gridInfo)
	{
		gridHashTable.put(cid, gridInfo);
	}
	//由uid获取cid
	public static int getCid(int uid)
	{
		return userHashTable.get(uid);
	}
	//uid-cid键值对放入哈希表
	public static void putUserToCid(int uid, int cid)
	{
		userHashTable.put(uid, cid);
	}
	//获取区域定位
	public static double[] getRect(int cid)
	{
		GridInfo gridInfo = getGridInfo(cid);
		return gridInfo.Rect;
	}
	//获取区域用户数
	public static int getRectNumber(int cid)
	{
		GridInfo gridInfo = getGridInfo(cid);
		return gridInfo.Number;
	}
	//获取区域面积
	public static double getRectArea(int cid)
	{
		GridInfo gridInfo = getGridInfo(cid);
		return gridInfo.Area;
	}
	/*
	 * 获取父节点cid,layer为当前cid所在层
	 */
	public static int getParentCid(QuadTree tree, int layer, int cid)
	{
		ArrayList<QuadTreeNode> layerNodeList = tree.getLayerList(layer);
		double rectGrid[] = tree.searchRect(layerNodeList, cid);
		Vector<Integer> vectorCid = new Vector<Integer>();
		tree.search(rectGrid, vectorCid);
		if(vectorCid.size() >= 2)
		{
			return vectorCid.elementAt(vectorCid.size()-2);
		}
		else
			return -1;
	}
	/*
	 * 哈希函数,对当前时间戳内每个新注册或已注册用户进行分析
	 */
	public static void userHashUpdate(QuadTree tree, int uid, double[] location)
	{
		double x = location[0], y = location[1];
		double rectUser[] = {x,y,x,y};
		//获取当前用户所在区域及其父节点区域cid向量
		Vector<Integer> vectorCid = new Vector<Integer>();
		tree.search(rectUser, vectorCid);
		
		//更新userHashTable
		Integer cidNew = vectorCid.elementAt(vectorCid.size()-1);
		if(userHashTable.containsKey(uid))
		{
			Integer cidOld = getCid(uid);
			if(!cidNew.equals(cidOld)){
				putUserToCid(uid, cidNew);
			}	
		}
		else{
			putUserToCid(uid, cidNew);
		}
	}
	/*
	 * 哈希函数，对当前时间戳的gridHashTable进行更新
	 */
	public static void gridHashUpdate(QuadTree tree)
	{
		Iterator<Integer> ite = userHashTable.values().iterator();
		Map<Integer, Integer> countMap =new HashMap<Integer, Integer>();
		while(ite.hasNext())
		{
			Integer cid = ite.next();
			if(countMap.containsKey(cid))
			{
				countMap.put(cid, Integer.valueOf(countMap.get(cid).intValue() + 1));
			}
			else{
				countMap.put(cid, new Integer(1));
			}
		}
		//更新cid单元格
		for(Iterator<Integer> iter = countMap.keySet().iterator(); iter.hasNext();) 
		{
			Integer cid = (Integer)iter.next();
			Integer num = (Integer)countMap.get(cid);
			ArrayList<QuadTreeNode> layerNodeList = tree.getLayerList(tree.maxDepth-1);
			double rectGrid[] = tree.searchRect(layerNodeList, cid);
			Vector<Integer> vectorCid = new Vector<Integer>();
			tree.search(rectGrid, vectorCid);
			if(!gridHashTable.containsKey(cid))
			{
				GridInfo grid = new GridInfo();
				grid.setUser(num);
				grid.setRect(rectGrid);
				putGridInfo(cid, grid);
			}
			else if(gridHashTable.containsKey(cid))
			{
				GridInfo grid = getGridInfo(cid);
				grid.setUser(num);
				putGridInfo(cid, grid);
			}
			//更新cid的所有父层单元格
			for(int i=vectorCid.size()-2,layer=tree.maxDepth-2; layer>=0; i--,layer--)
			{
				if(layer == 0)
				{
					double rootRect[] = tree.root.rect;
					if(!gridHashTable.containsKey(0))
					{
						GridInfo grid = new GridInfo();
						grid.addUser(num);
						grid.setRect(rootRect);
						putGridInfo(0, grid);
					}
					else if(gridHashTable.containsKey(0))
					{
						GridInfo grid = getGridInfo(0);
						grid.addUser(num);
						putGridInfo(0, grid);
					}
					break;
				}
				int parentCid = vectorCid.elementAt(i);
				ArrayList<QuadTreeNode> parentLayer = tree.getLayerList(layer);
				double parentRect[] = tree.searchRect(parentLayer, parentCid);
				if(!gridHashTable.containsKey(parentCid))
				{
					GridInfo grid = new GridInfo();
					grid.setUser(num);
					grid.setRect(parentRect);
					putGridInfo(parentCid, grid);
				}
				else if(gridHashTable.containsKey(parentCid))
				{
					GridInfo grid = getGridInfo(parentCid);
					grid.addUser(num);
					putGridInfo(parentCid, grid);
				}
			}
		}	
	}
	/*
	 * 哈希函数，对每个注销用户分析
	 */
	public static void hashFunctionRemove(QuadTree tree, int uid)
	{	
		if(userHashTable.containsKey(uid))
		{
			//userHashTable移除uid对应项
			Integer cidOld = getCid(uid);
			userHashTable.remove(uid);
			
			//gridHashTable将cidOld及其所有父节点用户数减少1
			int layer = tree.maxDepth-1;
			int cid = cidOld;
			while(layer >= 0 )
			{
				if(gridHashTable.containsKey(cid))
				{
					GridInfo grid = getGridInfo(cid);
					grid.removeUser();
					putGridInfo(cid, grid);
				}
				int parentCid = getParentCid(tree, layer, cid);
				cid = parentCid;
				layer --;
				if(layer == 0)
				{
					if(gridHashTable.containsKey(0))
					{
						GridInfo grid = getGridInfo(0);
						grid.removeUser();
						putGridInfo(0, grid);
					}
					break;
				}
			}
		}
	}
}
