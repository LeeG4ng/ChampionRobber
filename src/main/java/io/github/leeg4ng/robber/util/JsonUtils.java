package io.github.leeg4ng.robber.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;

public class JsonUtils {

    private static final JsonMapper JSON_MAPPER;

    static {
        JsonMapper.Builder builder = JsonMapper.builder();
        JSON_MAPPER = builder.build();
    }

    @SneakyThrows
    public static JsonNode readTree(String content) {
        return JSON_MAPPER.readTree(content);
    }
}
