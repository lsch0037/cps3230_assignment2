package main;

import main.Runner;

public class Main {
	public static void main(String[] args){
		String userId = "21ed7a53-ff36-4daf-8da0-c8b66b11c0de";
		String driverPath = "C:\\ChromeDriver\\chromedriver.exe";
		
		Runner runner = new Runner(userId, driverPath);
		runner.runLogins();
	}
}
