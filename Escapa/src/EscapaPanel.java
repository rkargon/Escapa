import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This panel implements the "Escapa" game shown at <a herf="http://members.iinet.net.au/~pontipak/redsquare.html">http://members.iinet.net.au/~pontipak/redsquare.html</a>. 
 * 
 * The player controls a red square in the center of the screen. 
 * Blue enemies bounce around, and the player must avoid touching them or the black border. 
 * The player's score is the amount of time he or she lasts before losing.
 * 
 * The player clicks the screen to start.
 * 
 * @author Raphael Kargon
 * @version 1.0
 */
public class EscapaPanel extends JPanel 
{
	/**
	 * DARKRED - The dark red color of the player, with RGB values of 160, 0, 0, or <code>#A00000</code>
	 */
	private final Color DARKRED = new Color(160, 0, 0);
	
	/**
	 * DARKBLUE - The dark blue color of the enemies, with RGB values of 0, 0, 160, or <code>#0000A0</code>
	 */
	private final Color DARKBLUE = new Color(0, 0, 160);
	
	/**
	 * INIT_SPEED - The initial speed of the enemy objects.
	 */
	private final double INIT_SPEED=6.0;
	
	/** Keeps track of player mouse location relative to the red square, used for mouse dragging */
	private int dx=0, dy=0;
	
	/* GAME VARIABLES */
	private double speed; //The current speed of the enemies
	private int bordersize=60; //The thickness of the black border
	private int num_enemies=4; //The number of enemies
	private ArrayList<Enemy> enemies; //The list of enemies in the game //NOTE: I can probably do this just with an array
	private Rectangle2D.Double player; //The red square controlled by the player
	private Timer gametimer;
	private long current_time=0;
	private long initial_time=0;
	
	/**
	 * Creates a new EscapaPanel
	 */
	public EscapaPanel() 
	{
		super();
		player = new Rectangle2D.Double(300, 300, 50, 50);
		enemies = new ArrayList<Enemy>();
		gametimer = new Timer(20, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				current_time=e.getWhen();
				gameupdate();
				repaint();
			}
		});
		this.addMouseListener(new EscapaMouseAdapter());
		this.addMouseMotionListener(new EscapaMotionAdapter());
	}
	
	/**
	 * Creates a new EscapaPanel with a given number of enemies
	 * 
	 * @param enemies The number of enemy squares
	 */
	public EscapaPanel(int enemies)
	{
		this();
		if(enemies>0) this.num_enemies = enemies;
	}
	
	/**
	 * Creates a new EscapaPanel with a given number of enemies and a given border thickness
	 * @param border
	 * @param enemies
	 */
	public EscapaPanel(int border, int enemies)
	{
		this(enemies);
		this.bordersize = border;
	}

	/**
	 * Resets the game window, to create a new game.
	 * This is called, for instance, when the player loses a game.
	 * 
	 * The following steps are taken:
	 * <ul>
	 * <li>Player is relocated to middle of screen</li>
	 * <li>Enemy speed is reset</li>
	 * <li>Enemy array is cleared and new enemies with random velocity and location are created</li>
	 * </ul>
	 */
	public void resetgame()
	{
		
		speed=INIT_SPEED;
		player.x = (double)this.getWidth()/2.0;
		player.y = (double)this.getHeight()/2.0;
		
		enemies.clear();
		while(enemies.size()<num_enemies){
			//set up rectangle size, 20-80 pixels each side
			//set up velocity in random angle, with magnitude 'speed'
			double theta = Math.random()*2*Math.PI;
			Enemy rtemp = new Enemy(0, 0, 20.0+Math.random()*60, 20.0+Math.random()*60, speed*Math.cos(theta), speed*Math.sin(theta));
			
			rtemp.x=1+bordersize+(Math.random()*(getWidth()-2-2*bordersize-rtemp.getWidth())); 
			rtemp.y=1+bordersize+(Math.random()*(getHeight()-2-2*bordersize-rtemp.getHeight()));
			
			if(!rtemp.intersects(player)) {enemies.add(rtemp);}
		}
		repaint();
	}
	
	/**
	 * Updates the state of the game.
	 * Enemies are moved, player location is checked to see if game is over.
	 * 
	 * This is where the <i><b>MAGIC</b></i> happens!
	 */
	public void gameupdate()
	{
		//update speed every 10 seconds
		if((current_time-initial_time) % 10000 <= gametimer.getDelay()){
			speed++;
			for(int i=0; i<enemies.size(); i++){
				enemies.get(i).setSpeed(speed);
			}
		}
		
		//check if player is touching border
		if(player.getX()<bordersize || player.getMaxX()>this.getWidth()-bordersize || player.getY()<bordersize || player.getMaxY()>this.getHeight()-bordersize){
				endgame();
				return;
		}
		
		//update enemy positions
		for(int i=0; i<enemies.size(); i++){
			Enemy tmp = enemies.get(i);
			
			if(player.intersects(tmp)){ endgame(); return;} //is player touching enemy?
			
			if(tmp.getX()<bordersize){
				tmp.reverseVx();
				tmp.x=bordersize; //make sure to clear enemy from border
			}
			else if( tmp.getMaxX()>this.getWidth()-bordersize){
				tmp.reverseVx();
				tmp.x=this.getWidth()-bordersize-tmp.width;
			}
			if(tmp.getY()<bordersize){
				tmp.reverseVy();
				tmp.y=bordersize; //make sure to clear enemy from border
			}
			else if(tmp.getMaxY()>this.getHeight()-bordersize){
				tmp.reverseVy();
				tmp.y=this.getHeight()-bordersize-tmp.height;
			}
			tmp.move();
		}
	}
	
	/**
	 * Ends the game, displaying an error message with the player's score (time elapsed) and calls {@link #resetgame()}
	 */
	public void endgame()
	{
		JOptionPane.showMessageDialog(null, "You lasted "+(double)(current_time-initial_time)/1000+" seconds.", "You've lost!", JOptionPane.ERROR_MESSAGE, new ImageIcon("frowny_face.jpg"));
		gametimer.stop();
		resetgame();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		super.paintComponent(g2d);
		
		
		int width=this.getWidth(), height=this.getHeight();
		
		//draw border
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, bordersize, height); //left border
		g2d.fillRect(0, 0, width, bordersize); //top border
		g2d.fillRect(width-bordersize, 0, width, height); //right border
		g2d.fillRect(0, height-bordersize, width, height); //bottom border
		
		g2d.setColor(DARKRED);
		g2d.fill(player);
		
		g2d.setColor(DARKBLUE);
		for(int i=0; i<enemies.size(); i++)
		{
			g2d.fill(enemies.get(i));
		}
	}
	
	private class EscapaMouseAdapter extends MouseAdapter
	{
		/*
		 * (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 * 
		 * The game starts when the player clicks on the red square. 
		 */
		public void mousePressed(MouseEvent e)
		{
			if(!gametimer.isRunning()){
				initial_time = System.nanoTime()/1000000;
				gametimer.start();
			}
			if (player.contains(e.getX(), e.getY())){
				dx = (int) (e.getX()-player.getX());
				dy = (int) (e.getY()-player.getY());
			}
		}
	}
	
	private class EscapaMotionAdapter extends MouseMotionAdapter {
		/*
		 * (non-Javadoc)
		 * @see java.awt.event.MouseMotionAdapter#mouseDragged(java.awt.event.MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {
				player.x=e.getX() - dx;
				player.y=e.getY() - dy;
		}
	}
}
