/* Phillip Mathew
 * April 21, 2014
 * Chemistory.java
 * 
*/

import java.awt.*;			//imports the awt
import java.awt.event.*;	//imports the event library
import javax.swing.*;		//imports swing library
import javax.swing.event.*;	//imports swing event lib
import javax.imageio.*;
import java.io.*;			//input output
import java.util.Scanner;	//scanner to read text files
import java.text.*;			//for special text
import java.util.ArrayList;	//imports arraylist to store and randomize the values of the positions of the game pieces
import java.util.List;		//imports List
import java.util.Collections;	//imports collections for randomizing lists
import javax.swing.border.*;	//for special borders

public class IonMatchers {
	JFrame frame;				//the main frame
	DisplayPanel displaypanel;	//the panel used on cardlayout that will display the other panels
	CardLayout cards;		//the cardlayout to shift between the panels
	StartPanel start;		//the beginning panel, with 4 buttons to choose from
	SettingsPanel settings;	//the panel with settings for the game
	GamePanel gamepanel;		//the panel that displays the main game
	GamePanel.GameTopPanel.TimerPanel tpan;
	GamePanel.GameTopPanel gtoppan;
	InstructionsPanel instructions;		//the panel with the instructions
	ScoresPanel scorepanel;				//the panel that displays the high scores
	CountDownPanel countdownpanel;		//the panel that displays the countdown sequence until the game starts
	int score;						//the user's score
	GamePanel.OverPanel overpanel;
	PrintWriter scorewriter;
	int [] highscores;
	
	public IonMatchers() {	//constructor
		cards = new CardLayout();	//the cardlayout is being initialized
		score = 0;		//user's score is reset
		highscores = new int[10];
	}
	
	public static void main(String[] args) {	//main method where the program is run
		IonMatchers game = new IonMatchers();
		game.run();
	}
	
	public void run() {	//the run method that sets up the frame
		frame = new JFrame("Ion-Matchers");	//initialize frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 700);
		displaypanel = new DisplayPanel();	//the panel with the cardlayout holding all the other panels
		frame.getContentPane().add(displaypanel);	//adds the displaypanel to the frame
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addWindowListener(new FrameListen());
	}

	class FrameListen implements WindowListener {
		public void windowClosing(WindowEvent e) {
			writeToText();
		}
		public void windowOpened(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
	}

	public void writeToText() {	
		try {
			scorewriter = new PrintWriter(new File("Hiscores.txt"));
		} catch (IOException e) {
			System.err.println("ERROR. Hiscores.txt cannot be found.");
			System.exit(1);
		}

		scorewriter.print("");
			for (int i = 0; i < 10; i++) {
				scorewriter.println(highscores[i]);
			}

		scorewriter.close();		
	}


	//
	// Beginning of class DisplayPanel
	//
	class DisplayPanel extends JPanel {
				
		public DisplayPanel() {		
			//constructor initializes all the panels and adds them all to the cardlayout with their respective names
			setLayout(cards);
			start = new StartPanel();
			settings = new SettingsPanel();
			gamepanel = new GamePanel();
			instructions = new InstructionsPanel();
			scorepanel = new ScoresPanel();
			countdownpanel = new CountDownPanel();
			overpanel = gamepanel.new OverPanel();
						
			add(start, "Start Panel");
			add(settings, "Settings Panel");
			add(gamepanel, "Gameboard Panel");
			add(instructions, "Instructions Panel");
			add(scorepanel, "Scores Panel");
			add(countdownpanel, "Count Down Panel");	
			add(overpanel, "Game Over Panel");		
		}
	}



	/////////////////////////////////////////////////////////////////
	//StartPanel - the panel that displays the start of menu of the game
	//
	class StartPanel extends JPanel {		
		JPanel top, bottom;		//two halves to the panel
				
		public StartPanel() {
			setLayout(new GridLayout(2, 1));
			
			top = new NameLogo();		//top contains nameLogo, the panel that displays the special logo	
			bottom = new ButtonsPanel();	//the botom has various panels, but mainly to pause/play the game
			
			add(top);		//add the panels to the halves
			add(bottom);				
		}			
		


		//////////////////////////////////////////////////////////////////////////////
		// NameLogo - the panel that prints out the name of the game in fancy text and alternating colors
		//
		class NameLogo extends JPanel {
			int width;
			Image image;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(Color.green);
				width = getWidth();		
				Font heading = new Font("Cambria", Font.BOLD, 60);
				g.setFont(heading);
				g.setColor(Color.blue);
				g.drawString("ION-MATCHERS", width/3 - 40, 60);

				getImage();

				g.drawImage(image, 400, 100, 200, 230, this);
			}

			public void getImage() {
				try {
					image = ImageIO.read(new File("Polyatomic ion picture.png"));
				}
				catch (IOException e){
					System.err.println("ERROR: Cannot open image file");
					System.exit(1);
				}
			}
		} 



		////////////////////////////////////////////////////////////////////////////////
		//	ButtonsPanel - the panel in the start panel that shows 4 buttons as options to choose from
		//		
		class ButtonsPanel extends JPanel {
			JPanel left, middle, right;		
			JButton playbutton, settingsbutton, instructbutton, scoresbutton;	//all the objects of the buttons
			
			public ButtonsPanel() {
				left = new JPanel();	//initializing
				middle = new JPanel();
				right = new JPanel();
				
				setLayout(new GridLayout(1, 3));	//the layout is a grid on the bottom and the buttons are put in the middle
				
				add(left);		//add the left green panel to match the other colors
				left.setBackground(Color.green);	
				
				add(middle);		//the middle panel contains all the buttons in the startpanel
				middle.setLayout(new GridLayout(4, 1));
				
				add(right);			//the right will just be like the left, essentially just a filler panel
				right.setBackground(Color.green);
				
				makePlayButton();	//calls methods to set up all the buttons
				makeSettingsButton();
				makeInstructButton();
				makeScoresButton();
			}
			
			public void makePlayButton() {
				//the play button is made
				playbutton = new JButton("PLAY");
				playbutton.setFont(new Font("Calibri", Font.PLAIN, 30));
				playbutton.setBackground(Color.blue);
				playbutton.setForeground(Color.white);
				playbutton.addActionListener(new PlayListener());	//has an action listener, it is button
				middle.add(playbutton);
			}
			
			public void makeSettingsButton() {
				//the settings button is made
				settingsbutton = new JButton("Settings");
				settingsbutton.setFont(new Font("Calibri", Font.PLAIN, 30));
				settingsbutton.setBackground(Color.white);
				settingsbutton.addActionListener(new SettingsListener());	//has an action listener, it is button
				middle.add(settingsbutton);
			}
			
			public void makeInstructButton() {
				//adds the button to the startpage
				instructbutton = new JButton("Instructions");
				instructbutton.setFont(new Font("Calibri", Font.PLAIN, 30));
				instructbutton.setBackground(Color.white);
				instructbutton.addActionListener(new InstructListener());	//has an action listener, it is button
				middle.add(instructbutton);
			}
			
