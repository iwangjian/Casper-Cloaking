package casper;

import java.io.BufferedReader;
import java.util.Vector;

import quadTree.QuadTree;

public class DataHandler {
	/*
	 * 处理NG节点生成器的输出文件数据
	 */
	public static void handleData(QuadTree tree, BufferedReader buff, int time, Vector<User> dataSet)
	{
		try 
		{
			String line;
			HashTable.userHashTable.clear();
			HashTable.gridHashTable.clear();
			if(!dataSet.isEmpty())
			{
				User u = dataSet.firstElement();
				HashTable.userHashUpdate(tree, u.getUid(),u.getLocation());
			}
			while((line=buff.readLine())!=null)
			{
				String pointType;
				int pointId;
				double pointX, pointY;
				int timestamp;
				//截取NG生成器输出文件中每个节点的必要信息
				int endIndex1 = line.indexOf("\t");
				int endIndex2 = line.indexOf("\t", endIndex1+1);
				int endIndex3 = line.indexOf("\t", endIndex2+1);
				int endIndex4 = line.indexOf("\t", endIndex3+1);
				int endIndex5 = line.indexOf("\t", endIndex4+1);
				int endIndex6 = line.indexOf("\t", endIndex5+1);
				int endIndex7 = line.indexOf("\t", endIndex6+1);
				pointType = line.substring(0, endIndex1);
				pointId = Integer.parseInt(line.substring(endIndex1+1, endIndex2));
				timestamp = Integer.parseInt(line.substring(endIndex4+1, endIndex5));
				pointX = Double.valueOf(line.substring(endIndex5+1, endIndex6));
				pointY = Double.valueOf(line.substring(endIndex6+1, endIndex7));
				User user = new User(pointId, pointX, pointY);
				dataSet.add(user);
				//更新userHashTable
				if(timestamp == time)
				{
					if(pointType.equals("newpoint") || pointType.equals("point"))
					{
						double[] location = { pointX, pointY};
						HashTable.userHashUpdate(tree, pointId,location);
					}
					else if(pointType.equals("disappearpoint"))
					{
						dataSet.remove(dataSet.lastElement());
						HashTable.hashFunctionRemove(tree, pointId);
					}
				}
				else
					break;
			}
			//更新gridHashTable
			HashTable.gridHashUpdate(tree);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
