package com.example.model;

public record BookRequest(String title, String author) {
    //language=JSON
    public static final String BOOK_DETAILS = """
            {
                "title": "Fake title",
                "author": "Fake Author"
            }""";
}