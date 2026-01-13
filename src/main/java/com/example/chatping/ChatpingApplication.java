package com.example.chatping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ChatpingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatpingApplication.class, args);
	}

}
