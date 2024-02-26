package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("phqh"));
		System.out.println(passwordEncoder.encode("pbb"));
		System.out.println(passwordEncoder.encode("pb"));
		System.out.println(passwordEncoder.encode("pa"));


	}

}