			public void makeScoresButton() {
				//add the button leading to the high scores was there
				scoresbutton = new JButton("High Scores");
				scoresbutton.setFont(new Font("Calibri", Font.PLAIN, 30));
				scoresbutton.setBackground(Color.white);
				scoresbutton.addActionListener(new ScoresListener());	//has an action listener, it is button
				middle.add(scoresbutton);
			}
			
				
			class PlayListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					cards.show(displaypanel, "Gameboard Panel");	//display the count down sequence right after the play button is hit
					gamepanel.top.startGameTimer();
					gamepanel.center.randomize();
					gamepanel.center.fillBlanks();
					gamepanel.center.resetFont();
					gamepanel.center.holdlabel = 0;
					gamepanel.center.blanksq = 0;
					gamepanel.clickcount = 0;
					for (int i = 0; i < 12; i++) {
						gamepanel.center.gamesquares[i].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.gray),
						BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray)));		
					}
					score = 0;
					gamepanel.bottom.incScore();
				}
			}
			
			class SettingsListener implements ActionListener {	//listener to display the settings panel
				public void actionPerformed(ActionEvent e) {
					cards.show(displaypanel, "Settings Panel");
				}
			}
			
			class InstructListener implements ActionListener {	//listener to display all the instructions
				public void actionPerformed(ActionEvent e) {
					cards.show(displaypanel, "Instructions Panel");
				}
			}
			
			class ScoresListener implements ActionListener {	//the listener to display the top 10 high scores
				public void actionPerformed(ActionEvent e) {
					cards.show(displaypanel, "Scores Panel");
				}
			}
		}							
	}




	/////////////////////////////////////////////////////////////////////////
	// SettingsPanel - this is the panel where the settings can be chosen from
	//	
	class SettingsPanel extends JPanel {
		JTextField sheader;		//the header of the panel
		JButton settingsbackbutton;	//the back button, it goes back to the startpanel
		JComboBox<String> colors;	//a combobox so you can choose the colorscheme
		JRadioButton easybut, medbut, hardbut;	//buttongroup of difficulties, based on the speed of the timer
		ButtonGroup difficgroup;	//group and panel for the jradiobutton
		JPanel difficpanel;
		//Color cyan, white, gold, black, 
		
		public SettingsPanel() {	//constructor
			setLayout(null);	//null layout, setBounds is used

			sheader = new JTextField("Settings");	//header initialized
			sheader.setFont(new Font("Calibri", Font.BOLD, 50));	//all other hardcoding stuff
			sheader.setEditable(false);
			sheader.setHorizontalAlignment(SwingConstants.CENTER);
			sheader.setForeground(Color.blue);
			sheader.setBackground(Color.white);
			sheader.setBounds(400, 20, 200, 50);
			add(sheader);

			settingsbackbutton = new JButton("<-- BACK");	//the back button for settings and pretty much all other classes
			settingsbackbutton.setBackground(Color.red);	//red and white back button
			settingsbackbutton.setForeground(Color.white);
			settingsbackbutton.setBorder(BorderFactory.createLineBorder(Color.white, 2));	//adjusts the thickness of the border
			
			settingsbackbutton.addActionListener(new SettingsBackListener());	//adds a listener to the button
			settingsbackbutton.setBounds(5, 5, 110, 35);
			add(settingsbackbutton);

			colors = new JComboBox<>();
			colors.addItem("Cyan/White");
			colors.addItem("Red/Green");
			colors.addItem("Gold/Black");		//add 3 color schemes to the combobox, it is at cyan/white right now		
			colors.setBounds(400, 150, 200, 20);
			colors.setSelectedItem("Cyan/White");
			colors.addActionListener(new ColorListener());
			add(colors);	

			difficpanel = new JPanel();		
			difficpanel.setLayout(new FlowLayout());	//flowlayout for the difficulty button group

			JLabel difflab = new JLabel("Duration");	//the label header on top of the difficulties combobox
			difflab.setForeground(Color.blue);
			difficpanel.add(difflab);

			RadioButtonListener rbutlisten = new RadioButtonListener();
			//all the radio buttons are being added to the button group and put together
			difficgroup = new ButtonGroup();
			easybut = new JRadioButton("20 seconds");
			easybut.addActionListener(rbutlisten);
			difficgroup.add(easybut);	
			difficpanel.add(easybut);	
			medbut = new JRadioButton("40 seconds");
			medbut.addActionListener(rbutlisten);
			difficgroup.add(medbut);
			difficpanel.add(medbut);
			hardbut = new JRadioButton("60 seconds");
			hardbut.addActionListener(rbutlisten);
			difficgroup.add(hardbut);
			difficpanel.add(hardbut);
			easybut.setSelected(true);
			difficpanel.setBounds(450, 300, 100, 110);

			add(difficpanel);

		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.blue);
		}

		class RadioButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (easybut.isSelected()) {
					gamepanel.top.gametimer.setDuration(20);
					gamepanel.top.gametimer.timelab.setText("Time Left: " + 20);
				}
				else if (medbut.isSelected()) {
					gamepanel.top.gametimer.setDuration(40);
					gamepanel.top.gametimer.timelab.setText("Time Left: " + 40);
				}
				else if (hardbut.isSelected()) {
					gamepanel.top.gametimer.setDuration(60);
					gamepanel.top.gametimer.timelab.setText("Time Left: " + 60);
				}
			}
		}

		class ColorListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {				
				String cols = String.valueOf(colors.getSelectedItem());
				if (cols.equals("Cyan/White")) gamepanel.center.setBoardColors(Color.cyan, Color.white, Color.black);
				else if (cols.equals("Red/Green")) gamepanel.center.setBoardColors(Color.green, Color.red, Color.white);
				else if (cols.equals("Gold/Black")) {
					Color g = new Color(214, 196, 49);
					gamepanel.center.setBoardColors(g, Color.black, Color.white);
				}
			}
		}

		class SettingsBackListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				cards.show(displaypanel, "Start Panel");	//when the back button is clicked, it goes back to the start panel			
			}
		}
	}
	


	//////////////////////////////////////////////////////////////////
	// InstructionsPanel - the panel that displays the game's instructions
	//
	class InstructionsPanel extends JPanel {
		JTextField iheader;
		JButton instructbackbutton;	//back button
		Scanner scan;	//scanner to read the instructions in the text file
		String readinstructs;	//the string that holds the instructions
		JLabel labelinst;	
		JTextArea labelarea;	//the text area that holds all the instructions text
		Color instcolor;	//special color of the panel
		Image screenshot;
		
		public InstructionsPanel() {
			setLayout(null);	//null Layout
			instcolor = new Color(0, 120, 122);
			setBackground(instcolor);	//the color is being set

			iheader = new JTextField("Instructions");	//header initialized
			iheader.setFont(new Font("Calibri", Font.BOLD, 30));	//all other hardcoding stuff
			iheader.setEditable(false);
			iheader.setHorizontalAlignment(SwingConstants.CENTER);
			iheader.setForeground(Color.white);
			iheader.setBackground(instcolor);
			iheader.setBounds(400, 10, 200, 40);
			add(iheader);
			
			instructbackbutton = new JButton("<-- BACK");	//this panel's back button and the way it is set up
			instructbackbutton.setBackground(Color.red);
			instructbackbutton.setForeground(Color.white);
			instructbackbutton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
			
			instructbackbutton.addActionListener(new InstructBackListener());
			instructbackbutton.setBounds(5, 5, 110, 35);
			add(instructbackbutton);
			
			try {	//try catch block for the instructions file
				scan = new Scanner(new File("Chemistory.txt"));
			}
			catch (IOException e) {
				System.err.println("ERROR. Chemistory.txt could not be found.");
				System.exit(1);
			}
			
			int i = 0;
			while (scan.hasNext()) {		//as long as there are still more letters, this will continue
				i++;
				if (i == 1) { readinstructs = ""; }		//needed to take away the null error at the beginning of the file
				else { readinstructs += scan.nextLine(); }						
			}
			scan.close();
			
			labelarea = new JTextArea(readinstructs);	//put the whole string with the instructions into the textarea
			labelarea.setBackground(instcolor);
			labelarea.setForeground(Color.white);
			
			labelarea.setOpaque(true);		//edit the labelarea and how it looks
			labelarea.setFont(new Font("Calibri", Font.BOLD, 20));
			labelarea.setBounds(150, 50, 700, 300);
			labelarea.setMargin(new Insets(10, 10, 10, 10));
			labelarea.setLineWrap(true);	//makes the words wrap to the next line, if there is not enough space on the one line
			labelarea.setWrapStyleWord(true);
			labelarea.setEditable(false);
			add(labelarea);
		}

		public void getScreenshot() {
			try {
				screenshot = ImageIO.read(new File("IonMatcherspic.png")); 
			}
			catch (IOException e) {
				System.err.println("Cannot find IonMatcherspic.png.");
				System.exit(1);
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			getScreenshot();
			g.drawImage(screenshot, 275, 350, 450, 275, this);
		}
		
		class InstructBackListener implements ActionListener {		//the backbutton listener
			public void actionPerformed(ActionEvent e) {
				cards.show(displaypanel, "Start Panel");				
			}
		}
	}



	/////////////////////////////////////////////////////////////
	// ScoresPanel - the panel that displays the top 10 high scores
	//	
	class ScoresPanel extends JPanel { 	
		Color gold;		//gold color made with rgb
		JButton scorebackbutton;	//the backbutton
		JPanel topleftpan, topfil1;		//filler panels that just fill up grid layouts so it blends with regular background
		RankingPanel rankpanel;		//this is the panel that shows 1,,2,3,4... next to the scores
		RankedScores rankedscores;		//the panel that actually displays the corresponding scores
		JPanel sbotfil;		//filler panels
		JPanel sptop, topfil2;
		JLabel scoretitle;		//the title on the panel
		JPanel center;		//the panel that holds the two main scores panels in a gridlayout
		JLabel [] tenlabels;	//an array of labels for rankpanel
		JLabel [] tenscores;	//an array of labels for rankedscores
		JPanel left, right, bottom;	//filler panels for the borderlayout
		String[] timescores;	
		Scanner scanner;	//a scanner to read hiscores.txt
		
		public ScoresPanel() {
			setLayout(new BorderLayout());	//borderlayout
			gold = new Color(214, 196, 49);		//initialize and set background as gold
			setBackground(gold);

			sptop = new JPanel();	//the top of the whole scores panel
			sptop.setLayout(new GridLayout(1, 3));
			add(sptop, BorderLayout.NORTH);
			sptop.setPreferredSize(new Dimension(1000, 90));

			topleftpan = new JPanel();	//one of 3 panels in the top of the scores panel, which shows the back button a little smaller,
										//so it doesn't look strange with the scores, which is why the panel is a grid of 2 parts
			topleftpan.setLayout(new GridLayout(2, 1));
			
			scorebackbutton = new JButton("<-- BACK");	//the panels backbutton
			scorebackbutton.setBackground(Color.red);
			scorebackbutton.setForeground(Color.white);
			scorebackbutton.setBorder(BorderFactory.createRaisedBevelBorder());
			
			scorebackbutton.addActionListener(new ScoresBackListener());	//adds action listener to button
			scorebackbutton.setBounds(5, 5, 110, 35);

			topfil1 = new JPanel();		//filler panel that is added to keep everything consistent
			topfil1.setBackground(gold);

			topleftpan.add(scorebackbutton);
			topleftpan.add(topfil1);
			sptop.add(topleftpan);

			scoretitle = new JLabel("High Scores");	//the header displayed at the top of the scores panel
			scoretitle.setBackground(gold);
			scoretitle.setForeground(Color.black);
			scoretitle.setFont(new Font("Calibri", Font.BOLD, 40));
			scoretitle.setHorizontalAlignment(SwingConstants.CENTER);	//the text is fully centered
			scoretitle.setVerticalAlignment(SwingConstants.CENTER);
			scoretitle.setOpaque(true);

			topfil2 = new JPanel();		topfil2.setBackground(gold);	//more filler panels to match the color
			
			sptop.add(scoretitle);
			sptop.add(topfil2);

			center = new JPanel();	//the center panel of the scores panel will be divided into the rankpanel, 
									//the places, and the rankedscores, the actual scores in order
			center.setLayout(new BorderLayout());
			rankpanel = new RankingPanel();
			rankpanel.setPreferredSize(new Dimension(150, 700));
			rankedscores = new RankedScores();
			sbotfil = new JPanel();		//filler panel
			sbotfil.setBackground(gold);
			center.add(rankpanel, BorderLayout.WEST);
			center.add(rankedscores, BorderLayout.CENTER);

			add(center, BorderLayout.CENTER);


			left = new JPanel();		left.setBackground(gold);	//more panels added for aesthetics
			left.setPreferredSize(new Dimension(200, 800));
			right = new JPanel();		right.setBackground(gold);
			right.setPreferredSize(new Dimension(200, 800));
			bottom = new JPanel();		bottom.setBackground(gold);
			add(bottom, BorderLayout.SOUTH);
			while (bottom.isPreferredSizeSet() == false) {
				bottom.setPreferredSize(new Dimension(1000, 50));
			}

			add(left, BorderLayout.WEST);
			add(right, BorderLayout.EAST);
		}
		
		class ScoresBackListener implements ActionListener {	//actionlistener for the backbutton
			public void actionPerformed(ActionEvent e) {
				cards.show(displaypanel, "Start Panel");
			}
		}

		public void paintComponent(Graphics g) { 	//paints panel
			super.paintComponent(g);
		}

		//////////////////////////////////////////////////////////////////
		// RankingPanel - the panel that displays the places of the scores
		//
		class RankingPanel extends JPanel {		
			public RankingPanel() {
				tenlabels = new JLabel[10];
				setBackground(Color.black);		//adding ten labels with borders on top of each other with an increasing number each successive square
				for (int i = 0; i < 10; i++) {
					tenlabels[i] = new JLabel((i + 1) + "");
					tenlabels[i].setFont(new Font("Calibri", Font.BOLD, 40));
				}	 
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				setLayout(new GridLayout(10, 1));		//gridlayout broken up into 10 parts because the top 10 scores are displayed
				for (int i = 0; i < 10; i++) {
					tenlabels[i].setBackground(Color.black);
					tenlabels[i].setForeground(gold);
					tenlabels[i].setHorizontalAlignment(SwingConstants.CENTER);
					tenlabels[i].setVerticalAlignment(SwingConstants.CENTER);
					tenlabels[i].setBorder(BorderFactory.createLineBorder(gold, 1));
					add(tenlabels[i]);
				}
			}
		}


		//////////////////////////////////////////////////////////////////////////////////
		//RankedScores - the panel that shows the top 10 scores beside the places
		//
		class RankedScores extends JPanel {
			Scanner scorereader;				//scanner to scan the hiscore txt file

			public RankedScores() {
				timescores = new String [10];	//constructor to initialize the scores as 0 in the string array
				for (int j = 0; j < 10; j++) {
					timescores[j] = "";
				}
				tenscores = new JLabel[10];
				for (int i = 0; i < 10; i++) {
					tenscores[i] = new JLabel("");
					tenscores[i].setHorizontalAlignment(SwingConstants.CENTER);
					tenscores[i].setVerticalAlignment(SwingConstants.CENTER);
					tenscores[i].setFont(new Font("Calibri", Font.BOLD, 40));
				}			

				try {
					scorereader = new Scanner(new File("Hiscores.txt"));
				} catch (IOException e) {
					System.err.println("ERROR. Hiscores.txt cannot be found.");
					System.exit(1);
				}

				int index = 0;
				while (scorereader.hasNext()) {
					timescores[index] = scorereader.nextInt() + "";
					index += 1;	
				}

				for (int i = 0; i < 10; i++) {
					tenscores[i].setText(timescores[i]);
					tenscores[i].setHorizontalAlignment(SwingConstants.CENTER);
					tenscores[i].setVerticalAlignment(SwingConstants.CENTER);
					tenscores[i].setFont(new Font("Calibri", Font.BOLD, 40));
				}
				scorereader.close();

				tenscores = new JLabel[10];
				for (int i = 0; i < 10; i++) {
					tenscores[i] = new JLabel(timescores[i]);
					tenscores[i].setHorizontalAlignment(SwingConstants.CENTER);
					tenscores[i].setVerticalAlignment(SwingConstants.CENTER);
					tenscores[i].setFont(new Font("Calibri", Font.BOLD, 40));
				}
			}

			public void readScores() {

				for (int i = 0; i < 10; i++) {
					tenscores[i].setText(highscores[i] + "");
					tenscores[i].setHorizontalAlignment(SwingConstants.CENTER);
					tenscores[i].setVerticalAlignment(SwingConstants.CENTER);
					tenscores[i].setFont(new Font("Calibri", Font.BOLD, 40));
				}
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				setLayout(new GridLayout(10, 1));
				for (int i = 0; i < 10; i++) {
					tenscores[i].setBackground(Color.white);
					add(tenscores[i]);
					tenscores[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
				} 
			}
		}
	}



	////////////////////////////////////////////////////////////////////////////////////////////
	//CountDownPanel - the panel that displays the countdown sequence of 3 seconds right before the game actually starts
	//	
	class CountDownPanel extends JPanel {		
		CountDownListener countlisten;	//the listener 
		Timer downtimer;	//timer to countdown every second
		int countdown;		//the int used to decrement every second
		JLabel countdownlabel;		//the label displayed containg int countdown
		GamePanel.GameTopPanel gametop;		
		GamePanel.GameTopPanel.TimerPanel gametoptime;
				
		public CountDownPanel() {	//constructor
			setLayout(null);	
			setBackground(Color.cyan);	
			countdown = 3;	//start going down from 3
			countdownlabel = new JLabel("" + countdown);	
			countdownlabel.setFont(new Font("Calibri", Font.BOLD, 500));	//huge font needed
			countdownlabel.setForeground(Color.red);
			countdownlabel.setBackground(Color.cyan);
			countdownlabel.setVerticalAlignment(SwingConstants.CENTER);		//align the test
			countdownlabel.setHorizontalAlignment(SwingConstants.CENTER);
			countdownlabel.setBounds(0, 0, 1000, 800);		//stretches label across the whole panel
			countdownlabel.setOpaque(true);
			add(countdownlabel);

			countlisten = new CountDownListener();	//initializing the listener
			downtimer = new Timer(1000, countlisten);	//initializing timer used, it runs every 1 second
		}
		
		public void startDownTimer() {	//method called to start the countdown
			downtimer.start();
		}		

		class CountDownListener implements ActionListener {		//the actionlistener
			public void actionPerformed(ActionEvent e) {
				countdown -= 1;		
				if (countdown == 0) {		//if countdown is done, after 1, it says GO! in green
					countdownlabel.setText("GO!");
				}
				else if (countdown < 0) {	//after the downtimer is done, it stops and the game timer is called to start
					downtimer.stop();
					countdown = 3;
					cards.show(displaypanel, "Gameboard Panel");	//the gameboard panel is now displayed
				}
				else {
					countdownlabel.setForeground(Color.red);		//for 3,2,1 they are in red
					countdownlabel.setText("" + countdown);		
				}			
			}			
		}

		public void paintComponent(Graphics g) {	//paintcomponent
			super.paintComponent(g);			
		}
	}

	



	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//GamePanel - the main panel with the game itself, this has a panel on the top, middle and bottom
	//
	class GamePanel extends JPanel { 		
		GameTopPanel top;	//the top panel
		GamePlayPanel center;	//center panel
		GameBottomPanel bottom;	//bottom panel
		int clickcount;		//int to keep track of the number of clicks by the user
		//PrintWriter scorewriter;	//a printwriter to write to the hiscores file
		Scanner scorescan;		//a scanner to read the hiscores file
		//Timer timer;	//the game's timer, it will be 60 seconds long on easy, and faster and faster on the other two difficulties
		int gscore;	
		
		public GamePanel() {
			setLayout(new BorderLayout());	//borderlayout used for north and south for the top and bottom panels, center for the center panel
			top = new GameTopPanel();
			top.setPreferredSize(new Dimension(1000, 50));
			center = new GamePlayPanel();
			bottom = new GameBottomPanel();
			
			add(center, BorderLayout.CENTER);
			add(top, BorderLayout.NORTH);
			add(bottom, BorderLayout.SOUTH);

			try {
				scorescan = new Scanner(new File("Hiscores.txt"));
			} catch (IOException e) {
				System.err.println("ERROR. Hiscores.txt cannot be found.");
				System.exit(1);
			}
			
			int index = 0;
			while (scorescan.hasNext()) {
				highscores[index] = scorescan.nextInt();
				index += 1;
			}
		}

		public void changeHiScores(int g) {	//the method that writes to the hiscores file
			gscore = g;

			boolean greaterThan = false;
			int index = 9;

			for (int i = index; i > -1; i--) {
				if (gscore > highscores[i]) {
					greaterThan = true;
					index = i;	
				}
				else {
					greaterThan = false;
					break;
				}
			}

			if (greaterThan == false && gscore > highscores[9]) {
				if (!(index + 1 > 9)) {
					for (int h = 9; h > index; h--) {
						highscores[h] = highscores[h-1]; 
					}
					highscores[index] = gscore;
				}
			}
			else if (greaterThan == false && gscore < highscores[9]) {}
			else if (greaterThan == true) {
				for (int h = 9; h > index; h--) {
					highscores[h] = highscores[h-1]; 
				}
				highscores[0] = gscore;
			}

			scorescan.close();						

			scorepanel.rankedscores.readScores();
		}		
		

		////////////////////////////////////////////////////////////////////////////////////////////////////
		//the top panel of the gamepanel that consists of 3 parts: the backbutton, the timer, and the logo
		/////
		class GameTopPanel extends JPanel {	
			JButton gamebackbutton;	//backbutton of the gamepanel
			TimerPanel gametimer;	//timer in the game
			LogoPanel logo;		//logo used
			int count;
			JPanel filler;	// a filler panel
			
			public GameTopPanel() {		//constructor
				setBackground(Color.cyan);
				setLayout(new GridLayout(1, 5));
				
				gamebackbutton = new JButton("<-- BACK");	//the backbutton
				gamebackbutton.setBackground(Color.red);
				gamebackbutton.setForeground(Color.white);
				gamebackbutton.setBorder(BorderFactory.createLineBorder(Color.red, 2));
				
				gamebackbutton.addActionListener(new GameBackListener());
				add(gamebackbutton);	
				
				gametimer = new TimerPanel();
				add(gametimer);
				
				logo = new LogoPanel();
				add(logo);			
				//adding the backbutton, then the game timer, then the logo, all in order
			}

			public void startGameTimer() {
				gametimer.startTimer();
			}

			class GameBackListener implements ActionListener {	//the listener for the backbutton
				public void actionPerformed(ActionEvent e) {
					cards.show(displaypanel, "Start Panel");
					gametimer.timer.stop();
					count = gametimer.dur; 
					gametimer.timelab.setText("Time Left: " + count);
					clickcount = 0;
				}
			}
			

			////////////////////////////////////////////////////////////
			//the timer panel for the gameplay - counts down 60 seconds
			//
			class TimerPanel extends JPanel {		
				TimerListener listener;
				Timer timer;
				JLabel timelab;	//label whose text is being changed every second
				int dur;
												
				public TimerPanel() {
					count = 20;		//int that decrements every second, starts at 60
					dur = 20;
					listener = new TimerListener();
					timer = new Timer(1000, listener);		//the timer that runs every second
					timelab = new JLabel("Time Left: " + dur);		//editing and making the label look nice
					timelab.setFont(new Font("Times New Roman", Font.BOLD, 30));
					timelab.setVerticalAlignment(SwingConstants.CENTER);
					timelab.setHorizontalAlignment(SwingConstants.CENTER);
					add(timelab);
				}

				public void setDuration(int d) {
					dur = d;
				}
				
				public void startTimer() {		//the method to start the timer in this class
					count = dur;
					timer.start();
				}
												
				public void paintComponent(Graphics g) {	//paintcomponent
					super.paintComponent(g);
				}
				
				private class TimerListener implements ActionListener {
					public void actionPerformed(ActionEvent e) {	//the listener for the game timer
						count -= 1;
						timelab.setText("Time Left: " + count);
						if (count == 0) {
							count = 20;
							timelab.setText("Time Left: " + count);
							timer.stop();
							overpanel.finScore.setText("Final Score: " + score);
							cards.show(displaypanel, "Game Over Panel");							
						}
						repaint();										
					}
				}
			}
			

			/////////////////////////////////////////////////////////////////////////////////////
			//a smaller scale version of the logo panel that was used in the start panel
			//
			class LogoPanel extends JPanel {	
				int width;
				public void paintComponent(Graphics g) {	//only uses paintcomponent
					super.paintComponent(g);
					width = getWidth();		
					setBackground(Color.green);
					width = getWidth();		
					Font heading = new Font("Cambria", Font.BOLD, 35);
					g.setFont(heading);
					g.setColor(Color.blue);
					g.drawString("ION-MATCHERS", width/3 - 65, 35);
				}
			}
		}



		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//GamePlayPanel- the panel that contains all the squares from the game, where all the action goes on for the user
		//		
		class GamePlayPanel extends JPanel implements MouseListener {		
			int xloc, yloc;		//ints that represent the location of the mouse
			int firstx, firsty;	
			String[] ionnames;	//a string array of all the ionnames
			String[] sionforms;	//a string array of all the ionforms
			String[] arrsquares;	//a string array of both names and forms together
			List<Integer> numlist;	//list from 1-12 that show the positions on the board
			List<Integer> poslist;	//contains 6 random numbers from 0 to 25, and are used as indexes when using the string arrays to get ionnames
									//and ionforms
			int[] squarepos;	//an int array that contains 2 sets of 6 random positions, one for each string array 
			int[] temppos;		//the int array that has the 12 positions of each square scrambled up, corresponding with tempsquares
			JLabel[] gamesquares;	//a string array with ion names and forms before being scrambled ip
			String[] tempsquares;	//the string array with scrambled up names and formulas
			int holdlabel;	
			int blanksq;
			int[][] blanksquares;	
			int blankcount;		
			int hold1, hold2;
			boolean blanktrue;		//a boolean to check if the board is blank, and if it is, more ions will be supplied
			String squaretxt1, squaretxt2, squaretxt3;
			Color bgcol, fgcol, textcol;
			boolean gameover;

			
			public GamePlayPanel() {		//constructor to initialize all the stuff
				setLayout(null);

				sionforms = new String[26];		
				ionnames = new String[26];
				gamesquares = new JLabel[12];

				squarepos = new int[12];
				temppos = new int[12];

				arrsquares = new String[12];
				tempsquares = new String[12];
				holdlabel = 0;

				blanksquares = new int[6][2];
				blankcount = 0;
				hold1 = 0;
				hold2 = 0;
			 
				bgcol = Color.cyan;
				fgcol = Color.white;
				textcol = Color.black;

				clickcount = 0;					
				addMouseListener(this);		//mouselistener used to click on the squares

				blanktrue = false; 			//the boolean is initially false because the board is not blank

				makeIonForms();		//the ion names and forms all have to be initialized and put into the string arrays
				makeIonNames();
				randomize();		//randomizes everything
				drawBoard();		//draws the board out	
			}

			public void randomize() {	//method that randomizes the ions that are used
				poslist = new ArrayList<Integer>();
				int randnum = (int)(Math.random() * 26);	//random int used as index of the 2 string arrays
				for (int i = 0; i < 6; i++) {
					while (poslist.contains(randnum)) {		//check of the list already has the number, if it does, keep running 
															//the same thing in a loop until it is a different number
						randnum = (int)(Math.random() * 26);
					}
					poslist.add(randnum);		//when it is different, add it to the list
				}

				for (int c = 0; c < 6; c++) {		//add the 2 sets of the 6 random numbers
					squarepos[c] = poslist.get(c);
				}
				for (int c = 6; c < 12; c++) {
					squarepos[c] = poslist.get(c-6);
				}

				numlist = new ArrayList<Integer>();		//makes a list of 0-11 and shuffles it, so order of the squares are shuffled on the gameboard
				for (int i = 0; i < 12; i++) {
					numlist.add(i);
				}
				Collections.shuffle(numlist);

				for (int i = 0; i < 6; i++) {
					arrsquares[i] = sionforms[poslist.get(i)];
				}
				for (int i = 6; i < 12; i++) {
					arrsquares[i] = ionnames[poslist.get(i - 6)];
				}

				for (int c = 0; c < 12; c++) {
					tempsquares[c] = arrsquares[numlist.get(c)];
					temppos[c] = squarepos[numlist.get(c)];
				}
			}
						
			public void makeIonForms() {	//the method that fills a string array with a bunch of polyatomic ions with superscript and subscript
				String nitrate = new String("NO" + "\u2083" + "\u207B" + "\u00B9");
				sionforms[0] = nitrate;

				String carbonate = new String("CO" +"\u2083" + "\u207B" + "\u00B2");
				sionforms[1] = carbonate;

				String phosphate = new String("PO" + "\u2084" + "\u207B" + "\u00B3");
				sionforms[2] = phosphate;

				String chromate = new String("CrO" + "\u2084" + "\u207B" + "\u00B2");
				sionforms[3] = chromate;

				String sulfate = new String("SO" + "\u2084" + "\u207B" + "\u00B2");
				sionforms[4] = sulfate;

				String chlorate = new String("ClO" + "\u2083" + "\u207B" + "\u00B9");
				sionforms[5] = chlorate;

				String chlorite = new String("ClO" + "\u2082" + "\u207B" + "\u00B9");
				sionforms[6] = chlorite;
				
				String ammonium = new String("NH" + "\u2084" + "\u207A" + "\u00B9");
				sionforms[7] = ammonium;

				String dimercury = new String("Hg" + "\u2082" + "\u00B2");
				sionforms[8] = dimercury;

				String acetate = new String("CH" + "\u2083" + "COO" + "\u207B" + "\u00B9");
				sionforms[9] = acetate;

				String bromate = new String("BrO" + "\u2083" + "\u207B" + "\u00B9");
				sionforms[10] = bromate;

				String cyanide = new String("CN" + "\u207B" + "\u00B9");
				sionforms[11] = cyanide;

				String dihyphosphate = new String("H" + "\u2082" + "PO" + "\u2084" + "\u207B" + "\u00B9");
				sionforms[12] = dihyphosphate;

				String bicarbonate = new String("HCO" + "\u2083" + "\u207B" + "\u00B9");
				sionforms[13] = bicarbonate;

				String hysulfate = new String("HSO" + "\u2084" + "\u207B" + "\u00B9");
				sionforms[14] = hysulfate;

				String hydroxide = new String("OH" + "\u207B" + "\u00B9");
				sionforms[15] = hydroxide;

				String hypochlor = new String("ClO" + "\u207B" + "\u00B9");
				sionforms[16] = hypochlor;

				String nitrite = new String("NO" + "\u2082" + "\u207B" + "\u00B9");
				sionforms[17] = nitrite;

				String perchlorate = new String("ClO" + "\u2084" + "\u207B" + "\u00B9");
				sionforms[18] = perchlorate;

				String permang = new String("MnO" + "\u2084" + "\u207B" + "\u00B9");
				sionforms[19] = permang;

				String dichromate = new String("Cr" + "\u2082" + "O" + "\u2087" + "\u207B" + "\u00B2");
				sionforms[20] = dichromate;

				String hyphosphate = new String("HPO" + "\u2084" + "\u207B" + "\u00B2");
				sionforms[21] = hyphosphate;

				String oxalate = new String("C" + "\u2082" + "O" + "\u2084" + "\u207B" + "\u00B2");
				sionforms[22] = oxalate;

				String peroxide = new String("O" + "\u2082" + "\u207B" + "\u00B2");
				sionforms[23] = peroxide;

				String sulfite = new String("SO" + "\u2083" + "\u207B" + "\u00B2");
				sionforms[24] = sulfite;

				String arsenate = new String("AsO" + "\u2084" + "\u207B" + "\u00B3");
				sionforms[25] = arsenate;			
			}
			
			public void makeIonNames() {	//fills the string array of ion names with the names of the formulas above
				ionnames[0] = "Nitrate";
				ionnames[1] = "Carbonate";
				ionnames[2] = "Phosphate";
				ionnames[3] = "Chromate";
				ionnames[4] = "Sulfate";
				ionnames[5] = "Chlorate";
				ionnames[6] = "Chlorite";
				ionnames[7] = "Ammonium";
				ionnames[8] = "Dimercury";
				ionnames[9] = "Acetate";
				ionnames[10] = "Bromate";
				ionnames[11] = "Cyanide";
				ionnames[12] = "Dihydrogen Phosphate";
				ionnames[13] = "Bicarbonate";
				ionnames[14] = "Hydrogen Sulfate";
				ionnames[15] = "Hydroxide";
				ionnames[16] = "Hypochlorite";
				ionnames[17] = "Nitrite";
				ionnames[18] = "Perchlorate";
				ionnames[19] = "Permanganate";
				ionnames[20] = "Dichromate";
				ionnames[21] = "Hydrogen Phosphate";
				ionnames[22] = "Oxalate";
				ionnames[23] = "Peroxide";
				ionnames[24] = "Sulfite";
				ionnames[25] = "Arsenate";				
			}

			public void fillBlanks() {		//method to fill the blank squares after all are cleared
				for (int i = 0; i < 12; i++) {
					gamesquares[i].setText(tempsquares[i]);
				}
			}

			public void drawBoard() {		//the method that draws out the board and the squares, row by row
				for (int i = 0; i < 4; i++) {	//first row
					gamesquares[i] = new JLabel(tempsquares[i] + "");	//make the labels using the randomized string array

					gamesquares[i].setHorizontalAlignment(SwingConstants.CENTER);	//make the label look nice
					gamesquares[i].setVerticalAlignment(SwingConstants.CENTER);
					gamesquares[i].setBounds((i+1)*100 + 112*(i), 60, 140, 140);	//the bounds of the labels in the first row
					
					gamesquares[i].setBackground(fgcol);
					gamesquares[i].setForeground(textcol);

					squaretxt1 = gamesquares[i].getText();
					//according to how long the ion name/formula is, the font is adjusted so the longer names won't run off the label
					if (squaretxt1.length() <= 5)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 40));
					else if (squaretxt1.length() == 6)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 35));
					else if (squaretxt1.length() == 7)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 30));
					else if (squaretxt1.length() == 8)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 22));
					else if (squaretxt1.length() >= 9 && squaretxt1.length() <= 12)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 17));
					else if (squaretxt1.length() > 12) {
						gamesquares[i].setText("<html>" + squaretxt1 + "</html>");
						gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 11));
					}
					
					gamesquares[i].setOpaque(true);
					gamesquares[i].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.gray),
					BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray)));	//fancy border for the labels

					add(gamesquares[i]);
				}

				for (int i = 4; i < 8; i++) {//second row, same code as first one
					int pos = i - 4;
					gamesquares[i] = new JLabel(tempsquares[i] + "");
					gamesquares[i].setHorizontalAlignment(SwingConstants.CENTER);
					gamesquares[i].setVerticalAlignment(SwingConstants.CENTER);
					gamesquares[i].setBounds((i-3)*100 + 112*(i-4), 240, 140, 140);	//the bounds of the labels in the second row
					
					gamesquares[i].setBackground(fgcol);
					gamesquares[i].setForeground(textcol);

					squaretxt2 = gamesquares[i].getText();
					
					if (squaretxt2.length() <= 5)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 40));
					else if (squaretxt2.length() == 6)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 35));
					else if (squaretxt2.length() == 7)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 30));
					else if (squaretxt2.length() == 8)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 22));
					else if (squaretxt2.length() >= 9 && squaretxt2.length() <= 12)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 17));
					else if (squaretxt2.length() > 12) {
						gamesquares[i].setText("<html>" + squaretxt2 + "</html>");
						gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 11));
					}

					gamesquares[i].setOpaque(true);
					gamesquares[i].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.gray),
					BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray)));

					add(gamesquares[i]);
				}

				for (int i = 8; i < 12; i++) {	//third row, same logic as first one
					gamesquares[i] = new JLabel(tempsquares[i] + "");

					gamesquares[i].setHorizontalAlignment(SwingConstants.CENTER);
					gamesquares[i].setVerticalAlignment(SwingConstants.CENTER);
					gamesquares[i].setBounds((i-7)*100 + 112*(i-8), 420, 140, 140);		//the bounds of the labels in the third row
					
					gamesquares[i].setBackground(fgcol);
					gamesquares[i].setForeground(textcol);

					squaretxt3 = gamesquares[i].getText();
					
					if (squaretxt3.length() <= 5)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 40));
					else if (squaretxt3.length() == 6)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 35));
					else if (squaretxt3.length() == 7)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 30));
					else if (squaretxt3.length() == 8)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 22));
					else if (squaretxt3.length() >= 9 && squaretxt3.length() <= 12)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 17));
					else if (squaretxt3.length() > 12) {
						gamesquares[i].setText("<html>" + squaretxt3 + "</html>");
						gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 11));
					}

					gamesquares[i].setOpaque(true);
					gamesquares[i].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.gray),
					BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray)));

					add(gamesquares[i]);
				}	
			}

			public void resetFont() {

				for (int i = 0; i < 4; i++) {
					squaretxt1 = gamesquares[i].getText();

					if (squaretxt1.length() <= 5)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 40));
					else if (squaretxt1.length() == 6)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 35));
					else if (squaretxt1.length() == 7)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 30));
					else if (squaretxt1.length() == 8)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 22));
					else if (squaretxt1.length() >= 9 && squaretxt1.length() <= 12)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 17));
					else if (squaretxt1.length() > 12) {
						gamesquares[i].setText("<html>" + squaretxt1 + "</html>");
						gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 11));
					}
				}

				for (int i = 4; i < 8; i++) {
					squaretxt2 = gamesquares[i].getText();

					if (squaretxt2.length() <= 5)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 40));
					else if (squaretxt2.length() == 6)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 35));
					else if (squaretxt2.length() == 7)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 30));
					else if (squaretxt2.length() == 8)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 22));
					else if (squaretxt2.length() >= 9 && squaretxt2.length() <= 12)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 17));
					else if (squaretxt2.length() > 12) {
						gamesquares[i].setText("<html>" + squaretxt2 + "</html>");
						gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 11));
					}
				}

				for (int i = 8; i < 12; i++) {
					squaretxt3 = gamesquares[i].getText();

					if (squaretxt3.length() <= 5)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 40));
					else if (squaretxt3.length() == 6)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 35));
					else if (squaretxt3.length() == 7)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 30));
					else if (squaretxt3.length() == 8)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 22));
					else if (squaretxt3.length() >= 9 && squaretxt3.length() <= 12)	gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 17));
					else if (squaretxt3.length() > 12) {
						gamesquares[i].setText("<html>" + squaretxt3 + "</html>");
						gamesquares[i].setFont(new Font("Calibri", Font.PLAIN, 11));
					}
				}
			}

			public void setBoardColors(Color one, Color two, Color three) {
				bgcol = one;
				fgcol = two;
				textcol = three;

				for (int i = 0; i < 4; i++) {
					gamesquares[i].setBackground(fgcol);
					gamesquares[i].setForeground(textcol);
				}
				for (int i = 4; i < 8; i++) {					
					gamesquares[i].setBackground(fgcol);
					gamesquares[i].setForeground(textcol);
				}
				for (int i = 8; i < 12; i++) {
					gamesquares[i].setBackground(fgcol);
					gamesquares[i].setForeground(textcol);
				}
			}
			
			public void paintComponent(Graphics g) {	//paintComponent
				super.paintComponent(g);				

				setBackground(bgcol);
				g.setColor(Color.white);

				blanksq = 0;

				if (clickcount == 0) {	
					//if clickcount is 0 and the user clicks on one of the squares, the below code will set the border of the label clicked to blue
					//to indicate that it was clicked
					for(int i = 1; i < 5; i++) {
						if (xloc >= i*100 + 112*(i-1) && xloc <= i*100 + 112*(i-1) + 140 && yloc >= 60 && yloc <= 200) {
							clickcount += 1;
							gamesquares[i-1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							holdlabel = i - 1;
							firstx = i*100 + 112*(i-1);
							firsty = 60;
						}
						
						if (xloc >= i*100 + 112*(i-1) && xloc <= i*100 + 112*(i-1) + 140 && yloc >= 240 && yloc <= 380) {
							clickcount += 1;
							gamesquares[i+3].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							holdlabel = i + 3;
							firstx = i*100 + 112*(i-1);
							firsty = 240;
						}
						
						if (xloc >= i*100 + 112*(i-1) && xloc <= i*100 + 112*(i-1) + 140 && yloc >= 420 && yloc <= 560) {
							clickcount += 1;
							gamesquares[i+7].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							holdlabel = i + 7;
							firstx = i*100 + 112*(i-1);
							firsty = 420;
						}
					}
				}

				else if (clickcount == 1) {
					//if clickcount is 1 and then the user clicks on another one, the second one will also be blue, depending 
					//on whether it was a correct match or not
					for(int i = 1; i < 5; i++) {
						if (xloc >= i*100 + 112*(i-1) && xloc <= i*100 + 112*(i-1) + 140 && yloc >= 60 && yloc <= 200) {
							gamesquares[holdlabel].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							gamesquares[i-1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							if (temppos[holdlabel] == temppos[i-1]) {							//what happens if there is a match, clickcount is made to be 2
								blanksq = i-1;
								clickcount = 2;
							}
							else {
								gamesquares[holdlabel].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.red), 
								BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, Color.red)));
								gamesquares[i-1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.red), 
								BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, Color.red)));
								score -= 1;
								clickcount = 0;
							}
							break;
						} 
						
						else if (xloc >= i*100 + 112*(i-1) && xloc <= i*100 + 112*(i-1) + 140 && yloc >= 240 && yloc <= 380) {
							gamesquares[holdlabel].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							gamesquares[i+3].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							if (temppos[holdlabel] == temppos[i+3]) {
								blanksq = i+3;
								clickcount = 2;
							}
							else {
								gamesquares[holdlabel].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.red), 
								BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, Color.red)));
								gamesquares[i+3].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.red), 
								BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, Color.red)));
								score -= 1;
								clickcount = 0;
							}
							break;
						} 
						
						else if (xloc >= i*100 + 112*(i-1) && xloc <= i*100 + 112*(i-1) + 140 && yloc >= 420 && yloc <= 560) {
							gamesquares[holdlabel].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							gamesquares[i+7].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.blue), 
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.blue)));
							if (temppos[holdlabel] == temppos[i+7]) {
								blanksq = i+7;
								clickcount = 2;
							}
							else {
								gamesquares[holdlabel].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.red), 
								BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, Color.red)));
								gamesquares[i+7].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.red), 
								BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, Color.red)));
								score -= 1;
								clickcount = 0;
							}
							break;
						} 
						else clickcount = 0;
					}

					if (clickcount == 2) {
						gamesquares[holdlabel].setText("");
						gamesquares[blanksq].setText("");
						gamesquares[holdlabel].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.gray),
						BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray)));		
						gamesquares[blanksq].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, Color.gray),
						BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray)));
						score+=1;				
						bottom.incScore();

						for (int i = 0; i < 12; i++) {	//tests each square's text, if all are blank and blanktrue is true
							if (gamesquares[i].getText() == "") blanktrue = true;
							else  {
								blanktrue = false;
								break; 
							}
						}

						if (blanktrue == true) {	//if this is true, the board is pretty much repainted again, with different ions
							randomize();
							fillBlanks();
							resetFont();
						}
					}
				}
			}

			public int getScore() {
				return score;
			}

			public void mousePressed(MouseEvent e) {
				xloc = e.getX();
				yloc = e.getY();
				if (clickcount == 2) {	//if the user got a match, clickcount is reset, and we check to see if the boad is blank
					clickcount = 0;
				}
				repaint();		
			}
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		}


		////////////////////////////////////////////////////////////
		// the bottom panel, which shows the score and the play/pause button
		//		
		class GameBottomPanel extends JPanel {	
			JPanel botlfiller, botrfiller;	//filler panels
			JButton actionbutton;
			ActionButtonListener actlistener;
			JLabel scorelab;
			
			public GameBottomPanel() {
				setBackground(Color.cyan);
				setLayout(new GridLayout(1, 3));
				
				botlfiller = new JPanel();	botlfiller.setBackground(Color.cyan);
				actionbutton = new JButton("||");
				actlistener = new ActionButtonListener();
				actionbutton.addActionListener(actlistener);
				botrfiller = new JPanel();	botrfiller.setBackground(Color.cyan);	

				scorelab = new JLabel("Your Score: " + score);
				botlfiller.add(scorelab);
				
				add(botlfiller);
				add(actionbutton);
				add(botrfiller);			
			}			

			public void incScore() {	//method to increment the score everytime a pair is matched
				scorelab.setText("Your Score: " + score);
				scorelab.setBackground(Color.white);
				scorelab.setForeground(Color.black);
				scorelab.setVerticalAlignment(SwingConstants.CENTER);
				scorelab.setHorizontalAlignment(SwingConstants.CENTER);
				scorelab.setOpaque(true);
			}

			public int getScore() {
				return score;
			}

			class ActionButtonListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					String command = e.getActionCommand();
					if (command.equals("||")) { 
						actionbutton.setText(String.valueOf('\u25B6')); 
					} 
					else if (command.equals(String.valueOf('\u25B6'))) { 
						actionbutton.setText("||"); 
					}
				}			
			}
		}	
		
		class OverPanel extends JPanel {
			JButton menreturn;
			JButton replay;
			JLabel overlabel;
			JLabel finScore;

			public OverPanel() {
				setLayout(null);
				setBackground(Color.green);

				overlabel = new JLabel("GAME OVER");
				overlabel.setForeground(Color.black);
				overlabel.setFont(new Font("Calibri", Font.PLAIN, 100));
				overlabel.setHorizontalAlignment(SwingConstants.CENTER);
				overlabel.setVerticalAlignment(SwingConstants.CENTER);
				overlabel.setBounds(200, 0, 600, 200);
				add(overlabel);

				finScore = new JLabel("Final Score: " + bottom.getScore());
				finScore.setForeground(Color.black);
				finScore.setFont(new Font("Calibri", Font.PLAIN, 100));
				finScore.setHorizontalAlignment(SwingConstants.CENTER);
				finScore.setVerticalAlignment(SwingConstants.CENTER);
				finScore.setBounds(100, 200, 800, 300);
				add(finScore);

				menreturn = new JButton("Return to Menu");
				menreturn.setBackground(Color.white);
				menreturn.setForeground(Color.red);
				menreturn.setFont(new Font("Calibri", Font.PLAIN, 40));
				menreturn.addActionListener(new MenuListen());
				menreturn.setBounds(350, 500, 300, 130);
				add(menreturn);
			}	

			private class MenuListen implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					gamepanel.changeHiScores(score);
					cards.show(displaypanel, "Start Panel");
				}
			}
		}	
	}	
}