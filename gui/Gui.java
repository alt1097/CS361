package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import Client.Client;
import Server.Server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.*;

import main.ChronoTimer;

public class Gui {
	
	private JFrame frame; // main window frame
	private JTextArea printerText; // text area for printer output
	private JTextArea displayText; // text area for display output
	private JScrollPane scrollPane; // addition for printer text box to make it scroll
	private String[][] simpleMenu = new String[7][6]; // menu items array. Do not treat it as a two dimensional array even if it looks like one
	private int row; // current row
	private int col; // current column
	private ChronoTimer chrono; // reference to ChronoTimer instance
	
	private boolean printerSwitch;	// indicate if printer text box can be updated
	private boolean numPadActive; // indicate if keypad can be used
	private boolean guiActive; // indicate if any gui elements can be used
	private boolean allowOutsideInput; // indicate if outside instance can use internal methods
	private boolean functionsMenuActive; // indicate if functions menu can be used
	private boolean usbActive; // indicate if usb connected
	
	private ArrayList<JComboBox> comboBoxes; // holds sensor type comboboxe instances
	private ArrayList<JRadioButton> radioButtons; // holds enable/disable radiobutton instances
	private ArrayList<JToggleButton> toggleButtons;
	private Map<String, String> map;
	

	
	/**
	 * Constructor for Gui class
	 * 
	 */
	public Gui(ChronoTimer chrono) {
		this.chrono = chrono;
		map = new HashMap<String, String>();
		map.put("Create a new run", "newRun");
		map.put("End current run", "endRun");
		map.put("Add new racer", "num");
		map.put("Remove racer", "clr");
		map.put("Individual", "IND");
		map.put("Parallel", "PARIND");
		map.put("Group", "GRP");
		map.put("Group parallel", "PARGRP");
		map.put("Export to USB", "export");
		map.put("Show all stats", null);
		map.put("Show only one", "print");
		map.put("Set time", "time");
		
		comboBoxes = new ArrayList<JComboBox>(); 
		radioButtons = new ArrayList<JRadioButton>(); 
		toggleButtons = new ArrayList<JToggleButton>(); 
		// row column	
		// main menu
		simpleMenu[0][0] = "Choose race type";
		simpleMenu[0][1] = "Add/Remove racer";
		simpleMenu[0][2] = "New run/End run";
		simpleMenu[0][3] = "Export";
		simpleMenu[0][4] = "Print";
		simpleMenu[0][5] = "Set time";
		
		// new race type menu
		simpleMenu[1][0] = "Individual";
		simpleMenu[1][1] = "Parallel";
		simpleMenu[1][2] = "Group";
		simpleMenu[1][3] = "Group parallel";
		
		// add racer function
		simpleMenu[2][0] = "Add new racer";
		simpleMenu[2][1] = "Remove racer";
//		simpleMenu[2][2] = null;
//		simpleMenu[2][3] = null;
		
		// generic menu page
		simpleMenu[3][0] = "Create a new run";
		simpleMenu[3][1] = "End current run";
//		simpleMenu[3][2] = null;
//		simpleMenu[3][3] = null;
		
		// export menu
		simpleMenu[4][0] = "Export to USB";
//		simpleMenu[4][1] = null;
//		simpleMenu[4][2] = null;
//		simpleMenu[4][3] = null;
		
		// print menu page
		simpleMenu[5][0] = "Show all stats";
		simpleMenu[5][1] = "Show only one";
//		simpleMenu[5][2] = null;
//		simpleMenu[5][3] = null;
		
	//	// generic menu page
//		simpleMenu[6][0] = "Item 4 0";
//		simpleMenu[4][1] = "Item 4 1";
//		simpleMenu[4][2] = "Item 4 2";
//		simpleMenu[4][3] = "Item 4 2";	
		
////		// generic menu page
//		simpleMenu[4][0] = "Item 4 0";
//		simpleMenu[4][1] = "Item 4 1";
//		simpleMenu[4][2] = "Item 4 2";
//		simpleMenu[4][3] = "Item 4 2";		
		
		initialize();
	}
		
