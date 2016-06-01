/**
 * 坦克大战4.0版
 * 1，画出坦克
 * 2.我的坦克可以上下左右移动
 * 3.可以发射子弹 可以连发，并且一次最多可以发5颗
 * 4.当我的坦克击中敌人的坦克时，敌人就消失（爆炸的效果）
 * 5.我也可以被击中，被击中后显示爆炸效果
 * 6.防止敌人坦克重叠运动
 *   6.1。决定把判断是否碰撞的方法写到EnemyTank 类中
 * 7.可以分关
 *   7.1.做一个开始的panel，它是一个空的。
 *   7.2.字体做闪烁。
 * 8.可以在玩的时候暂停和继续
 * 9.可以记录玩家的成绩
 *   存盘退出，可以记录当时敌人的坦克坐标，并可以恢复
 * 10.java如何操作声音文件
 */
package com.tank;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
public class MyTankGame3 extends JFrame implements ActionListener{

	/**
	 * @param args
	 */
	//定义一个开始的面板
	MyStartPanel msp=null;
	MyPanel mp=null;
	//定义我需要的菜单
	JMenuBar jmb=null;
	//开始游戏
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		MyTankGame3 mtg=new MyTankGame3();

	}
	//构造函数
	public MyTankGame3()
	{
//		mp=new MyPanel();
//		//启动mp
//		Thread t=new Thread(mp);
//		t.start();
//		this.add(mp);
//		//注册监听
//		this.addKeyListener(mp);
		//定义和创建菜单选项
		jmb=new JMenuBar();
		jm1=new JMenu("游戏(G)");
		//设置快捷方式
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("开始新游戏(N)");
		jmi1.setMnemonic('N');
		//对jmi1进行注册监听
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jmi2=new JMenuItem("退出游戏(E)");
		jmi2.setMnemonic('E');
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exitgame");
		jmi3=new JMenuItem("存盘退出游戏(C)");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("savegame");
		jmi4=new JMenuItem("继续上局游戏(S)");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("congame");
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setSize(600,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO 自动生成的方法存根
		//对用户不同的点击做不同的事情
		if(arg0.getActionCommand().equals("newgame"))
		{
			//删除就得开始面板
			this.remove(msp);
			//创建战争面板
			mp=new MyPanel("newGame");
			//启动mp
			Thread t=new Thread(mp);
			t.start();
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);
			//显示刷新新的面板
			this.setVisible(true);
		}else if(arg0.getActionCommand().equals("exitgame"))
		{
			//用户点击了退出
			//保存击毁敌人的数量
			Recorder.keepRecording();
			System.exit(0);
		}else if(arg0.getActionCommand().equals("savegame"))
		{
			//保存敌人的数量和敌人的坐标
			Recorder.setEts(mp.ets);
			Recorder.keepRecAndEnemyTank();
			//退出
			System.exit(0);
		}else if(arg0.getActionCommand().equals("congame"))
		{
			//删除就得开始面板
			this.remove(msp);
			//创建战争面板
			mp=new MyPanel("con");
			//启动mp
			Thread t=new Thread(mp);
			t.start();
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);
			//显示刷新新的面板
			this.setVisible(true);
		}
	}

}

