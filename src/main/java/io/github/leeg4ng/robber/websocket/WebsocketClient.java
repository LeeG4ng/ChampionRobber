package io.github.leeg4ng.robber.websocket;

import javax.net.ssl.SSLContext;
import javax.websocket.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.leeg4ng.robber.GlobalContext;
import io.github.leeg4ng.robber.util.AppUtils;
import io.github.leeg4ng.robber.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tomcat.websocket.Constants;
import org.apache.tomcat.websocket.pojo.PojoEndpointClient;

import java.net.URI;
import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ClientEndpoint
public class WebsocketClient {

    private Session session = null;

    public WebsocketClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.setDefaultMaxBinaryMessageBufferSize(5120000);
            container.setDefaultMaxTextMessageBufferSize(5120000);

            ClientEndpointConfig clientConfig = createClientConfig();
            Pattern pattern = Pattern.compile("([\\w-]*):([\\w-]*)");
            Matcher matcher = pattern.matcher(endpointURI.getUserInfo());
            if (matcher.find()) {
                clientConfig.getUserProperties().put(Constants.WS_AUTHENTICATION_USER_NAME, matcher.group(1));
                clientConfig.getUserProperties().put(Constants.WS_AUTHENTICATION_PASSWORD, matcher.group(2));
            }
            container.connectToServer(new PojoEndpointClient(this, new ArrayList<>()), clientConfig, endpointURI);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        log.warn("opening websocket");
        this.session = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        log.warn("closing websocket, reason: {}", reason);
        this.session = null;
        GlobalContext.connectionSemaphore.release();
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        JsonNode root = JsonUtils.readTree(message);
        String type = root.at("/2/eventType").asText();
        String uri = root.at("/2/uri").asText();
        String data = root.at("/2/data").toString();
//        log.info("{}: {}\n{}", type, uri, message);

        ClientEvent clientEvent = new ClientEvent(this, type, uri, data);
        AppUtils.applicationContext.publishEvent(clientEvent);
    }

    @OnMessage
    public void onMessage(ByteBuffer bytes) {
        System.out.println("Handle byte buffer");
    }



    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }


    private ClientEndpointConfig createClientConfig() throws Exception {
        ClientEndpointConfig.Builder builder = ClientEndpointConfig.Builder.create();
        ClientEndpointConfig config = builder.decoders(new ArrayList<>()).encoders(new ArrayList<>())
                .preferredSubprotocols(new ArrayList<>()).build();
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        config.getUserProperties().put(Constants.SSL_CONTEXT_PROPERTY, sslContext);
        return config;
    }
}
