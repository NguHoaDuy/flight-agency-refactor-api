package vn.nhd.flightagency.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "vn.nhd.flightagency.*.domain")
@ComponentScan(basePackages = "vn.nhd.flightagency")
@EnableJpaRepositories(basePackages = "vn.nhd.flightagency.*.repository")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
