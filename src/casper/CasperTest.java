package casper;

import quadTree.*;

public class CasperTest {
	/*
	 * Casper算法的数据集导入程序入口
	 */
	public static void main(String[] args) {	
		//初始化Oldenburg区域
		double mapWidth = 32000, mapHeight = 32000;
		double rootrect[]={0, 0, mapWidth, mapHeight};
		//划分四叉树的层数
		QuadTree tree = new QuadTree(rootrect,6);
		tree.createChildNodes();
		//初始化GUI
		CasperWindow window = new CasperWindow(tree);
		window.setVisible(true);
	}	
}
