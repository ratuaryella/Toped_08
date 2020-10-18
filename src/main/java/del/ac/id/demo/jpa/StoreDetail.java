package del.ac.id.demo.jpa;


import org.springframework.data.mongodb.core.mapping.Document;

@Document("store_id")
public class StoreDetail {

	
	private String store_name;

	public StoreDetail() {}
	
	public StoreDetail(
			
			final String store_name)
	{
		this.store_name = store_name;
		
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	

	
	
}
