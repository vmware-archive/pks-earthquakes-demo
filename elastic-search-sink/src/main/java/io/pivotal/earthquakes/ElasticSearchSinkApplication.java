package io.pivotal.earthquakes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticSearchSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticSearchSinkApplication.class, args);
	}
}
