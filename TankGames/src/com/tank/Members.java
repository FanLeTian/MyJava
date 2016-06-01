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
//��¼��,ͬʱҲ���Ա�����ҵ�����
class Recorder
{
	//��¼ÿ���ж��ٸ�����
	private static int enNum=20;
	//�����ҿ����ж�������
	private static int myLife=3;
	//��¼�ܹ������˶��ٸ�����
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
			//�ȶ�ȡ��һ��
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

	//������ٵ���̹�˵����������꣬����
	public static void keepRecAndEnemyTank()
	{
		try {
			fw=new FileWriter("f:/Recording.txt");
			bw=new BufferedWriter(fw);
			bw.write(allEnNum+"\r\n");
			
			//���浱ǰ��ĵ��˵�̹�����귽��
			for(int i=0;i<ets.size();i++)
			{
				//ȡ����һ��̹��
				EnemyTank et=ets.get(i);
				if(et.isIslive())
				{
					//��ľͱ���
					String recorde=et.getX()+" "+et.getY()+" "+et.getDirect();
					//д��
					bw.write(recorde+"\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally
		{
			//���ȹر�
			try {
				bw.close();
				fw.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
		
	}
	
	//���ļ��ж�ȡ��¼
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
	
	//����һ��ٵ�̹�˱������ļ���
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
			//���ȹر�
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
	//���ٵ��˵�����
	public static void reduceEnNum()
	{
		enNum--;
	}
	//����������˵�����
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
//ը����
class Bomb
{
	//����ը��������
	private int x=0;
	private int y=0;
	
	//ը��������
	private int life=9;
	private boolean islive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//��������ֵ 
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
//�ӵ���
class Shot implements Runnable
{
	//�ӵ��ĺ�����
	private int x=0;
	//�ӵ���������
	private int y=0;
	//�ӵ��ķ��� 0������ 1������ 2������ 3������
	private int direct=0;
	//�ӵ����ٶ�
	private int speed=1;
	//�ж��ӵ��Ƿ񻹻���
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
		// TODO �Զ����ɵķ������
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
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
			//System.out.println("�ӵ�����x="+x+" y="+y);
			//�ӵ���ʱ������
			if(x<0||x>400||y<0||y>300)
			{
				this.islive=false;
				break;
			}
		}
	}

}
//̹����
class Tank
{
	//̹�˵ĺ�����
	private int x=0;
	//̹�˵�������
	private int y=0;
	//̹�˵ķ��� 0������ 1������ 2������ 3������
	private int direct=0;
	//̹�˵��ٶ�
	private int speed=2;
	//̹�˵���ɫ
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

//�����ҵ�̹��
class Hero extends Tank
{
	//�ӵ�
	Vector<Shot> ss=new Vector<Shot>();
	private Shot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//����
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
		//�����ӵ�
		Thread t=new Thread(s);
		t.start();
	}
	//̹�������ƶ�
	public void movUp()
	{
		setY(getY() - getSpeed());
	}
	//̹�������ƶ�
	public void movDown()
	{
		setY(getY() + getSpeed());
	}
	//̹�������ƶ�
	public void movLeft()
	{
		setX(getX() - getSpeed());
	}
	//̹�������ƶ�
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

//���˵�̹�� �ѵ���̹�������߳���
class EnemyTank extends Tank implements Runnable
{
	//����һ�����������Դ�ŵ��˵��ӵ�
	Vector<Shot> ss=new Vector<Shot>();
	int times=0;
	//����һ���������Է��ʵ�Mypanel�����еĵ���̹��
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	public EnemyTank(int x, int y) {
		super(x, y);
		// TODO �Զ����ɵĹ��캯�����
	}
	//�õ�Mypanel�ϵĵ���̹����
	public void setEts(Vector<EnemyTank> v)
	{
		this.ets=v;
	}
	//�ж��Ƿ������˱�ĵ��˵�̹��
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;
		switch(this.getDirect())
		{
		case 0:
			//�ҵ�̹������
			//ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				//ȡ�����˵�̹��
				EnemyTank et=ets.get(i);
				//�жϲ����Լ���̹��
				if(et!=this)
				{
					//������˵ķ��������»�������
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
			//ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				//ȡ�����˵�̹��
				EnemyTank et=ets.get(i);
				//�жϲ����Լ���̹��
				if(et!=this)
				{
					//������˵ķ��������»�������
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
			//ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				//ȡ�����˵�̹��
				EnemyTank et=ets.get(i);
				//�жϲ����Լ���̹��
				if(et!=this)
				{
					//������˵ķ��������»�������
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
			//ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				//ȡ�����˵�̹��
				EnemyTank et=ets.get(i);
				//�жϲ����Լ���̹��
				if(et!=this)
				{
					//������˵ķ��������»�������
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
		// TODO �Զ����ɵķ������
		while(true)
		{
			switch(this.getDirect())
			{
			case 0:
				//˵��̹������������
				for(int i=0;i<30;i++)
				{
					if(getY()>0&&!this.isTouchOtherEnemy())
					{
					setY(getY() - getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
				break;
			case 1:
				//����
				for(int i=0;i<30;i++)
				{
					if(getX()>0&&!this.isTouchOtherEnemy())
					{
					setX(getX() - getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
				break;
			case 2:
				//����
				for(int i=0;i<30;i++)
				{
					if(getY()<300&&!this.isTouchOtherEnemy())
					{
					setY(getY() + getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
				break;
			case 3:
				//����
				for(int i=0;i<30;i++)
				{
					if(getX()<400&&!this.isTouchOtherEnemy())
					{
					setX(getX() + getSpeed());
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
				break;
			}
			
			this.times++;
			//�ж��Ƿ���Ҫ�����µ��ӵ�
			if(times%2==0)
			{
				if(isIslive())
				{
					if(ss.size()<5)
					{
						Shot s=null;
						//û���ӵ������
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
						
						//�����ӵ�
						Thread t=new Thread(s);
						t.start();
					}
				}
			}
			
			//��̹���������һ���·���
			this.setDirect((int)(Math.random()*4));
			//�жϵ��˵�̹���Ƿ�����
			if(this.isIslive()==false)
			{
				//�õ��˵�̹���������˳����̣�����������ʬ����
				break;
			}
		}
		
	}
	
	
}