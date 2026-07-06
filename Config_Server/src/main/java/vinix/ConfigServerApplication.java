package vinix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication implements CommandLineRunner{

	@Value("${GIT_USERNAME}")
	private String name;
	
	@Value("${GIT_PASSWORD}")
	private String password;
	
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*System.out.println("nome: " + name);
		System.out.println("password: " + password);
		*/
	}

}
