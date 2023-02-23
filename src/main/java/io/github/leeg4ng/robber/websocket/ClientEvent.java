package io.github.leeg4ng.robber.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.leeg4ng.robber.util.JsonUtils;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ClientEvent extends ApplicationEvent {

    private final String type;

    private final String uri;

    private final String data;

    public ClientEvent(Object source, String type, String uri, String data) {
        super(source);
        this.type = type;
        this.uri = uri;
        this.data = data;
    }
}
