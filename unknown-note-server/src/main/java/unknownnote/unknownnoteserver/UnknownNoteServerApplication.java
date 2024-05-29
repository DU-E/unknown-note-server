package unknownnote.unknownnoteserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class UnknownNoteServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnknownNoteServerApplication.class, args);
	}

}
