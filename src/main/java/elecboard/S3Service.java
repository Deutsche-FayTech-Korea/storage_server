package elecboard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

        return amazonS3.getUrl(bucket, fileName).toString(); // 업로드된 파일의 S3 URL 반환
    }

    public void deleteByUrl(String url) {
        try {
            String key = extractKeyFromUrl(url);
            amazonS3.deleteObject(bucket, key);
            System.out.println("✅ S3에서 삭제 완료: " + key);
        } catch (Exception e) {
            System.err.println("❌ S3 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * S3 URL에서 오브젝트 키 추출
     * 예: https://your-bucket.s3.amazonaws.com/folder/image.png → folder/image.png
     */
    private String extractKeyFromUrl(String url) {
        String bucketUrlPrefix = "https://" + bucket + ".s3.amazonaws.com/";
        if (url.startsWith(bucketUrlPrefix)) {
            return url.substring(bucketUrlPrefix.length());
        } else {
            throw new IllegalArgumentException("URL이 예상 형식과 다릅니다: " + url);
        }
    }
}