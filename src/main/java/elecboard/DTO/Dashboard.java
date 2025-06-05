package elecboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {
    private String roomId;
    private String roomName;
    private String createdAt;
    private List<Participant> userNames;
}
