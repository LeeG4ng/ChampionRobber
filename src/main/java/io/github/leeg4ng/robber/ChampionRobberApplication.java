package io.github.leeg4ng.robber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ChampionRobberApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChampionRobberApplication.class, args);
    }

}
