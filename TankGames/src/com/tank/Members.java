package com.tank;

import java.util.*;
import java.io.*;
class Node
{
	int x;
	int y;
	int direct;
	public Node(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}
//记录类,同时也可以保存玩家的设置
class Recorder
{
	//记录每关有多少个敌人
	private static int enNum=20;
	//设置我可以有多少条名
	private static int myLife=3;
	//记录总共消灭了多少个敌人
	private static int allEnNum=0;
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static FileReader fr=null;
	private static BufferedReader br=null;
	private static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	static Vector<Node> nodes=new Vector<Node>();
	
	public static Vector<Node> getNodesAndEnNum()
	{
		try {
			fr=new FileReader("f:/Recording.txt");
			br=new BufferedReader(fr);
			String n="";
			//先读取第一行
			n=br.readLine();
			allEnNum=Integer.parseInt(n);
			
			while((n=br.readLine())!=null)
			{
				String xyz[]=n.split(" ");
				Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
				nodes.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally
		{
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
		return nodes;
	}

	//保存击毁敌人坦克的数量，坐标，方向，
	public static void keepRecAndEnemyTank()
	{
		try {
			fw=new FileWriter("f:/Recording.txt");
			bw=new BufferedWriter(fw);
			bw.write(allEnNum+"\r\n");
			
			//保存当前活的敌人的坦克坐标方向
			for(int i=0;i<ets.size();i++)
			{
				//取出第一个坦克
				EnemyTank et=ets.get(i);
				if(et.isIslive())
				{
					//活的就保存
					String recorde=et.getX()+" "+et.getY()+" "+et.getDirect();
					//写入
					bw.write(recorde+"\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally
		{
			//后开先关闭
			try {
				bw.close();
				fw.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
		
	}
	
	//从文件中读取记录
	public static void getRecording()
	{
		try {
			fr=new FileReader("f:/Recording.txt");
			br=new BufferedReader(fr);
			String s=br.readLine();
			allEnNum=Integer.parseInt(s);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally
		{
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	//把玩家击毁的坦克保存在文件中
	public static void keepRecording()
	{
		try {
			fw=new FileWriter("f:/Recording.txt");
			bw=new BufferedWriter(fw);
			bw.write(allEnNum+"");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally
		{
			//后开先关闭
			try {
				bw.close();
				fw.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	public static int getAllEnNum() {
		return allEnNum;
	}
	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}
	//减少敌人的数量
	public static void reduceEnNum()
	{
		enNum--;
	}
	//增加消灭敌人的数量
	public static void addEnNum()
	{
		allEnNum++;
	}
	public  static Vector<EnemyTank> getEts() {
		return ets;
	}

	public  static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
	}
}
//炸弹类
class Bomb
{
	//定义炸弹的坐标
	private int x=0;
	private int y=0;
	
	//炸弹的生命
	private int life=9;
	private boolean islive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//减少生命值 
	public void downLife()
	{
		if(life>0)
		{
			life--;
		}else{
			this.islive=false;
		}
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public boolean isIslive() {
		return islive;
	}
	public void setIslive(boolean islive) {
		this.islive = islive;
	}
	
}
//子弹类
class Shot implements Runnable
{
	//子弹的横坐标
	private int x=0;
	//子弹的纵坐标
	private int y=0;
	//子弹的方向 0代表上 1代表左 2代表下 3代表右
	private int direct=0;
	//子弹的速度
	private int speed=1;
	//判断子弹是否还活着
	private boolean islive=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public boolean isIslive() {
		return islive;
	}
	public void setIslive(boolean islive) {
		this.islive = islive;
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			switch(direct)
			{
			case 0:
				y-=speed;
				break;
			case 1:
				x-=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x+=speed;
			}
			//System.out.println("子弹坐标x="+x+" y="+y);
			//子弹何时死亡？
			if(x<0||x>400||y<0||y>300)
			{
				this.islive=false;
				break;
			}
		}
	}

}
//坦克类
class Tank
{
	//坦克的横坐标
	private int x=0;
	//坦克的纵坐标
	private int y=0;
	//坦克的方向 0代表上 1代表左 2代表下 3代表右
	private int direct=0;
	//坦克的速度
	private int speed=2;
	//坦克的颜色
	private int color;
	private boolean islive=true;
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public boolean isIslive() {
		return islive;
	}
	public void setIslive(boolean islive) {
		this.islive = islive;
	}
}

//定义我的坦克
class Hero extends Tank
{
	//子弹
	Vector<Shot> ss=new Vector<Shot>();
	private Shot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//开火
	public void shotEnemy()
	{
		switch(this.getDirect())
		{
		case 0:
			s=new Shot(this.getX()+10,this.getY(),0);
			ss.add(s);
			break;
		case 1:
			s=new Shot(this.getX(),this.getY()+10,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(this.getX()+10,this.getY()+30,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(this.getX()+30,this.getY()+10,3);
			ss.add(s);
			break;
		}
		//启动子弹
		Thread t=new Thread(s);
		t.start();
	}
	//坦克向上移动
	public void movUp()
	{
		setY(getY() - getSpeed());
	}
	//坦克向下移动
	public void movDown()
	{
		setY(getY() + getSpeed());
	}
	//坦克向左移动
	public void movLeft()
	{
		setX(getX() - getSpeed());
	}
	//坦克向右移动
	public void movRight()
	{
		setX(getX() + getSpeed());
	}
	public Shot getS() {
		return s;
	}
	public void setS(Shot s) {
		this.s = s;
	}
}

//敌人的坦克 把敌人坦克做成线程类
class EnemyTank extends Tank implements Runnable
{
	//定义一个向量，可以存放敌人的子弹
	Vector<Shot> ss=new Vector<Shot>();
	int times=0;
	//定义一个向量可以访问到Mypanel上所有的敌人坦克
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	public EnemyTank(int x, int y) {
		super(x, y);
		// TODO 自动生成的构造函数存根
	}
	//得到Mypanel上的敌人坦克量
	public void setEts(Vector<EnemyTank> v)
	{
		this.ets=v;
	}
	//判断是否碰到了别的敌人的坦克
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;
		switch(this.getDirect())
		{
		case 0:
			//我的坦克向上
			//取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				//取出敌人的坦克
				EnemyTank et=ets.get(i);
				//判断不是自己的坦克
				if(et!=this)
				{
					//如果敌人的方向是向下或者向下
					if(et.getDirect()==0||et.getDirect()==2)
					{
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+20&&this.getY()>=et.getY()&&this.getY()<=et.getY()+30)
						{
							return true;
						}
						if(this.getX()+20>=et.getX()&&this.getX()+20<=et.getX()+20&&this.getY()>=et.getY()&&this.getY()<=et.getY()+30)
						{
							return true;
						}
					}
					if(et.getDirect()==1||et.getDirect()==3)
					{
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+30&&this.getY()>=et.getY()&&this.getY()<=et.getY()+20)
						{
							return true;
						}
						if(this.getX()+20>=et.getX()&&this.getX()+20<=et.getX()+30&&this.getY()>=et.getY()&&this.getY()<=et.getY()+20)
						{
							return true;
						}
					}
				}
				
			}
			break;
		case 1:
			//取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				//取出敌人的坦克
				EnemyTank et=ets.get(i);
				//判断不是自己的坦克
				if(et!=this)
				{
					//如果敌人的方向是向下或者向下
					if(et.getDirect()==0||et.getDirect()==2)
					{
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+20&&this.getY()>=et.getY()&&this.getY()<=et.getY()+30)
						{
							return true;
						}
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+20&&this.getY()+20>=et.getY()&&this.getY()+20<=et.getY()+30)
						{
							return true;
						}
					}
					if(et.getDirect()==1||et.getDirect()==3)
					{
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+30&&this.getY()>=et.getY()&&this.getY()<=et.getY()+20)
						{
							return true;
						}
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+30&&this.getY()+20>=et.getY()&&this.getY()+20<=et.getY()+20)
						{
							return true;
						}
					}
				}
				
			}
			break;
		case 2:
			//取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				//取出敌人的坦克
				EnemyTank et=ets.get(i);
				//判断不是自己的坦克
				if(et!=this)
				{
					//如果敌人的方向是向下或者向下
					if(et.getDirect()==0||et.getDirect()==2)
					{
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+20&&this.getY()+30>=et.getY()&&this.getY()+30<=et.getY()+30)
						{
							return true;
						}
						if(this.getX()+20>=et.getX()&&this.getX()+20<=et.getX()+20&&this.getY()+30>=et.getY()&&this.getY()+30<=et.getY()+30)
						{
							return true;
						}
					}
					if(et.getDirect()==1||et.getDirect()==3)
					{
						if(this.getX()>=et.getX()&&this.getX()<=et.getX()+30&&this.getY()+30>=et.getY()&&this.getY()+30<=et.getY()+20)
						{
							return true;
						}
						if(this.getX()+20>=et.getX()&&this.getX()+20<=et.getX()+30&&this.getY()+30>=et.getY()&&this.getY()+30<=et.getY()+20)
						{
							return true;
						}
					}
				}
				
			}
			break;
		case 3:
			//取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				//取出敌人的坦克
				EnemyTank et=ets.get(i);
				//判断不是自己的坦克
				if(et!=this)
				{
					//如果敌人的方向是向下或者向下
					if(et.getDirect()==0||et.getDirect()==2)
					{
						if(this.getX()+30>=et.getX()&&this.getX()+30<=et.getX()+20&&this.getY()>=et.getY()&&this.getY()<=et.getY()+30)
						{
							return true;
						}
						if(this.getX()+30>=et.getX()&&this.getX()+30<=et.getX()+20&&this.getY()+20>=et.getY()&&this.getY()+20<=et.getY()+30)
						{
							return true;
						}
					}
					if(et.getDirect()==1||et.getDirect()==3)
					{
						if(this.getX()+30>=et.getX()&&this.getX()+30<=et.getX()+30&&this.getY()>=et.getY()&&this.getY()<=et.getY()+20)
						{
							return true;
						}
						if(this.getX()+30>=et.getX()&&this.getX()+30<=et.getX()+30&&this.getY()+20>=et.getY()&&this.getY()+20<=et.getY()+20)
						{
							return true;
						}
					}
				}
				
			}
			break;
		
		}
		
		
		return b;
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(true)
		{
			switch(this.getDirect())
			{
			case 0:
				//说明坦克正在往上走
				for(int i=0;i<30;i++)
				{
					if(getY()>0&&!this.isTouchOtherEnemy())
					{
					setY(getY() - getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				break;
			case 1:
				//向左
				for(int i=0;i<30;i++)
				{
					if(getX()>0&&!this.isTouchOtherEnemy())
					{
					setX(getX() - getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				break;
			case 2:
				//向下
				for(int i=0;i<30;i++)
				{
					if(getY()<300&&!this.isTouchOtherEnemy())
					{
					setY(getY() + getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				break;
			case 3:
				//向右
				for(int i=0;i<30;i++)
				{
					if(getX()<400&&!this.isTouchOtherEnemy())
					{
					setX(getX() + getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				break;
			}
			
			this.times++;
			//判断是否需要加入新的子弹
			if(times%2==0)
			{
				if(isIslive())
				{
					if(ss.size()<5)
					{
						Shot s=null;
						//没有子弹，添加
						switch(getDirect())
						{
						case 0:
							s=new Shot(getX()+10,getY(),0);
					        ss.add(s);
							break;
						case 1:
							s=new Shot(getX(),getY()+10,1);
							ss.add(s);
							break;
						case 2:
							s=new Shot(getX()+10,getY()+30,2);
							ss.add(s);
							break;
						case 3:
							s=new Shot(getX()+30,getY()+10,3);
							ss.add(s);
							break;
						}
						
						//启动子弹
						Thread t=new Thread(s);
						t.start();
					}
				}
			}
			
			//让坦克随机产生一个新方向
			this.setDirect((int)(Math.random()*4));
			//判断敌人的坦克是否死亡
			if(this.isIslive()==false)
			{
				//让敌人的坦克死亡后退出进程，否则会产生僵尸进程
				break;
			}
		}
		
	}
	
	
}