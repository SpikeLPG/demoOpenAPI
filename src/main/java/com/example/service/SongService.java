package com.example.service;

import com.example.error.SongMissingDetailsException;
import com.example.error.SongNotFoundException;
import com.example.model.Song;
import com.example.model.SongRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class SongService {
    @Getter
    private List<Song> songs;

    public void deleteSong(long id) {
        if (this.songs.stream().noneMatch(song -> song.id() == id)) throw new SongNotFoundException();
        this.songs.removeIf(song -> song.id() == id);
    }

    public Song addSong(SongRequest songRequest) {
        if (songRequest.title() != null && !songRequest.title().isEmpty() && songRequest.composer() != null && !songRequest.composer().isEmpty()) {
            Song newSong = this.songs.stream()
                    .filter(it -> Objects.equals(it.title(), songRequest.title()) && Objects.equals(it.composer(), songRequest.composer()))
                    .findFirst().orElse(null);
            if (newSong == null) {
                long highestIndex = this.songs.stream().max(Comparator.comparing(Song::id)).orElse(new Song(0, "", "", Collections.emptyList())).id();
                newSong = new Song(highestIndex + 1, songRequest.title(), songRequest.composer(), songRequest.performers());
                songs.add(newSong);
            }
            return newSong;
        } else throw new SongMissingDetailsException();
    }

    public Song getSongById(long id) {
        return this.songs.stream().filter(book -> book.id() == id).findAny().orElseThrow(SongNotFoundException::new);
    }

    public void deleteAll() {
        songs.clear();
    }
}
