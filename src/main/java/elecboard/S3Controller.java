package elecboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/uploadImage")
    public ResponseEntity<String> saveFile(
            @RequestParam("fileName") MultipartFile file
    ) {
        String url = s3Service.uploadFile(file);
        return ResponseEntity.ok("Uploaded to: " + url);
    }
}
