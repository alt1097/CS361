package export;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;

import com.google.gson.Gson;

public class Export {
	
	Gson gson;
	
	
	public Export(){
		gson = new Gson();
		
	}
	
	/**
	 * Saves object to JSON file in root
	 * 
	 */
	public void objectToJsonFile(Object singleObject){
		PrintWriter writer;
		try {
			writer = new PrintWriter(new Exception().getStackTrace()[1].getClassName()+".json", "UTF-8");
			writer.println(objectToJsonString(singleObject));
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("Cannot write JSON in file");
			e.printStackTrace();
		}
	}

	/**
	 * Returns object as a JSON string
	 * 
	 */
	public String objectToJsonString(Object singleObject){
		return gson.toJson(singleObject);
	}

}