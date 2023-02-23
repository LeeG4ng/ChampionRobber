package io.github.leeg4ng.robber;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;
import io.github.leeg4ng.robber.api.LeagueClientApi;
import io.github.leeg4ng.robber.util.ClientUtils;
import io.github.leeg4ng.robber.util.YamlUtils;
import io.github.leeg4ng.robber.websocket.WebsocketClient;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Timer;

@Slf4j
@Component
public class ConnectionManager {

    @Autowired
    private LeagueClientApi leagueClientApi;


//    @PostConstruct
//    public void init() {
//        new Timer().s() -> {
//            while (true) {
//                GlobalContext.connectionLock.lock();
//                try {
//                    connect();
//                } catch (ClientUtils.ClientNotFoundException e) {
//                    log.info("client not found");
//                } catch (Exception e) {
//                    log.error("connect error", e);
//                }
//
//                Thread.sleep(Duration.ofSeconds(5).toMillis());
//            }
//        }).start();
//    }

    @Scheduled(fixedDelayString = "#{T(java.time.Duration).ofSeconds(5)}")
    public void detectClient() throws InterruptedException {
        GlobalContext.connectionSemaphore.acquire();
        try {
            connect();
        } catch (ClientUtils.ClientNotFoundException e) {
            log.info("client not found");
            GlobalContext.connectionSemaphore.release();
        } catch (Exception e) {
            log.error("connect error", e);
            GlobalContext.connectionSemaphore.release();
        }
    }

    public void connect() throws ClientUtils.ClientNotFoundException, URISyntaxException {
        ClientUtils.ClientInfo clientInfo = ClientUtils.getClientInfo();
        ClientUtils.currentClientInfo = clientInfo;

        // getChampionIdMap
        GlobalContext.championIdMap = leagueClientApi.getChampionIdMap();
        log.info(GlobalContext.championIdMap.toString());

        // convert aramWantChampionIds
        JsonNode config = YamlUtils.readTree(new File("./config.yml"));
        JsonNode champions = config.at("/aram/champions");
        GlobalContext.aramTiers = Streams.stream(champions)
                .map(JsonNode::asText)
                .map(tier -> tier.split("\\s*(,|，|\\s+)\\s*"))
                .map(Arrays::asList)
                .map(tier -> tier.stream().map(GlobalContext.championIdMap::get).toList())
                .toList();

        // connect ws
        String path = String.format("wss://riot:%s@127.0.0.1:%s/", clientInfo.getToken(), clientInfo.getPort());
        log.info(path);
        WebsocketClient client = new WebsocketClient(new URI(path));
        client.sendMessage("[5, \"OnJsonApiEvent\"]");
    }
}
