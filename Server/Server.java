package Server;

/**
 * Simple HTTP handler for testing ChronoTimer
 */

import java.awt.Desktop;
import java.io.*;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import race.Racer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    // a shared area where we get the POST data and then use it in the other handler
    static String sharedResponse = "data=[]\n";
    static boolean gotMessageFlag = false;

    public static void main(String[] args) throws Exception {

        // set up a simple HTTP server on our local host
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // create a context to get the request to display the results
        server.createContext("/displayresults/endtime", new DisplayHandlerEndTime());
        server.createContext("/displayresults/starttime", new DisplayHandlerStartTime());
        server.createContext("/displayresults/bib", new DisplayHandlerBib());
        server.createContext("/displayresults/elapsed", new DisplayHandlerElapsed());
        server.createContext("/displayresults/mystyle.css", new CSSHandler());

        // create a context to get the request for the POST
        server.createContext("/sendresults",new PostHandler());
        server.setExecutor(null); // creates a default executor

        // get it going
        System.out.println("Starting Server...");
        server.start();
    }
    
    private static String getResponseBodyFromArrayList(ArrayList<Racer> fromJson) {
    	SimpleDateFormat millisFormat = new SimpleDateFormat("H:mm:ss.SSS");
    	int counter = 0;
        String result = "<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">";
        result += "<script type=\"text/javascript\">setTimeout(function () { location.reload();}, 15 * 1000);</script>";
        result += "<div class=\"table-title\"><h3>Race results</h3></div>";
        result += "<table class=\"table-fill\">\n";
        result += "\t<thead><tr>" +
                "<th class=\"text-left\">Place</th>" +
                "<th class=\"text-left\"><a href=\"/displayresults/bib\">Bib</a></th>" +
                "<th class=\"text-left\">Name</th>" +
                "<th class=\"text-left\"><a href=\"/displayresults/starttime\">Start time</a></th>" +
                "<th class=\"text-left\"><a href=\"/displayresults/endtime\">Finish time</a></th>" +
                "<th class=\"text-left\"><a href=\"/displayresults/elapsed\">Elapsed time</a></th>" +
                "</tr></thead><tbody class=\"table-hover\">";
        for (Racer e : fromJson) {
            result += "<tr>" +
            		"<td class=\"text-left\">" + (++counter) + "</td>" +
                    "<td class=\"text-left\">" + e.getNumber() + "</td>" +
                    "<td class=\"text-left\">" + (getName(e.getNumber())) + "</td>" +
                    "<td class=\"text-left\">" + millisFormat.format((new Date(e.getStartTime()))) + "</td>" +
                    "<td class=\"text-left\">" + millisFormat.format((new Date(e.getEndTime()))) + "</td>" +
                    "<td class=\"text-left\">" + millisFormat.format((new Date(e.getFinalTime()))) + "</td>" +
                    "</tr>";            
        }
        result += "</tbody></table>";
        return result;
    }
    
    private static String getName(int bib){
    	
    	String fileName = "racers.txt";
    	String line = "";

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {                
            	if((line.substring(0, line.indexOf(' ')).equals(Integer.toString(bib)))){
            		return line.substring(line.indexOf(' ') + 1);
            	}
            }   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file " + fileName);                
        }
        catch(IOException ex) {
            System.out.println("Error reading file " + fileName);
        }
        return line;
    }

    private static void createResponseWithComparator(HttpExchange t, Comparator c) throws IOException {
        Gson g = new Gson();
        ArrayList<Racer> fromJson = g.fromJson(sharedResponse.substring(5),
                new TypeToken<Collection<Racer>>() {
                }.getType());
        Collections.sort(fromJson, c);
        String response = getResponseBodyFromArrayList(fromJson);
        // write out the response
        t.getResponseHeaders().set("Content-Type", "text/html");
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    static class DisplayHandlerEndTime implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            createResponseWithComparator(t, new ByEnd());
        }
        
        /**
         * The value 0 if the argument Date is equal to this Date; a value less than 0
         * if this Date is before the Date argument; and a value greater than 0 if this
         * Date is after the Date argument.
         */
    	private class ByEnd implements Comparator<Object> {
    		@Override
    		public int compare(Object o1, Object o2) {
    			if (o1 instanceof Racer && o2 instanceof Racer) {
    				return ((Racer) o1).getEndTime().compareTo(((Racer) o2).getEndTime());
    			}
    			return 0;
    		}
    	}        
    }    
    static class DisplayHandlerStartTime implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            createResponseWithComparator(t, new ByStart());
        }
        /**
         * The value 0 if the argument Date is equal to this Date; a value less than 0
         * if this Date is before the Date argument; and a value greater than 0 if this
         * Date is after the Date argument.
         */
        private class ByStart implements Comparator<Object> {
        	@Override
        	public int compare(Object o1, Object o2) {
        		if (o1 instanceof Racer && o2 instanceof Racer) {
        			return ((Racer) o1).getStartTime().compareTo(((Racer) o2).getStartTime());
        		}
        		return 0;
        	}
        }
    }  
    static class DisplayHandlerBib implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            createResponseWithComparator(t, new ByBib());
        }
        /**
         * Returns 0 if bib # are same
         * 		   1 if o1 smaller than o2
         *		  -1 if o1 bigger than o2
         */
        private class ByBib implements Comparator<Object> {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Racer && o2 instanceof Racer) { 
                	
                	if(((Racer) o1).getNumber() < ((Racer) o2).getNumber()){
                		return -1;
                	}else if(((Racer) o1).getNumber() > ((Racer) o2).getNumber()){
                		return 1;
                	}
                }
                return 0;
            }
        }        
    }

    static class DisplayHandlerElapsed implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            createResponseWithComparator(t, new ByElapsed());
        }
        /**
         * The value 0 if the argument Date is equal to this Date; a value less than 0
         * if this Date is before the Date argument; and a value greater than 0 if this
         * Date is after the Date argument.
         */
        private class ByElapsed implements Comparator<Object> {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Racer && o2 instanceof Racer) {
                	return ((Racer) o1).getElapsedTime().compareTo(((Racer) o2).getElapsedTime());
                }
                return 0;
            }
        }
    }

    static class PostHandler implements HttpHandler {
        public void handle(HttpExchange transmission) throws IOException {
            //  shared data that is used with other handlers
            sharedResponse = "";

            // set up a stream to read the body of the request
            InputStream inputStr = transmission.getRequestBody();

            // set up a stream to write out the body of the response
            OutputStream outputStream = transmission.getResponseBody();

            // string to hold the result of reading in the request
            StringBuilder sb = new StringBuilder();

            // read the characters from the request byte by byte and build up the sharedResponse
            int nextChar = inputStr.read();
            while (nextChar > -1) {
                sb=sb.append((char)nextChar);
                nextChar=inputStr.read();
            }

            // create our response String to use in other handler
            sharedResponse = sharedResponse+sb.toString();

            // respond to the POST with ROGER
            String postResponse = "ROGER JSON RECEIVED";

            System.out.println("response: " + sharedResponse);
            
//            Desktop dt = Desktop.getDesktop();
//            dt.open(new File("./raceresults.html"));          
            

            // assume that stuff works all the time
            transmission.sendResponseHeaders(300, postResponse.length());

            // write it and return it
            outputStream.write(postResponse.getBytes());

            outputStream.close();
        }
    }

    private static class CSSHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String filename = httpExchange.getRequestURI().toString();
            File file = new File("./Server/" + filename.substring(filename.lastIndexOf('/') + 1));
            Headers h = httpExchange.getResponseHeaders();
            h.set("Content-Type", "text/css");
            OutputStream os = httpExchange.getResponseBody();
            Scanner s = new Scanner(file);
            String response = "";
            while (s.hasNextLine()) {
                response += s.nextLine();
            }
            httpExchange.sendResponseHeaders(200, response.length());
            os.write(response.getBytes());
            os.close();
            s.close();
        }
    }
}