package elecboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import elecboard.DTO.Page;
import elecboard.DTO.PageDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

//    @PostMapping("/save/{roomId}")
//    public ResponseEntity<String> savePage(@RequestBody Page page){
//        boardService.savePage(page);
//        return ResponseEntity.ok("Saved successfully");
//    }

    @PostMapping("/save")
    public ResponseEntity<String> savePage(@RequestBody Page page) {
        PageDocument saved = boardService.saveOrUpdatePage(page);
        return ResponseEntity.ok("Saved successfully");
    }
}
