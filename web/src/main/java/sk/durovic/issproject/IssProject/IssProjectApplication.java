package sk.durovic.issproject.IssProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "sk.durovic")
@ComponentScan("sk.durovic")
@EntityScan("sk.durovic.model")
@EnableJpaRepositories("sk.durovic.service")
public class IssProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssProjectApplication.class, args);
	}

}
