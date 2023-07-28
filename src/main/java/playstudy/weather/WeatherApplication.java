package playstudy.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement	// weather 어플 (프로젝트) 안에서 트랜잭션을 사용할 수 있게 됨!
@EnableScheduling	// weather 어플 (프로젝트)안에서 스케줄링을 사용할 수 있게 됨!
public class WeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherApplication.class, args);

	}

}

