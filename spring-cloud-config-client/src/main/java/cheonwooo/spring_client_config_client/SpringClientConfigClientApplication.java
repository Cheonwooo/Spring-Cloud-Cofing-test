package cheonwooo.spring_client_config_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(MyConfig.class)
@EnableScheduling
public class SpringClientConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringClientConfigClientApplication.class, args);
	}

}
