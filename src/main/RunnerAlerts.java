package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Json.Alert;
import Json.EventsLog;

import com.google.gson.Gson;

public class RunnerAlerts {
	private String userId;
	private Gson gson;
	
	public RunnerAlerts(String userId){
		this.userId = userId;
		this.gson = new Gson();
	}

	public void run(){	
		
		while(true){
			
			switch(Util.randomInt(0,4)){
                case 0:
                	//Post valid alert
                	postValidAlert();
                    break;
                    
                case 1:
                	//Post invalid alert
                	postInvalidAlert();
                    break;
                    
                case 2:
                	//Delete Alerts
					deleteAlerts();
					break;
				
                case 3:
                	EventsLog[] events = getEventsLog();
                	break;
            }
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Alert createValidAlert(){
		int alertType = Util.randomInt(1, 6);
		String heading = Util.getAlphaNumericString(10);
		String description = Util.getAlphaNumericString(10);
		String url = Util.getAlphaNumericString(10);
		String imageUrl = Util.getAlphaNumericString(10);
		int priceInCents = Util.randomInt(0, 1000000);

		return new Alert(alertType, heading, description, url, imageUrl, userId ,priceInCents);
	}
	
	public String postValidAlert(){
		System.out.println("Posting Valid Alert");
		Alert alert = createValidAlert();
    	
    	try{
    		return postAlert(alert);
    	}catch(Exception e){
    		System.out.println("Failed to Post Alert");
    		e.printStackTrace();
    		return "";
    	}
	}
	
	public String postInvalidAlert(){
		System.out.println("Posting Invalid Alert");
		Alert alert = createValidAlert();
    	alert.setPostedBy("Bad Id");
    	
    	try{
    		return postAlert(alert);
    	}catch(Exception e){
    		System.out.println("Failed to Post as expected");
    		return "";
    	}
	}
	
	public String postAlert(Alert alert) throws Exception{
		String alertJson = gson.toJson(alert);
		System.out.println("Posting Alert:" + alertJson);
		
		return marketAlertApiCall("https://api.marketalertum.com/Alert", "POST", alertJson);
	}
	
	public String deleteAlerts(){
		System.out.println("Deleting Alerts");
		try {
			return marketAlertApiCall("https://api.marketalertum.com/Alert?userid=" + userId, "DELETE", "");
		} catch (Exception e) {
			System.out.println("Failed to delete Alerts");
			e.printStackTrace();
			return null;
		}
	}
	
	//TODO: GET THE EVENTS LOG CALL TO WORK
	public EventsLog[] getEventsLog(){
		System.out.println("Getting Events Log");
		try {
			String response = marketAlertApiCall("https://api.marketalertum.com/EventsLog/" + userId, "GET", "");
			return gson.fromJson(response, EventsLog[].class);
		} catch (Exception e) {
			System.out.println("Failed to get Events Log");
			e.printStackTrace();
			return null;
		}	
	}
	
	private String marketAlertApiCall(String urlString, String requestMethod, String alertJson) throws Exception{
		URL url = new URL (urlString);
    	HttpURLConnection con = (HttpURLConnection)url.openConnection();
    	
    	con.setRequestMethod(requestMethod);
    	con.setDoOutput(true);
    	con.setReadTimeout(1000);
    	con.setConnectTimeout(1000);
    	
    	con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		
		con.connect();
	
		try(OutputStream os = con.getOutputStream()) {
			byte[] inBytes = alertJson.getBytes("utf-8");
			os.write(inBytes, 0, inBytes.length);
			os.close();
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
 
    	System.out.println("Response:" + response.toString());
    	
    	con.disconnect();
    	return response.toString();
	}
}
