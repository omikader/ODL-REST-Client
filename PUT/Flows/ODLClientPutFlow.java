import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.*;

public class ODLClientPutFlow {

    public static void main(String[] args) {

	try {

	    String link = "http://10.42.200.130:8181/restconf/config/opendaylight-inventory:nodes/node/" + args[0] + "/flow-node-inventory:table/" + args[1] + "/flow/" + args[2];
  	    URL url = new URL(link);

	    String userpass = "admin:admin";
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
	    
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setRequestMethod("PUT");
	    conn.setDoOutput(true);
	    conn.setRequestProperty("Authorization", basicAuth);
	    conn.setRequestProperty("Content-Type", "application/json");

	    File file = new File(args[3]);
	    String flow = "";
	    BufferedReader br = null;
	    
	    try {

	        br = new BufferedReader(new FileReader(file));
		String line;
		
		while ((line = br.readLine()) != null) {
		    flow += line + "\n";
		}
		
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    } finally {
		br.close();
	    }
	    
	    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
	    osw.write(flow);
	    osw.close();
	    
	    if (conn.getResponseCode() != 200 && conn.getResponseCode() != 201 && conn.getResponseCode() != 202 && conn.getResponseCode() != 204) {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    } else {
		System.out.println("Rest Service invoked successfully...");
	    }

	    conn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }

}
