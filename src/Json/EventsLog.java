package Json;

import java.util.List;

public class EventsLog {
	public String id;
	public String timestamp;
	public int eventLogType;
	public String userId;
	public SystemState systemState;
	
	public class SystemState {
		String userId;
		boolean loggedIn;
		List<Alert> alerts;
	}
	
	/*public EventLog(String id, String timestamp, int eventLogType, String userId, SystemState systemState) {
		this.id = id;
		this.timestamp = timestamp;
		this.eventLogType = eventLogType;
		this.userId = userId;
		this.systemState = systemState;
	}*/
}
