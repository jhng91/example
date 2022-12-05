package org.example.daos;

import org.example.models.Show;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShowDao {
    private final Map<Integer, Show> shows;
    public ShowDao() {
        this.shows = new HashMap<Integer, Show>();
    }

    public Optional<Show> get(int number) {
        return Optional.ofNullable(shows.get(number));
    }

    public void save(Show show) {
        this.shows.put(show.getNumber(), show);
    }

}
