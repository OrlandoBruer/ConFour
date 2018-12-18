import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;





public class Game {
	
	private static int windowHeight;
	private static int windowWidth;
	private static int height = 0;
	private static int width = 0;
	private static char[][] board;
	private static Integer[] tokensPerColumn; //Stores the amount of moves made in each column of the board
	private static int totalTokens;
	private static char activePlayer = 'r';
	private static Queue<Point> clickQueue;
	private static BufferedImage border = null;
	private static BufferedImage yellowToken = null;
	private static BufferedImage redToken = null;
	private static Graphics graphic;
	private static JLabel infotext;
	private static boolean gameWon = false;
	
	//Load settings from file
	public static void loadSettings() {
		List<String> settingsList = new ArrayList<String>();
		
		try {
			File settingsFile = new File("ConFour/settings.txt");
			FileReader fileReader = new FileReader(settingsFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
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
	}
	
	/*Receives a player's move and returns a value based on the state of the board
	 * 0: successful move
	 * 1: invalid move
	 * 2: victory
	 * 3: draw
	 */
	static int sendMove(int x, char colour) {
		
		Integer xPos = (x/40);
		//Case where there are no more available places to go
		if(totalTokens == 0) {
			infotext.setText("<html><font color='black'>Draw!</font></html>"); 
			return 3;
		}
		if(xPos < 0 || xPos > width) {
			infotext.setText("<html><font color='black'>Invalid move</font></html>"); 
			return 1;
		}
		
		//Column is full
		if(tokensPerColumn[xPos] >= height-1) {
			infotext.setText("<html><font color='black'>Invalid move</font></html>"); 
			return 1;
		}else {
			//Add the token to the board
			board[xPos][tokensPerColumn[xPos]] = colour;
			tokensPerColumn[xPos]++;
			totalTokens--;
			if(activePlayer == 'r') {
				activePlayer = 'g';
				infotext.setText("<html><font color='yellow'>Yellow's turn</font></html>"); 
			}else {
				activePlayer = 'r';
				infotext.setText("<html><font color='red'>Red's turn</font></html>");
			}
			int directionCount[] = new int[6];

		
		
			/*
			 *  index of directionCount corresponds to direction of token row
			 *  
			 *      6     0
			 *      5 [o] 1
			 *      4  3  2
			 * 
			 * 
			 */
			int i = 0;
			// CHECK TOP RIGHT

			directionCount[0] = 0;
			while(i < 3 & xPos + i < width-1) {
				if(tokensPerColumn[xPos + i] < height-1) {
					if(board[xPos + i+1][tokensPerColumn[xPos]+i] == colour) {
						directionCount[0]++;
						if(directionCount[0] >= 3) {
							return 2;
						}
					}					
				}
				i++;
			}
			
			
//			//CHECK RIGHT
			directionCount[1] = 0;
			i = 0;
			while(i < 3 & xPos + i < width-1) {
				if(board[xPos + i+1][tokensPerColumn[xPos]-1] == colour) {
					directionCount[1]++;
					if(directionCount[1] >= 3) {

						return 2;
					}
				}
				i++;
			}
			
//			//CHECK BOTTOM RIGHT
			directionCount[2] = 0;
			i = 0;
			while(i < 3 & xPos + i < width-1) {
				if(tokensPerColumn[xPos]-i > 1) {
					if(board[xPos + i+1][tokensPerColumn[xPos]-1-i] == colour) {
						directionCount[2]++;
						if(directionCount[2] >= 3) {
							System.out.println("bottom right");
							return 2;
						}
					}					
				}
				i++;
			}
//			for(int xT = xPos; xT < width-1; xT++) {
//				if(tokensPerColumn[xT] > 1) {
//					if(board[xT+1][tokensPerColumn[xT]-2] == colour) {
//						directionCount[2]++;
//						if(directionCount[2] >= 3) {
//							return 2;
//						}
//					}					
//				}
//			}
//			
//			//CHECK BOTTOM
//			directionCount[3] = 0;
//			for(int yT = tokensPerColumn[xPos]; yT > 0; yT--) {
//				if(board[xPos][yT] == colour) {
//					directionCount[3]++;
//					if(directionCount[3] >= 3) {
//						return 2;
//					}
//				}
//			}
			
			//CHECK BOTTOM LEFT
//			directionCount[4] = 0 + directionCount[0];
//			System.out.println("direction count:" + directionCount[4]);		
//			
//			
//			for(int xT = xPos; xT > 0; xT--) {
//				if(tokensPerColumn[xT] > 1 ) {
//					if(board[xT-1][tokensPerColumn[xT]-2] == colour) {
//						directionCount[4]++;
//						if(directionCount[4] >= 3) {
//							System.out.println("direction ASDSADnt:" + directionCount[4]);		
//							return 2;
//						}
//					}					
//				}
//			}
//			

//			int i = 0;
//			for(int yT = tokensPerColumn[xPos]-1; yT > 0; yT--) {
//				if(xPos - i > 0) {
//					i++;
//				}
//				if(board[xPos-i][yT] == colour) {
//					directionCount[4]++;
//					if(directionCount[4] >= 3) {
//						System.out.println("test");
//						return 2;
//					}
//				}
//			}
			
			
//			while(board[(xPos+j)][tokensPerColumn[xPos]-1] == colour) {
//				
//				directionCount[i]++;
//				j++;
//				if(directionCount[i] >= 4) {
//					return 2;
//				}
//
//			}
			
			
			

//			System.out.println("Board pos:" + board[xPos][tokensPerColumn[xPos]-1]);
//			for(int i = 0; i < 7; i++) {
//				int j = 0;
//				switch(i) {
//					case(0): //Checking up and to the right for consecutive tokens
//						
////						if(board[xPos][tokensPerColumn[xPos]-1])
//						
//						
//						break;
//					case(1): //  Checking directly right
//						//Check for board edge
//						if(xPos > width-4) {
//							break;
//						}
//						while(board[(xPos+j)][tokensPerColumn[xPos]-1] == colour) {
//							
//							directionCount[i]++;
//							j++;
//							if(directionCount[i] >= 4) {
//								return 2;
//							}
//		
//						}
//						break;
//						
//					
//					case(2):		
//					case(3):
//					case(4):
//					case(5): //  Checking directly left
//						
//						//Check for board edge
//						if(xPos < 3) {
//							break;
//						}
//						System.out.println("test");
//						while(board[(xPos-j)][tokensPerColumn[xPos]-1] == colour) {
//							
//							directionCount[i]++;
//							j++;
//							if(directionCount[i] >= 4) {
//								return 2;
//							}
//		
//						}
//						
//						directionCount[i] = directionCount[i] + directionCount[1];
//						System.out.println("Direction count: " + directionCount[i]);
//						if(directionCount[i] >= 4) {
//							return 2;
//						}
//						break;
//						
//					case(6):
//					default:
//						break;
//				}
//			}
			
		}
		
		return 0;
	}
	
	private static class MouseHandler implements MouseListener, MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Point currentPoint = e.getPoint();
			clickQueue.add(currentPoint);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
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
		
	}
	
	public static void main(String[] args){
		
		//Defaults in case load settings cannot get valid width and height
		windowWidth = 960; 
		windowHeight = 720;

		loadSettings();
		
		tokensPerColumn = new Integer[width];
		for(int i = 0; i < width; i++) {
			tokensPerColumn[i] = 0;
		}
		board = new char[width][height];
		totalTokens = width*height;
		
		JFrame window = new JFrame("Connect Four");
		JPanel panel = new JPanel();
//		panel.setLayout(null);
		panel.setBackground(Color.gray); 
		panel.setSize(width*40,height*40);
		window.getContentPane().add(panel);
		
		Dimension dim = new Dimension(windowWidth, windowHeight);
		window.setPreferredSize(dim);
		window.setMaximumSize(dim);
		window.setMinimumSize(dim);
		System.out.println(window.getSize().getWidth());
	
		MouseHandler mouse = new MouseHandler();
		
		clickQueue = new LinkedList<>();
		panel.addMouseListener(mouse);
		panel.addMouseMotionListener(mouse);
		


		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setVisible(true);

				
				
		
		ImageIcon borderImage = new ImageIcon();
		JLabel borderIcon = new JLabel();
		
		//Loading images from files
		try {
			border = ImageIO.read(new File("ConFour/border.png"));
		} catch (Exception e) {
			System.out.println("Unable to find border.png file");
		}
		try {
			yellowToken = ImageIO.read(new File("ConFour/yellowtoken.png"));
		} catch (Exception e) {
			System.out.println("Unable to find yellowtoken.png file");
		}
		try {
			redToken = ImageIO.read(new File("ConFour/redtoken.png"));
		} catch (Exception e) {
			System.out.println("Unable to find redtoken.png file");
		}		
		
		

		graphic = panel.getGraphics();

		
		infotext = new JLabel("default");
//		infotext.setSize(200, 40);
		infotext.setText("<html><font color='red'>Red's turn</font></html>");
//		infotext.setLocation(15, 0);
		panel.add(infotext);
		
		JButton restart = new JButton("Re");
		panel.add(restart);

		
		
		for(int i = 0; i < width; i++) {
			for( int j = 0; j < height; j++) {
				graphic.drawImage(border, 40*i, 40+40*j, null);
			}
		}
		window.repaint();
		panel.removeAll();
		infotext.setBounds(15, windowHeight - 15, 100,20);
		panel.add(infotext);
		
		
		restart.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	
		        activePlayer = 'r';
				infotext.setText("<html><font color='red'>Red's turn</font></html>");
		        
		        for(int i = 0; i < width; i++) {
		        	for(int j = 0; j < height; j++) {
		        		board[i][j] = ' ';
		        	}
		        	tokensPerColumn[i] = 0;
		        }
		        window.repaint();
		        gameWon = false;
		    }
		});
		
		panel.add(restart);

		while(true) {
				

				
				panel.add(infotext);
				panel.add(restart);

				
//				while(gameWon == true) {
//					if(gameWon = false) {
//						break;
//					}
//				}
//				
				if(!(clickQueue.isEmpty())) {
					Point clickPos = clickQueue.poll();
					if(clickPos.y >  40 & gameWon == false) {
						if(sendMove(clickPos.x,activePlayer) == 2) {
							gameWon = true;
							if(activePlayer == 'r') {
								infotext.setText("<html><font color='yellow'><bold>Yellow wins</bold></font></html>");
							}else {
								infotext.setText("<html><font color='red'><bold>Red wins</bold></font></html>");
							}
							
						};
						
					}
				}
				
				for(int i = 0; i < width; i++) {
					for( int j = 0; j < height; j++) {
						graphic.drawImage(border, 40*i, 40+40*j, null);
					}
				}
				for(int i = 0; i < width; i++) {
					for( int j = 0; j < height-1; j++) {
						if(board[i][j] == 'r') {
							graphic.drawImage(redToken, 5+ 40*i, windowHeight -65 -40*j, null);
						}else if(board[i][j] == 'g'){
							graphic.drawImage(yellowToken, 5+ 40*i, windowHeight -65 -40*j, null);
						}
						
					}
				}

		}

	
	}



		

		
	

}





