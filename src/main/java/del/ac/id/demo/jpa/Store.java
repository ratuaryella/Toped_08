package del.ac.id.demo.jpa;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;




@Document("store")
public class Store {
	@Id
	String id;
	private String store_name,location;
	private double points;
	private String reputation;
	private int latest_online;
	
	
	
	public Store() {}
	
	public Store(
		final String store_name,
		final String location,
		final double points,
		final String reputation,
		final int latest_online) {
		this.store_name = store_name;
		this.location = location;
		this.points = points;
		this.reputation = reputation;
		this.latest_online = latest_online;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public String getReputation() {
		return reputation;
	}

	public void setReputation(String reputation) {
		this.reputation = reputation;
	}

	public int getLatest_online() {
		return latest_online;
	}

	public void setLatest_online(int latest_online) {
		this.latest_online = latest_online;
	}
	
	
}
