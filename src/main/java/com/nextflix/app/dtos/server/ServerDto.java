package com.nextflix.app.dtos.server;

import java.util.Optional;

import com.nextflix.app.entities.Server;
import com.nextflix.app.enums.ServerType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerDto {

    private Long id;
    private String url;
    private String port;
    private int failedHeartbeat;
    private ServerType serverType;

    public ServerDto(Server server) {
        this.id = server.getId();
        this.url = server.getUrl();
        this.port = server.getPort();
        this.failedHeartbeat = server.getFailedHeartbeat();
        this.serverType = server.getType();
    }

    public ServerDto(Optional<Server> server) {
        server.ifPresent(srv -> {
            this.id = srv.getId() != null ? srv.getId() : null;
            this.url = srv.getUrl() != null ? srv.getUrl() : null;
            this.port = srv.getPort() != null ? srv.getPort() : null;
            this.failedHeartbeat = srv.getFailedHeartbeat();
            this.serverType = srv.getType() != null ? srv.getType() : null;
        });
    }
}
