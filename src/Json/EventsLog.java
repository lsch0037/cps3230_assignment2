package Json;

import java.util.List;

public class EventsLog {
	public String id;
	public String timestamp;
	public int eventLogType;
	public String userId;
	public SystemState systemState;
	
	public class SystemState {
		public String userId;
		public boolean loggedIn;
		public Alert alerts[];
	}
}
