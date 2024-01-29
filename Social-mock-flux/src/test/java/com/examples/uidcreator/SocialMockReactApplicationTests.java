package com.examples.uidcreator;

import com.examples.uidcreator.controller.Employee;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@SpringBootTest
class SocialMockReactApplicationTests {

	@Test
	void contextLoads() {
	}


}
