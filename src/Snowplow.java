/*
 * Name: Emily Lin
 * Description: This code asks the user to input a value for the number of rows and columns. It then randomly generates 1 and 2
 *              and stores it in an array. When the user presses the "start" button, the grid will commence to be plowed by finding 
 *              the first one in the first row and changing it and all the touching ones to 0, until it has all been plowed. 
 * Date: 01/04/16 
 */ 

import java.util.Random; 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*; 
import java.awt.event.*;
import javax.swing.Timer; 

public class Snowplow extends JFrame implements ActionListener{ 
	// Variables used to store size of 2D array 
	static int numRows;
	static int numCol;
	static int sequence = 0;

	// JPanels
	static JPanel gridPanel = new JPanel();
	static JPanel optionsPanel = new JPanel();
	static int [][] grid; // Array that holds the values of 0, 1 or 2 

	// JButtons
	static JButton close = new JButton("Close");
	static JButton tryAgain = new JButton("Try again");
	static JButton start = new JButton("Start"); 

	// Timer 
	static Timer timer; 

	public Snowplow(){
		// Basic setup
		setTitle("Snowplow");
		setSize(400,400); 

		numRows = numRows(); 
		numCol = numCol(); 

		grid = new int [numRows][numCol]; 
		GridLayout gridLayout = new GridLayout(numRows, numCol); // gridLayout for gridPanel 

		// layouts
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // set layout of overall wimdpw
		gridPanel.setLayout(gridLayout); // set layout for the panel with the grid
		FlowLayout optionsLayout = new FlowLayout(); // layout for the options panel that has "close", "try again" and "start"
		optionsPanel.setLayout(optionsLayout); 

		gridPanel.setBorder(new EmptyBorder(0, 10, 0, 0)); // adds padding to the grid 

		// add action listener to JButton
		close.addActionListener(this);
		tryAgain.addActionListener(this);
		start.addActionListener(this);

		// deactivates close and tryAgain buttons
		close.setEnabled(false);
		tryAgain.setEnabled(false);

		// add buttons to panel
		optionsPanel.add(close);
		optionsPanel.add(tryAgain);
		optionsPanel.add(start); 

		fillArray(); // fills the array with 1s or 2s 
		displayArray(); // displays values in array on GUI

		// Add panels to the frame
		add(gridPanel); 

		add(optionsPanel); 

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close when the exit button is pressed

		setVisible(true); // sets the visibility to true
		setResizable(false); // prevents user from changing its size 
	}

	public static int numRows(){ // asks the user how many rows they wish to have 
		boolean valid; // keeps track of if the input is valid
		String row; // stores the value of the row input by the user 

		do {
			JFrame j = new JFrame(); 
			j.setAlwaysOnTop(true);
			j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			j.setVisible(true);
			j.setVisible(false);
			row = JOptionPane.showInputDialog(j, "Please input the number of rows you wish to have. Note that the value should be between 1 and 20."); // outputs message to user 

			valid = validInput(row); // checks if the input is valid 
		} while (valid == false); 
		return Integer.parseInt(row); //converts the string into an int 
	}

	public static int numCol(){ // asks user how many columns they wish to have 
		boolean valid; // keeps track of if the input is valid  
		String column; // stores the value of the column input by the user 

		do {
			JFrame j = new JFrame(); 
			j.setAlwaysOnTop(true);
			j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			j.setVisible(true);
			j.setVisible(false);

			column = JOptionPane.showInputDialog(j, "Please input the number of columns you wish to have. Note that the value should be between 1 and 20."); // outputs message to user 
			valid = validInput(column); // checks if the input is valid 
		} while (valid == false);
		return Integer.parseInt(column); // converts the string into an int and returns it 
	}

	public static boolean valid (int r, int c){ // checks if the row and column values are valid 
		if ((r > -1) && (r < numRows) && (c > -1) && (c < numCol)){
			return true;
		}
		return false;
	}

	public static boolean validInput(String row){
		boolean valid = false;
		int num = 0;

		if (row == null){ // if the user presses "cancel" or "exit" on the dialogue
			System.exit(0); // force quits the program 
		} else {
			try { // checks it is an integer
				num = Integer.parseInt(row); 
				valid = true;
				if (num > 20 || num < 1){ // makes sure the user does not input an unrealistic number given the size of the window
					JOptionPane.showMessageDialog(optionsPanel, "This is not a valid value. Please try again!", "Error", JOptionPane.ERROR_MESSAGE);
					valid = false; 
				}
			} catch (NumberFormatException ex) { // if the input is not valid 
				JOptionPane.showMessageDialog(optionsPanel, "This is not an integer. Please try again!", "Error", JOptionPane.ERROR_MESSAGE);
				valid = false; 
			}
		}
		return valid; 
	}

