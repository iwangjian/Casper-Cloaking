package quadTree;

import java.util.*;

public class QuadTree {
	/*
	 * 四叉树类
	 */
	public QuadTreeNode root; 
	public QuadTreeNode currentParentNode;
	public QuadTreeNode[] currentChildNodes;
	//建立哈希表索引树的每一层的结点链表
	public HashMap<Integer,ArrayList<QuadTreeNode>> layerIndex = 
			new HashMap<Integer,ArrayList<QuadTreeNode>>();

	public ArrayList<QuadTreeNode> layerNodesList[];
	public double[] currentRect;
    public int depth;  //树的当前深度
    public int insertDepth; //树的搜索深度
    public int maxDepth;  //树的最大深度
    public static int cidCreator; 
    //根据层数返回哈希表中该层的结点链表
    public ArrayList<QuadTreeNode> getLayerList(int i)
    {
    	return layerIndex.get(i);
    }
    @SuppressWarnings("unchecked")
	public QuadTree(double[] rootrect, int maxDepth) 
    {  
        depth = 0;  
        this.maxDepth = maxDepth;
        layerNodesList = new ArrayList[maxDepth+1];
        cidCreator = 0;
        root = new QuadTreeNode();  
        root.initial(rootrect);  
        currentParentNode = root;
        currentChildNodes = root.node;
        currentRect = root.rect;
        this.insert(cidCreator, currentRect);
    }
    //根据树的最大深度创建所有的子结点
    public void createChildNodes()
    {
    	depth = 1;
    	layerNodesList[1] = new ArrayList<QuadTreeNode>();
		currentChildNodes = currentParentNode.creatChild();
    	for(int i=0; i<4; i++)
    	{
    		layerNodesList[1].add(currentChildNodes[i]);
    	}
    	
    	Integer lay1 = new Integer(1);
        layerIndex.put(lay1, layerNodesList[1]);
    	while(depth < maxDepth)
    	{
    		int layer = depth;
    		int nextLayer = layer + 1;
    	    layerNodesList[nextLayer] = new ArrayList<QuadTreeNode>();
    		
    		for(int i=0; i<layerNodesList[layer].size(); i++)
    		{
    			cidCreator ++;
    			 currentParentNode = layerNodesList[layer].get(i);
    			 currentRect = currentParentNode.rect;
    			 //把每一个子结点的区域插入到树中
	    		 this.insert(cidCreator, currentRect);
	    		 
	    		 currentChildNodes = currentParentNode.creatChild();
	    		 for(int j=0; j<4; j++)
	         	{
	         		layerNodesList[nextLayer].add(currentChildNodes[j]);
	         	}
    		}
    		//以层号为关键字把该层的结点链表放入到静态哈希表中
    		Integer lay = new Integer(layer);
    		layerIndex.put(lay, layerNodesList[layer]);
    		depth = depth + 1;
    	}
    }
    //结点区域和关键字插入函数
    public QuadTreeNode insert(int node_key, double[] rect) 
    {  
        insertDepth = -1;   
        return root.insertNode(this, rect, node_key);  
    }  
    //由给定区域搜索其cid及父层节点cid，得到一个向量
    public void search(double[] search_box, Vector<Integer> results_list) 
    {  
        root.searchNodes(search_box, results_list);  
    }
    //由关键字搜索对应的区域
    public double[] searchRect(ArrayList<QuadTreeNode>layerNodesList, int cid)
    {
    	Integer Cid = new Integer(cid);
    	double rect[] = {0,0,0,0};
    	for(int i=0; i<layerNodesList.size(); i++)
    	{
    		int indexCid = 0;
    		QuadTreeNode currentNode = layerNodesList.get(i);
    		rect = currentNode.rect;
    		Vector<Integer> vectorCid = currentNode.vector;
    		if (vectorCid!=null && vectorCid.size()>0) 
    		{
    			indexCid = vectorCid.size()-1;
    		}
    		if(vectorCid.elementAt(indexCid).equals(Cid))
    		{
    			break;
    		}
    	}
    	return rect;
    }
}
