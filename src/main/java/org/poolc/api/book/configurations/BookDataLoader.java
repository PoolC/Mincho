package org.poolc.api.book.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.repository.BookRepository;
import org.poolc.api.enums.BookStatus;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.infra.PasswordHashProvider;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Profile("bookTest")
@RequiredArgsConstructor
public class BookDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;


    @Override
    public void run(String... args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Member member = new Member(UUID.randomUUID().toString(),
                "MEMBER_ID",
                passwordHashProvider.encodePassword("MEMBER_PASSWORD"),
                "example@email.com",
                "examplePhoneNumber",
                "MEMBER_NAME",
                "exampleDepartment",
                "exampleStudentID",
                true,
                false,
                null,
                null,
                null,
                null,
                false,
                null);
        memberRepository.save(member);
        Member member2 = new Member(UUID.randomUUID().toString(),
                "MEMBER_ID1",
                passwordHashProvider.encodePassword("MEMBER_PASSWORD1"),
                "example@email.com2",
                "examplePhoneNumber2",
                "MEMBER_NAME2",
                "exampleDepartment",
                "exampleStudentID2",
                true,
                true,
                null,
                null,
                null,
                null,
                false,
                null);
        memberRepository.save(member2);
        Book book1 = new Book("형철이의 삶",
                "박형철",
                "http",
                "형철이의 삶에 대해 적었다",
                BookStatus.AVAILABLE,
                localDateTime);
        book1.borrowBook(member);
        bookRepository.save(book1);
        bookRepository.save(new Book("형철이의 삶2",
                "박형철",
                "http",
                "형철이의 삶에 대해 적었다",
                BookStatus.AVAILABLE,
                localDateTime));
    }
}