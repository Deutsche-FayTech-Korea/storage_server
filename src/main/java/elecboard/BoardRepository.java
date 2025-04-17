package elecboard;

import elecboard.DTO.PageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<PageDocument,String> {

    PageDocument findByRoomId(String roomId);
}
