//package elecboard;
//import elecboard.DTO.Auth.UserInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.User;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@Service
//public class AuthClient {



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

    public Optional<UserInfo> getUserInfo(String token) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.kimtuna.kr/board/api/auth/user")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader("Cookie", "access_token=" + token + "; refresh_token=dummy")
                .build();

        try {
            log.info("WebClient Authorization: Bearer {}", token);
            log.info("WebClient Cookie: access_token={}, refresh_token=dummy", token);

            UserInfo user = webClient.get()
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .doOnNext(body -> log.error("인증 서버 응답 오류: {}", body))
                                    .then(Mono.error(new RuntimeException("인증 실패")))
                    )
                    .bodyToMono(UserInfo.class)
                    .block();

            log.info("인증된 사용자 정보: {}", user);
            return Optional.ofNullable(user);

        } catch (WebClientResponseException e) {
            log.error("WebClientResponseException: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("예외 발생: {}", e.getMessage());
        }

        return Optional.empty();
    }
}
