package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class RunnerAlerts {
	private String userId;
	
	public RunnerAlerts(String userId){
		this.userId = userId;
	}
	
	public void testPostAlert(){
		JSONObject alert = createValidAlert();
		
		JSONObject response = postAlert(alert);
		
		System.out.println(response.toString());
	}
	
	public void testDeleteAlerts(){
		JSONObject response = deleteAlerts();
		
		System.out.println(response.toString());
	}

	public void run(){	
		int action = 0;
		JSONObject alert;
		
		while(true){
			action = Util.randomInt(0,3);
			
			switch(action){
                case 0:
                	//Post valid alert
                	alert = createValidAlert();
				try {
					postAlert(alert);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
                    break;
                    
                case 1:
                	//Post invalid alert
                	alert = createInvalidAlert();
				try {
					postAlert(alert);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
                    break;
                    
                case 2:
                	//Delete Alerts
				try {
					deleteAlerts();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private JSONObject createJson(int alertType, String heading, String description, String url ,String imageUrl, String userId, int priceInCents){
		JSONObject object = new JSONObject();

		try {
			object.put("alertType", alertType);
			object.put("heading", heading);
			object.put("description", description);
			object.put("url", url);
			object.put("imageUrl", imageUrl);
			object.put("postedBy", userId);
			object.put("priceInCents", priceInCents);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return object;
	}

	private JSONObject createValidAlert(){
		System.out.println("Creating Alert");
		int alertType = Util.randomInt(1, 6);
		String heading = Util.getAlphaNumericString(10);
		String description = Util.getAlphaNumericString(10);
		String url = Util.getAlphaNumericString(10);
		String imageUrl = Util.getAlphaNumericString(10);
		int priceInCents = Util.randomInt(0, 1000000);

		return createJson(alertType, heading, description, url, imageUrl, userId ,priceInCents);
	}
	
	private JSONObject createInvalidAlert(){
    	JSONObject alert = createValidAlert();
    	System.out.print(" with invalid ");
    	
    	int invalidAttribute = Util.randomInt(0,7);
    	
    	try{
    		switch(invalidAttribute){
    		case 0:
    			System.out.print("Type\n");
    			if(Util.randomInt(0,1) == 1){
    				alert.put("alertType", Util.randomInt(7,100));
    			}else{
    				alert.put("alertType", Util.randomInt(-100, 0));
    			}
    			break;
    		case 1:
    			System.out.print("Heading\n");
    			alert.put("heading", "");
    			break;
    		case 2: 
    			System.out.print("Description\n");
    			alert.put("description", "");
    			break;
    		case 3: 
    			System.out.print("URL\n");
    			alert.put("url", "");
    			break;
    		case 4: 
    			System.out.print("Image URL\n");
    			alert.put("imageUrl", "");
    			break;
    		case 5: 
    			System.out.print("userId\n");
    			alert.put("userId", "Bad Id");
    			break;
    		case 6: 
    			System.out.print("Price\n");
    			alert.put("alertType", Util.randomInt(-100, 0));
    			break;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return alert;
    }
	
	public JSONObject postAlert(JSONObject alert){
		System.out.println("Posting Alert");
		try {
			return marketAlertApiCall("https://api.marketalertum.com/Alert","POST", alert);
		} catch (Exception e) {
			System.out.println("Failed to post Alert");
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONObject deleteAlerts(){
		System.out.println("Deleting Alerts");
		try {
			return marketAlertApiCall("https://api.marketalertum.com/Alert?userid=" + userId, "DELETE", null);
		} catch (Exception e) {
			System.out.println("Failed to delete Alerts");
			e.printStackTrace();
			return null;
		}
	}
	
	//TODO: CONVERT THIS TO USE MARKLETALERTAPICALL
	public JSONObject getEventsLog() throws Exception{
    	URL url = new URL ("https://api.marketalertum.com/EventsLog/" + userId);
    	HttpURLConnection con = (HttpURLConnection)url.openConnection();
    	con.setRequestMethod("GET");
    	con.setDoOutput(true);
    	
    	con.connect();
    	
    	StringBuilder response;
    	try(BufferedReader br = new BufferedReader(
    			  new InputStreamReader(con.getInputStream(), "utf-8"))) {
    			    response = new StringBuilder();
    			    String responseLine = null;
    			    while ((responseLine = br.readLine()) != null) {
    			        response.append(responseLine.trim());
    			    }
    			    br.close();
    			}
    	
    	con.disconnect();
    	return new JSONObject(response.toString());
    }
	
	
	private JSONObject marketAlertApiCall(String urlString, String requestMethod, JSONObject alert) throws Exception{
		URL url = new URL (urlString);
    	HttpURLConnection con = (HttpURLConnection)url.openConnection();
    	
    	con.setRequestMethod(requestMethod);
    	con.setDoOutput(true);
    	
    	if(requestMethod == "POST"){
    		con.setRequestProperty("Content-Type", "application/json");
    		con.setRequestProperty("Accept", "application/json");
    		
    		String jsonInputString = alert.toString();
    	
    		try(OutputStream os = con.getOutputStream()) {
    			byte[] input = jsonInputString.getBytes("utf-8");
    			os.write(input, 0, input.length);		
    			os.close();
    		}
    	}else{
    		con.connect();
    	}
    	
    	StringBuilder response;
    	try(BufferedReader br = new BufferedReader(
    			  new InputStreamReader(con.getInputStream(), "utf-8"))) {
    			    response = new StringBuilder();
    			    String responseLine = null;
    			    while ((responseLine = br.readLine()) != null) {
    			        response.append(responseLine.trim());
    			    }
    			    br.close();
    			}
    	
    	con.disconnect();
    	return new JSONObject(response.toString());
	}
}
