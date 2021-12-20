package br.com.apiintegracao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ApiIntegracaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiIntegracaoApplication.class, args);
	}

}
