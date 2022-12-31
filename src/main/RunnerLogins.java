package main;

import Pages.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RunnerLogins {
	private String userId;
	private WebDriver driver;
	private MarketAlertLogin marketAlertLogin;
	private MarketAlertList marketAlertList;

	public RunnerLogins(String userId, String chromeDriverPath){
        //set path to ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		this.userId = userId;
		this.driver = new ChromeDriver();
		this.marketAlertLogin = new MarketAlertLogin(driver);
		this.marketAlertList = new MarketAlertList(driver);
	}

	public void run(){
		
		int action = 0;
		
        while(true){
			action = Util.randomInt(0,3);
			
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
	
	public boolean loginCheck(){
		return marketAlertList.isOnAlertsPage();
	}
}