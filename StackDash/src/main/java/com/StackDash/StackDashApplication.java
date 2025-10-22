package com.StackDash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.StackDash", "com.other.package"})
public class StackDashApplication {

	public static void main(String[] args) {
		SpringApplication.run(StackDashApplication.class, args);
	}

}
