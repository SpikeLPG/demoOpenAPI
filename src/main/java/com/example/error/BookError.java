package com.example.error;

public record BookError(String errorCode, String message) {
    public static final String BOOK_NOT_FOUND="{\"errorCode\":\"BOOK-001\",\"message\":\"We were unable to locate a book with the id number that was supplied.\"}";
}