	/**
	 * Method to return this JFrame object
	 * 
	 */
	public JFrame getFrame(){
		return frame;
	}
	
	/**
	 * Appends a string of text to printer text field as a new line if printer
	 * activated. Do nothing if printer deactivated String length for current
	 * setup can be around 15 chars
	 * 
	 * @param stringToPrinter
	 *            - string that will be attached to the bottom of printer text
	 *            frame. Omit \n in chars in stringToPrinter
	 */
	public void appendToPrinter(String stringToPrinter) {
//		if (allowOutsideInput) {
			appendToPrinterInternal(stringToPrinter);
//		}
	}

	/**
	 * Append given text to text box as a new line
	 * 
	 * @param stringToAppend
	 *            - some text to append
	 */
	public void appendToDisplay(String stringToAppend) {
		if (allowOutsideInput) {
			appendToDisplayInternal(stringToAppend);
		}
	}

	/**
	 * Wipe current text from text box and add some new text
	 * 
	 * @param stringToAppend
	 *            - some text to set
	 */
	public void setDisplay(String stringToAppend) {
		if (allowOutsideInput) {
			setDisplayInternal(stringToAppend);
		}
	}
	

	private void togglePrinter() {
		printerSwitch = printerSwitch ? false : true;
	}	
	
	private void drawCol() {
		if (guiActive) {
			displayText.setText("");
			Highlighter highlighter = displayText.getHighlighter();
			HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
			for (int i = 0; i < simpleMenu[0].length; i++) {
				if ((simpleMenu[row][i] != null)) {
					if (i == col) {
//						displayText.append("* " + simpleMenu[row][i] + "\n"); // asterix version
						displayText.append(simpleMenu[row][i] + "\n"); // highlighted version
						int beginIndex = displayText.getText().indexOf(simpleMenu[row][i]);
						int endIndex = beginIndex + simpleMenu[row][i].length();
						try {
							highlighter.addHighlight(beginIndex, endIndex, painter);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					} else {
						appendToDisplayInternal(simpleMenu[row][i]);
					}
				}
			}
		}
	}
	
	private void invoke(String what){
		try {
			ChronoTimer.class.getMethod(what).invoke(chrono);
		} catch (Exception cause) {
		    StackTraceElement elements[] = cause.getStackTrace();
		    for (int i = 0, n = elements.length; i < n; i++) {       
		        System.err.println(elements[i].getFileName()
		            + ":" + elements[i].getLineNumber() 
		            + ">> "
		            + elements[i].getMethodName() + "()");
		    }
		}
	}
	
	private void invoke(String what, int arg){
		try {
//			Method m = ChronoTimer.class.getMethod(what, int.class);
			ChronoTimer.class.getMethod(what, int.class).invoke(chrono, arg);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}
	
	private void invoke(String what, String arg){
		try {
			ChronoTimer.class.getMethod(what, String.class).invoke(chrono, (Object)arg);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}
	
	
	private void clear(){
		displayText.setText("");
		//printerText.setText(""); // if printer text need to be wiped
		col = 0;
		row = 0;		
	}
	
	/*
	 * Returns a "length" of an menu array
	 * This is not an actual array length
	 * This number shows how many items in array is not null i.e. shows non-empty textual menu options
	 */
	private int getLength(String[] someArray){
		int counter = 0;
		for(int i = 0; i < someArray.length; i++){
			if(someArray[i] != null){
				counter++;
			}
		}		
		return counter;
	}
	
	private void appendToDisplayInternal(String stringToAppend){
		displayText.append(stringToAppend + "\n");
	}
	
	private void setDisplayInternal(String stringToAppend){
		displayText.setText(stringToAppend);
	}
	
	private void appendToPrinterInternal(String stringToPrinter){		
		if(printerSwitch){
			printerText.append(stringToPrinter + "\n");
		}		
	}	
	
	/*
	 * Method to initialize all window components and functions
	 */
	private void initialize() {
		frame = new JFrame("ChronoTimer 1009");
		frame.setBounds(500, 100, 850, 700);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel indicator = new JLabel("•");
		indicator.setForeground(Color.RED);
		indicator.setFont(new Font("Tahoma", Font.PLAIN, 50));
		indicator.setBounds(150, 7, 25, 25);
		frame.getContentPane().add(indicator);
		
		// Left section
		JButton power = new JButton("Power");
		power.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Power");
				chrono.power();
				indicator.setForeground(Color.GREEN);
				guiActive = guiActive ? false : true;
				
				if(!guiActive){
					allowOutsideInput = false;					
					functionsMenuActive = false; // this line allows to fix bug with double click on function button
					indicator.setForeground(Color.RED);
					clear();
					printerText.setText(""); // get rid of "sensor disconnected" messages in printer box
				}else{
					for (JRadioButton radio : radioButtons) {
						// logic is follow: channel selector radio button can be activated even when system is off
						// during the power on routine all necessary channels will be toggled if radio button is in active state 
						if(radio.isSelected()){
							radio.getActionListeners()[0].actionPerformed(null);
						}
					}
					
					// same logic as above
					for (JComboBox comboBox : comboBoxes) {
						comboBox.getActionListeners()[0].actionPerformed(new ActionEvent(comboBox, 0, (String) comboBox.getSelectedItem()));
					}
					
					// seems like unnecessary action. Printer text box will show some console output although
//					for(JToggleButton toggle : toggleButtons){
//						if(toggle.isSelected()){
//							toggle.getActionListeners()[0].actionPerformed(new ActionEvent(toggle, 0, toggle.getActionCommand()));
//						}					
//					}					
				}
			}
		});
		power.setBounds(61, 11, 80, 23);
		frame.getContentPane().add(power);
		
		JButton functions = new JButton("Funct");
		functions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// functions menu active only when whole gui accessible
				// this is why there is AND comparison in parentheses
				if (guiActive) {
					functionsMenuActive = (functionsMenuActive) ? false : true;
					if (functionsMenuActive) {
						allowOutsideInput = false;
						drawCol();
					} else {
						allowOutsideInput = true;
						clear();
					}
				}
			}
		});
		functions.setBounds(61, 170, 80, 23);
		frame.getContentPane().add(functions);
		
		JButton swap = new JButton("Swap");
		swap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Swap");
				chrono.swap();
			}
		});
		swap.setBounds(61, 400, 80, 23);
		frame.getContentPane().add(swap);
		
		BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Up col: " + col +" row: " + row);	
				if(functionsMenuActive){ // TODO
					if((--col < 0)){
//						col = simpleMenu[row].length - 1;
						col = getLength(simpleMenu[row]) - 1; // such approach allows to avoid scrolling menu over "invisible" menu items
					}
				drawCol();
				}
			}
		});
		up.setBounds(90, 260, 25, 25);
		frame.getContentPane().add(up);
		
		BasicArrowButton left = new BasicArrowButton(BasicArrowButton.WEST);
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Left");
				if (functionsMenuActive) {
					if ((row - 1) >= 0) {
						col = row - 1;
					}
					// this can be changed for bigger menu
					row = 0;
					drawCol();
				}
			}
		});
		left.setBounds(65, 285, 25, 25);
		frame.getContentPane().add(left);
		
		BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST);
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Right");
				System.out.println("ROW " + row);
				if (functionsMenuActive) {					
					if (row == 1) { // race type menu page						
						chrono.event(map.get(simpleMenu[row][col]));
						return;
					} else if (row == 2) { // add racer menu page
						numPadActive = true; // activate numpad
						// it is necessary to have whitespace at the end of menu
						// description
						displayText.setText("Enter racer's bib number. \n");
						displayText.append("(keypad and #): ");
						return;
					} else if (row == 3) { // end run menu page
						//chrono.endRun();
						invoke(map.get(simpleMenu[row][col]));
						return;
					} else if (row == 4) { // export menu
						if (usbActive) {
							numPadActive = true;
							// it is necessary to have whitespace at the end of
							// menu description
							displayText.setText("Provide a run number. \n");
							displayText.append("(keypad and #): ");
						} else {
							displayText.setText("USB not found");
						}
						return;
					} else if (row == 5) { // print menu
						if(map.get(simpleMenu[row][col]) == null){
							chrono.print();
						}else{
							numPadActive = true;
							// it is necessary to have whitespace at the end of
							// menu description
							displayText.setText("Provide a run number. \n");
							displayText.append("(keypad and #): ");
						}
						return;	
					} else if (row == 0 && col == 5) { // time set menu
							numPadActive = true;
							displayText.setText("Set system time. \n");
							displayText.setText("Format HH*MM*SS \n");
							displayText.append("(keypad and */#): ");
						return;					
					} else {
						row = col + 1;
						col = 0;
					}
					drawCol();
				}
			}
		});
		right.setBounds(115, 285, 25, 25);
		frame.getContentPane().add(right);
		
		BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Down");
				if (functionsMenuActive) {
					if ((++col > (getLength(simpleMenu[row]) - 1))) {
						col = 0;
					}
					drawCol();
				}
			}
		});
		down.setBounds(90, 310, 25, 25);
		frame.getContentPane().add(down);		
		
		// Middle Section
		
		// This is 4 labels Start and Enable/Disable
		JLabel startLabel1 = new JLabel("Start");
		startLabel1.setBounds(285, 15, 30, 14);
		frame.getContentPane().add(startLabel1);
		
		JLabel enableDisableLabel1 = new JLabel("Enable/Disable");
		enableDisableLabel1.setBounds(230, 46, 85, 14);
		frame.getContentPane().add(enableDisableLabel1);
		
		JLabel startLabel2 = new JLabel("Start");
		startLabel2.setBounds(285, 89, 30, 14);
		frame.getContentPane().add(startLabel2);
		
		JLabel enableDisableLabel2 = new JLabel("Enable/Disable");
		enableDisableLabel2.setBounds(230, 120, 85, 14);
		frame.getContentPane().add(enableDisableLabel2);
		
		// Middle section
		// Buttons to fire channel and radio buttons to disable channel
		// Pair 1
		JButton fireChan1 = new JButton("1");
		fireChan1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan1.setBounds(325, 11, 40, 23);
		fireChan1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 1 was fired by button fireChan1");
				chrono.trig(1);
			}
		});
		frame.getContentPane().add(fireChan1);
		
		JRadioButton togChan1 = new JRadioButton("");
		togChan1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 1");
				chrono.tog(1);
			}
		});
		radioButtons.add(togChan1);
		togChan1.setBounds(335, 41, 20, 20);
		frame.getContentPane().add(togChan1);
		

		// Pair 2
		JButton fireChan3 = new JButton("3");
		fireChan3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 3 was fired by button fireChan3");
				chrono.trig(3);
			}
		});
		fireChan3.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan3.setBounds(375, 11, 40, 23);
		frame.getContentPane().add(fireChan3);
		
		JRadioButton togChan3 = new JRadioButton("");
		togChan3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 3");
				chrono.tog(3);
			}
		});
		radioButtons.add(togChan3);
		togChan3.setBounds(385, 41, 20, 20);
		frame.getContentPane().add(togChan3);
		
		// Pair 3
		JButton fireChan5 = new JButton("5");
		fireChan5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 5 was fired by button fireChan5");
				chrono.trig(5);
			}
		});
		fireChan5.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan5.setBounds(425, 11, 40, 23);
		frame.getContentPane().add(fireChan5);
		
		JRadioButton togChan5 = new JRadioButton("");
		togChan5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 5");
				chrono.tog(5);
			}
		});
		radioButtons.add(togChan5);
		togChan5.setBounds(435, 41, 20, 20);
		frame.getContentPane().add(togChan5);
		
		// Pair 4
		JButton fireChan7 = new JButton("7");
		fireChan7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 7 was fired by button fireChan7");
				chrono.trig(7);
			}
		});
		fireChan7.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan7.setBounds(475, 11, 40, 23);
		frame.getContentPane().add(fireChan7);
		
		JRadioButton togChan7 = new JRadioButton("");
		togChan7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 7");
				chrono.tog(7);
			}
		});
		radioButtons.add(togChan7);
		togChan7.setBounds(485, 41, 20, 20);
		frame.getContentPane().add(togChan7);
		

		

		// Second row of pairs
		// Pair 5
		JButton fireChan2 = new JButton("2");
		fireChan2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 2 was fired by button fireChan2");
				chrono.trig(2);
			}
		});
		fireChan2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan2.setBounds(325, 85, 40, 23);
		frame.getContentPane().add(fireChan2);
		
		JRadioButton togChan2 = new JRadioButton("");
		togChan2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 2");
				chrono.tog(2);
			}
		});
		radioButtons.add(togChan2);
		togChan2.setBounds(335, 115, 20, 20);
		frame.getContentPane().add(togChan2);
		
		
		// Pair 6
		JButton fireChan4 = new JButton("4");
		fireChan4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 4 was fired by button fireChan4");
				chrono.trig(4);
			}
		});
		fireChan4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan4.setBounds(375, 85, 40, 23);
		frame.getContentPane().add(fireChan4);
		
		JRadioButton togChan4 = new JRadioButton("");
		togChan4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 4");
				chrono.tog(4);
			}
		});
		radioButtons.add(togChan4);
		togChan4.setBounds(385, 115, 20, 20);
		frame.getContentPane().add(togChan4);
		
		// Pair 7
		JButton fireChan6 = new JButton("6");
		fireChan6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 6 was fired by button fireChan6");
				chrono.trig(6);
			}
		});
		fireChan6.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan6.setBounds(425, 85, 40, 23);
		frame.getContentPane().add(fireChan6);
		
		JRadioButton togChan6 = new JRadioButton("");
		togChan6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 6");
				chrono.tog(6);
			}
		});
		radioButtons.add(togChan6);
		togChan6.setBounds(435, 115, 20, 20);
		frame.getContentPane().add(togChan6);
		
		// Pair 8
		JButton fireChan8 = new JButton("8");
		fireChan8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Chan 8 was fired by button fireChan8");
				chrono.trig(8);
			}
		});
		fireChan8.setFont(new Font("Tahoma", Font.PLAIN, 10));
		fireChan8.setBounds(475, 85, 40, 23);
		frame.getContentPane().add(fireChan8);
		
		JRadioButton togChan8 = new JRadioButton("");
		togChan8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Radio 8");
				chrono.tog(8);
			}
		});
		radioButtons.add(togChan8);
		togChan8.setBounds(485, 115, 20, 20);
		frame.getContentPane().add(togChan8);
		
		// Display text area
		Font displayTextFont = new Font("Verdana", Font.BOLD, 16);
		displayText = new JTextArea();
		displayText.setBounds(290, 170, 250, 250);
		displayText.setEditable(false);
		displayText.setFont(displayTextFont);
		frame.getContentPane().add(displayText);		
		
		// Right section
		JToggleButton printerPower = new JToggleButton("Printer");
		printerPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				printerText.append("Printer Power button\n");
				//if(guiActive){
					togglePrinter();
				//}					
			}
		});
		toggleButtons.add(printerPower);
		printerPower.setBounds(665, 11, 89, 23);
		frame.getContentPane().add(printerPower);	
		
		// Text area for printer output
		printerText = new JTextArea();
