import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

/** 
 * Sets up a window that displays an Escapa game, using the EscapaPanel class.
 * 
 * @author Raphael Kargon
 *
 */
public class EscapaMain {

	public static void main(String[] args) {
		EscapaPanel gamep = new EscapaPanel();

		//set up game window
		JFrame win = new JFrame("J-scapa!");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setSize(600, 600);
		
		//set up game panel
		Container pane = win.getContentPane();
		pane.setLayout(new GridLayout(1, 1));
		pane.add(gamep);		
		gamep.setBackground(Color.WHITE);
		win.setVisible(true);

		//initialize game panel
		gamep.resetgame();
		
		
	}

}
