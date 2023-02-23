package io.github.leeg4ng.robber.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.SneakyThrows;

import java.io.File;

public class YamlUtils {

    private static final YAMLMapper YAML_MAPPER;

    static {
        YAMLMapper.Builder builder = YAMLMapper.builder();
        YAML_MAPPER = builder.build();
    }

    @SneakyThrows
    public static JsonNode readTree(String content) {
        return YAML_MAPPER.readTree(content);
    }

    @SneakyThrows
    public static JsonNode readTree(File file) {
        return YAML_MAPPER.readTree(file);
    }
}
