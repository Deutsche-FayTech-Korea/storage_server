package elecboard.DTO.WhiteboardObjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "objectType") //타입 구별을 objectType으로 하겠다.
@JsonSubTypes({
        //objectType의 값이 name과 같으면 value의 클래스와 매치
        @JsonSubTypes.Type(value = LineObject.class, name = "line"),
        @JsonSubTypes.Type(value = RectObject.class, name = "rect"),
        @JsonSubTypes.Type(value = CircleObject.class, name = "circle"),
        @JsonSubTypes.Type(value = TextObject.class, name = "text"),
        @JsonSubTypes.Type(value = ImageObject.class, name = "image"),
})
public abstract class WhiteboardObject {
    private String id;
    //private String objectType; -> 12번 줄에서 정의해줌
    private String createdBy;
    private long createdAt;
}
