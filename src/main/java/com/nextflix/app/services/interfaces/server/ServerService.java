package com.nextflix.app.services.interfaces.server;

import java.util.List;

import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.entities.Server;
import com.nextflix.app.enums.ServerType;

public interface ServerService {
    ServerDto getServersByType(ServerType serverType);

    ServerDto getServerByUrlAndPort(String url, String port);

    List<ServerDto> getAllServers();

    ServerDto addServer(ServerDto server);

    void removeServer(ServerDto server);

    Server updateServer(ServerDto serverDto);
}
