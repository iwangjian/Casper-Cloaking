package casper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import quadTree.*;

public class CasperWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public BufferedReader buff = null;
	public QuadTree tree = null;
	public Vector<User> dataSet = null;
	public int scale = 64;
	public int time = -1;
	public int pointX, pointY;
	public int rectX, rectY;
	public int width, height;
	public boolean paintPoint = false, paintRect = false;
	private paintPanel panel;
	private JTextField txtUid;
	private JTextField txtK;
	private JTextField txtAmin;
	private JLabel lblTimestamp, lblTime, lblScale, lblCasperParameters, lblUid, lblK, lblAmin;
	private JButton btnTime, btnQuery; 
	
	public CasperWindow(QuadTree tree) 
	{
		initialize();
		this.tree = tree;
		this.dataSet = new Vector<User>();
		File data = new File("oldenburg.txt");
		FileReader fileReader;
		try {
			fileReader = new FileReader(data);
			buff = new BufferedReader(fileReader);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	/*
	 * 初始化函数
	 */
	private void initialize() 
	{
		setBounds(100,100,544,722);
		setLocation(600,0);
		setTitle("DataSet Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.white);
		
		panel = new paintPanel();
		panel.setBounds(10, 10, 510, 550);
		panel.setBackground(Color.WHITE);
		getContentPane().add(panel);
		
		lblTimestamp = new JLabel("TimeStamp:");
		lblTimestamp.setBounds(213, 580, 76, 23);
		getContentPane().add(lblTimestamp);
		
		lblTime = new JLabel("-1");
		lblTime.setBounds(287, 580, 45, 23);
		getContentPane().add(lblTime);
		
		btnTime = new JButton("Time +");
		btnTime.setBounds(417, 580, 93, 23);
		btnTime.addActionListener(this);
		getContentPane().add(btnTime);
		
		lblScale = new JLabel("Scale:  1:64");
		lblScale.setBounds(10, 584, 87, 19);
		getContentPane().add(lblScale);
		
		lblCasperParameters = new JLabel("Casper Parameters");
		lblCasperParameters.setBounds(10, 613, 134, 28);
		getContentPane().add(lblCasperParameters);
		
		lblUid = new JLabel("UID");
		lblUid.setBounds(10, 651, 24, 15);
		getContentPane().add(lblUid);
		
		txtUid = new JTextField();
		txtUid.setBounds(31, 651, 66, 21);
		getContentPane().add(txtUid);
		txtUid.setColumns(10);
		
		lblK = new JLabel("k");
		lblK.setBounds(117, 654, 13, 15);
		getContentPane().add(lblK);
		
		txtK = new JTextField();
		txtK.setBounds(127, 651, 66, 21);
		getContentPane().add(txtK);
		txtK.setColumns(10);
		
		lblAmin = new JLabel("Amin");
		lblAmin.setBounds(211, 654, 32, 15);
		getContentPane().add(lblAmin);
		
		txtAmin = new JTextField();
		txtAmin.setBounds(243, 651, 66, 21);
		getContentPane().add(txtAmin);
		txtAmin.setColumns(12);
		
		btnQuery = new JButton("Query");
		btnQuery.setBounds(352, 647, 93, 23);
		btnQuery.addActionListener(this);
		getContentPane().add(btnQuery);
		
	}
	/*
	 * 重写panint方法，实现显示矩形框功能
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g)
	{
		super.paint(g);
		int offSet = 10;
		if(paintPoint == true)
		{
			for(int i=0; i<dataSet.size()-1; i++)
			{
				User u = dataSet.elementAt(i);
				double[] location = u.getLocation();
				pointX = (new Double(location[0]/scale)).intValue();
				pointY = (new Double(location[1]/scale)).intValue();		
			    g.setColor(Color.black);
			    g.fillOval(pointX+offSet*2-1, pointY+offSet*2-1, 8, 8);
			}
			
		}
		if(paintRect == true)
		{
			g.setColor(Color.red);
			g.drawRect(rectX+offSet*2-1, rectY+offSet*2-1, width, height);
		}
		
	}
	/*
	 * 增加时间戳
	 */
	public void addTime()
	{
		time = time + 1;
		lblTime.setText(String.valueOf(time));
		if(!dataSet.isEmpty())
		{
			User user = dataSet.lastElement();
			dataSet.clear();
			dataSet.add(user);
		}
		//读取NG生成器输出文件数据并显示该时间戳下的所有节点
		CPUTimer timer = new CPUTimer();
		timer.start();
		DataHandler.handleData(tree, buff, time, dataSet);
		timer.stop();
		System.out.println("All users ID in current timestamp:");
		for(int i=0;i<dataSet.size()-1;i++)
		{
			System.out.println(dataSet.elementAt(i).getUid());
		}
		System.out.println("Update time: "+timer.get());
		paintPoint = true;
		paintRect = false;
		repaint();
	}
	/*
	 * 查询函数
	 */
	public void query()
	{	
		if(!txtUid.getText().isEmpty() && !txtK.getText().isEmpty() && !txtAmin.getText().isEmpty())
		{
			int uid = Integer.valueOf(txtUid.getText());
			int k = Integer.valueOf(txtK.getText());
			double Amin = Double.valueOf(txtAmin.getText());
			int cid = HashTable.getCid(uid);
			//执行Casper算法获得匿名区域
			CPUTimer timer = new CPUTimer();
			timer.start();
			CasperCloaking c = new CasperCloaking();
			c.initial(tree);
			double[] cloakRect = c.Cloaking(tree, k, Amin, cid);
		    timer.stop();
			if(cloakRect !=null)
			{
				System.out.println("Cloaking area: "+cloakRect[0]+" "+cloakRect[1]+" "+cloakRect[2]+" "+cloakRect[3]);
				System.out.println("Cloaking time: "+timer.get());
				rectX = (new Double(cloakRect[0]/scale)).intValue();
				rectY = (new Double(cloakRect[1]/scale)).intValue();
				width = (new Double((cloakRect[2]-cloakRect[0])/scale)).intValue();
				height = (new Double((cloakRect[3]-cloakRect[1])/scale)).intValue();
				paintPoint = true;
				paintRect = true;
				repaint();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "No such cloaking area！", "Attention", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = (Object)e.getSource();
		if(source.equals(btnTime))
		{
			addTime();
		}
		if(source.equals(btnQuery))
		{
			query();
		}
	}

	class paintPanel extends JPanel {
		/**
		 * 重写JPanel的paint方法
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g)
		{
			super.paint(g);
			g.drawRect(0, 0, 500, 540);
		}
	}
}
