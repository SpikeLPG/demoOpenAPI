package com.example.controller;

import com.example.DemoApplication;
import com.example.model.Performer;
import com.example.model.SongRequest;
import com.example.service.SongService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.example.util.RestDocUtil.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
class SongControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private SongService service;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(restDocSetup(restDocumentation))
                .build();
    }

    @AfterEach
    void teardown() {
        service.deleteAll();
    }

    private static Stream<Arguments> provideSongsForSongList() {
        return Stream.of(
                Arguments.of("Single Song", Collections.singletonList(new SongRequest("temp", "temp", List.of())), status().isOk()),
                Arguments.of("No Songs", Collections.EMPTY_LIST, status().isOk()),
                Arguments.of("Multiple Songs", List.of(new SongRequest("Temp", "temp", List.of()), new SongRequest("Temp 1", "temp 1", Collections.singletonList(new Performer("Name", LocalDateTime.of(2023, Month.AUGUST, 12, 11, 0))))), status().isOk())
        );
    }

    @ParameterizedTest(name = "Shall return a list of songs - {0}")
    @DisplayName("Get all songs")
    @MethodSource("provideSongsForSongList")
    void getAllSongs(String name, List<SongRequest> songs, ResultMatcher status) throws Exception {
        songs.forEach(songRequest -> service.addSong(songRequest));
        ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.get("/api/songs").accept(MediaType.APPLICATION_JSON));
        resp.andExpect(status)
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(songs.size()))
                //If the document name is the same then only the last result is saved to the source files
                .andDo(document("Songs - " + name, responseSongListDescriptor));
    }


    private static Stream<Arguments> getSongById() {
        return Stream.of(
                Arguments.of("Song exists - no performer", 1, new SongRequest("temp", "temp", null), status().isOk(), true),
                Arguments.of("Song exists - performer", 1, new SongRequest("temp1", "temp", List.of(new Performer("temp", LocalDateTime.now()))), status().isOk(), true),
                Arguments.of("Song does not exist", 9999, null, status().isNotFound(), false),
                Arguments.of("Invalid id", null, null, status().is5xxServerError(), false)
        );
    }

    @ParameterizedTest(name = "Shall get a book using id - {0}")
    @DisplayName("Get a song by id")
    @MethodSource("getSongById")
    void getSongById(String name, Object id, SongRequest song, ResultMatcher status, boolean isOK) throws Exception {
        if (song != null) service.addSong(song);
        service.addSong(new SongRequest("temp", "temp", Collections.emptyList()));
        ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.get("/api/songs/" + id).accept(MediaType.APPLICATION_JSON));
        resp.andExpect(status);
        if (isOK) {
            resp.andDo(document("Song By Id - " + name, responseSongDescriptor));
        } else {
            resp.andDo(document("Song By Id - " + name, responseErrorDescriptor));
        }

    }

    private static Stream<Arguments> addSong() {
        return Stream.of(
                Arguments.of("Add a song with performers", "{\"title\":\"Temp\", \"composer\":\"Temp\", \"performers\":[{\"performerName\":\"Temp\", \"dateTime\":\"2023-06-30T14:07:54.630519\"}]}", status().isOk(), true),
                Arguments.of("Add a song with no performers", "{\"title\":\"Temp\", \"composer\":\"Temp\"}", status().isOk(), true),
                Arguments.of("Add a song with missing information", "{\"title\":\"Temp\"}", status().isBadRequest(), false)
        );
    }

    @ParameterizedTest(name = "Shall {0}")
    @DisplayName("Add a song")
    @MethodSource("addSong")
    void addASong(String name, String body, ResultMatcher status, boolean isOk) throws Exception {
        ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.post("/api/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON));
        resp.andExpect(status);
        if(isOk) {
            resp.andDo(document(name, requestAddSongDescriptor, responseSongDescriptor));
        } else {
            resp.andDo(document(name, responseErrorDescriptor));
        }

    }
}
