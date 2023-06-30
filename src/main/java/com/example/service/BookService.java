package com.example.service;

import com.example.error.BookMissingDetailsException;
import com.example.error.BookNotFoundException;
import com.example.model.Book;
import com.example.model.BookRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    @Getter
    private List<Book> books;

    public void deleteBook(long id) {
        if (this.books.stream().anyMatch(book -> book.id() == id)) {
            this.books.removeIf(book -> book.id() == id);
        } else {
            throw new BookNotFoundException();
        }
    }

    public Book addBook(BookRequest bookRequest) {
        if (bookRequest.author() != null && !bookRequest.author().isEmpty() && bookRequest.title() != null && !bookRequest.title().isEmpty()) {
            Book newBook = this.books.stream()
                    .filter(it -> Objects.equals(it.title(), bookRequest.title()) && Objects.equals(it.author(), bookRequest.author()))
                    .findFirst().orElse(null);
            if (newBook == null) {
                long highestIndex = this.books.stream().max(Comparator.comparing(Book::id)).orElse(new Book(0, "", "")).id();
                newBook = new Book(highestIndex + 1, bookRequest.title(), bookRequest.author());
                books.add(newBook);
            }
            return newBook;
        } else throw new BookMissingDetailsException();
    }

    public Book getBookById(long id) {
        Optional<Book> matchedBook = this.books.stream().filter(book -> book.id() == id).findAny();
        if (matchedBook.isPresent()) {
            return matchedBook.get();
        } else throw new BookNotFoundException();
    }
}
