package elecboard.DTO.WhiteboardObjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import elecboard.DTO.Sub.Position;
import elecboard.DTO.Sub.Style;
import elecboard.DTO.Sub.Payload;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageObject extends WhiteboardObject {
    private Style style;
    private Position position;
    private Payload payload;
}