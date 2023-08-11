package com.example.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Book Schema")
public record Book(
        @NotBlank
        @Schema(description = "Unique identifier of the book", example = "1")
        long id,
        @NotBlank
                @Schema(description = "Title of the book", example = "Dune")
        String title,
        @NotBlank
                @Schema(description = "Author of the book", example = "James Herbert")
        String author) {
    //language=JSON
    public static final String SINGLE_BOOK = """
            { "id": 123, "title":"Fake title", "author": "Fake Author"}
            """;

    //language=JSON
    public static final String EMPTY_LIST = """
            []
            """;

    //language=JSON
    public static final String LIST_ONE_BOOK = """
            [{
            "id": 123, "title":"Fake title", "author": "Fake Author"
            }]""";

    //language=JSON
    public static final String LIST_BOOKS = """
            [{
            "id": 123, "title":"Fake title", "author": "Fake Author"},{"id": 5842, "title":"2nd Fake title", "author": "Fake Author"
            }]""";
}
