package elecboard;

import elecboard.DTO.PageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends MongoRepository<PageDocument,String> {

    PageDocument findByRoomId(String roomId);

    List<PageDocument> findByUserNamesContaining(String userName);

    Optional<PageDocument> findByRoomIdAndUserNamesContaining(String roomId, String userName);

    void deleteByRoomIdAndUserNamesContaining(String roomId, String userName);
}
