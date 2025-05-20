package mx.dgtic.unam.simin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SiminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiminApplication.class, args);
	}

}
