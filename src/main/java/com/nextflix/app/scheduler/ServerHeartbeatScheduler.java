package com.nextflix.app.scheduler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.services.interfaces.server.ServerService;
@Component
@EnableAsync
public class ServerHeartbeatScheduler {

    @Value("${video_server_apikey}")
    private String validApiKey;

    @Autowired
    private ServerService serverService;

    @Autowired
    private WebClient webClient;

    @Async
    @Scheduled(fixedDelay = 5000)
    public void checkHeartbeats() throws URISyntaxException {

        List<ServerDto> servers = new ArrayList<>();

        try {
            servers = serverService.getAllServers();
        } catch (Exception e) {
            return;
        }
        for (ServerDto server : servers) {

            try {
                webClient.get()
                        .uri(new URI("http://" + server.getUrl() + ":" + server.getPort() + "/heartbeat"))
                        .header("Authorization", validApiKey)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                if (server.getFailedHeartbeat() > 0) {
                    server.setFailedHeartbeat(0);
                    serverService.updateServer(server);
                }

            } catch (Exception e) {

                server.setFailedHeartbeat(server.getFailedHeartbeat() + 1);

                if (server.getFailedHeartbeat() < 12)
                    serverService.updateServer(server);
                else
                    serverService.removeServer(server);
            }
        }
    }
}