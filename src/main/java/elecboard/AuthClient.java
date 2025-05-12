package elecboard;

import elecboard.DTO.Auth.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;
@Slf4j
@Service
public class AuthClient {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://www.kimtuna.kr/board/api")
            .build();

    public Optional<UserInfo> getUserInfo(String token) {
        try {
            UserInfo user = webClient.get()
                    .uri("/auth/user")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .then(Mono.error(new RuntimeException("인증 실패")))
                    )
                    .bodyToMono(UserInfo.class)
                    .block();

            return Optional.ofNullable(user);

        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("예외 발생: {}", e.getMessage());
        }

        return Optional.empty();
    }
}