	public static int firstOne(){ // finds the first 1 in the first row 
		int column = -1; // stores the index of the column of the first 
		for (int i = 0; i < numCol; i++){ // searches in the first row for the first value of 1
			if (grid[0][i] == 1){
				column = i;
				i = numCol; // exits out of the loop
			}
		}
		return column;
	}

	public static void fillArray(){ // fills the array with numbers 
		Random rand = new Random(); // allows for random generation of values 
		for (int r = 0; r < numRows; r++){
			for (int c = 0; c < numCol; c++){
				grid[r][c] = rand.nextInt(2) + 1;  // outputs a random value and stores it in the array
			}
		} 
	}

	public static void displayArray(){ // displays the values in the array as a JLabel
		for (int r = 0; r < numRows; r++){
			for (int c = 0; c < numCol; c++){
				JLabel label = new JLabel(""+ grid[r][c]);
				if (grid[r][c] == 2){
					label.setForeground(Color.BLUE);
				} 
				gridPanel.add(label); 
				gridPanel.revalidate();
				gridPanel.repaint();
			} 
		}
	}

	public static void clear(int r, int c){ // clears the snow
		if (valid(r,c) == true && grid[r][c] == 1){
			grid[r][c] = 0; // changes values inside of the array and the value portrayed on the JPanel
			clear (r -1, c);  // checks north direction
			clear (r - 1, c + 1);  // checks northeast direction
			clear (r, c + 1); // checks east direction
			clear (r + 1, c  +1); // checks southeast direction
			clear (r  + 1, c); // checks south direction
			clear (r + 1, c - 1); // checks southwest direction
			clear (r, c - 1); // checks west direction
			clear (r - 1, c - 1); // checks northwest direction 
		}
	}

	public static void clearedDisplay(final int r, final int c){ // uses a timer to delay the changing of 1 to 0 
		timer = new Timer(sequence++*50, new ActionListener(){
			public void actionPerformed (ActionEvent e){
				((JLabel) gridPanel.getComponent(r * numCol + c)).setText("0");  // sets the text on label to 0
				((JLabel) gridPanel.getComponent(r * numCol + c)).setForeground(Color.GREEN);  // sets the color of the text to green 
			} 
		});
		timer.setRepeats(false); // ensures timer does not repeat 
		timer.start(); // starts the timer 
	} 

	public static void done(){ // executes this once all of the values have been changed 
		timer = new Timer(sequence*50, new ActionListener(){
			public void actionPerformed (ActionEvent e){
				// changes whether a button is active or inactive 
				start.setEnabled(false);
				tryAgain.setEnabled(true);
				close.setEnabled(true);
				// informs the user that the snow has been plowed 
				JOptionPane.showMessageDialog(optionsPanel, "The snow has been plowed!");
			} 
		});
		timer.setRepeats(false); // ensures timer does not repeat 
		timer.start(); // starts the timer 
	} 

	public void actionPerformed(ActionEvent event){
		String command = event.getActionCommand(); 

		if (command.equals("Try again")){
			gridPanel.removeAll(); // removes the array from the panel 
			fillArray(); // fills array with 1 and 2 
			displayArray();  // displays the array of values 

			// changes whether a button is active or inactive 
			start.setEnabled(true);
			tryAgain.setEnabled(false);
			close.setEnabled(false);

		} else if (command.equals("Close")){ // closes the program
			setVisible(false); // makes the window invisible
			dispose();  // closes the program 

		} else if (command.equals("Start")){ // starts the program 
			int column = firstOne(); // finds the first 1 in the first row 
			start.setEnabled(false);
			close.setEnabled(false);
			tryAgain.setEnabled(false); 

			if (firstOne()!= -1){  // if a value was found, clearing will begin
				clear (0, column); // begins to plow
				for (int r = 0; r < numRows; r++){ // checks for values that are equal to 0 in array and changes values of JLabels such that they have 0 on it 
					for (int c = 0; c < numCol; c++){
						if (grid[r][c] == 0){
							clearedDisplay(r, c);
						} 
					}
				}
				done();
				sequence = 0; 

			} else { // if a value was not found, a message will be output to the user
				start.setEnabled(false);
				close.setEnabled(true);
				tryAgain.setEnabled(true); 
				JOptionPane.showMessageDialog(optionsPanel, "There was no snow to be plowed!", "Oh no!", JOptionPane.PLAIN_MESSAGE); 
			}
		}
	}

	public static void main(String[] args){
		Snowplow frame = new Snowplow();  
	}
}

