package com.example.egerdon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class EgerdonApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(">>> context loaded successfully!");
	}

}
