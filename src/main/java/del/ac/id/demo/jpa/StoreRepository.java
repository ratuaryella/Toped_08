package del.ac.id.demo.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreRepository extends MongoRepository<Store ,String> {
	
	
	
}
