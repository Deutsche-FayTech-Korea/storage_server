package elecboard;

import elecboard.DTO.PageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends MongoRepository<PageDocument,String> {

    List<PageDocument> findByParticipants_Id(int id);

    PageDocument findByRoomId(String roomId);

    Optional<PageDocument> findByRoomIdAndParticipants_Id(String roomId, int userId);

    void deleteByRoomIdAndParticipants_Id(String roomId, int userId);
}
