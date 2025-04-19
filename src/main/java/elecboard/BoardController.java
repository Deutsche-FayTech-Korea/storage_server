package elecboard;

import elecboard.DTO.Dashboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import elecboard.DTO.Page;
import elecboard.DTO.PageDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/save")
    public ResponseEntity<String> savePage(@RequestBody Page page) {
        PageDocument saved = boardService.saveOrUpdatePage(page);
        return ResponseEntity.ok("Saved successfully");
    }

    @GetMapping("/page/{userName}")
    public ResponseEntity<List<Dashboard>> getBoardsByUser(@PathVariable String userName) {
        List<Dashboard> result = boardService.getBoardsByUser(userName);
        return ResponseEntity.ok(result);
    }
}
