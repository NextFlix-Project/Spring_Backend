package com.nextflix.app.repositories.server;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nextflix.app.entities.Server;
import com.nextflix.app.enums.ServerType;

public interface ServerRepository extends JpaRepository<Server, Long> {
    Server findAllByType(ServerType type);

    @Query("SELECT s FROM Server s WHERE s.url = :url AND s.port = :port")
    Optional<Server> findByUrlAndPort(@Param("url") String url, @Param("port") String port);
}
