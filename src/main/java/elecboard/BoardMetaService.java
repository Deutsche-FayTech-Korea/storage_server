package elecboard;

import lombok.RequiredArgsConstructor;
import elecboard.DTO.MySQL.BoardMeta;
import elecboard.DTO.PageDocument;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardMetaService {

    private final BoardMetaRepository boardMetaRepository;

    public Long save(PageDocument saved){
        BoardMeta meta = new BoardMeta();
        meta.setDrawingKey(saved.getId());
        boardMetaRepository.save(meta);

        return meta.getId();
    }
}
