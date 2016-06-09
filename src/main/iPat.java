package main;
//import org.rosuda.JRI.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.MigLayout;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;


public class iPat {	

	public static void main(String[] args){    	
		JFrame main = new JFrame();
		main.setTitle("iPat");	
		main.setSize(550,800);
		main.setLocation(200,0); 
		main.setLayout(new BorderLayout());
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cPane = main.getContentPane();
		cPane.add(new myPanel());	
		main.setVisible(true);
		main.setResizable(false);
		System.out.println(cPane.getWidth());
	}
	
}

class myPanel extends JPanel implements MouseMotionListener{	
	private static final int TBMAX=99;
	private static final int MOMAX=99;
	private static final int COMAX=10;	
	
	int[] TBimageX= new int[TBMAX];
	int[] TBimageY= new int[TBMAX];
	int[] TBimageH= new int[TBMAX];
	int[] TBimageW= new int[TBMAX];
	
	int[] MOimageX= new int[MOMAX];
	int[] MOimageY= new int[MOMAX];
	int[] MOimageH= new int[MOMAX];
	int[] MOimageW= new int[MOMAX];
	
	int[] COimageX= new int[COMAX];
	int[] COimageY= new int[COMAX];
	int[] COimageH= new int[COMAX];
	int[] COimageW= new int[COMAX];
		
	int MOimageX_int=430;
	int MOimageY_int=200;
	
	Image[] TB= new Image[TBMAX];
	Image[] MO= new Image[MOMAX];
	Image Excel, Powerpoint, Word, Music, Video, Unknown, Trash, 
		  TBimage, MOimage;
		
	Rectangle[] TBBound= new Rectangle[TBMAX];
	Rectangle[] MOBound= new Rectangle[MOMAX];
	Rectangle[] COBound= new Rectangle[COMAX];
	
	int TBindex =0;
	int MOindex =0;
	int COindex =0;
	
	int TBcount =0;
	int MOcount =0;
	int COcount =0;
	
	int[] TBposX={50,  50,  50,  50,  50,  50,  50,
				  200, 200, 200, 200, 200, 200, 200,
				  350, 350, 350, 350, 350, 350, 350,
				  60, 60, 60, 60, 60, 60, 60,
				  210, 210, 210, 210, 210, 210, 210,
				  360, 360, 360, 360, 360, 360, 360,
				  70, 70, 70, 70, 70, 70, 70,
				  220, 220, 220, 220, 220, 220, 220,
				  370, 370, 370, 370, 370, 370, 370};
	int[] TBposY={160, 240, 320, 400, 480, 560, 640,
				  160, 240, 320, 400, 480, 560, 640,
				  160, 240, 320, 400, 480, 560, 640,
				  170, 250, 330, 410, 490, 570, 650,
				  170, 250, 330, 410, 490, 570, 650,
				  170, 250, 330, 410, 490, 570, 650,
				  180, 260, 340, 420, 500, 580, 660,
				  180, 260, 340, 420, 500, 580, 660,
				  180, 260, 340, 420, 500, 580, 660};
	int pos;
	
	JLayeredPane startPanel;
	JPanel mainPanel;	
	JPanel nullPanel;
	JPanel buttonPanel;
	JPanel layoutPanel;
	
	JButton TBButton = new JButton();
	JButton MOButton = new JButton();
	JLabel iPat = new JLabel();

	JFileChooser[] TBchooser= new JFileChooser[TBMAX];
	
	String[] TBfile= new String[TBMAX];
		
	int[] TBvalue= new int[TBMAX];
	
	JLabel[] TBname= new JLabel[TBMAX];	
	JLabel[] MOname= new JLabel[MOMAX];		

	Timer timer;
	int panH;
	int panW;

	Timer fade;
	public static final long RT=100;
	private float alpha=0f;
	private long startTime=-1;
	
	boolean removeornot=false;
	int delbbound=200;
	JLabel trashl;
	
