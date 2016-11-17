import org.json.*;
import java.util.*;
import java.lang.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.*;

public class ODLClientGetSwitches {

    public static void main(String[] args) {

	try {
	    
	    String link = "http://10.42.200.130:8181/restconf/config/opendaylight-inventory:nodes/";
	    URL url = new URL(link);

	    String userpass = "admin:admin";
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
	    
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setRequestMethod("GET");
	    conn.setDoOutput(true);
	    conn.setRequestProperty("Authorization", basicAuth);
	    conn.setRequestProperty("Accept", "application/json");

	    if (conn.getResponseCode() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }

	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	    String line, output = "";
	    while ((line = br.readLine()) != null) {
	        output += line;
	    }

	    br.close();
	    JSONObject obj = null;
	    JSONArray arr = null;

	    try {
		obj = new JSONObject(output);
		arr = obj.getJSONObject("nodes").getJSONArray("node");
		System.out.println("These are the switches on your network: ");

		for (int i = 0; i < arr.length(); i++) {
		    String switch_name = arr.getJSONObject(i).getString("id");
		    System.out.println(switch_name);
		}
	    } catch (JSONException e) {
		e.printStackTrace();
	    }

	    conn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }

}
