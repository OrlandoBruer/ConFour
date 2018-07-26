import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author Orly
 *
 */




public class Game {

	private static JFrame window;
	private static JPanel panel;
	private static int windowHeight;
	private static int windowWidth;

	

	
	/**
	 * @param args
	 */
	
	public static void main(String[] args){
		
		windowWidth = 960; 
		windowHeight = 720;
		int height = 0;
		int width = 0;

		List<String> settingsList = new ArrayList<String>();
		
		//File settingsFile = new File("./settings.txt");
		try {
			File settingsFile = new File("ConFour/settings.txt");
			String settingsFilePath = settingsFile.getAbsolutePath();
			FileReader fileReader = new FileReader(settingsFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			System.out.println(settingsFilePath);
			String lineIterator;
			try {
				lineIterator = bufferedReader.readLine();
	
				while(lineIterator != null) {
					settingsList.add(lineIterator);
					lineIterator = bufferedReader.readLine();
				}
			} catch (IOException e) {
				System.out.println("Unable to read settings.txt properly");
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find settings.txt file");
		}
		
		int settingsDelimiterPos; //Stores position of ':' in a setting String
		
		//Iterate through settings.txt checking for entries that match: [valid setting name]:[setting value] and applying them
		for(String setting : settingsList) {
			if((settingsDelimiterPos = setting.indexOf(":") ) != -1) {
				switch(setting.substring(0, settingsDelimiterPos)){
					
				case ("height"):
					height = Integer.parseInt(setting.substring(settingsDelimiterPos+1));
						if(!(height > 1 && height <= 15)) {
							height = 6;
						}
					windowHeight = height*40 + 30; // Each 'slot' for a connect 4 token is 40px wide + 30 for the top menu bar.
					break;
				
				case ("width"):
					width = Integer.parseInt(setting.substring(settingsDelimiterPos+1));
					if(!(width > 1 && width <= 15)) {
						width = 7;
					}
				
					windowWidth = width*40;
					break;
				
				
				default:
					System.out.println("Invalid setting"); //Occurs when a setting contains a ':' but no valid setting name or value
					break;
				
				}
			}
		}
		
		window = new JFrame("Connect 4");
		//window.setLayout(null);
		JButton b1;
		
		Dimension dim = new Dimension(windowWidth, windowHeight);
		window.setPreferredSize(dim);
		window.setMaximumSize(dim);
		window.setMinimumSize(dim);
		System.out.println(window.getSize().getWidth());
	
		
		panel = new JPanel();

		window.add(panel);
		
		
		b1 = new JButton("Button");
		panel.add(b1);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		BufferedImage border = null;
		try {
			border = ImageIO.read(new File("ConFour/border.png"));


		} catch (IOException e) {
			System.out.println("Unable to find border.png file");
		}

		Graphics graphic = panel.getGraphics();
		graphic.drawImage(border, 100, 100,null);
	
		panel.repaint();
	}
	

		

		
	

}





