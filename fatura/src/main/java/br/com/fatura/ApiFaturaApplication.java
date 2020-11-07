package br.com.fatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiFaturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiFaturaApplication.class, args);
	}

}
