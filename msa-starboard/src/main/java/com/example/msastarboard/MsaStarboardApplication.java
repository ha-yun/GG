package com.example.msastarboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing 활성화
public class MsaStarboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsaStarboardApplication.class, args);
    }

}
