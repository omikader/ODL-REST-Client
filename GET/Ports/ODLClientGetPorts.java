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

public class ODLClientGetPorts {

    public static void main(String[] args) {

	try {
	    String link = "http://10.42.200.130:8181/restconf/operational/opendaylight-inventory:nodes/";
	    URL url = new URL(link);

	    String userpass = "admin:admin";
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
	    
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    // REST call
	    conn.setRequestMethod("GET");
	    conn.setDoOutput(true);
	    conn.setRequestProperty("Authorization", basicAuth);
	    conn.setRequestProperty("Accept", "application/json");

	    if (conn.getResponseCode() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }

	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	    // read JSON output from REST call
	    String line, output = "";
	    while ((line = br.readLine()) != null) {
	        output += line;
	    }

	    br.close();
	    JSONObject obj = null, nodes = null;
	    JSONArray nodeArr = null, nodeConnectors = null;
	    List<String> ports = new ArrayList<String>();

	    try {
		obj = new JSONObject(output);
		nodes = obj.getJSONObject("nodes");
		nodeArr = nodes.getJSONArray("node");
		int numSwitches = nodeArr.length() - 1;

		System.out.println("There are " + numSwitches + " switches on this SDN network");

		for (int i = 0; i < numSwitches; i++) {
		    String switchName = nodeArr.getJSONObject(i).getString("id");
		    System.out.println("\n" + switchName + " has the following ports:");

		    nodeConnectors = nodeArr.getJSONObject(i).getJSONArray("node-connector");

		    for (int j = 0; j < nodeConnectors.length(); j++) {
			String portId = nodeConnectors.getJSONObject(j).getString("id");
			String portName = nodeConnectors.getJSONObject(j).getString("flow-node-inventory:name");
			ports.add(portId);
			System.out.println("\t" + portName + " - " + portId);
		    }
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
