package io.github.leeg4ng.robber.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonMapperConfiguration {

    @Bean
    public JsonMapper jsonMapper() {
        JsonMapper.Builder builder = JsonMapper.builder();
        return builder.build();
    }
}
