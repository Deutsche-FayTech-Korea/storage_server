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
@RequestMapping
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardMetaService boardMetaService;

    @PostMapping("/api/save")
    public ResponseEntity<String> savePage(@RequestBody Page page){
        PageDocument saved = boardService.savePage(page);
        boardMetaService.save(saved);
        log.info("saved");
        return ResponseEntity.ok("Saved to MongoDB");
    }

    //page를 찾은 경우 PageDocument값을 Page에 담아서 외부에 전송
    //찾았다면 200ok, 아니라면 notFound
    @GetMapping("/api/{id}")
    public ResponseEntity<Page> getPage(@PathVariable String id, Pageable pageable){

        return boardService.findPageById(id)
                .map(doc -> {
                    Page page = new Page();
                    page.setCreatedAt(doc.getCreatedAt());
                    page.setCreatedBy(doc.getCreatedBy());
                    page.setObjects(doc.getObjects());
                    return page;
                })
                .map(page-> {
                    return ResponseEntity.ok(page);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
