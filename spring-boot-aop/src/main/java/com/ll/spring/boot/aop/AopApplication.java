package com.ll.spring.boot.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AopApplication {

	@RequestMapping("/hello")
	public String hello() {
		return "hello spring boot";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AopApplication.class, args);
	}
}
