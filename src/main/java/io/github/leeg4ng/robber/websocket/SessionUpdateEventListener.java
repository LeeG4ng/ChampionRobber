package io.github.leeg4ng.robber.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;
import io.github.leeg4ng.robber.GlobalContext;
import io.github.leeg4ng.robber.api.LeagueClientApi;
import io.github.leeg4ng.robber.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class SessionUpdateEventListener {

    @Autowired
    private LeagueClientApi leagueClientApi;

    @EventListener(value = ClientEvent.class, condition = "#event.type=='Update' and #event.uri=='/lol-champ-select/v1/session'")
    public void onSessionUpdated(ClientEvent event) {
        log.info("session updated, {}", event.getData());
        JsonNode data = JsonUtils.readTree(event.getData());
        if (data.at("/benchEnabled").asBoolean()) {
            // In Aram mode
            int myCellIndex = data.at("/localPlayerCellId").intValue() % 5;
            int currentChampionId = data.at(String.format("/myTeam/%d/championId", myCellIndex)).asInt();
            List<Integer> benchChampions = Streams.stream(data.at("/benchChampions"))
                    .map(node -> node.at("/championId").asInt())
                    .toList();
            Integer select = selectChampionByTier(currentChampionId, benchChampions);
            if (select != null) {
                leagueClientApi.aramSwapChampion(select);
            }
        }
    }

    public Integer selectChampionByTier(Integer currentChampion, List<Integer> benchChampions) {
        Set<Integer> selectable = new HashSet<>(benchChampions);
        for (List<Integer> tier : GlobalContext.aramTiers) {
            if (tier.contains(currentChampion)) {
                return null;
            }
            for (Integer want : tier) {
                if (selectable.contains(want)) {
                    return want;
                }
            }
        }
        return null;
    }
}
