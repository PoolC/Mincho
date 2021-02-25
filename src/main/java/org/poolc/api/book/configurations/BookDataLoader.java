package org.poolc.api.book.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookStatus;
import org.poolc.api.book.repository.BookRepository;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("bookTest")
@RequiredArgsConstructor
public class BookDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) {
        Book book = new Book("형철이의 삶",
                "박형철",
                "http",
                "형철이의 삶에 대해 적었다",
                BookStatus.AVAILABLE);
        bookRepository.save(book);

        bookRepository.save(new Book("형철이의 삶2",
                "박형철",
                "http",
                "형철이의 삶에 대해 적었다",
                BookStatus.AVAILABLE));
    }
}