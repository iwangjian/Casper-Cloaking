package quadTree;
import java.util.*;

public class QuadTreeNode {
	/*
	 * 四叉树节点类
	 */
	public static final int nodeNums = 4;
	public static final int QUADRANT1 = 0,QUADRANT2 = 1,QUADRANT3 = 2,QUADRANT4 = 3;
	public static final int LX = 0,LY = 1,RX = 2,RY = 3;
	public double[] rect; //存储lx,ly,rx,ry
	public Vector<Integer> vector; //该结点及父结点的cid向量
	public QuadTreeNode[] node = new QuadTreeNode[nodeNums];
	
	public QuadTreeNode(double lx, double ly, double rx, double ry)
	{
		double[] rect = new double[4];
		rect[LX] = lx;
		rect[LY] = ly;
		rect[RX] = rx;
		rect[RY] = ry;
		this.rect = rect;
		vector = new Vector<Integer>();
	}
	public QuadTreeNode(){
		
	}
	public void initial(double[] temprect) 
	{  
        this.rect = new double[4];  
        System.arraycopy(temprect, 0, rect, 0, 4);  
        vector = new Vector<Integer>();  
    } 
	public QuadTreeNode[] creatChild() 
	{
		double lx, ly, rx, ry;  
		lx = rect[LX];  
	    ly = rect[LY];  
	    rx = rect[RX];  
	    ry = rect[RY];  
	    this.node[QUADRANT1] = new QuadTreeNode(lx,ly,(lx+rx)/2,(ly+ry)/2);  
	    this.node[QUADRANT2] = new QuadTreeNode((lx+rx)/2,ly,rx,(ly+ry)/2);  
	    this.node[QUADRANT3] = new QuadTreeNode((lx+rx)/2,(ly+ry)/2,rx,ry);  
	    this.node[QUADRANT4] = new QuadTreeNode(lx,(ly+ry)/2,(lx+rx)/2,ry);  
	    return this.node;
	}
	public boolean isLeafNode()
	{  
		if(node[QUADRANT1] == null)  
			return true;  
		else 
			return false;
	}
	public QuadTreeNode insertNode(QuadTree tree,double[] nodeRect,int key)
	{ 
	    if(Util.isInsideRect(nodeRect, rect))
	    {
	    	if((tree.insertDepth ++) < tree.maxDepth) 
	    	{
	    		if(isLeafNode()){
	    			creatChild(); 
	    		}
	            if(Util.isInsideRect(nodeRect, node[QUADRANT1].rect)){
	            	return node[QUADRANT1].insertNode(tree, nodeRect, key);
	            }
	            if(Util.isInsideRect(nodeRect, node[QUADRANT2].rect)){
	            	return node[QUADRANT2].insertNode(tree, nodeRect, key); 
	            }
	            if(Util.isInsideRect(nodeRect, node[QUADRANT3].rect)){
	            	return node[QUADRANT3].insertNode(tree, nodeRect, key); 
	            }
	            if(Util.isInsideRect(nodeRect, node[QUADRANT4].rect)){
	            	return node[QUADRANT4].insertNode(tree, nodeRect, key ); 
	            }
	        }
	    	//当该节点不包括任何叶子节点时插入该节点
	    	addData(key);
            return this; 
        }
	    return null;
	}
	public void addData(int node_key) 
	{  
        if (vector == null){
        	vector = new Vector<Integer>(); 
        }
        vector.addElement(node_key);  
    }
	public void searchNodes(double[] search_box, Vector<Integer> results_list) 
	{  
        if (Util.isOverLapped(rect, search_box)) 
        {  
            if (vector!=null && vector.size()>0)  
                for (int i = 0; i < vector.size(); i++) 
                {  
                    results_list.addElement(vector.elementAt(i));  
                }  
       
            if (!isLeafNode()) 
            {  
                node[QUADRANT1].searchNodes(search_box, results_list);  
                node[QUADRANT2].searchNodes(search_box, results_list);  
                node[QUADRANT3].searchNodes(search_box, results_list);  
                node[QUADRANT4].searchNodes(search_box, results_list);  
            }  
        }  
    }
}
