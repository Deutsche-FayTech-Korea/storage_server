package elecboard;

import elecboard.DTO.Auth.UserInfo;
import elecboard.DTO.Dashboard;
import elecboard.DTO.Page;
import elecboard.DTO.PageDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/board/s3/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final AuthClient authClient;

    // === 공통 메서드 ===

    private String extractTokenFromCookie(String cookieHeader, String tokenName) {
        if (cookieHeader == null) return null;
        for (String cookie : cookieHeader.split(";")) {
            cookie = cookie.trim();
            if (cookie.startsWith(tokenName + "=")) {
                return cookie.substring((tokenName + "=").length());
            }
        }
        return null;
    }

    private Optional<UserInfo> validateTokenAndGetUser(String cookieHeader) {
        String accessToken = extractTokenFromCookie(cookieHeader, "access_token");
        if (accessToken == null) return Optional.empty();
        return authClient.getUserInfo(accessToken);
    }


    // === API 구현 ===

    @PostMapping("/save")
    public ResponseEntity<String> savePage(
            @RequestHeader("Cookie") String cookieHeader,
            @RequestBody Page page
    ) {

        Optional<UserInfo> userOpt = validateTokenAndGetUser(cookieHeader);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        boardService.saveOrUpdatePage(page);
        return ResponseEntity.ok()
                .body("Saved successfully");
    }

    @GetMapping("/page")
    public ResponseEntity<?> getBoardsByUser(@RequestHeader("Cookie") String cookieHeader) {


        Optional<UserInfo> userOpt = validateTokenAndGetUser(cookieHeader);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String userName = userOpt.get().getName();
        List<Dashboard> result = boardService.getBoardsByUser(userName);

        return ResponseEntity.ok()
                .body(result);
    }

    @GetMapping("/page/{roomId}")
    public ResponseEntity<?> getPage(
            @RequestHeader("Cookie") String cookieHeader,
            @PathVariable String roomId
    ) {


        Optional<UserInfo> userOpt = validateTokenAndGetUser(cookieHeader);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String userName = userOpt.get().getName();
        Optional<Page> pageOpt = boardService.getPageByRoomId(roomId, userName);
        if (pageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this page");
        }

        return ResponseEntity.ok()
                .body(pageOpt.get());
    }

    @DeleteMapping("/page/{roomId}")
    public ResponseEntity<?> deletePage(
            @RequestHeader("Cookie") String cookieHeader,
            @PathVariable String roomId
    ) {


        Optional<UserInfo> userOpt = validateTokenAndGetUser(cookieHeader);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String userName = userOpt.get().getName();
        boolean deleted = boardService.deletePageByRoomId(roomId, userName);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission or page does not exist.");
        }

        return ResponseEntity.ok()
                .body("Deleted successfully");
    }
}