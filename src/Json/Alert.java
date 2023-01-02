package Json;

public class Alert {
	private int alertType;
	private String heading;
	private String description;
	private String url;
	private String imageUrl;
	private String postedBy;
	private int priceInCents;
	
	public Alert(int alertType, String heading, String description, String url, String imageUrl, String postedBy, int priceInCents){
		this.alertType = alertType;
		this.heading = heading;
		this.description = description;
		this.url = url;
		this.imageUrl = imageUrl;
		this.postedBy = postedBy;
		this.priceInCents = priceInCents;
	}

	public int getAlertType() {
		return alertType;
	}

	public void setAlertType(int alertType) {
		this.alertType = alertType;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public int getPriceInCents() {
		return priceInCents;
	}

	public void setPriceInCents(int priceInCents) {
		this.priceInCents = priceInCents;
	}
}
