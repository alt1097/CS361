package export;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;

import race.RaceIND;

public class Export {
	
	Gson gson;
	String json;
//	StringBuilder sb;
	
	
	public Export(){
		gson = new Gson();
		
	}	
	
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

	public String objectToJsonString(Object singleObject){
		json = gson.toJson(singleObject);
		System.out.println(json);
		return json;
		// not finished yet		
//		if(json != null){			
//			sb = new StringBuilder();
//			System.out.println("this - " + json.substring(0, json.length()));
//			sb.append(json.substring(0, json.length()));
//			sb.append(',');
//			sb.append(gson.toJson(singleObject).substring(1));
//			json = sb.toString();
////			System.out.println(json);
//		}else{	
//
//			System.out.println("that - " + json);
//			json = gson.toJson(singleObject);
//			System.out.println(json);
//		}
//		
//		return json;
	}	
	
	// not finished yet	
//	public String fewObjectsToJson(Object... someObjects){
//		
//		for(Object singleObject: someObjects){
//			objectToJson(singleObject);
//		}
//		
//		return json;
//	}
	
	// not tested at all
	public Object fromJson(String fileName){
		RaceIND obj = null;
		try {

			BufferedReader br = new BufferedReader(
				new FileReader(fileName));
			obj = gson.fromJson(br, RaceIND.class);
			
			System.out.println(obj);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}

}// end of Export