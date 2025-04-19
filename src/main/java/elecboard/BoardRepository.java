package elecboard;

import elecboard.DTO.PageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BoardRepository extends MongoRepository<PageDocument,String> {

    PageDocument findByRoomId(String roomId);

    List<PageDocument> findByUserNamesContaining(String userName);
}
