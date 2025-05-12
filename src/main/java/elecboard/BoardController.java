package elecboard;

import elecboard.DTO.Auth.UserInfo;
import elecboard.DTO.Dashboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import elecboard.DTO.Page;
import elecboard.DTO.PageDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final AuthClient authClient;

    @PostMapping("/save")
    public ResponseEntity<String> savePage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Page page
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or malformed Authorization header");
        }
        String token = authHeader.substring(7); // "Bearer " 제거 (공백 포함해서 7글자)

        //토큰이 유효하지 않을 수도 있음
        Optional<UserInfo> userOpt = authClient.getUserInfo(token);
        if (userOpt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        PageDocument saved = boardService.saveOrUpdatePage(page);
        return ResponseEntity.ok("Saved successfully");
    }

    @GetMapping("/page/{userName}")
    public ResponseEntity<List<Dashboard>> getBoardsByUser(@PathVariable String userName) {
        List<Dashboard> result = boardService.getBoardsByUser(userName);
        return ResponseEntity.ok(result);
    }
}
