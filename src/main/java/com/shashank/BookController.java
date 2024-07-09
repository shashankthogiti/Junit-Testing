package com.shashank;

import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBookRecords() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "{bookId}")
    public  Book getBookById(@PathVariable(value = "bookId") Long bookId) {
        return bookRepository.findById(bookId).get();
    }

    @PostMapping
    public  Book createBookReord(@RequestBody @Validated Book bookRecord) {
        return  bookRepository.save(bookRecord);
    }

    @PutMapping
    public  Book updateBookRecord(@RequestBody @Validated Book bookRecord) throws Exception {
        if(bookRecord == null || bookRecord.getBookId() == null) {
            throw new Exception("Book");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());

        if(!optionalBook.isPresent()) {
            throw  new Exception("Book with ID: " + bookRecord.getBookId() + "does not exist. ");
        }

        Book existingBookRecord = optionalBook.get();
        existingBookRecord.setName(bookRecord.getName());
        existingBookRecord.setRating(bookRecord.getRating());
        existingBookRecord.setSummmary(bookRecord.getSummmary());

        return  bookRepository.save(existingBookRecord);
    }

    @DeleteMapping(value = "{bookId}")
    public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws Exception {
        if(!bookRepository.findById(bookId).isPresent()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        bookRepository.deleteById(bookId);
    }


}
