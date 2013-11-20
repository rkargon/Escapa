import java.awt.geom.Rectangle2D;

/**
 * 
 *  
 * @author Raphael Kargon
 */
public class Enemy extends Rectangle2D.Double {

	/**
	 * The horizontal and vertical velocity of the object
	 */
	private double vx;
	private double vy;
	
	/**
	 * @return the horizontal velocity of the object
	 */
	public double getVx() {
		return vx;
	}

	/**
	 * @param vx the horizontal velocity to set
	 */
	public void setVx(double vx) {
		this.vx = vx;
	}

	/**
	 * Reverses the horizontal velocity of the object
	 */
	public void reverseVx(){
		vx = -vx;
	}

	/**
	 * @return the vertical velocity of the object
	 */
	public double getVy() {
		return vy;
	}

	/**
	 * @param vy the vertical velocity to set
	 */
	public void setVy(double vy) {
		this.vy = vy;
	}

	/**
	 * Reverses the vertical velocity of the object
	 */
	public void reverseVy(){
		vy = -vy;
	}
	
	/**
	 * Creates a new enemy with the given shape and 0 velocity.
	 * @param x The x coordinate of the top left corner of the rectangle
	 * @param y The y coordinate of the top left corner of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public Enemy(double x, double y, double width, double height) {
		super(x, y, width, height);
		vx=0;
		vy=0;
	}

	/**
	 * Creates an enemy with a given velocity.
	 * @param x The x coordinate of the top left corner of the rectangle
	 * @param y The y coordinate of the top left corner of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @param vx The horizontal velocity of the object
	 * @param vy The vertical velocity of the object
	 */
	public Enemy(double x, double y, double width, double height, double vx, double vy)
	{
		super(x, y, width, height);
		this.vx = vx;
		this.vy = vy;
	}
	
	/**
	 * Displaces the rectangle based on the velocity.
	 */
	public void move()
	{
		x+=vx;
		y+=vy;
	}

	/**
	 * Sets the speed of the rectangle, maintaining the direction.
	 * 
	 * @param speed the new speed of the rectangle
	 */
	public void setSpeed(double speed) {
		double currspeed = Math.sqrt(vx*vx+vy*vy);
		double ratio = speed/currspeed;
		vx *= ratio;
		vy *= ratio;
	}
	
}
