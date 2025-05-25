package elecboard;

import elecboard.DTO.Auth.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/s3/api")
public class S3Controller {

    private final S3Service s3Service;
    private final AuthClient authClient;

    @PostMapping("/uploadImage")
    public ResponseEntity<String> saveFile(
            @RequestHeader("Cookie") String cookieHeader,
            @RequestParam("fileName") MultipartFile file
    ) {
        Optional<UserInfo> userOpt = validateTokenAndGetUser(cookieHeader);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        String url = s3Service.uploadFile(file);
        return ResponseEntity.ok("Uploaded to: " + url);
    }

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
}
