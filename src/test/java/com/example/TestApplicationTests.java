package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestApplicationTests {

    @Test
    void contextLoads() {
        String url = "/job/api/registry";
        System.out.println(url.matches(".+job.+"));
    }

}
