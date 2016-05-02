package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {

	private HttpURLConnection conn;
	private String url;

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
}