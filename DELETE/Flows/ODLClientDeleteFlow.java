import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.*;

public class ODLClientDeleteFlow {

    public static void main(String[] args) {

	try {

	    String link = "http://10.42.200.130:8181/restconf/config/opendaylight-inventory:nodes/node/" + args[0] + "/flow-node-inventory:table/" + args[1] + "/flow/" + args[2];
	    URL url = new URL(link);

	    String userpass = "admin:admin";
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setRequestMethod("DELETE");
	    conn.setDoOutput(true);
	    conn.setRequestProperty("Authorization", basicAuth);
	    conn.setRequestProperty("Accept", "application/json");

	    if (conn.getResponseCode() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    } else {
		System.out.print("Rest Service invoked successfully...\n");
	    }
	    
	    conn.disconnect();
	    
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }

}
