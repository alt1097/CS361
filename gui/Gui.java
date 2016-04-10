package gui;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


import main.ChronoTimer;

public class Gui {
	
	private JFrame frame;
	private JTextArea printerText;
	private JTextArea displayText;
	private JScrollPane scrollPane;
	private String[][] simpleMenu = new String[5][4];
	private int row;
	private int col;
	private ChronoTimer chrono;
	
	private boolean printerSwitch;	
	private boolean numPadActive;
	private boolean guiActive;
	private boolean allowOutsideInput;
	private boolean functionsMenuActive;
	
	private ArrayList<JComboBox> comboBoxes; // = new ArrayList<String>();
	private ArrayList<JRadioButton> radioButtons;
	
	/**
	 * Constructor for Gui class
	 * 
	 */
	public Gui(ChronoTimer chrono) {
		this.chrono = chrono;
			
		comboBoxes = new ArrayList<JComboBox>(); 
		radioButtons = new ArrayList<JRadioButton>(); 
		
		// row column	
		// main menu
		simpleMenu[0][0] = "New Race";
		simpleMenu[0][1] = "Add/Clr racer";
		simpleMenu[0][2] = "End run";
		simpleMenu[0][3] = "Export";
		
		// new race type menu
		simpleMenu[1][0] = "IND";
		simpleMenu[1][1] = "PARIND";
		simpleMenu[1][2] = "GRP";
		simpleMenu[1][3] = "PARGRP";
		
		// add racer function
		simpleMenu[2][0] = "num";
		simpleMenu[2][1] = "clr";
		simpleMenu[2][2] = "Item 2 2";
		simpleMenu[2][3] = "Item 2 3";
		
		// generic menu page
		simpleMenu[3][0] = "End active run";
		simpleMenu[3][1] = "Item 4 1";
		simpleMenu[3][2] = "Item 4 2";
		simpleMenu[3][3] = "Item 4 2";
		
		// export menu
		simpleMenu[4][0] = "File 2 0";
		simpleMenu[4][1] = "Server 2 1";
		simpleMenu[4][2] = "Item 2 2";
		simpleMenu[4][3] = "Item 2 2";
		
////		// generic menu page
//		simpleMenu[4][0] = "Item 4 0";
//		simpleMenu[4][1] = "Item 4 1";
//		simpleMenu[4][2] = "Item 4 2";
//		simpleMenu[4][3] = "Item 4 2";		
		
		initialize();
	}
		
