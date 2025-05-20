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
@RequestMapping("/board/s3/api")
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
        //Optional<UserInfo> userOpt = authClient.getUserInfo(token);
        Optional<UserInfo> userOpt = authClient.getUserInfo(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        PageDocument saved = boardService.saveOrUpdatePage(page);
        return ResponseEntity.ok("Saved successfully");
    }

    @GetMapping("/page")
    public ResponseEntity<?> getBoardsByUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        //
        log.info("요청 들어온 Authorization 헤더: {}", authHeader);
        //
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or malformed Authorization header");
        }
        String token = authHeader.substring(7);
        //
        log.info("파싱된 토큰: {}", token);
        //

        Optional<UserInfo> userOpt = authClient.getUserInfo(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        //토큰에서 유저 이름 추출
        String userName = userOpt.get().getName();
        List<Dashboard> result = boardService.getBoardsByUser(userName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/page/{roomId}")
    public ResponseEntity<?> getPage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String roomId
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or malformed Authorization header");
        }

        String token = authHeader.substring(7);
        Optional<UserInfo> userOpt = authClient.getUserInfo(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }

        String userName = userOpt.get().getName();

        Optional<Page> pageOpt = boardService.getPageByRoomId(roomId, userName);
        if (pageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have access to this page");
        }

        return ResponseEntity.ok(pageOpt.get());
    }

//    @GetMapping("/page/{userName}/{roomId}")
//    public ResponseEntity<?> getPage(
//            @PathVariable String userName,
//            @PathVariable String roomId
//    ) {
//        Optional<Page> pageOpt = boardService.getPageByRoomId(roomId, userName);
//        if (pageOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("You do not have access to this page");
//        }
//
//        return ResponseEntity.ok(pageOpt.get());
//    }

    @DeleteMapping("/page/{roomId}")
    public ResponseEntity<?> deletePage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String roomId
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or malformed Authorization header");
        }

        String token = authHeader.substring(7);
        Optional<UserInfo> userOpt = authClient.getUserInfo(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }

        String userName = userOpt.get().getName();

        boolean deleted = boardService.deletePageByRoomId(roomId, userName);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission or page does not exist.");
        }

        return ResponseEntity.ok("Deleted successfully");
    }

//    @DeleteMapping("/page/{userName}/{roomId}")
//    public ResponseEntity<?> deletePage(
//            @PathVariable String userName,
//            @PathVariable String roomId
//    ) {
//
//
//        boolean deleted = boardService.deletePageByRoomId(roomId, userName);
//        if (!deleted) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("You do not have permission or page does not exist.");
//        }
//
//        return ResponseEntity.ok("Deleted successfully");
//    }
}
