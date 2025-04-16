package elecboard.DTO;


import lombok.Data;
import elecboard.DTO.WhiteboardObjects.WhiteboardObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//외부와 통신할 때 PageDocument의 값을 여기에 담아서 전달
@Document(collection = "whiteboard_pages")
@Data
public class Page {

    private List<String> userNames;
    private String roomId;
    private String roomName;
    private List<WhiteboardObject> objects;
}
