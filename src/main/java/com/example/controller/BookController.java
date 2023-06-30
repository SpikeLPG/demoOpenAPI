package com.example.controller;

import com.example.model.Book;
import com.example.error.BookError;
import com.example.model.BookRequest;
import com.example.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(BookController.API_URL)
@Tag(name = "Books", description = "API to manage and view a list of books")
public class BookController {
    public static final String API_URL = "/api";
    public static final String BOOKS_ROOT = "/books";
    public static final String BOOK_BY_ID = BOOKS_ROOT + "/{id}";

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(BOOKS_ROOT)
    @Operation(summary = "Get a list of books")
    @ApiResponse(responseCode = "200", description = "List of existing books",
            content = {@Content(mediaType = "text/json",
                    schema = @Schema(implementation = Book.class),
                    examples = {
                            @ExampleObject(name = "No books list", description = "No books exist", value = Book.EMPTY_LIST),
                            @ExampleObject(name = "One book list", description = "Only 1 book exists", value = Book.LIST_ONE_BOOK),
                            @ExampleObject(name = "Multiple books list", description = "Multiple books exist", value = Book.LIST_BOOKS)
                    })})
    public Collection<Book> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping(BOOKS_ROOT)
    @Operation(summary = "Add a book to the service, if the book already exists then the book is not created",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request requires both title and author",
                    content = {@Content(
                            mediaType = "text/json",
                            schema = @Schema(
                                    implementation = BookRequest.class),
                            examples = {
                                    @ExampleObject(name = "New book", description = "Add a new book", value = BookRequest.BOOK_DETAILS)
                            })
                    })
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", description = "Add a new book to the service",
                    content = {@Content(mediaType = "text/json",
                            schema = @Schema(implementation = Book.class),
                            examples = {
                                    @ExampleObject(name = "New book", description = "Add a new book to the service", value = Book.SINGLE_BOOK),
                                    @ExampleObject(name = "Book exists", description = "If the book already exists then the existing book is returned", value = Book.SINGLE_BOOK)
                            })}
            )
    )
    public Book addBook(@RequestBody @NotEmpty BookRequest bookRequest) {
        return bookService.addBook(bookRequest);
    }

    @GetMapping(BOOK_BY_ID)
    @Operation(summary = "Get the book matching the id supplied")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matching book details",
                    content = {@Content(mediaType = "text/json",
                            schema = @Schema(implementation = Book.class),
                            examples = {
                                    @ExampleObject(name = "Book found", description = "Book details returned", value = Book.SINGLE_BOOK),
                            })
                    }),
            @ApiResponse(responseCode = "404", description = "No book matches the id",
                    content = {@Content(mediaType = "text/json", schema = @Schema(implementation = BookError.class),
                            examples = {
                                    @ExampleObject(name = "Book not found", description = "Book was not found matching the id", value = BookError.BOOK_NOT_FOUND)
                            })
                    })
    })
    public Book getBookById(@Parameter(description = "Id of the book to find", required = true) @PathVariable long id) {
        return bookService.getBookById(id);
    }


    //README this endpoint is hidden from the OpenAPI documentation so even with the API response set it will never be displayed
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User wants to delete a book from the list",
                    content = {@Content(mediaType = "text/json",
                            schema = @Schema(),
                            examples = {
                                    @ExampleObject(name = "Book deleted", description = "Book details were deleted"),
                            })}),
            @ApiResponse(responseCode = "404", description = "No book matches the id",
                    content = {@Content(mediaType = "text/json", schema = @Schema(implementation = BookError.class),
                            examples = {
                                    @ExampleObject(name = "Book not found", description = "Book was not found matching the id", value = BookError.BOOK_NOT_FOUND)
                            })})
    })
    @DeleteMapping("/internal" + BOOK_BY_ID)
    public void deleteBook(@Parameter(description = "Id of the book to find", required = true) @PathVariable long id) {
        bookService.deleteBook(id);
    }
}
