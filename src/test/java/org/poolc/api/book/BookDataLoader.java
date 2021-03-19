package org.poolc.api.book;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookStatus;
import org.poolc.api.book.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("bookTest")
@RequiredArgsConstructor
public class BookDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        bookRepository.save(new Book("형철이의 삶",
                "박형철",
                "http",
                "형철이의 삶에 대해 적었다",
                BookStatus.AVAILABLE));

        bookRepository.save(new Book("형철이의 삶2",
                "박형철",
                "http",
                "형철이의 삶에 대해 적었다",
                BookStatus.AVAILABLE));

        for (int i = 0; i < 5; i++) {
            bookRepository.save(new Book("풀씨",
                    "형철띠",
                    "http",
                    "인생이란 무엇인가",
                    BookStatus.AVAILABLE));
        }
    }
}