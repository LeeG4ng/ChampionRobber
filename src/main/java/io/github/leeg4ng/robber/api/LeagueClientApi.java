package io.github.leeg4ng.robber.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;
import io.github.leeg4ng.robber.util.ClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LeagueClientApi {

    @Autowired
    private RestTemplate restTemplate;

    public String getBaseUrl() {
        return String.format("https://riot:%s@127.0.0.1:%s",
                ClientUtils.currentClientInfo.getToken(), ClientUtils.currentClientInfo.getPort());
    }

    public Map<String, Integer> getChampionIdMap() {
        JsonNode champions = restTemplate.getForObject(getBaseUrl() + "/lol-game-data/assets/v1/champion-summary.json", JsonNode.class);
        return Streams.stream(champions).collect(Collectors.toMap(node -> node.get("name").asText(), node -> node.get("id").asInt()));
    }

    public void aramSwapChampion(int championId) {
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(getBaseUrl() + "/lol-champ-select/v1/session/bench/swap/{championId}", null, Object.class, championId);
        log.info("request aramSwapChampion, res:{}", responseEntity);
    }
}
