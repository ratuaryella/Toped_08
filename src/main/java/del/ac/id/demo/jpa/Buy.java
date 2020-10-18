package del.ac.id.demo.jpa;

public class Buy {
	private int total_item;
	private double rating;
	
	public Buy() {}
	public Buy(int total_item,double rating) {
		this.total_item = total_item;
		this.rating = rating;
	}
	
	public void setTotal_item(int total_item) {
		this.total_item = total_item;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public int getTotal_item() {
		return this.total_item;
	}
	public double getRating() {
		return this.rating;
	}
}
