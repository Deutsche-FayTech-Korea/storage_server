package elecboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/savefile")
    public ResponseEntity<String> saveFile(
            @RequestParam("file") MultipartFile file,
            //@RequestParam("userId") String userId,
            @RequestParam("contentKey") String contentKey
    ) {
        String url = s3Service.uploadFile(file);
        return ResponseEntity.ok("Uploaded to: " + url);
    }

//    @DeleteMapping("/deletefile")
//    public ResponseEntity<String> deleteFile(@RequestParam String fileName){
//        s3Service.deleteFile(fileName);
//        return ResponseEntity.ok("삭제 완료: " + fileName);
//    }
}
