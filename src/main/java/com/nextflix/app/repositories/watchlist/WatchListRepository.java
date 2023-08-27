package com.nextflix.app.repositories.watchlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.WatchList;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {
}
