import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * 
 */

/**
 * @author Orly
 *
 */
public class Game {

	private static JFrame window;
	private static int height;
	private static int width;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		width = 960; 
		height = 720;
		
		window = new JFrame("This is the title");
		window.setLayout(null);
		
		Dimension dim = new Dimension(width, height);
		window.setPreferredSize(dim);
		window.setMaximumSize(dim);
		window.setMinimumSize(dim);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
	}

}
