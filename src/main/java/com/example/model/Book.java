package com.example.model;


import jakarta.validation.constraints.NotBlank;

public record Book(@NotBlank long id, @NotBlank String title, @NotBlank String author) {
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
