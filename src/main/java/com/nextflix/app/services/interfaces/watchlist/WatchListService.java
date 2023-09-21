package com.nextflix.app.services.interfaces.watchlist;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.dtos.watchlist.WatchListDto;

public interface WatchListService {
    void addToWatchlist(MovieDto movieDto, UserDto userDto) throws Exception;

    WatchListDto findById(Long id) throws Exception;

    void removeFromWatchList(WatchListDto watchList) throws Exception;
}