	/**
	 * Method to return JFrame object
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
		if (allowOutsideInput) {
			appendToPrinterInternal(stringToPrinter);
		}
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


	private void drawCol() {
		if (guiActive) {
			displayText.setText("");
			for (int i = 0; i < simpleMenu[0].length; i++) {
				if ((simpleMenu[row][i] != null)) {
					if (i == col) {
						displayText.append("* " + simpleMenu[row][i] + "\n");
					} else {
						appendToDisplayInternal(simpleMenu[row][i]);
					}
				}
			}
		}
	}
	
	// remove or modify later
	private String getNumPadNumber(){		
		if(numPadActive){
			displayText.setText("");			
		}		
		return null;
	}	
	
	private void clear(){
		displayText.setText("");
		wipe();
		
	}
	
	private void wipe(){
		col = 0;
		row = 0;
	}
	
	/**
	 * Toggle printer state. Flip-flop switch for printer.
	 */
	private void togglePrinter() {
		printerSwitch = printerSwitch ? false : true;
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
	// ex
//	if(new Exception().getStackTrace()[1].getClassName().toString().equals("main.ChronoTimer")){
//	return;
//}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	/**
	 * Method to initialize all window components and functions
	 * 
	 */
	private void initialize() {
		frame = new JFrame("ChronoTimer 1009");
		frame.setBounds(100, 100, 850, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Left section
		JButton power = new JButton("Power");
		power.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Power");
				chrono.power();
							
				guiActive = guiActive ? false:true;				
				
				for(JRadioButton radio : radioButtons){
					radio.setSelected(false);
				}
				
				for(JComboBox comboBox : comboBoxes){
					comboBox.setSelectedIndex(0);
				}
				
				if(guiActive){
//					allowOutsideInput = true;
					
					
				}else{
					allowOutsideInput = false;
					clear();
					
					for(JRadioButton radio : radioButtons){
						radio.setSelected(false);
					}
					
					for(JComboBox comboBox : comboBoxes){
						comboBox.setSelectedIndex(0);
					}
					
					
				}
				
				
			}
		});
		power.setBounds(61, 11, 80, 23);
		frame.getContentPane().add(power);
		

		
		JButton functions = new JButton("Functions");
		functions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Function");
				
//				functionsMenuActive = functionsMenuActive ? true : false;				
				
				
				functionsMenuActive = functionsMenuActive ? false : true;
				
				
				if(functionsMenuActive){
					allowOutsideInput = false;
					drawCol();
				}else{
					allowOutsideInput = true;
					clear();
				}
				
				
			}
		});
		functions.setBounds(61, 141, 80, 23);
		frame.getContentPane().add(functions);
		
		JButton swap = new JButton("Swap");
		swap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Swap");
				chrono.swap();
			}
		});
		swap.setBounds(61, 332, 80, 23);
		frame.getContentPane().add(swap);
		
		BasicArrowButton up = new BasicArrowButton(BasicArrowButton.NORTH);
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Up");	
				if(functionsMenuActive){
									if((--col < 0)){
					col = simpleMenu[0].length - 1;
				}
				drawCol();
				}

			}
		});
		up.setBounds(90, 180, 25, 25);
		frame.getContentPane().add(up);
		
		BasicArrowButton left = new BasicArrowButton(BasicArrowButton.WEST);
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Left");
				if(functionsMenuActive){
									if((row-1) >= 0){
					col = row - 1;
				}	
				
				// TODO this need to be changed for bigger menu
				row = 0;
				
				drawCol();	
				}

			}
		});
		left.setBounds(65, 205, 25, 25);
		frame.getContentPane().add(left);
		
		BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST);
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Right");
				
				if(functionsMenuActive){
				
				// quick fix to eliminate infinite right click
				if(row == 1){ // race type menu page
					chrono.event(simpleMenu[row][col]);
					return;
				}else if(row == 2){ // add racer menu
					numPadActive = true; // activate numpad
					getNumPadNumber();
					return;
				}else if(row == 3){ // end run menu
					chrono.endRun();
					System.out.println("END RUN ISSUED");
					// TODO do something
					return;
				}else if(row == 4){ // export menu
					// TODO do something
					return;
				}else{
					row = col+1;
					col = 0;
				}
				
				drawCol();
				}
			}
		});
		right.setBounds(115, 205, 25, 25);
		frame.getContentPane().add(right);
		
		BasicArrowButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Down");	
				if(functionsMenuActive){
									if((++col > (simpleMenu[0].length - 1))){
					col = 0;
				}
				drawCol();
				}

			}
		});
		down.setBounds(90, 230, 25, 25);
		frame.getContentPane().add(down);
		
		
		// Middle Section
		
		// This is 4 labels Start and Enable/Desable
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
		displayText = new JTextArea();
		displayText.setBounds(297, 172, 250, 250);
		displayText.setEditable(false);
		frame.getContentPane().add(displayText);		
		
		// Right section
		JToggleButton printerPower = new JToggleButton("Printer");
		printerPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				printerText.append("Printer Power button\n");
//				appendToPrinter("OK");
				togglePrinter();
				
			}
		});
		printerPower.setBounds(665, 11, 89, 23);
		frame.getContentPane().add(printerPower);
		
		// TEST BUTTON///////////////////////////////////////////////////////////////
		JButton testButton = new JButton("TEST");
		testButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				printerText.append("Printer Power button\n");
				appendToPrinter("OKOKOKOKOKOKOKO");
//				togglePrinter();	
				chrono.print();
			}
		});
		testButton.setBounds(500, 11, 89, 23);
		frame.getContentPane().add(testButton);
		// TEST BUTTON///////////////////////////////////////////////////////////////
		
		
		// Text area for printer output
		printerText = new JTextArea();
//		printerText.setBounds(630, 41, 150, 200);
		printerText.setEditable(false);
		scrollPane = new JScrollPane(printerText);
		scrollPane.setBounds(630, 41, 150, 200);
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
					displayText.append("*");
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
				
				
				if(numPadActive){
					System.out.println("Numpad button #");
				
				 try {
					Method m = ChronoTimer.class.getMethod(simpleMenu[row][col], int.class);
					m.invoke(chrono, Integer.parseInt(displayText.getText()));
				} catch (NoSuchMethodException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		usbConnect.setBounds(384, 563, 121, 23);
		frame.getContentPane().add(usbConnect);
		
		JLabel usbLabel = new JLabel("Usb port");
		usbLabel.setBounds(538, 567, 66, 14);
		frame.getContentPane().add(usbLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 500, 814, 2);
		frame.getContentPane().add(separator);
		
	}
}
