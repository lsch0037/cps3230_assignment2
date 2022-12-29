package main;

import Pages.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class Runner {
	private String userId;
	private WebDriver driver;
	private MarketAlertLogin marketAlertLogin;
	private MarketAlertList marketAlertList;
	//private URL url;
	//private HttpURLConnection con;

	public Runner(String userId, String chromeDriverPath){
        //set path to ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		this.userId = userId;
		this.driver = new ChromeDriver();
		this.marketAlertLogin = new MarketAlertLogin(driver);
		this.marketAlertList = new MarketAlertList(driver);
	}

	public void runLogins(){
		
		int action = 0;
		
        while(true){
			action = randomInt(0,3);
			
			switch(action){
                case 0:
                    goToLoginPage();
                    goodLogin();
                    logout();
                    break;
                case 1:
                	goToLoginPage();
                    badLogin();
                    break;
            }
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void runAlerts(){
		
		int action = 0;
		int invalidField = 0;
		JSONObject alert;
		JSONObject invalidAlert;
		
		
		while(true){
			action = randomInt(0,3);
			
			switch(action){
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
            }
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		String userId = "21ed7a53-ff36-4daf-8da0-c8b66b11c0de";
		String driverPath = "C:\\ChromeDriver\\chromedriver.exe";
		
		Runner runner = new Runner(userId, driverPath);
		System.out.println("Created Runner");
		
		JSONObject alert = runner.createRandomJson();
		try {
			runner.testPostAlert(alert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testPostAlert(JSONObject alert) throws Exception{
		
    	URL url = new URL ("https://api.marketalertum.com/Alert");
    	HttpURLConnection con = (HttpURLConnection)url.openConnection();
    	con.setRequestMethod("POST");
    	con.setRequestProperty("Content-Type", "application/json");
    	con.setRequestProperty("Accept", "application/json");
    	con.setDoOutput(true);
    	
    	String jsonInputString = alert.toString();
    	
    	try(OutputStream os = con.getOutputStream()) {
    	    byte[] input = jsonInputString.getBytes("utf-8");
    	    os.write(input, 0, input.length);			
    	}
    	
    	try(BufferedReader br = new BufferedReader(
    			  new InputStreamReader(con.getInputStream(), "utf-8"))) {
    			    StringBuilder response = new StringBuilder();
    			    String responseLine = null;
    			    while ((responseLine = br.readLine()) != null) {
    			        response.append(responseLine.trim());
    			    }
    			    System.out.println(response.toString());
    			}
    }

	private void goToLoginPage(){
		driver.get("https://www.marketalertum.com/Alerts/login");
	}

	// Logs in with a correct user Id
	public void goodLogin(){
		System.out.println("Good login at: " + System.currentTimeMillis());
		login(userId);
	}

	// Logs in with a bad user Id
	public void badLogin(){
		System.out.println("Bad login at: " + System.currentTimeMillis());
		login("Bad Id");
	}
	
	// Attempts to log into the system with the given userId
	private void login(String userId){
		marketAlertLogin.inputUserId(userId);
		marketAlertLogin.submit();
	}

	// Logs out from the system
	public void logout(){
		System.out.println("Logout at: " + System.currentTimeMillis());
		marketAlertList.logOut();
	}
    
    // This function is take from: https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
    // function to generate a random string of length n
    static String getAlphaNumericString(int n){
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        
        for (int i = 0; i < n; i++) {
        
        // generate a random number between
        // 0 to AlphaNumericString variable length
        int index
            = (int)(AlphaNumericString.length()
            * Math.random());
        
        // add Character one by one in end of sb
        sb.append(AlphaNumericString
            .charAt(index));
        }
        
        return sb.toString();
    }

    /*
     * This method exists to simplify the generation of random integers
     * returns a random integer between and including min and max
     */
    public int randomInt(int min, int max){
        return (int) (Math.random()* (max - min) + min);
    }

    private JSONObject createRandomJson(){
        int alertType = randomInt(1, 6);
        String heading = getAlphaNumericString(10);
        String description = getAlphaNumericString(10);
        String url = getAlphaNumericString(10);
        String imageUrl = getAlphaNumericString(10);
        int priceInCents = randomInt(0, 1000000);

        return createJson(alertType, heading, description, url, imageUrl, userId ,priceInCents);
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
    
    

    /*
     * Attepts to post the json object to the api
     * Returns the response object
     * In case send fails return null
     */
    /*
    public HttpResponse postAlert(JSONObject json){

        HttpClient client = HttpClient.newHttpClient();
        
        //create post request out of json object
        HttpRequest request = HttpRequest.newBuilder(
            URI.create("https://api.marketalertum.com/Alert"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json.toString()))
        .build();
        
        try{
            //attempt to send 
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
            return res;     //return response 
        }catch(Exception e){
            return null;    //in case of fail return a null object
        }
    }*/
    
    private void postValidAlert(){
    	System.out.println("Posting Valid Alert");
    	JSONObject alert = createRandomJson();
        postAlert(alert);
    }
    
    private void postInvalidAlert(){
    	System.out.println("Posting Alert With Invalid ");
    	JSONObject alert = createRandomJson();
    	
    	int invalidAttribute = randomInt(0,7);
    	
    	try{
    		switch(invalidAttribute){
    		case 0:
    			System.out.print("Type");
    			if(randomInt(0,1) == 1){
    				alert.put("alertType", randomInt(7,100));
    			}else{
    				alert.put("alertType", randomInt(-100, 0));
    			}
    			break;
    		case 1:
    			System.out.print("Heading");
    			alert.put("heading", "");
    			break;
    		case 2: 
    			System.out.print("Description");
    			alert.put("description", "");
    			break;
    		case 3: 
    			System.out.print("URL");
    			alert.put("url", "");
    			break;
    		case 4: 
    			System.out.print("Image URL");
    			alert.put("imageUrl", "");
    			break;
    		case 5: 
    			System.out.print("userId");
    			alert.put("userId", "Bad Id");
    			break;
    		case 6: 
    			System.out.print("Price");
    			alert.put("alertType", randomInt(-100, 0));
    			break;
    		}
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
    	
    	postAlert(alert);
    }
    
    
    
    //https://www.baeldung.com/httpurlconnection-post
    public void postAlert(JSONObject alert){
    	//TODO: SEND JSONOBJECT TO API
    	try{
    		URL url = new URL ("https://www.marketalertum.com/Alert");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			
			//String jsonInputString = alert.toString();
			String jsonInputString = "{\"name\": \"Upendra\", \"job\": \"Programmer\"}";
			
			OutputStream os = con.getOutputStream();
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);		
			//os.close();
			
			//BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			
			InputStream is = con.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			
			/*StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println(response.toString());*/
			
			//return HTTP.toJSONObject(response.toString());
			
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	//return null;
    }
    
    public List<JSONObject> getAlerts(){
    	System.out.println("Getting Alerts");
    	List<JSONObject> alerts = new LinkedList<JSONObject>();
    	//TODO: SEND GET REQUEST TO API
    	
    	return alerts;
    }
    
    public void deleteAlerts(){
    	System.out.println("Deleting Alerts");
    	//TODO: SEND DELETE REQUEST TO API
    	return;
    }
    
    public void getEventsLog(){
    	
    }
    
    /*
     * Sends a delete request with the given user Id to the marketAlertUm api
     */
    /*
    public int deleteAlerts(){
        HttpClient client = HttpClient.newHttpClient();
        
        //create delete request
        HttpRequest request = HttpRequest.newBuilder(
            URI.create("https://api.marketalertum.com/Alert?userId=" + userId))
        .DELETE()
        .build();
        
        HttpResponse<String> response;
        try{
            //attempt to make send request
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(Exception e){
            return -1;  //in case of exception return -1
        }

        //return the statuscode of the request
        return response.statusCode();
    }*/

	// TODO: MODIFY THIS TO RETURN THE NUMBER OF ALERTS
	//return a list of alerts that are displayed on marketAlertUM
    public List<WebElement> getAlerts(WebDriver driver){
        return marketAlertList.getAlerts();
    }

	public void getEventLog(){

	}
}