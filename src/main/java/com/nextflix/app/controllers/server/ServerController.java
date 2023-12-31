package com.nextflix.app.controllers.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.services.interfaces.server.ServerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @PostMapping("/internal/registerserver")
    public ResponseEntity<?> addServer(@RequestBody ServerDto server, HttpServletRequest request) {

        try{
        String ipAddr = request.getRemoteAddr();

        ServerDto foundServer = serverService.getServerByUrlAndPort(ipAddr, server.getPort());

        if (foundServer.getPort() != null || foundServer.getUrl() != null)
            return ResponseEntity.status(409).build();

        server.setUrl(ipAddr);

        serverService.addServer(server);
        return ResponseEntity.status(200).build();
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getallservers")
    public ResponseEntity<?> getAllServers() {

        try {
            List<ServerDto> servers = serverService.getAllServers();

            return ResponseEntity.status(200).body(servers);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return ResponseEntity.status(404).build();
    }
}
