package elecboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {
    private String roomId;
    private String roomName;
    private String mode;
    private MadeBy madeBy;
    private LocalDateTime createdAt;
    private List<Participant> participants;
}
