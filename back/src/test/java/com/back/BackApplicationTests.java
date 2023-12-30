package com.back;

import com.back.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	UserService userService;
	@Test
	void a(){
		System.out.println(userService.getUserById(1));
	}

}
