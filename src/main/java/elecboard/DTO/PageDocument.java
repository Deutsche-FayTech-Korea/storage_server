package elecboard.DTO;

import jakarta.persistence.Id;
import lombok.Data;
import elecboard.DTO.WhiteboardObjects.WhiteboardObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//db에 넣을 때 꺼낼 때 사용, 외부와 통신할 때는 직접적으로 사용x(Page에 담아서 전달)
@Document(collection = "whiteboard_pages")
@Data
public class PageDocument {
    @Id
    private String id;

    private String createdBy;
    private long createdAt;
    private List<WhiteboardObject> objects;
}
