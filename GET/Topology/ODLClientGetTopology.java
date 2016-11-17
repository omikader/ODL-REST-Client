import org.json.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.*;

public class ODLClientGetTopology {

    public static void main(String[] args) {

	try {
	    
	    String link = "http://10.42.200.130:8181/restconf/operational/network-topology:network-topology/";
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

	    JSONObject ret = null;
	    JSONArray links = null, nodes = null;
	    DirectedGraph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<String, DefaultEdge> (DefaultEdge.class);
		
	    try {
		ret = new JSONObject(output);

		//vertices
		nodes = ret.getJSONObject("network-topology").getJSONArray("topology").getJSONObject(2).getJSONArray("node");
		for (int i = 0; i < nodes.length(); i++) {
		    directedGraph.addVertex(nodes.getJSONObject(i).getString("node-id"));
		}

		//edges
		links = ret.getJSONObject("network-topology").getJSONArray("topology").getJSONObject(2).getJSONArray("link");
		for (int i = 0; i < links.length(); i++) {
		    String src = links.getJSONObject(i).getJSONObject("source").getString("source-node");
		    String dest = links.getJSONObject(i).getJSONObject("destination").getString("dest-node");
		    directedGraph.addEdge(src,dest);
		}
	    } catch (JSONException e) {
		e.printStackTrace();
	    }

		System.out.println(directedGraph);	    

	    conn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }

}
