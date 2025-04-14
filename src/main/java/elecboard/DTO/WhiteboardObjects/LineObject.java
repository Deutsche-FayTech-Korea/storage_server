package elecboard.DTO.WhiteboardObjects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import elecboard.DTO.Sub.Point;
import elecboard.DTO.Sub.Style;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class LineObject extends WhiteboardObject {
    private Style style;
    private List<Point> points;
}