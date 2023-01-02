package main;

import Pages.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RunnerLogins {
	private String userId;
	private WebDriver driver;
	private MarketAlertLogin marketAlertLogin;
	private MarketAlertList marketAlertList;
	private boolean isLoggedIn;

	public RunnerLogins(String userId, String chromeDriverPath){
        //set path to ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		this.userId = userId;
		this.driver = new ChromeDriver();
		this.marketAlertLogin = new MarketAlertLogin(driver);
		this.marketAlertList = new MarketAlertList(driver);
		this.isLoggedIn = false;
	}

	public void run(){
		
		//Loop forever
        while(true){
        	
        	//if system is logged in
			if(!isLoggedIn){
				switch(Util.randomInt(0,3)){
            		case 0:
            			//good login
            			goToLoginPage();
            			goodLogin();
            			break;
            		case 1:
            			//bad login
            			goToLoginPage();
            			badLogin();
            			break;
            		case 2:
            			//verify login status
            			loginCheck();
            			break;
				}
			//if system is logged out
			}else{
				switch(Util.randomInt(0,2)){
        		case 0:
        			//log out
        			logout();
        			break;
        		case 1:
        			//verify login status
        			loginCheck();
        			break;
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void goToLoginPage(){
		driver.get("https://www.marketalertum.com/Alerts/login");
	}

	// Logs in with a correct user Id
	public void goodLogin(){
		System.out.println("Good login at: " + System.currentTimeMillis());
		login(userId);
		isLoggedIn = true;
	}

	// Logs in with a bad user Id
	public void badLogin(){
		System.out.println("Bad login at: " + System.currentTimeMillis());
		login("Bad Id");
		isLoggedIn = false;
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
		isLoggedIn = false;
	}
	
	public void loginCheck(){
		//Empty method that triggers the larva script to check the login
		System.out.println("Checking Login Status");
		return;
	}
	
	//Returns whether the system is logged in based on whether it is on the Alerts Page or not
	/*
	public boolean isLoggedIn(){
		return marketAlertList.isOnAlertsPage();
	}
	*/
}