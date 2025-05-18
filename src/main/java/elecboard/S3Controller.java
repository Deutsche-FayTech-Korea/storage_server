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
@RequestMapping("/api")
public class S3Controller {

    private final S3Service s3Service;
    private final AuthClient authClient;

    @PostMapping("/uploadImage")
    public ResponseEntity<String> saveFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("fileName") MultipartFile file
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or malformed Authorization header");
        }
        String token = authHeader.substring(7); // "Bearer " 제거 (공백 포함해서 7글자)

        //토큰이 유효하지 않을 수도 있음
        Optional<UserInfo> userOpt = authClient.getUserInfo(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        String url = s3Service.uploadFile(file);
        return ResponseEntity.ok("Uploaded to: " + url);
    }
}
