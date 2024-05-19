package co.develhope.bookExampleWithDTO.controllers;

import co.develhope.bookExampleWithDTO.DTO.BookCreationDTO;
import co.develhope.bookExampleWithDTO.Mapper.BookMapper;
import co.develhope.bookExampleWithDTO.entities.BookEntity;
import co.develhope.bookExampleWithDTO.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper mapper;


    @GetMapping
    public List<BookEntity> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Long id) {
        BookEntity book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok().body(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity createBook(@Valid @RequestBody BookCreationDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        } else {
            BookEntity book = mapper.toBook(bookDTO);
            BookEntity createdBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookEntity> updateBook(@PathVariable Long id, @Valid @RequestBody BookEntity book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            BookEntity updatedBook = bookService.updateBook(id, book);
            if (updatedBook != null) {
                return ResponseEntity.ok().body(updatedBook);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<BookEntity> patchBook(@PathVariable Long id, @RequestBody Map<String, String> updates) {

        BookEntity updatedBook = bookService.updateBook(id, updates);
        if (updatedBook == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedBook);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBook() {
        bookService.deleteAllBooks();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BookEntity> deleteBook(@PathVariable Long id) {
        BookEntity book = bookService.deleteBook(id);
        if (book != null)
            return ResponseEntity.ok().body(book);
        else {
            return ResponseEntity.notFound().build();
        }
    }


}