package com.nextflix.app.services.implementations.server;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.entities.Server;
import com.nextflix.app.enums.ServerType;
import com.nextflix.app.repositories.server.ServerRepository;
import com.nextflix.app.services.interfaces.server.ServerService;

@Service
public class ServerServiceImpl implements ServerService {
    @Autowired
    ServerRepository serverRepository;

    @Override
    public ServerDto getServersByType(ServerType serverType) {
        return new ServerDto(serverRepository.findAllByType(serverType));
    }

    @Override
    public ServerDto getServerByUrlAndPort(String url, String port) {
        return new ServerDto(serverRepository.findByUrlAndPort(url, port));
    }

    @Override
    public List<ServerDto> getAllServers() {
        return serverRepository.findAll().stream().map((server) -> new ServerDto(server)).collect(Collectors.toList());
    }

    @Override
    public ServerDto addServer(ServerDto server) {
        Server newServer = new Server();

        newServer.setPort(server.getPort());
        newServer.setUrl(server.getUrl());
        newServer.setType(server.getServerType());

        return new ServerDto(serverRepository.save(newServer));

    }

    @Override
    public void removeServer(ServerDto server) {
        serverRepository.deleteById(server.getId());
    }

    @Override
    public Server updateServer(ServerDto serverDto) {

        Server server = new Server();

        server.setId(serverDto.getId());
        server.setPort(serverDto.getPort());
        server.setUrl(serverDto.getUrl());
        server.setType(serverDto.getServerType());
        server.setFailedHeartbeat(serverDto.getFailedHeartbeat());

        return serverRepository.saveAndFlush(server);
    }
}
