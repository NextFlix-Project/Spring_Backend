package com.nextflix.app.services.implementations.watchlist;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.dtos.watchlist.WatchListDto;
import com.nextflix.app.entities.Movie;
import com.nextflix.app.entities.User;
import com.nextflix.app.entities.WatchList;
import com.nextflix.app.repositories.watchlist.WatchListRepository;
import com.nextflix.app.services.interfaces.watchlist.WatchListService;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    WatchListRepository watchListRepository;

    @Override
    public void addToWatchlist(MovieDto movieDto, UserDto userDto) throws Exception {
        try {
            WatchList watchList = new WatchList();
            watchList.setMovie(new Movie(movieDto));
            watchList.setUser(new User(userDto));

            watchListRepository.save(watchList);

        } catch (Exception e) {
            throw new Exception("Error adding to watch list");

        }
    }

    @Override
    public void removeFromWatchList(WatchListDto watchList) throws Exception {
        try {

            watchListRepository.deleteById(watchList.getId());

        } catch (Exception e) {
            throw new Exception("Error deleting watch list item");

        }
    }
    public WatchListDto findById(Long id) throws Exception {
       try {

            Optional<WatchList> watchList = watchListRepository.findById(id);
            if (watchList.isPresent())
                return new WatchListDto(watchList.get());
            

        } catch (Exception e) {
            throw new Exception("Error finding watch list item");

        }

        return null;
    }

}
