package elecboard.DTO.WhiteboardObjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import elecboard.DTO.Sub.Position;
import elecboard.DTO.Sub.Style;

@Data
@EqualsAndHashCode(callSuper = true)
public class CircleObject extends WhiteboardObject {
    private Style style;
    private Position position;
}