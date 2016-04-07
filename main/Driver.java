package main;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 -- ChronoTimer 1009 --
 Author:  The Unnameables
 */

public class Driver {
	static Simulator simulator;
	static String tf = "Test Files/";
	static String t = ".txt";
	
	
	public static void main(String[] args)	{
		
		Scanner scanner = new Scanner(System.in);
		simulator = new Simulator();
		System.out.print("Choose file (f), console (c), or GUI (g) input:  ");
		String inputType = scanner.nextLine();
		if (inputType.startsWith("f"))
		{
			System.out.println("Please enter the filename of the test data:");
			simulator.fileReader(scanner.nextLine());
		}
		else if (inputType.startsWith("c"))
		{
			System.out.println("Enter commands into the console:");
			simulator.consoleReader();
		}
		else if (inputType.startsWith("g"))
		{
			System.out.println("Gui starting...");
			simulator.gui();
		}
		scanner.close();
	}

	@Test
	public void channelLimit(){
		testUnit("channelLimit");
	}

	@Test
	public void exportCorrectEmpty(){
		testUnit("exportCorrectEmpty");
	}

	@Test
	public void exportCorrectPartial(){
		testUnit("exportCorrectPartial");
	}

	@Test
	public void exportFourLaneLarge(){
		testUnit("exportFourLaneLarge");
	}

	@Test
	public void exportOneLaneFinish(){
		testUnit("exportOneLaneFinish");
	}

	@Test
	public void offTest(){
		testUnit("offTest");
	}

	@Test
	public void powerTest(){
		testUnit("powerTest");
	}

	@Test
	public void racerSetting(){
		testUnit("racerSetting");
	}

	@Test
	public void resetVerify(){
		testUnit("resetVerify");
	}

	@Test
	public void testAddOneRacer(){
		testUnit("testAddOneRacer");
	}

	@Test
	public void testAddOneRacerPARIND(){
		testUnit("testAddOneRacerPARIND");
	}

	@Test
	public void testAddTwoRacers(){
		testUnit("testAddTwoRacers");
	}

	@Test
	public void testAddTwoRacersPARIND(){
		testUnit("testAddTwoRacersPARIND");
	}

	@Test
	public void testClrOneRacerDoesntExistNotAllowed(){
		testUnit("testClrOneRacerDoesntExistNotAllowed");
	}

	@Test
	public void testClrOneRacerDoesntExistNotAllowedPARIND(){
		testUnit("testClrOneRacerDoesntExistNotAllowedPARIND");
	}

	@Test
	public void testClrOneRacerRaceOngoingNotAllowed(){
		testUnit("testClrOneRacerRaceOngoingNotAllowed");
	}

	@Test
	public void testClrOneRacerRaceOngoingNotAllowedPARIND(){
		testUnit("testClrOneRacerRaceOngoingNotAllowedPARIND");
	}

	@Test
	public void testClrOneRacerSuccessful(){
		testUnit("testClrOneRacerSuccessful");
	}

	@Test
	public void testClrOneRacerSuccessfulPARIND(){
		testUnit("testClrOneRacerSuccessfulPARIND");
	}

	@Test
	public void testData1(){
		testUnit("testData1");
	}

	@Test
	public void testData2(){
		testUnit("testData2");
	}

	@Test
	public void testOneRacerDNF(){
		testUnit("testOneRacerDNF");
	}

	@Test
	public void testOnlyOneRacerTriggersFinish(){
		testUnit("testOnlyOneRacerTriggersFinish");
	}

	@Test
	public void testOnlyOneRacerTriggersFinishPARIND(){
		testUnit("testOnlyOneRacerTriggersFinishPARIND");
	}

	@Test
	public void testOnlyOneRacerTriggersStartAndFinish(){
		testUnit("testOnlyOneRacerTriggersStartAndFinish");
	}

	@Test
	public void testOnlyOneRacerTriggersStartAndFinishPARIND(){
		testUnit("testOnlyOneRacerTriggersStartAndFinishPARIND");
	}

	@Test
	public void testSwapTwoRacersAllowed(){
		testUnit("testSwapTwoRacersAllowed");
	}

	@Test
	public void testSwapTwoRacersNotAllowed(){
		testUnit("testSwapTwoRacersNotAllowed");
	}

	@Test
	public void testTwoRacersDNF(){
		testUnit("testTwoRacersDNF");
	}

	@Test
	public void triggerLimit(){
		testUnit("triggerLimit");
	}

	private void testUnit(String fileName){
		System.out.println("==  "+fileName+"  ================================================");
		simulator = new Simulator();
		simulator.handler.enableJUnit();
		ChronoTimer.enableJUnit();
		simulator.fileReader(tf+fileName+t);
		debugCompare(fileRetrieve(fileName), ChronoTimer.debugLog.outputTestString());
		System.out.println("\n\n\n\n");
	}

	private ArrayList<String> fileRetrieve(String fileName){
		ArrayList<String> debug = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new FileReader(tf+"Results/"+fileName+" Debug"+t))){
			String line;
			while((line = br.readLine()) != null){
				debug.add(line);
			}
			return debug;
		}catch(IOException e){
			debug.add("Debug file retrieval failed!");
			return debug;
		}
	}

	private void debugCompare(ArrayList<String> trueDebug, ArrayList<String> testDebug){
//		if(trueDebug.size() != testDebug.size()){
//			fail("Number of true debug file lines != tested debug!");
//		}
		for(int i = 0; i < trueDebug.size(); i++){
			if(!trueDebug.get(i).equals(testDebug.get(i))){
				System.out.println("! ! ! FAILURE: "+trueDebug.get(i)+"  |  "+testDebug.get(i));
				fail("True debug and tested debug does not match!  Line number: "+i);
				break;
			}
		}
	}
}