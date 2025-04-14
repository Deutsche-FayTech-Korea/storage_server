package elecboard;

import elecboard.DTO.MySQL.BoardMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMetaRepository extends JpaRepository<BoardMeta, Long> {
}
