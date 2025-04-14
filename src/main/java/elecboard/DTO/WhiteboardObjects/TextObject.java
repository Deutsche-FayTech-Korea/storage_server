package elecboard.DTO.WhiteboardObjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import elecboard.DTO.Sub.Position;
import elecboard.DTO.Sub.Style;
import elecboard.DTO.Sub.TextPayload;

@Data
@EqualsAndHashCode(callSuper = true)
public class TextObject extends WhiteboardObject {
    private Style style;
    private Position position;
    private TextPayload payload;
}