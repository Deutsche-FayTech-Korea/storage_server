package elecboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BoardTestApplication {

	public static void main(String[] args) {

		SpringApplication.run(BoardTestApplication.class, args);
	}

}
