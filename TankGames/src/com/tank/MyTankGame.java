/**
 * ̹�˴�ս4.0��
 * 1������̹��
 * 2.�ҵ�̹�˿������������ƶ�
 * 3.���Է����ӵ� ��������������һ�������Է�5��
 * 4.���ҵ�̹�˻��е��˵�̹��ʱ�����˾���ʧ����ը��Ч����
 * 5.��Ҳ���Ա����У������к���ʾ��ըЧ��
 * 6.��ֹ����̹���ص��˶�
 *   6.1���������ж��Ƿ���ײ�ķ���д��EnemyTank ����
 * 7.���Էֹ�
 *   7.1.��һ����ʼ��panel������һ���յġ�
 *   7.2.��������˸��
 * 8.���������ʱ����ͣ�ͼ���
 * 9.���Լ�¼��ҵĳɼ�
 *   �����˳������Լ�¼��ʱ���˵�̹�����꣬�����Իָ�
 * 10.java��β��������ļ�
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
	//����һ����ʼ�����
	MyStartPanel msp=null;
	MyPanel mp=null;
	//��������Ҫ�Ĳ˵�
	JMenuBar jmb=null;
	//��ʼ��Ϸ
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		MyTankGame3 mtg=new MyTankGame3();

	}
	//���캯��
	public MyTankGame3()
	{
//		mp=new MyPanel();
//		//����mp
//		Thread t=new Thread(mp);
//		t.start();
//		this.add(mp);
//		//ע�����
//		this.addKeyListener(mp);
		//����ʹ����˵�ѡ��
		jmb=new JMenuBar();
		jm1=new JMenu("��Ϸ(G)");
		//���ÿ�ݷ�ʽ
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("��ʼ����Ϸ(N)");
		jmi1.setMnemonic('N');
		//��jmi1����ע�����
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jmi2=new JMenuItem("�˳���Ϸ(E)");
		jmi2.setMnemonic('E');
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exitgame");
		jmi3=new JMenuItem("�����˳���Ϸ(C)");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("savegame");
		jmi4=new JMenuItem("�����Ͼ���Ϸ(S)");
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
		// TODO �Զ����ɵķ������
		//���û���ͬ�ĵ������ͬ������
		if(arg0.getActionCommand().equals("newgame"))
		{
			//ɾ���͵ÿ�ʼ���
			this.remove(msp);
			//����ս�����
			mp=new MyPanel("newGame");
			//����mp
			Thread t=new Thread(mp);
			t.start();
			this.add(mp);
			//ע�����
			this.addKeyListener(mp);
			//��ʾˢ���µ����
			this.setVisible(true);
		}else if(arg0.getActionCommand().equals("exitgame"))
		{
			//�û�������˳�
			//������ٵ��˵�����
			Recorder.keepRecording();
			System.exit(0);
		}else if(arg0.getActionCommand().equals("savegame"))
		{
			//������˵������͵��˵�����
			Recorder.setEts(mp.ets);
			Recorder.keepRecAndEnemyTank();
			//�˳�
			System.exit(0);
		}else if(arg0.getActionCommand().equals("congame"))
		{
			//ɾ���͵ÿ�ʼ���
			this.remove(msp);
			//����ս�����
			mp=new MyPanel("con");
			//����mp
			Thread t=new Thread(mp);
			t.start();
			this.add(mp);
			//ע�����
			this.addKeyListener(mp);
			//��ʾˢ���µ����
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
		g.setFont(new Font("������κ",Font.BOLD,30));
		//��ʾ��Ϣ
		g.drawString("Stage: 1", 140, 140);
		}
	}

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while(true)
		{
			//����
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			times++;
			//�ػ�
			this.repaint();
		}
	}
}
//�����ҵ����
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//����һ���ҵ�̹��
	Hero hero=null;
	//������˵�̹����
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<Node> nodes=new Vector<Node>();
	int etsize=6;
	//����ը���ļ�����
	Vector<Bomb> bombs=new Vector<Bomb>();
	//��������ͼƬ ����ͼƬ�������һ��ը��
	Image image1=null;
	Image image2=null;
	Image image3=null;
	//���캯��
	public MyPanel(String flag)
	{
		//�ָ���¼
		Recorder.getRecording();
		
		hero=new Hero(180,230);
		//��ʼ�����˵�̹��
		if(flag.equals("newGame"))
		{
			for(int i=0;i<etsize;i++)
			{
				//����һ������̹��
				EnemyTank et=new EnemyTank((i+1)*40,0);
				et.setColor(0);
				et.setDirect(2);
				//��Mypanel�ĵ���̹��������������̹��
				et.setEts(ets);
				//��������̹��
				Thread t=new Thread(et);
				t.start();
				//�����˵�̹�˼���һ���ӵ�
				Shot s=new Shot(et.getX()+10,et.getY()+30,2);
				//��������˵�̹��
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				//����
				ets.add(et);
			}
		}
		else
		{
			nodes=Recorder.getNodesAndEnNum();
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);
				//����һ������̹��
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setColor(0);
				et.setDirect(node.direct);
				//��Mypanel�ĵ���̹��������������̹��
				et.setEts(ets);
				//��������̹��
				Thread t=new Thread(et);
				t.start();
				//�����˵�̹�˼���һ���ӵ�
				Shot s=new Shot(et.getX()+10,et.getY()+30,2);
				//��������˵�̹��
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				//����
				ets.add(et);
			}
		}
		try {
			image1=ImageIO.read(new File("bomb1.gif"));
			image2=ImageIO.read(new File("bomb2.gif"));
			image3=ImageIO.read(new File("bomb3.gif"));
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
//		//��ʼ������ͼƬ
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb1.gif"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb2.gif"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb3.gif"));
	}
	//������ʾ��Ϣ
	public void showInfo(Graphics g)
	{
		//������ʾ��Ϣ
		this.drawTank(80, 330, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"",110,350);
		this.drawTank(130, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"",160,350);
		//������ҵ��ܳɼ�
		g.setColor(Color.black);
		g.setFont(new Font("����",Font.BOLD,20));
		g.drawString("�����ܳɼ���:",420,30);
		
		this.drawTank(420,60,g,0,0);
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"",460,80);
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		this.showInfo(g);
		//�����Լ���̹��
		if(hero.isIslive())
		{
		this.drawTank(hero.getX(),hero.getY() , g, hero.getDirect(), 1);
		}
		//�����ҵ��ӵ�
		for(int i=0;i<hero.ss.size();i++)
		{
			Shot myShot=hero.ss.get(i);
			//�����ӵ� һ���ӵ�
			if(myShot!=null&&myShot.isIslive()==true)
			{
				g.draw3DRect(myShot.getX(), myShot.getY(), 1, 1, false);
			}
			if(myShot.isIslive()==false)
			{
				//��ss��ɾ�������ӵ�
				hero.ss.remove(myShot);
			}
		}
		//����ը��
		for(int i=0;i<bombs.size();i++)
		{
			//ȡ��ը��
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
			//��b������ֵ��С
			b.downLife();
			//���ը��������ֵ��Ϊ0,�ͰѸ�ը����������ȥ��
			if(b.getLife()==0)
			{
				bombs.remove(b);
			}
		}
		//�������˵�̹��
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			if(et.isIslive())
			{
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
				//�ٻ������˵��ӵ�
				//System.out.println("̹���ӵ��� "+et.ss.size());
				for(int j=0;j<et.ss.size();j++)
				{
					//ȡ���ӵ�
					Shot enemyShot=et.ss.get(j);
					if(enemyShot.isIslive())
					{
						//System.out.println("�� "+i+"��̹�˵� "+j+"���ӵ� x= "+enemyShot.getX());
						g.draw3DRect(enemyShot.getX(), enemyShot.getY(), 1, 1, false);
					}
					else
					{
						//������˵��ӵ���������ô�ʹ�vector��ȥ��
						et.ss.remove(enemyShot);
					}
				}
			}
		}
		
	}
	//���˵��ӵ��Ƿ������
	public void hitMe()
	{
		//ȡ��ÿһ�����˵�̹��
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			//ȡ��ÿ������̹�˵��ӵ�
			for(int j=0;j<et.ss.size();j++)
			{
				//ȡ���ӵ�
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
	//�ж��ҵ��ӵ��Ƿ񱻻��е��˵�̹��
	public void hitEnemyTank()
	{
	for(int i=0;i<hero.ss.size();i++)
	{
		//ȡ���ӵ�
		Shot myShot=hero.ss.get(i);
		//�ж��ӵ��Ƿ���Ч
		if(myShot.isIslive())
		{
			//ȡ��ÿ������̹��,�����ж�
			for(int j=0;j<ets.size();j++)
			{
				//ȡ������̹��
				EnemyTank et=ets.get(j);
				//�ж�̹����Ч
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
	
	//дһ������ר���ж��Ƿ������̹��
	public boolean hitTank(Shot s,Tank et)
	{
		boolean b2=false;
		//�жϸ�̹�˵ķ���
		switch(et.getDirect())
		{
		//������˵�̹�˷������ϻ�����
		case 0:
		case 2:
			if(s.getX()>et.getX()&&s.getX()<et.getX()+20&&s.getY()>et.getY()&&s.getY()<et.getY()+30)
			{
				//����
				//�ӵ�����
				s.setIslive(false);
				//����̹������
				et.setIslive(false);
				b2=true;
				//����һ��ը������Vector
				Bomb b=new Bomb(et.getX(),et.getY());
				bombs.add(b);
				
			}
			break;
		case 1:
		case 3:
			if(s.getX()>et.getX()&&s.getX()<et.getX()+30&&s.getY()>et.getY()&&s.getY()<et.getY()+20)
			{
				//����
				//�ӵ�����
				s.setIslive(false);
				//����̹������
				et.setIslive(false);
				b2=true;
				//����һ��ը������Vector
				Bomb b=new Bomb(et.getX(),et.getY());
				bombs.add(b);
			}
			break;
			
		}
		return b2;
	}
	//����̹�˵Ĳ���
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		//�ж�����
		switch(type)
		{
		case 0:
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		//�жϷ���
		switch(direct)
		{
		case 0:
			//�����ҵ�̹�ˣ���ʱ���ٷ�װ��һ��������
			//1������̹�˵����
			g.fill3DRect(x, y, 5, 30,false);
			//2������̹�˵��ұ�
			g.fill3DRect(x+15, y, 5, 30,false);
			//3�������м�ľ���
			g.fill3DRect(x+5,y+5,10,20,false);
			//4������Բ��
			g.drawOval(x+4,y+10, 10, 10);
		    //5��������
			g.drawLine(x+9, y+15, x+9, y);
			break;
		case 1:
			//��Ͳ����
			//1������̹�˵����
			g.fill3DRect(x, y, 30, 5,false);
			//2������̹�˵��ұ�
			g.fill3DRect(x, y+15, 30, 5,false);
			//3�������м�ľ���
			g.fill3DRect(x+5,y+5,20,10,false);
			//4������Բ��
			g.drawOval(x+10,y+4, 10, 10);
			//5��������
			g.drawLine(x+15, y+9, x, y+10);
			break;
		case 2:
			//��Ͳ����
			//1������̹�˵����
			g.fill3DRect(x, y, 5, 30,false);
			//2������̹�˵��ұ�
			g.fill3DRect(x+15, y, 5, 30,false);
			//3�������м�ľ���
			g.fill3DRect(x+5,y+5,10,20,false);
			//4������Բ��
			g.drawOval(x+4,y+10, 10, 10);
		    //5��������
			g.drawLine(x+9, y+15, x+9, y+30);
			break;
		case 3:
			//��Ͳ����
			//1������̹�˵����
			g.fill3DRect(x, y, 30, 5,false);
			//2������̹�˵��ұ�
			g.fill3DRect(x, y+15, 30, 5,false);
			//3�������м�ľ���
			g.fill3DRect(x+5,y+5,20,10,false);
			//4������Բ��
			g.drawOval(x+10,y+4, 10, 10);
			//5��������
			g.drawLine(x+15, y+9, x+30, y+10);
			break;
			
			
		}
	}
	@Override
	//�����´���w��ʾ�� s��ʾ��a��ʾ�� d��ʾ��
	public void keyPressed(KeyEvent arg0) {
		// TODO �Զ����ɵķ������
		if(arg0.getKeyCode()==KeyEvent.VK_W)
		{
			//�����ҵ�̹�˵ķ���
			this.hero.setDirect(0);
			this.hero.movUp();
		}else if(arg0.getKeyCode()==KeyEvent.VK_A)
		{
			//����
			this.hero.setDirect(1);
			this.hero.movLeft();
		}else if(arg0.getKeyCode()==KeyEvent.VK_S)
		{
			//����
			this.hero.setDirect(2);
			this.hero.movDown();
		}else if(arg0.getKeyCode()==KeyEvent.VK_D)
		{
			//����
			this.hero.setDirect(3);
			this.hero.movRight();
		}
		
		if(arg0.getKeyCode()==KeyEvent.VK_J)
		{
			//���û�����j��ʱ�򴴽�һ���ӵ�
			if(hero.ss.size()<=4)
			{
			this.hero.shotEnemy();
			}
		}
		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			this.hitMe();
			//�ػ�
			this.repaint();
		}
	}
	
}

