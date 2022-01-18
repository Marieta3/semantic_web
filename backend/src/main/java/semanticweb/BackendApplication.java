package semanticweb;

import org.apache.log4j.varia.NullAppender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {

		org.apache.log4j.BasicConfigurator.configure(new NullAppender());
		SpringApplication.run(BackendApplication.class, args);
	}

}
