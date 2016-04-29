package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Scanner;

import race.Racer;

import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

public class Client {

	private HttpURLConnection conn;
	private String url;
//	private ArrayList<Racer> racers = new ArrayList<>();

	public Client(String ip, int port) {
		url = ip + ":" + port + "/";
	}

	public String sendData(String route, String data){
		String urlSite = url + route;
		StringBuilder response = new StringBuilder();
		try {
			URL site = new URL(urlSite);
			conn = (HttpURLConnection) site.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes("data=" + data);
			out.flush();
			out.close();
			InputStreamReader inputStr = new InputStreamReader(conn.getInputStream());

			// read the characters from the request byte by byte and build up
			// the Response
			int nextChar;
			while ((nextChar = inputStr.read()) > -1) {
				response = response.append((char) nextChar);
			}

		} catch (IOException e) {
			System.out.println("No response from server. Check server instance/port settings");
//			e.printStackTrace();
		}
		return response.toString();
	}

//	public int addToList(Racer em, int index) {
//		if (!em.isEmpty()) {
//			if (index > employees.size() - 1) {
//				employees.add(em);
//			} else {
//				employees.set(index, em);
//			}
//			return index + 1;
//		}
//		return index;
//
//	}

//	public Racer getEmployee(int index) {
//		return employees.get(index);
//	}

//	public String getJSON() {
//		Gson g = new Gson();
//		return g.toJson(racers);
//	}

}