//		printerText.setBounds(630, 41, 150, 200);
		printerText.setEditable(false);
		scrollPane = new JScrollPane(printerText);
		scrollPane.setBounds(615, 40, 175, 200);
		frame.getContentPane().add(scrollPane);	

		// Numpad section
		JButton num_1 = new JButton("1");
		num_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){
//					System.out.println("Numpad button 1");
					displayText.append("1");	
				}
				
			}
		});
		num_1.setBounds(630, 250, 50, 50);
		frame.getContentPane().add(num_1);
		
		JButton num_2 = new JButton("2");
		num_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){
//					System.out.println("Numpad button 2");
					displayText.append("2");	
				}
			}
		});
		num_2.setBounds(680, 250, 50, 50);
		frame.getContentPane().add(num_2);
		
		JButton num_3 = new JButton("3");
		num_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){
//					System.out.println("Numpad button 3");
					displayText.append("3");	
				}
			}
		});
		num_3.setBounds(730, 250, 50, 50);
		frame.getContentPane().add(num_3);
		
		JButton num_4 = new JButton("4");
		num_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){
//					System.out.println("Numpad button 4");
					displayText.append("4");	
				}
				
			}
		});
		num_4.setBounds(630, 300, 50, 50);
		frame.getContentPane().add(num_4);
		
		JButton num_5 = new JButton("5");
		num_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){

//					System.out.println("Numpad button 5");
					displayText.append("5");	
				}
			}
		});
		num_5.setBounds(680, 300, 50, 50);
		frame.getContentPane().add(num_5);
		
		JButton num_6 = new JButton("6");
		num_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){

					System.out.println("Numpad button 6");
					displayText.append("6");	
				}
			}
		});
		num_6.setBounds(730, 300, 50, 50);
		frame.getContentPane().add(num_6);		
		
		JButton num_7 = new JButton("7");
		num_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){

					System.out.println("Numpad button 7");
					displayText.append("7");	
				}
			}
		});
		num_7.setBounds(630, 350, 50, 50);
		frame.getContentPane().add(num_7);
		
		JButton num_8 = new JButton("8");
		num_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){

					System.out.println("Numpad button 8");
					displayText.append("8");	
				}
			}
		});
		num_8.setBounds(680, 350, 50, 50);
		frame.getContentPane().add(num_8);
		
		JButton num_9 = new JButton("9");
		num_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){
					System.out.println("Numpad button 9");
					displayText.append("9");	
				}
			}
		});
		num_9.setBounds(730, 350, 50, 50);
		frame.getContentPane().add(num_9);		
		
		JButton num_star = new JButton("*");
		num_star.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){
//					System.out.println("Numpad button *");	
					displayText.append(":");
				}
				
			}
		});
		num_star.setBounds(630, 400, 50, 50);
		frame.getContentPane().add(num_star);
		
		JButton num_0 = new JButton("0");
		num_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(numPadActive){
//					System.out.println("Numpad button 0");
					displayText.append("0");					
				}
				
			}
		});
		num_0.setBounds(680, 400, 50, 50);
		frame.getContentPane().add(num_0);
		
		JButton button_pound = new JButton("#");
		button_pound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (numPadActive) { // TODO					

					if(row == 0 && col == 5){
						invoke(map.get(simpleMenu[row][col]), (displayText.getText().substring(displayText.getText().lastIndexOf(' ') + 1)));
					}else{
						invoke(map.get(simpleMenu[row][col]), Integer.parseInt(displayText.getText().substring(displayText.getText().lastIndexOf(' ') + 1)));
					}
//					invoke(map.get(simpleMenu[row][col]), Integer.parseInt(displayText.getText().substring(displayText.getText().lastIndexOf(' ') + 1)));
					
				}
				numPadActive = false;
				drawCol();
			}
		});
		button_pound.setBounds(730, 400, 50, 50);
		frame.getContentPane().add(button_pound);
		

		
		
		// Back view section
		String[] sensors = {"-", "EYE", "GATE", "PAD"};
		
		JLabel backViewLabel = new JLabel("Back View");
		backViewLabel.setBounds(375, 507, 66, 14);
		frame.getContentPane().add(backViewLabel);
		
		// this is a numbers above a channel type selector
		JLabel backChanLabel_1 = new JLabel("1");
		backChanLabel_1.setBounds(140, 540, 15, 14);
		frame.getContentPane().add(backChanLabel_1);
		
		JLabel backChanLabel_3 = new JLabel("3");
		backChanLabel_3.setBounds(200, 540, 15, 14);
		frame.getContentPane().add(backChanLabel_3);
		
		JLabel backChanLabel_5 = new JLabel("5");
		backChanLabel_5.setBounds(270, 539, 15, 14);
		frame.getContentPane().add(backChanLabel_5);
		
		JLabel backChanLabel_7 = new JLabel("7");
		backChanLabel_7.setBounds(330, 539, 15, 14);
		frame.getContentPane().add(backChanLabel_7);
		
		JLabel backChanLabel_2 = new JLabel("2");
		backChanLabel_2.setBounds(140, 589, 15, 14);
		frame.getContentPane().add(backChanLabel_2);
		
		JLabel backChanLabel_4 = new JLabel("4");
		backChanLabel_4.setBounds(200, 589, 15, 14);
		frame.getContentPane().add(backChanLabel_4);
		
		JLabel backChanLabel_5_1 = new JLabel("6");
		backChanLabel_5_1.setBounds(270, 588, 15, 14);
		frame.getContentPane().add(backChanLabel_5_1);
		
		JLabel backChanLabel_8 = new JLabel("8");
		backChanLabel_8.setBounds(330, 588, 15, 14);
		frame.getContentPane().add(backChanLabel_8);
		
		
		JComboBox<String> backChan_1 = new JComboBox<String>(sensors);
		backChan_1.setSelectedIndex(0);
		backChan_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 1");
				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(1);
				}else{
					chrono.conn(sensorType, 1);
				}	
			}
		});
		comboBoxes.add(backChan_1);
		backChan_1.setBounds(115, 560, 60, 20);
		frame.getContentPane().add(backChan_1);
		
		JComboBox<String> backChan_3 = new JComboBox<String>(sensors);
		backChan_3.setSelectedIndex(0);
		backChan_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 3");
				
				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(3);
				}else{
					chrono.conn(sensorType, 3);
				}
			}
		});
		comboBoxes.add(backChan_3);
		backChan_3.setBounds(180, 560, 60, 20);
		frame.getContentPane().add(backChan_3);
		
		JComboBox<String> backChan_5 = new JComboBox<String>(sensors);
		backChan_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 5");

				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(5);
				}else{
					chrono.conn(sensorType, 5);
				}
				
			}
		});
		comboBoxes.add(backChan_5);
		backChan_5.setBounds(245, 560, 60, 20);
		frame.getContentPane().add(backChan_5);
		
		JComboBox<String> backChan_7 = new JComboBox<String>(sensors);
		backChan_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 7");
				
				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(7);
				}else{
					chrono.conn(sensorType, 7);
				}
			}
		});
		comboBoxes.add(backChan_7);
		backChan_7.setBounds(310, 560, 60, 20);
		frame.getContentPane().add(backChan_7);
		

		
		JComboBox<String> backChan_2 = new JComboBox<String>(sensors);
		backChan_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 2");
				
				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(2);
				}else{
					chrono.conn(sensorType, 2);
				}
			}
		});
		comboBoxes.add(backChan_2);
		backChan_2.setBounds(115, 609, 60, 20);
		frame.getContentPane().add(backChan_2);
		
		JComboBox<String> backChan_4 = new JComboBox<String>(sensors);
		backChan_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 4");
				
				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(4);
				}else{
					chrono.conn(sensorType, 4);
				}
			}
		});
		comboBoxes.add(backChan_4);
		backChan_4.setBounds(180, 609, 60, 20);
		frame.getContentPane().add(backChan_4);
		

		
		JComboBox<String> backChan_6 = new JComboBox<String>(sensors);
		backChan_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 6");
				
				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(6);
				}else{
					chrono.conn(sensorType, 6);
				}
			}
		});
		comboBoxes.add(backChan_6);
		backChan_6.setBounds(245, 609, 60, 20);
		frame.getContentPane().add(backChan_6);
		

		
		JComboBox<String> backChan_8 = new JComboBox<String>(sensors);
		backChan_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Back radio 8");
				
				String sensorType = (String)(((JComboBox<String>) e.getSource()).getSelectedItem());
				if(sensorType.equals("-")){
					chrono.disc(8);
				}else{
					chrono.conn(sensorType, 8);
				}
			}
		});
		comboBoxes.add(backChan_8);
		backChan_8.setBounds(310, 609, 60, 20);
		frame.getContentPane().add(backChan_8);
		
		JLabel backChanLabel = new JLabel("Channel");
		backChanLabel.setBounds(27, 540, 46, 14);
		frame.getContentPane().add(backChanLabel);
		
		JToggleButton usbConnect = new JToggleButton("Connect");
		usbConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("USB connect");	
				//if(guiActive){
				System.out.println("THIS");
					usbActive = usbActive ? false : true;		
					System.out.println(usbActive);
				//}							
			}
		});
		toggleButtons.add(usbConnect);
		usbConnect.setBounds(384, 558, 121, 23);
		frame.getContentPane().add(usbConnect);
		
		JLabel usbLabel = new JLabel("Usb port");
		usbLabel.setBounds(420, 543, 66, 14);
		frame.getContentPane().add(usbLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 500, 814, 2);
		frame.getContentPane().add(separator);
		
		
		// buttons to test specific functions not available for user
		
		// TEST BUTTONS///////////////////////////////////////////////////////////////
		JButton testButton_1 = new JButton("Run server");
		testButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
		                Server.main(new String[0]);
		            } catch (Exception e1) {
		                e1.printStackTrace();
		          }
				
			}
		});
		testButton_1.setBounds(600, 510, 90, 25);
		frame.getContentPane().add(testButton_1);
