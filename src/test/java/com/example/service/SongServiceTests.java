package com.example.service;

import com.example.DemoApplication;
import com.example.model.Performer;
import com.example.model.Song;
import com.example.model.SongRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
class SongServiceTests {
    @Autowired
    private SongService service;

    @AfterEach
    public void teardown() {
        service.deleteAll();
    }

    @DisplayName("All songs can be deleted")
    @Test
    void shouldRemoveAllSongs() {
        service.addSong(new SongRequest("Fake Song", "Fake composer", new ArrayList<>()));
        assertThat(service.getSongs()).hasSize(1);
        service.deleteAll();
        assertThat(service.getSongs()).isEmpty();
    }


    @DisplayName("A song can be deleted")
    @Test
    void shouldRemoveSongById() {
        service.addSong(new SongRequest("Fake Song", "Fake composer", new ArrayList<>()));
        service.addSong(new SongRequest("Fake Song 2", "Fake composer", new ArrayList<>()));
        List<Song> songs = service.getSongs();
        service.deleteSong(songs.get(0).id());
        assertThat(service.getSongs()).hasSize(1);
        assertThat(songs.get(0).title()).isEqualTo("Fake Song 2");
    }

    @DisplayName("An empty list of existing songs is returned")
    @Test
    void shouldReturnAnEmptyListOfSongs() {
        List<Song> songs = service.getSongs();
        assertThat(songs).isEmpty();
    }

    @DisplayName("A list of a single song is returned")
    @Test
    void shouldReturnAListOfOneSong() {
        service.addSong(new SongRequest("Fake Song", "Fake composer", new ArrayList<>()));
        List<Song> songs = service.getSongs();
        assertThat(songs).hasSize(1);
        assertThat(songs.get(0).id()).isInstanceOf(Long.class);
        assertThat(songs.get(0).title()).isEqualTo("Fake Song");
        assertThat(songs.get(0).composer()).isEqualTo("Fake composer");
        assertThat(songs.get(0).performers()).isEmpty();
    }

    @DisplayName("A list of songs is returned")
    @Test
    void shouldReturnAListOfSongs() {
        LocalDateTime now = LocalDateTime.now();
        service.addSong(new SongRequest("Fake Song", "Fake composer", new ArrayList<>()));
        service.addSong(new SongRequest("2nd Fake Song", "Fake composer", List.of(new Performer("Fake Singer", now))));
        List<Song> songs = service.getSongs();
        assertThat(songs).hasSize(2);
        assertThat(songs.get(0).id()).isInstanceOf(Long.class);
        assertThat(songs.get(0).title()).isEqualTo("Fake Song");
        assertThat(songs.get(0).composer()).isEqualTo("Fake composer");
        assertThat(songs.get(0).performers()).isEmpty();
        assertThat(songs.get(1).id()).isInstanceOf(Long.class);
        assertThat(songs.get(1).title()).isEqualTo("2nd Fake Song");
        assertThat(songs.get(1).composer()).isEqualTo("Fake composer");
        assertThat(songs.get(1).performers().get(0).performerName()).isEqualTo("Fake Singer");
        assertThat(songs.get(1).performers().get(0).dateTime()).isEqualTo(now);
    }
}
