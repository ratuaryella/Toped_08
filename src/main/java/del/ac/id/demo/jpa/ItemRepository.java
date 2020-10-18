package del.ac.id.demo.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import del.ac.id.demo.jpa.Item;

@Repository
public interface ItemRepository extends MongoRepository<Item, String>{

}
