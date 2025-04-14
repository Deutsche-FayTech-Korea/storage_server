package elecboard.DTO.MySQL;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BoardMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String createdBy;
    private long createdAt;
    private String drawingKey; //mongodb의 _id
}
