package com.example.controller;

import com.example.service.SongService;
import com.example.model.SongRequest;
import com.example.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(SongController.API_URL)
public class SongController {
    public static final String API_URL = "/api";
    public static final String SONGS_ROOT = "/songs";
    public static final String SONG_BY_ID = SONGS_ROOT + "/{id}";

    SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping(SONGS_ROOT)
    public ResponseEntity<List<Song>>
    getSongs() {
        return new ResponseEntity<>(songService.getSongs(), HttpStatus.OK);
    }

    @PostMapping(SONGS_ROOT)
    public ResponseEntity<Song> addSong(@RequestBody SongRequest songRequest) {
        return new ResponseEntity<>(songService.addSong(songRequest), HttpStatus.OK);
    }

    @GetMapping(SONG_BY_ID)
    public ResponseEntity<Song> getSongById(@PathVariable long id) {
        return new ResponseEntity<>(songService.getSongById(id), HttpStatus.OK);
    }

    @DeleteMapping("/internal" + SONG_BY_ID)
    public void deleteSong(@PathVariable long id) {
        songService.deleteSong(id);
    }
}