	int link;
	int coA, coB;
	//int[][] x = new int[10][20];
	
	int linex1=-100,
		linex2=-100,
		liney1=-100,
		liney2=-100;
	
	public myPanel(){	
		this.setBackground(Color.white);
		try{
			Image iconIP = ImageIO.read(getClass().getResource("iPat.png"));
			iPat.setIcon(new ImageIcon(iconIP));
		} catch (IOException ex){}
		try{
			Trash = ImageIO.read(getClass().getResource("Trash.png"));
		} catch (IOException ex){}
	
		
		try{
			Excel = ImageIO.read(this.getClass().getResourceAsStream("Excel.png"));
		} catch (IOException ex){}
		try{
			Powerpoint = ImageIO.read(this.getClass().getResourceAsStream("Powerpoint.png"));
		} catch (IOException ex){}
		try{
			Word = ImageIO.read(this.getClass().getResourceAsStream("Word.png"));
		} catch (IOException ex){}
		try{
			Video = ImageIO.read(this.getClass().getResourceAsStream("Video.png"));
		} catch (IOException ex){}
		try{
			Music = ImageIO.read(this.getClass().getResourceAsStream("Music.png"));
		} catch (IOException ex){}
		try{
			Unknown = ImageIO.read(this.getClass().getResourceAsStream("Unknown.png"));
		} catch (IOException ex){}				
		
		
		
		try{
			TBimage = ImageIO.read(this.getClass().getResourceAsStream("Table.png"));
			TBButton.setIcon(new ImageIcon(TBimage));
		} catch (IOException ex){}
		try{
			MOimage = ImageIO.read(this.getClass().getResourceAsStream("Model.png"));
			MOButton.setIcon(new ImageIcon(MOimage));
		} catch (IOException ex){}			
		for (int i=1; i<=TBMAX-1; i++){
			TB[i] = TBimage;
		}
		for (int i=1; i<=MOMAX-1; i++){
			MO[i] = MOimage;
		}
		
		
		
		
		
		final Timer fade = new Timer(40, new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 if (startTime < 0) {
                     startTime = System.currentTimeMillis();
                 } else {
                     long time = System.currentTimeMillis();
                     long duration = time - startTime;
                     if (duration >= RT) {
                         startTime = -1;
                         ((Timer) e.getSource()).stop();
                         alpha = 1f;
                     } else {
                         alpha = 1f - ((float) duration / (float) RT);
                     }
                     repaint();
                 }
             }
         });
		
		////////////
		////////////
		////////////
		//LAYOUT.START
		////////////
		////////////
		////////////
		
		TBButton.setOpaque(false);
		TBButton.setContentAreaFilled(false);
		TBButton.setBorderPainted(false);
		MOButton.setOpaque(false);
		MOButton.setContentAreaFilled(false);
		MOButton.setBorderPainted(false);
		iPat.setOpaque(false);
		
		startPanel = new JLayeredPane();
		layoutPanel = new JPanel(new MigLayout("fillx", "[]","[][]"));	
		nullPanel= new JPanel();
		
		layoutPanel.add(iPat,"wrap, span 2, alignx c");
		layoutPanel.add(TBButton,"alignx r");
		layoutPanel.add(MOButton,"alignx l");
				
		startPanel.setPreferredSize(new Dimension(550, 200));
		trashl = new JLabel(new ImageIcon(Trash));
		startPanel.add(trashl, new Integer(1));
		startPanel.add(layoutPanel,  new Integer(3));
		trashl.setBounds(new Rectangle(-1000, -50, 550, 300));
		trashl.setVisible(true);
		layoutPanel.setBounds(new Rectangle(0, 0, 550,200));
		layoutPanel.setVisible(true);

		this.setLayout(new MigLayout(" fillx","[grow]","[grow]"));
		this.add(startPanel,"dock north");
		this.add(nullPanel,"grow"); 
	
		nullPanel.setLayout(null);
		startPanel.setOpaque(false);
		layoutPanel.setOpaque(false);
		nullPanel.setOpaque(false);
			
		////////////
		////////////
		////////////
		//LAYOUT.END
		////////////
		////////////
		////////////
				
		for (int i=1; i<=TBMAX-1; i++){
			TBchooser[i]= new JFileChooser();
			TBfile[i]= new String();
			TBname[i]= new JLabel();
			nullPanel.add(TBname[i]);
			TBimageH[i]=TB[i].getHeight(null);	
			TBimageW[i]=TB[i].getWidth(null);		
		}	
		for (int i=1; i<=MOMAX-1; i++){
			MOname[i]= new JLabel();		
			nullPanel.add(MOname[i]);
			MOimageH[i]=MO[i].getHeight(null);	
			MOimageW[i]=MO[i].getWidth(null);
		}
		
		addMouseMotionListener(this);
		
		timer = new Timer(10, new ActionListener() {
			int ant=10;
		    @Override
		    public void actionPerformed(ActionEvent ae) {
		    	double d=Math.pow(ant*50, .3);
		    	int dd= (int)d;
				TBimageY[TBcount]=TBimageY[TBcount]+(dd);
		        repaint();
		        ant--;
		        if(ant==0){
		        	timer.stop();
		        	TBBound[TBcount]=new Rectangle(TBimageX[TBcount], TBimageY[TBcount],TB[TBcount].getWidth(null), TB[TBcount].getHeight(null));
		        	ant=10;
		        }
		    }
		});
			
		
		
		
		TBButton.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent evt) {
    			int x = evt.getX();
    			int y = evt.getY();
    			if (contains(x,y)){
    				TBcount++;
    				pos= ((TBcount-1)%63);
    				System.out.println(pos);
    				TBimageX[TBcount]=TBposX[pos];
    				TBimageY[TBcount]=TBposY[pos];					
       				timer.start();	   			
    			}		
    		}
    	});  
		
		MOButton.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent evt) {
    			int x = evt.getX();
    			int y = evt.getY();
    			if (contains(x,y)){
    				MOcount++;
    				MOimageX[MOcount]=MOimageX_int;
    				MOimageY[MOcount]=MOimageY_int+100*(MOcount-1);		
    				MOBound[MOcount]=new Rectangle(MOimageX[MOcount], MOimageY[MOcount],MO[MOcount].getWidth(null), MO[MOcount].getHeight(null));
    				repaint();
    			}		
    		}
    	}); 

		
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent ee){				
				int move_x=ee.getX();
    			int move_y=ee.getY();		    			
    			if (TBcount>0){			
    				for (int i=1; i<=TBcount;i++){
    					if (TBBound[i].contains(move_x, move_y)){
    						TBindex=i;
    						System.out.println(i);					
    						if (SwingUtilities.isRightMouseButton(ee)){
    							System.out.println("right");
    							 TBvalue[i]= TBchooser[i].showOpenDialog(null);
    	    					 if (TBvalue[i] == JFileChooser.APPROVE_OPTION){
    	    					    File selectedfile = TBchooser[i].getSelectedFile();  	    					    
    	    					  	TBfile[i]= selectedfile.getAbsolutePath();
    	    					  	iconchange(i); 
    	    					  	TBimageH[TBindex]=TB[i].getHeight(null);
    	    					  	TBimageW[TBindex]=TB[i].getHeight(null);
    	    						TBname[i].setLocation(TBimageX[i], TBimageY[i]+TBimageH[i]-195);
    	    						TBname[i].setSize(200,15);
    	    						TBname[i].setText(selectedfile.getName());
    	    						TBindex=0;
    	    						repaint();
    	    					 }
    					   	};
    					}
    				}
    			}     	
    			if (MOcount>0&&TBindex==0){			
    				for (int i=1; i<=MOcount;i++){
    					if (MOBound[i].contains(move_x, move_y)){
    						MOindex=i;
    						System.out.println(i);		
    					}
    				}
    			} 
    			if (COBound[1]!=null && COBound[1].contains(move_x, move_y)){
					COindex=1;
					MOindex=0;
					TBindex=0;
					System.out.println("CO is selected");	
    			}else{
    				COindex=0;
    			}
			}	
				
			@Override
			public void mouseReleased(MouseEvent ee){	
				int y=ee.getY();				
				if (removeornot){
					if (TBindex!=0&&y<delbbound){
						TBimageX[TBindex]=-100;
						TBimageY[TBindex]=-100;
						TBBound[TBindex]=new Rectangle(-100,-100,0,0);
						TBname[TBindex].setLocation(-100,-100);
						repaint();
					}else if(MOindex!=0&&y<delbbound){
						MOimageX[MOindex]=-100;
						MOimageY[MOindex]=-100;
						MOBound[MOindex]=new Rectangle(-100,-100,0,0);
						MOname[MOindex].setLocation(-100,-100);
						repaint();
					}
    				trashl.setBounds(new Rectangle(-1000, -50, 550, 300));  				
					trashl.setVisible(true);
					removeornot=false;
	    		}
				if(link!=0 & TBindex!=0){
					
					System.out.println("linked");
					COimageX[1]=Math.min(TBimageX[link], TBimageX[TBindex]);
					COimageY[1]=Math.min(TBimageY[link], TBimageY[TBindex]);
					if (TBimageX[link]>=TBimageX[TBindex]){
						COimageW[1]=TBimageX[link]+TBimageW[link]-TBimageX[TBindex];
					}else{
						COimageW[1]=TBimageX[TBindex]+TBimageW[TBindex]-TBimageX[link];
					}
					if (TBimageY[link]>=TBimageY[TBindex]){
						COimageH[1]=TBimageY[link]+TBimageH[link]-TBimageY[TBindex];
					}else{
						COimageH[1]=TBimageY[TBindex]+TBimageH[TBindex]-TBimageY[link];
					}
		        	COBound[1]=new Rectangle(COimageX[1], COimageY[1], COimageW[1], COimageH[1]);
					coA= link;
					coB= TBindex;
				}else{
					COindex=0;
				}
				
				TBindex=0;
				MOindex=0;
			}
			
			@Override
    		public void mouseClicked(MouseEvent evt) {
    			int x = evt.getX();
    			int y = evt.getY();
    			if (evt.getClickCount() >= 2) {
    				for (int i=1; i<=TBcount; i++){
    					if (TBBound[i].contains(x,y)){
        					TBopenfile(i);
    					}
    				}	
    			}
    		}
			
		});				
	}	
	
	@Override
	protected void paintComponent(Graphics g) {
	     super.paintComponent(g);
		 System.out.println("use paint");
		 g.drawLine(linex1, liney1, linex2, liney2);
	     if (TBcount>0){	    	 
	    	 for (int i=1; i<=TBcount; i++){
			     g.drawImage(TB[i], TBimageX[i], TBimageY[i], this);
	    	 }
	     }	   
	     if (MOcount>0){	    	 
	    	 for (int i=1; i<=MOcount; i++){
			     g.drawImage(MO[i], MOimageX[i], MOimageY[i], this);	
	    	 }
	     }
	    
	}
	
	@Override
	 public void mouseDragged(MouseEvent e) {
		int imX = e.getX();
		int imY = e.getY();
		int boundN=0; //205
		int boundS=800;
		int boundE=550;
		KeepInPanel (imX, imY, TBindex, TBimageW, TBimageH, TBimageX, TBimageY,
					 TBname,  boundN,  boundS,  boundE, TBBound, TB);
		KeepInPanel (imX, imY, MOindex, MOimageW, MOimageH, MOimageX, MOimageY,
					 MOname,  boundN,  boundS,  boundE, MOBound, MO);
		if(COindex!=0){
			///
			///
			int dx= imX- (COimageX[1]+(COimageW[1]/2));
			int dy= imY- (COimageY[1]+(COimageH[1]/2));
			System.out.println("dx="+dx+" , dy="+dy+" COindex= "+COindex);
		
			TBimageX[coA]=TBimageX[coA]+dx;
			TBimageX[coB]=TBimageX[coB]+dx;
			COimageX[1]=COimageX[1]+dx;					
			
			TBimageY[coA]=TBimageY[coA]+dy;
			TBimageY[coB]=TBimageY[coB]+dy;		
			COimageY[1]=COimageY[1]+dy;
			
			COBound[1]=new Rectangle(COimageX[1], COimageY[1], COimageW[1], COimageH[1]);
			TBBound[coA]=new Rectangle(TBimageX[coA], TBimageY[coA], TB[coA].getWidth(null), TB[coA].getHeight(null));
			TBBound[coB]=new Rectangle(TBimageX[coB], TBimageY[coB], TB[coB].getWidth(null), TB[coB].getHeight(null));
			TBname[coA].setLocation(TBimageX[coA], TBimageY[coA]+TBimageH[coA]-195);
			TBname[coB].setLocation(TBimageX[coB], TBimageY[coB]+TBimageH[coB]-195);
			linex1=TBimageX[coA]+(TBimageW[coA]/2);
			linex2=TBimageX[coB]+(TBimageW[coB]/2);
			liney1=TBimageY[coA]+(TBimageH[coA]/2);
			liney2=TBimageY[coB]+(TBimageH[coB]/2);
			repaint();
			///
			///
			
		}	
		
		if ((TBindex!=0|MOindex!=0)&&imY<=(delbbound)){
			trashl.setBounds(new Rectangle(0, -50, 550, 300));
			startPanel.setLayer(trashl, new Integer(200));
			trashl.setVisible(true);
			startPanel.revalidate();
			removeornot=true;		
		}else if(imY>delbbound&&removeornot){
			trashl.setBounds(new Rectangle(-1000, -50, 550, 300));
			startPanel.setLayer(trashl, new Integer(1));
			trashl.setVisible(true);
			removeornot=false;				
		}
		
		if(TBindex!=0){
			for (int i=1; i<=TBcount; i++){
				if (i == TBindex) {
		                continue;
		        }
				int x= TBimageX[TBindex]+(TBimageW[TBindex]/2);
				int y= TBimageY[TBindex]+(TBimageH[TBindex]/2);
				int x2= TBimageX[i]+(TBimageW[i]/2);
				int y2= TBimageY[i]+(TBimageH[i]/2);
				double dist= Math.sqrt(Math.pow((x-x2), 2) + Math.pow((y-y2), 2));
				if (dist<100){
					System.out.println("close to "+i);
					link=i;
					linex1=x;
					linex2=x2;
					liney1=y;
					liney2=y2;
					break;	
				}else{
					link=0;
					linex1=0;
					linex2=0;
					liney1=0;
					liney2=0;
				}
			}
		}
	}
	
	
	
	@Override
	public void mouseMoved(MouseEvent ev) {
		int x= ev.getX();
		int y= ev.getY();
		//System.out.println("("+x+", "+y+")");
	}
	
	public void TBopenfile(int i){
		File openfile= new File(TBfile[i]);
			try{
				Desktop.getDesktop().open(openfile);
			} catch(IOException e) {
				e.printStackTrace();
			}
	}	
	
	public void iconchange(int i){
	  	if 		(TBfile[i].indexOf(".csv")>=0){TB[i]=Excel;}
	  	else if	(TBfile[i].indexOf(".xlm")>=0){TB[i]=Excel;}
	  	else if	(TBfile[i].indexOf(".xls")>=0){TB[i]=Excel;}
	  	else if	(TBfile[i].indexOf(".xlt")>=0){TB[i]=Excel;}
	  	
	  	else if	(TBfile[i].indexOf(".ppt")>=0){TB[i]=Powerpoint;}
	  	else if	(TBfile[i].indexOf(".ppa")>=0){TB[i]=Powerpoint;}
	  	else if	(TBfile[i].indexOf(".pps")>=0){TB[i]=Powerpoint;}
	  	else if	(TBfile[i].indexOf(".pot")>=0){TB[i]=Powerpoint;}
	  	else if	(TBfile[i].indexOf(".sldx")>=0){TB[i]=Powerpoint;}
	  	
	  	else if	(TBfile[i].indexOf(".doc")>=0){TB[i]=Word;}
	  	else if	(TBfile[i].indexOf(".dot")>=0){TB[i]=Word;}
	  	  	
	  	else if	(TBfile[i].indexOf(".m4a")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".m4b")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".m4p")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".mmf")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".mp3")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".mpc")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".msv")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".wav")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".wma")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".aiff")>=0){TB[i]=Music;}
	  	else if	(TBfile[i].indexOf(".3gp")>=0){TB[i]=Music;}
	  	
	  	else if	(TBfile[i].indexOf(".avi")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".asf")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".mov")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".avchd")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".flv")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".swf")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".gif")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".mpg")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".mp4")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".wmv")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".H.264")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".m4v")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".mkv")>=0){TB[i]=Video;}
	  	else if	(TBfile[i].indexOf(".rm")>=0){TB[i]=Video;}
	  	
	  	else{TB[i]=Unknown;}	  	
	}
	
	// html
	

	public void KeepInPanel (int x, int y, int index, int[] imageW, int[] imageH, int[] imageX, int[] imageY,
							 JLabel[] name, int boundN, int boundS, int boundE, Rectangle[] Bound, Image[] image){
		if (index !=0){		
			if(x<(0+(imageW[index]/2))){
				imageX[index]=0;
				if(y<(boundN+(imageH[index]/2))){
				 	imageY[index]=boundN;
				}else if(y>(boundS-(imageH[index]/2))){
					imageY[index]=boundS-imageH[index];
				}else{
					imageY[index]=y-(imageH[index]/2);
				}
				
			}else if(y<(boundN+(imageH[index]/2))){
			 	imageY[index]=boundN;
			 	if(x<(0+imageW[index]/2)){
					imageX[index]=0;
			 	}else if(x>(boundE-imageW[index]/2)){
			 		imageX[index]=boundE-imageW[index];
			 	}else{
			 		imageX[index]=x-(imageW[index]/2);
			 	}
			 	
			}else if(x>(boundE-(imageW[index]/2))){
			 	imageX[index]=boundE-imageW[index];
			 	if(y<(boundN+(imageH[index]/2))){
				 	imageY[index]=boundN;
				}else if(y>(boundS-(imageH[index]/2))){
					imageY[index]=boundS-imageH[index];
				}else{
					imageY[index]=y-(imageH[index]/2);
				}
			 	
			}else if(y>(boundS-(imageH[index]/2))){
			 	imageY[index]=boundS-imageH[index];
			 	if(x<(0+imageW[index]/2)){
					imageX[index]=0;
			 	}else if(x>(boundE-imageW[index]/2)){
			 		imageX[index]=boundE-imageW[index];
			 	}else{
			 		imageX[index]=x-(imageW[index]/2);
			 	}	
			 	
			 }else{
				imageX[index]=x-(imageW[index]/2);
			 	imageY[index]=y-(imageH[index]/2);
			}
		 	name[index].setLocation(imageX[index],imageY[index]+imageH[index]-195);
			Bound[index]=new Rectangle(imageX[index], imageY[index], image[index].getWidth(null), image[index].getHeight(null));
		 	repaint();
		}
	}
	
	/*
	static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon)icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
    */
}