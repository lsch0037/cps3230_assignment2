package main;

public class Main {
	public static void main(String[] args){
		String userId = "21ed7a53-ff36-4daf-8da0-c8b66b11c0de";
		String driverPath = "C:\\ChromeDriver\\chromedriver.exe";
		
		RunnerLogins runner = new RunnerLogins(userId, driverPath);
		//RunnerAlerts runner = new RunnerAlerts(userId);
		System.out.println("Created Runner");
		
		runner.run();
	}
}
