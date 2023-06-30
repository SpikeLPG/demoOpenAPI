package com.example.error;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice()
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BookError> handleBookNotFoundException() {
        return new ResponseEntity<>(new BookError("BOOK-001", "We were unable to locate a book with the id number that was supplied."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BookMissingDetailsException.class})
    public ResponseEntity<BookError> handleBookMissingDetailsException() {
        return new ResponseEntity<>(new BookError("BOOK-002", "Please supply all required book details"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<SongError> handleSongNotFoundException() {
        return new ResponseEntity<>(new SongError("SONG-001", "We were unable to locate a song with the id number that was supplied."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({SongMissingDetailsException.class})
    public ResponseEntity<SongError> handleSongMissingDetailsException() {
        return new ResponseEntity<>(new SongError("SONG-002", "Please supply all required song details"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) //@ResponseStatus will add this as a default response to all endpoints
    @ApiResponse(responseCode = "503", description = "Service error",
            content = {@Content(mediaType = "text/json",
                    schema = @Schema(implementation = ServiceError.class),
                    examples = {
                            @ExampleObject(name = "Service Down", description = "Service is unavailable", value = ServiceError.EXAMPLE_SERVICE_UNAVAILABLE)
                    })}
    )
    @ExceptionHandler(RuntimeException.class)
    public ServiceError handleServiceError() {
        return ServiceError.SERVICE_UNAVAILABLE;
    }
}
