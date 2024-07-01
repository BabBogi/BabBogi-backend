package BobBogi.BobBogispring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BobBogiSpringApplication {
	int a;
	int b;
	int c;
	public static void main(String[] args) {
		SpringApplication.run(BobBogiSpringApplication.class, args);
	}

}