//
		JButton testButton_2 = new JButton("Test serv");
		testButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client c = new Client("http://localhost", 8000);
//				String test = "[{\"number\": 111,\"startTime\": \"Apr 21, 2016 10:25:17 PM\",\"endTime\": \"Apr 21, 2016 10:25:32 PM\"},{\"number\": 222,\"startTime\": \"Apr 21, 2016 10:25:18 PM\",\"endTime\": \"Apr 21, 2016 10:25:33 PM\"},{\"number\": 333,\"startTime\": \"Apr 21, 2016 10:25:19 PM\",\"endTime\": \"Apr 21, 2016 10:25:34 PM\"},{\"number\": 444,\"startTime\": \"Apr 21, 2016 10:25:20 PM\",\"endTime\": \"Apr 21, 2016 10:25:35 PM\"},{\"number\": 555,\"startTime\": \"Apr 21, 2016 10:25:21 PM\",\"endTime\": \"Apr 21, 2016 10:25:37 PM\"}]";
				String test = "[{\"number\": 111,\"startTime\": \"1086073200000\",\"endTime\": \"1086073200000\"},{\"number\": 222,\"startTime\": \"1086073200000\",\"endTime\": \"1086073200000\"}]";
				c.sendData("sendresults", test);

			}
		});
		testButton_2.setBounds(600, 540, 90, 25);
		frame.getContentPane().add(testButton_2);
//		
//		JButton testButton_3 = new JButton("Dnf Test");
//		testButton_3.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				chrono.dnf();
//			}
//		});
//		testButton_3.setBounds(600, 570, 90, 25);
//		frame.getContentPane().add(testButton_3);
//		
//		JButton testButton_4 = new JButton("Strt Test");
//		testButton_4.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {	
//				chrono.start();
//			}
//		});
//		testButton_4.setBounds(600, 600, 90, 25);
//		frame.getContentPane().add(testButton_4);
//		
//		JButton testButton_5 = new JButton("Fin Test");
//		testButton_5.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				chrono.finish();
//			}
//		});
//		testButton_5.setBounds(600, 630, 90, 25);
//		frame.getContentPane().add(testButton_5);
		
		
	}
}
