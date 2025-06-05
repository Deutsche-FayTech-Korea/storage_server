package elecboard;

import elecboard.DTO.PageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends MongoRepository<PageDocument,String> {

    List<PageDocument> findByParticipants_UserId(String id);

    PageDocument findByRoomId(String roomId);

    Optional<PageDocument> findByRoomIdAndParticipants_UserId(String roomId, String userId);

    void deleteByRoomIdAndParticipants_UserId(String roomId, String userId);
}