class MyStartPanel extends JPanel implements Runnable
{
	int times=0;
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		if(times%2==0)
		{
		g.setColor(Color.yellow);
		g.setFont(new Font("华文新魏",Font.BOLD,30));
		//提示信息
		g.drawString("Stage: 1", 140, 140);
		}
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(true)
		{
			//休眠
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			times++;
			//重画
			this.repaint();
		}
	}
}
//定义我的面板
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//定义一个我的坦克
	Hero hero=null;
	//定义敌人的坦克组
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<Node> nodes=new Vector<Node>();
	int etsize=6;
	//定义炸弹的集合类
	Vector<Bomb> bombs=new Vector<Bomb>();
	//定义三张图片 三张图片才能组成一颗炸弹
	Image image1=null;
	Image image2=null;
	Image image3=null;
	//构造函数
	public MyPanel(String flag)
	{
		//恢复记录
		Recorder.getRecording();
		
		hero=new Hero(180,230);
		//初始化敌人的坦克
		if(flag.equals("newGame"))
		{
			for(int i=0;i<etsize;i++)
			{
				//创建一个敌人坦克
				EnemyTank et=new EnemyTank((i+1)*40,0);
				et.setColor(0);
				et.setDirect(2);
				//将Mypanel的敌人坦克向量交给敌人坦克
				et.setEts(ets);
				//启动敌人坦克
				Thread t=new Thread(et);
				t.start();
				//给敌人的坦克加人一颗子弹
				Shot s=new Shot(et.getX()+10,et.getY()+30,2);
				//加入给敌人的坦克
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				//加入
				ets.add(et);
			}
		}
		else
		{
			nodes=Recorder.getNodesAndEnNum();
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);
				//创建一个敌人坦克
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setColor(0);
				et.setDirect(node.direct);
				//将Mypanel的敌人坦克向量交给敌人坦克
				et.setEts(ets);
				//启动敌人坦克
				Thread t=new Thread(et);
				t.start();
				//给敌人的坦克加人一颗子弹
				Shot s=new Shot(et.getX()+10,et.getY()+30,2);
				//加入给敌人的坦克
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				//加入
				ets.add(et);
			}
		}
		try {
			image1=ImageIO.read(new File("bomb1.gif"));
			image2=ImageIO.read(new File("bomb2.gif"));
			image3=ImageIO.read(new File("bomb3.gif"));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
//		//初始化三张图片
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb1.gif"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb2.gif"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb3.gif"));
	}
	//画出提示信息
	public void showInfo(Graphics g)
	{
		//画出提示信息
		this.drawTank(80, 330, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"",110,350);
		this.drawTank(130, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"",160,350);
		//画出玩家的总成绩
		g.setColor(Color.black);
		g.setFont(new Font("宋体",Font.BOLD,20));
		g.drawString("您的总成绩是:",420,30);
		
		this.drawTank(420,60,g,0,0);
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"",460,80);
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		this.showInfo(g);
		//画出自己的坦克
		if(hero.isIslive())
		{
		this.drawTank(hero.getX(),hero.getY() , g, hero.getDirect(), 1);
		}
		//画出我的子弹
		for(int i=0;i<hero.ss.size();i++)
		{
			Shot myShot=hero.ss.get(i);
			//画出子弹 一颗子弹
			if(myShot!=null&&myShot.isIslive()==true)
			{
				g.draw3DRect(myShot.getX(), myShot.getY(), 1, 1, false);
			}
			if(myShot.isIslive()==false)
			{
				//从ss中删除掉该子弹
				hero.ss.remove(myShot);
			}
		}
		//画出炸弹
		for(int i=0;i<bombs.size();i++)
		{
			//取出炸弹
			Bomb b=bombs.get(i);
					
			if(b.getLife()>6)
			{
				g.drawImage(image1, b.getX(), b.getY(), 30, 30, this);
			}else if(b.getLife()>4)
			{
				g.drawImage(image2, b.getX(), b.getY(), 30, 30, this);
			}else
			{
				g.drawImage(image3, b.getX(), b.getY(), 30, 30, this);
			}
			//让b的生命值减小
			b.downLife();
			//如果炸弹的生命值变为0,就把该炸弹从向量中去掉
			if(b.getLife()==0)
			{
				bombs.remove(b);
			}
		}
		//画出敌人的坦克
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			if(et.isIslive())
			{
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
				//再画出敌人的子弹
				//System.out.println("坦克子弹有 "+et.ss.size());
				for(int j=0;j<et.ss.size();j++)
				{
					//取出子弹
					Shot enemyShot=et.ss.get(j);
					if(enemyShot.isIslive())
					{
						//System.out.println("第 "+i+"个坦克的 "+j+"颗子弹 x= "+enemyShot.getX());
						g.draw3DRect(enemyShot.getX(), enemyShot.getY(), 1, 1, false);
					}
					else
					{
						//如果敌人的子弹死亡，那么就从vector中去掉
						et.ss.remove(enemyShot);
					}
				}
			}
		}
		
	}
	//敌人的子弹是否击中我
	public void hitMe()
	{
		//取出每一个敌人的坦克
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			//取出每个敌人坦克的子弹
			for(int j=0;j<et.ss.size();j++)
			{
				//取出子弹
				Shot enemyShot=et.ss.get(j);
				if(hero.isIslive())
				{
					if(this.hitTank(enemyShot, hero))
					{
						
					}
				}
			}
		}
	}
	//判断我的子弹是否被击中敌人的坦克
	public void hitEnemyTank()
	{
	for(int i=0;i<hero.ss.size();i++)
	{
		//取出子弹
		Shot myShot=hero.ss.get(i);
		//判断子弹是否有效
		if(myShot.isIslive())
		{
			//取出每个敌人坦克,与它判断
			for(int j=0;j<ets.size();j++)
			{
				//取出敌人坦克
				EnemyTank et=ets.get(j);
				//判断坦克有效
				if(et.isIslive())
				{
					if(this.hitTank(myShot, et))
					{
						Recorder.reduceEnNum();
						Recorder.addEnNum();
					}
				}
			}
			
		}
	}
	}
	
	//写一个函数专门判断是否击中了坦克
	public boolean hitTank(Shot s,Tank et)
	{
		boolean b2=false;
		//判断该坦克的方向
		switch(et.getDirect())
		{
		//如果敌人的坦克方向是上或者下
		case 0:
		case 2:
			if(s.getX()>et.getX()&&s.getX()<et.getX()+20&&s.getY()>et.getY()&&s.getY()<et.getY()+30)
			{
				//击中
				//子弹死亡
				s.setIslive(false);
				//敌人坦克死亡
				et.setIslive(false);
				b2=true;
				//创建一颗炸弹放入Vector
				Bomb b=new Bomb(et.getX(),et.getY());
				bombs.add(b);
				
			}
			break;
		case 1:
		case 3:
			if(s.getX()>et.getX()&&s.getX()<et.getX()+30&&s.getY()>et.getY()&&s.getY()<et.getY()+20)
			{
				//击中
				//子弹死亡
				s.setIslive(false);
				//敌人坦克死亡
				et.setIslive(false);
				b2=true;
				//创建一颗炸弹放入Vector
				Bomb b=new Bomb(et.getX(),et.getY());
				bombs.add(b);
			}
			break;
			
		}
		return b2;
	}
	//画出坦克的参数
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		//判断类型
		switch(type)
		{
		case 0:
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		//判断方向
		switch(direct)
		{
		case 0:
			//画出我的坦克，到时候再封装成一个函数；
			//1，画出坦克的左边
			g.fill3DRect(x, y, 5, 30,false);
			//2，画出坦克的右边
			g.fill3DRect(x+15, y, 5, 30,false);
			//3，画出中间的矩形
			g.fill3DRect(x+5,y+5,10,20,false);
			//4，画出圆形
			g.drawOval(x+4,y+10, 10, 10);
		    //5，画出线
			g.drawLine(x+9, y+15, x+9, y);
			break;
		case 1:
			//炮筒向左
			//1，画出坦克的左边
			g.fill3DRect(x, y, 30, 5,false);
			//2，画出坦克的右边
			g.fill3DRect(x, y+15, 30, 5,false);
			//3，画出中间的矩形
			g.fill3DRect(x+5,y+5,20,10,false);
			//4，画出圆形
			g.drawOval(x+10,y+4, 10, 10);
			//5，画出线
			g.drawLine(x+15, y+9, x, y+10);
			break;
		case 2:
			//炮筒向下
			//1，画出坦克的左边
			g.fill3DRect(x, y, 5, 30,false);
			//2，画出坦克的右边
			g.fill3DRect(x+15, y, 5, 30,false);
			//3，画出中间的矩形
			g.fill3DRect(x+5,y+5,10,20,false);
			//4，画出圆形
			g.drawOval(x+4,y+10, 10, 10);
		    //5，画出线
			g.drawLine(x+9, y+15, x+9, y+30);
			break;
		case 3:
			//炮筒向右
			//1，画出坦克的左边
			g.fill3DRect(x, y, 30, 5,false);
			//2，画出坦克的右边
			g.fill3DRect(x, y+15, 30, 5,false);
			//3，画出中间的矩形
			g.fill3DRect(x+5,y+5,20,10,false);
			//4，画出圆形
			g.drawOval(x+10,y+4, 10, 10);
			//5，画出线
			g.drawLine(x+15, y+9, x+30, y+10);
			break;
			
			
		}
	}
	@Override
	//键按下处理，w表示上 s表示下a表示左 d表示右
	public void keyPressed(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		if(arg0.getKeyCode()==KeyEvent.VK_W)
		{
			//设置我的坦克的方向
			this.hero.setDirect(0);
			this.hero.movUp();
		}else if(arg0.getKeyCode()==KeyEvent.VK_A)
		{
			//向左
			this.hero.setDirect(1);
			this.hero.movLeft();
		}else if(arg0.getKeyCode()==KeyEvent.VK_S)
		{
			//向下
			this.hero.setDirect(2);
			this.hero.movDown();
		}else if(arg0.getKeyCode()==KeyEvent.VK_D)
		{
			//向右
			this.hero.setDirect(3);
			this.hero.movRight();
		}
		
		if(arg0.getKeyCode()==KeyEvent.VK_J)
		{
			//当用户按下j的时候创建一颗子弹
			if(hero.ss.size()<=4)
			{
			this.hero.shotEnemy();
			}
		}
		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			this.hitMe();
			//重绘
			this.repaint();
		}
	}
	
}

