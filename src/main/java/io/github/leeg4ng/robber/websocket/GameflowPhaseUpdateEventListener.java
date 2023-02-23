package io.github.leeg4ng.robber.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameflowPhaseUpdateEventListener {

    /**
     *  # None
     *  # Lobby
     *  # Matchmaking
     *  # ReadyCheck
     *  # ChampSelect
     *  # InProgress
     *  # PreEndOfGame
     *  # EndOfGame
     * @param event
     */
    @EventListener(value = ClientEvent.class, condition = "#event.type=='Update' and #event.uri=='/lol-gameflow/v1/gameflow-phase'")
    public void onGameflowPhaseUpdated(ClientEvent event) {
        log.info("gameflow phase updated, {}", event.getData());
        if (StringUtils.equals(event.getData(), "ChampSelect")) {

        }
    }

    @EventListener(value = ClientEvent.class, condition = "#event.type=='Update' and #event.uri=='/lol-champ-select/v1/pickable-champion-ids'")
    public void championsUpdated(ClientEvent event) {
        log.info("champions updated, {}", event.getData());
        if (StringUtils.equals(event.getData(), "ChampSelect")) {

        }
    }

    @EventListener(value = ClientEvent.class, condition = "#event.type=='Create' and #event.uri=='/lol-champ-select/v1/pickable-champion-ids'")
    public void championsCreated(ClientEvent event) {
        log.info("champions created, {}", event.getData());
        if (StringUtils.equals(event.getData(), "ChampSelect")) {

        }
    }
}
