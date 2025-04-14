package elecboard;

import lombok.RequiredArgsConstructor;
import elecboard.DTO.Page;
import elecboard.DTO.PageDocument;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public PageDocument savePage(Page page){

        PageDocument doc = new PageDocument();
        doc.setCreatedAt(page.getCreatedAt());
        doc.setObjects(page.getObjects());
        doc.setCreatedBy(page.getCreatedBy());

        return boardRepository.save(doc);
    }

    //id가 없는 값을 경우 null이므로 Optional
    public Optional<PageDocument> findPageById(String id) {
        return boardRepository.findById(id);
    }
}
