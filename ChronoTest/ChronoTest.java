package ChronoTest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import main.ChronoTimer;
import main.Simulator;

public class ChronoTest {
	
 	Simulator simulator;
	static String tf = "testFiles";
	String t = ".txt"; 
	
	public static void main(String[] args) throws Exception {	     
		 JUnitCore jCore = new JUnitCore();
	     Result result = jCore.run(ChronoTest.class);
	     System.out.println("Runtime: " + result.getRunTime());
	     System.out.println("Tests failed: " + result.getFailureCount());
	     System.out.println("Tests ignored: " + result.getIgnoreCount());
	     System.out.println("\nFailed tests:\n");
	     for(Failure failure : result.getFailures()){
	    	 System.out.println("Test description: " + failure.getTestHeader());
	     }
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
	public void testAddRacerNoneAvailableGRP(){
		testUnit("testAddRacerNoneAvailableGRP");
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
	public void testData3(){
		testUnit("testData3");
	}

	@Test
	public void testEmptyFunctionalityGRP(){
		testUnit("testEmptyFunctionalityGRP");
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
	public void testRunCompleteRaceGRP(){
		testUnit("testRunCompleteRaceGRP");
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
		ClassLoader classLoader = getClass().getClassLoader();
		simulator = new Simulator();
		simulator.handler.enableJUnit();		
		ChronoTimer.enableJUnit();
		
		// IDE file path
        simulator.fileReader("\\"+classLoader.getResource(tf+"/" + fileName + t).toString().substring(5));
        // separate executable file path
//        simulator.fileReader((new File("").getAbsolutePath())+"\\"+classLoader.getResource(tf+"/" + fileName + t).toString().substring(5)); 
        debugCompare(fileRetrieve(fileName), ChronoTimer.debugLog.outputTestString());
		System.out.println("\n");
	}

	private ArrayList<String> fileRetrieve(String fileName){
		ClassLoader classLoader = getClass().getClassLoader();
		ArrayList<String> debug = new ArrayList<String>();
		
		// File path for Separate executable
//		try(BufferedReader br = new BufferedReader(new FileReader(new FileInputStream((new File("").getAbsolutePath())+"\\"+classLoader.getResource(tf+"/Results/" + fileName + " Debug"+t).toString().substring(5).replaceAll("%20", " ")).getFD()))){
// File path for IDE
			try(BufferedReader br = new BufferedReader(new FileReader(new FileInputStream("\\"+classLoader.getResource(tf+"/Results/" + fileName + " Debug"+t).toString().substring(5).replaceAll("%20", " ")).getFD()))){
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
		for(int i = 0; i < trueDebug.size(); i++){
			if(!trueDebug.get(i).equals(testDebug.get(i))){
				System.out.println("! ! ! FAILURE: "+trueDebug.get(i)+"  |  "+testDebug.get(i));
				fail("True debug and tested debug does not match!  Line number: "+i);
				break;
			}
		}
	}
}