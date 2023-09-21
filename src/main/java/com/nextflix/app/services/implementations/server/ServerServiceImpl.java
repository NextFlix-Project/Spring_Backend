package com.nextflix.app.services.implementations.server;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        
        try {
            return new ServerDto(serverRepository.findAllByType(serverType));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public ServerDto getServerByUrlAndPort(String url, String port) {

        try {
            return new ServerDto(serverRepository.findByUrlAndPort(url, port));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ServerDto> getAllServers() {

        try {
            return serverRepository.findAll(Sort.by(Sort.Order.asc("id"))).stream()
                    .map((server) -> new ServerDto(server))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public ServerDto addServer(ServerDto server) {

        try {
            Server newServer = new Server();

            newServer.setPort(server.getPort());
            newServer.setUrl(server.getUrl());
            newServer.setType(server.getServerType());

            return new ServerDto(serverRepository.save(newServer));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void removeServer(ServerDto server) {

        try {
            serverRepository.deleteById(server.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Server updateServer(ServerDto serverDto) {

        try {
            Server server = new Server();

            server.setId(serverDto.getId());
            server.setPort(serverDto.getPort());
            server.setUrl(serverDto.getUrl());
            server.setType(serverDto.getServerType());
            server.setFailedHeartbeat(serverDto.getFailedHeartbeat());

            return serverRepository.saveAndFlush(server);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
