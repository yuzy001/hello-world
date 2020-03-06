package xuexiba;
import java.awt.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameFrame extends JFrame implements ActionListener ,MouseListener,KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int grade = 0;//�ؿ���
	private int row = 7,column = 7,leftX = 0,leftY = 0;//row,column��ʾ�������ꣻleftX,leftY�������Ͻ�ͼƬλ��
	private int mapRow = 0,mapColumn = 0;//��ͼ��������
	private int width = 0,height = 0;//��Ļ��С
	private boolean acceptKey = true;
	private Image pic[] = null;
	private byte[][] map = null;
	private ArrayList<Map> list = new ArrayList<Map>();//���ڳ��ز���
	Sound sound;
	public MenuBar JB;
	public MenuItem GW;
	public Menu YM;
	
	final byte WALL = 1,BOX = 2,BOXONEND = 3,END = 4,MANDOWN = 5,
			MANLEFT = 6,MANRIGHT = 7,MANUP = 8,GRASS = 9,MANDOWNONEND = 10,MANLEFTONEND = 11,
			MANRIGHTONEND = 12,MANUPONEND = 13;
	
	public GameFrame(){
		super("��������Ϸ");
		setSize(600,600);
		JB=new MenuBar();
		YM=new Menu("�˵�");
		GW=new MenuItem("����");
		GW.addActionListener(this);
		setMenuBar(JB);
		YM.add(GW);
		JB.add(YM);
		setVisible(true);
		setResizable(false);
		setLocation(300,20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cont = getContentPane();
		cont.setLayout(null);
		cont.setBackground(Color.black);
		getPic();
		width = this.getWidth();
		height = this.getHeight();
		this.setFocusable(true);
		initMap();
		this.addKeyListener(this);
		this.addMouseListener(this);
		//��������
		sound = new Sound();
		sound.loadSound();
	}
	
	public void initMap(){
		map = getMap(grade);
		list.clear();
		byte[][] temp = map;
		for(int i=0;i<temp.length;i++)
		{
			for(int j=0;j<temp[0].length;j++){
				System.out.print(temp[i][j]+" ");		
				}
			System.out.println();
		}	
		
		getMapSizeAndPosition();
		getManPosition();
	}
	
	//��ȡ���ﵱǰλ��
	public void getManPosition(){
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				if(map[i][j]==MANDOWN||map[i][j]==MANUP||map[i][j]==MANLEFT||map[i][j]==MANRIGHT){
					row = i;
					column = j;
					break;
				}			
			}
		}
	}
	
	
	//��ȡ��Ϸ�����С����ʾ��Ϸ�����Ͻ�λ��
	public void getMapSizeAndPosition(){
		mapRow = map.length;
		mapColumn = map[0].length;
		leftX = (width - map[0].length * 30)/2;
		leftY = (height - map.length * 30)/2;
		System.out.println(leftX);
		System.out.println(leftY);
		System.out.println(mapRow);
		System.out.println(mapColumn);
	}
	
	public void getPic(){
		pic = new Image[14];
		for(int i=0;i<=13;i++){
			pic[i] = Toolkit.getDefaultToolkit().getImage("D:/Game/pic"+i+".png");
		}
	}
	
	public byte grassOrEnd(byte man){
		byte result = GRASS;
		if(man == MANLEFTONEND || man == MANRIGHTONEND || man == MANUPONEND || man == MANDOWNONEND){
			result = END;
		}
		
		return result;
	}

	private void moveUp(){
		if(map[row-1][column]==WALL)
			return;
		byte tempBox;
		byte tempMan;
		
		if(map[row-1][column]==BOX||map[row-1][column]==BOXONEND){		//�������һ��������
			if(map[row-2][column]==GRASS||map[row-2][column]==END){     //������ϵڶ����ǹ��������յ�
				Map currentMap = new Map(row,column,map);
				list.add(currentMap);//���ڳ��ز���
				tempBox = map[row-2][column]==END?BOXONEND:BOX;
				tempMan = map[row-1][column]==BOXONEND?MANUPONEND:MANUP;
				map[row][column] = grassOrEnd(map[row][column]);
				map[row-2][column] = tempBox;
				map[row-1][column] = tempMan;
				row--;
			}
		}else{//�������һ���ǹ��������յ�
			Map currentMap = new Map(row,column,map);
			list.add(currentMap);//���ڳ��ز���
			tempMan = map[row-1][column]==GRASS?MANUP:MANUPONEND;
			map[row][column] = grassOrEnd(map[row][column]);
			map[row-1][column] = tempMan;
			row--;
		}
		
	}
	
	private void moveDown(){
	
		if(map[row+1][column]==WALL)
			return ;
		
		byte tempBox;
		byte tempMan;
		
		if(map[row+1][column]==BOX||map[row+1][column]==BOXONEND){
			if(map[row+2][column]==END||map[row+2][column]==GRASS){
				Map currentMap = new Map(row,column,map);
				list.add(currentMap);//���ڳ��ز���
				tempBox = map[row+2][column] == END?BOXONEND:BOX;
				tempMan = map[row+1][column] == BOXONEND?MANDOWNONEND:MANDOWN;
				map[row][column] = grassOrEnd(map[row][column]);
				map[row+2][column] = tempBox;
				map[row+1][column] = tempMan;
				row++;
			}
		}else{
			Map currentMap = new Map(row,column,map);
			list.add(currentMap);//���ڳ��ز���
			tempMan = map[row+1][column]==GRASS?MANDOWN:MANDOWNONEND;
			map[row][column] = grassOrEnd(map[row][column]);
			map[row+1][column] = tempMan;
			row++;
		}
		
	}
	
	private void moveLeft(){
		
		if(map[row][column-1]==WALL)
			return ;
		
		byte tempBox;
		byte tempMan;
		
		if(map[row][column-1]==BOX||map[row][column-1]==BOXONEND){
			if(map[row][column-2]==END||map[row][column-2]==GRASS){
				Map currentMap = new Map(row,column,map);
				list.add(currentMap);//���ڳ��ز���
				tempBox = map[row][column-2] == END?BOXONEND:BOX;
				tempMan = map[row][column-1] == BOXONEND?MANLEFTONEND:MANLEFT;
				map[row][column] = grassOrEnd(map[row][column]);
				map[row][column-2] = tempBox;
				map[row][column-1] = tempMan;
				column--;
			}
		}else{
			Map currentMap = new Map(row,column,map);
			list.add(currentMap);//���ڳ��ز���
			tempMan = map[row][column-1]==GRASS?MANLEFT:MANLEFTONEND;
			map[row][column] = grassOrEnd(map[row][column]);
			map[row][column-1] = tempMan;
			column--;
		}
		
	}
	
	private void moveRight(){
		
		if(map[row][column+1]==WALL)
			return ;
		
		byte tempBox;
		byte tempMan;
		
		if(map[row][column+1]==BOX||map[row][column+1]==BOXONEND){
			if(map[row][column+2]==END||map[row][column+2]==GRASS){
				Map currentMap = new Map(row,column,map);
				list.add(currentMap);//���ڳ��ز���
				tempBox = map[row][column+2] == END?BOXONEND:BOX;
				tempMan = map[row][column+1] == BOXONEND?MANRIGHTONEND:MANRIGHT;
				map[row][column] = grassOrEnd(map[row][column]);
				map[row][column+2] = tempBox;
				map[row][column+1] = tempMan;
				column++;
			}
		}else{
			Map currentMap = new Map(row,column,map);
			list.add(currentMap);//���ڳ��ز���
			tempMan = map[row][column+1]==GRASS?MANRIGHT:MANRIGHTONEND;
			map[row][column] = grassOrEnd(map[row][column]);
			map[row][column+1] = tempMan;
			column++;
		}
		
	}
	
	
	public boolean isFinished(){
		for(int i=0;i<mapRow;i++)
		for(int j=0;j<mapColumn;j++){
	//		System.out.println("ֵ��"+map[i][j]+",END ��ֵ��"+END+"�����������:"+(map[i][j]==END));
		if(map[i][j]==END||map[i][j]==MANDOWNONEND||map[i][j]==MANUPONEND||map[i][j]==MANLEFTONEND||map[i][j]==MANRIGHTONEND){
			return false;
		}
	    }
		return true;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W){
			moveUp();
		}
		if(e.getKeyCode() == KeyEvent.VK_S){
			moveDown();
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			moveLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			moveRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){//��һ��
			acceptKey = true;
			priorGrade();
			return ;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){//��һ��
			acceptKey = true;
			nextGrade();
			return ;
		}
		if(e.getKeyCode() == KeyEvent.VK_B){//����
			undo();
		}
		
		repaint();
		
		if(isFinished()){
			//���ð���
			acceptKey = false;
			if(grade==10){JOptionPane.showMessageDialog(this, "��ϲͨ�����һ��");}
			else{
				String msg = "��ϲ��ͨ����"+(grade+1)+"�أ�����\n�Ƿ�Ҫ������һ�أ�";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "����";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					System.exit(0);
				}else if(choice == 0){
					acceptKey = true;
					nextGrade();
				}
			}
		}
		
	}

	public void paint(Graphics g){
		//System.out.println("�ұ�������");
		for(int i=0;i<mapRow;i++)
		for(int j=0;j<mapColumn;j++){
			if(map[i][j]!=0){
//				System.out.println("���λ�� ����0������ֵ��"+map[i][j]);
//				g.drawRect(10, 30, getWidth()/2-50, getHeight()/2-50);
				g.drawImage(pic[map[i][j]],leftX+j*30,leftY+i*30,30,30,this);
			}
		}
		g.setColor(Color.RED);
		g.setFont(new Font("����_2312",Font.BOLD,30));
		g.drawString("�����ǵ�", 150, 140);
		g.drawString(String.valueOf(grade+1),310,140);
		g.drawString("��", 360, 140);
	}
	
	public int getManX(){
		return row;
	}
	
	public int getManY(){
		return column;
	}
	
	public int getGrade(){
		return grade;
	}
	
	public byte[][] getMap(int grade){		
		return MapFactory.getMap(grade);
	}
	
	public void DisplayToast(String str){
		JOptionPane.showMessageDialog(null, str,"��ʾ",JOptionPane.ERROR_MESSAGE);
	}
	
	public void undo(){
		if(acceptKey){
			if(list.size()>0){
				Map priorMap = (Map)list.get(list.size()-1);
				map = priorMap.getMap();
				row = priorMap.getManX();
				column = priorMap.getManY();
				repaint();
				list.remove(list.size()-1);
			}else{
				DisplayToast("�����ٳ���");
			}
		}else{
			DisplayToast("�˹�����ɣ����ܳ���");
		}
	}
	
	public void priorGrade(){
		grade--;
		acceptKey = true;
		if(grade<0)
			grade = 0;
		initMap();
		clearPaint(this.getGraphics());
		repaint();
	}
	
	public void nextGrade(){
		if(grade>=MapFactory.getCount()-1){
			DisplayToast("��ϲ��������йؿ�");
			acceptKey = false;
		}else{
			grade++;
			initMap();
			clearPaint(this.getGraphics());
			repaint();
			acceptKey = true;
		}
	}
	
	private void clearPaint(Graphics g) {
	 g.clearRect(0, 0, width+leftX, height+leftY);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			undo();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==GW) {
			JFrame f=new JFrame("��Ϸ������֪");
			JPanel P=new JPanel();
			JLabel L_1=new JLabel("'W':���������ƶ�			");
			JLabel L_2=new JLabel("'A':���������ƶ�			");
			JLabel L_3=new JLabel("'S':���������ƶ�			");
			JLabel L_4=new JLabel("'D':���������ƶ�			");
			JLabel L_5=new JLabel("'B':���ص���һ��			");
			JLabel L_6=new JLabel("'��':��һ��");
			JLabel L_7=new JLabel("'��':��һ��");

			f.setSize(550,360);
			f.add(P);
			P.add(L_1);
			P.add(L_2);
			P.add(L_3);
			P.add(L_4);
			P.add(L_5);
			P.add(L_6);
			P.add(L_7);
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
	}
	
	public void WindowsListener(WindowEvent e) {
		
	}
	public static void main(String[] args){
		new GameFrame();
	}
	
}
