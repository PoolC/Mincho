package org.poolc.api.book.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.repository.BookRepository;
import org.poolc.api.enums.BookStatus;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        bookRepository.delete(findOneBook(bookId));
    }

    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    public Book findOneBook(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findByIdWithBorrower(bookId);
        return bookOptional.orElseThrow(() -> new NoSuchElementException("존재하지 않는 책입니다"));
    }

    @Transactional
    public void borrowBook(String memberUUID, Long bookId) {
        Book book = findOneBook(bookId);
        validateAvailableBook(book);
        Member member = memberRepository.findById(memberUUID).get();
        book.borrowBook(member);
    }

    private void validateAvailableBook(Book book) {
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("대여된 책입니다!");
        }
    }

    @Transactional
    public void returnBook(String memberUUID, Long bookId) {
        Book book = findOneBook(bookId);
        validateMyBook(book, memberUUID);
        book.returnBook();
    }

    private void validateMyBook(Book book, String memberUUID) {
        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new IllegalStateException("대여되지 않은 책입니다!");
        }
        if (!book.getBorrower().getUUID().equals(memberUUID)) {
            throw new IllegalStateException("본인이 빌린 책이 아닙니다!");
        }
    }
}
