package elecboard.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import elecboard.DTO.WhiteboardObjects.WhiteboardObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.List;

//db에 넣을 때 꺼낼 때 사용, 외부와 통신할 때는 직접적으로 사용x(Page에 담아서 전달)
@Document(collection = "whiteboard_pages")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PageDocument {
    @Id
    private String id;

    private List<Participant> participants;
    private String madeBy;
    private String createdAt;
    private String roomId;
    private String roomName;
    private String mode;
    private List<WhiteboardObject> objects;
}
