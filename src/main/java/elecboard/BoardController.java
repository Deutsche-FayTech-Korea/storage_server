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
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final AuthClient authClient;

    // 공통: 쿠키에서 token 추출
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

    @PostMapping("/save")
    public ResponseEntity<String> savePage(
            @RequestHeader("Cookie") String cookieHeader,
            @RequestBody Page page
    ) {
        String accessToken = extractTokenFromCookie(cookieHeader, "access_token");
        String refreshToken = extractTokenFromCookie(cookieHeader, "refresh_token");

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing access token in cookies");
        }

        Optional<UserInfo> userOpt = authClient.getUserInfo(accessToken);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        boardService.saveOrUpdatePage(page);

        // 응답 시 access_token과 refresh_token 다시 세팅
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken != null ? refreshToken : "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString(), accessCookie.toString())
                .body("Saved successfully");
    }

    @GetMapping("/page")
    public ResponseEntity<?> getBoardsByUser(@RequestHeader("Cookie") String cookieHeader) {
        String accessToken = extractTokenFromCookie(cookieHeader, "access_token");
        String refreshToken = extractTokenFromCookie(cookieHeader, "refresh_token");

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing access token in cookies");
        }

        Optional<UserInfo> userOpt = authClient.getUserInfo(accessToken);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String userName = userOpt.get().getName();
        List<Dashboard> result = boardService.getBoardsByUser(userName);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken != null ? refreshToken : "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString(), accessCookie.toString())
                .body(result);
    }

    @GetMapping("/page/{roomId}")
    public ResponseEntity<?> getPage(
            @RequestHeader("Cookie") String cookieHeader,
            @PathVariable String roomId
    ) {
        String accessToken = extractTokenFromCookie(cookieHeader, "access_token");
        String refreshToken = extractTokenFromCookie(cookieHeader, "refresh_token");

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing access token in cookies");
        }

        Optional<UserInfo> userOpt = authClient.getUserInfo(accessToken);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String userName = userOpt.get().getName();
        Optional<Page> pageOpt = boardService.getPageByRoomId(roomId, userName);
        if (pageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this page");
        }

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken != null ? refreshToken : "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString(), accessCookie.toString())
                .body(pageOpt.get());
    }

    @DeleteMapping("/page/{roomId}")
    public ResponseEntity<?> deletePage(
            @RequestHeader("Cookie") String cookieHeader,
            @PathVariable String roomId
    ) {
        String accessToken = extractTokenFromCookie(cookieHeader, "access_token");
        String refreshToken = extractTokenFromCookie(cookieHeader, "refresh_token");

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing access token in cookies");
        }

        Optional<UserInfo> userOpt = authClient.getUserInfo(accessToken);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String userName = userOpt.get().getName();
        boolean deleted = boardService.deletePageByRoomId(roomId, userName);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission or page does not exist.");
        }

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken != null ? refreshToken : "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString(), accessCookie.toString())
                .body("Deleted successfully");
    }
}
