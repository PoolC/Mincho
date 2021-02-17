package org.poolc.api.book.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.repository.BookRepository;
import org.poolc.api.book.vo.BookCreateValues;
import org.poolc.api.book.vo.BookUpdateValues;
import org.poolc.api.enums.BookStatus;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void saveBook(BookCreateValues values) {
        boolean hasDuplicate = bookRepository.existsByTitleAndAuthor(values.getTitle(), values.getAuthor());

        if (hasDuplicate) {
            throw new RuntimeException("이미 존재하는 책입니다");
        }

        bookRepository.save(new Book(values.getTitle(),
                values.getAuthor(),
                values.getImageURL(),
                values.getInfo(),
                BookStatus.AVAILABLE,
                null));
    }

    @Transactional
    public void updateBook(Long bookId, BookUpdateValues values) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 책입니다"));
        boolean hasDuplicate = bookRepository.existsByTitleAndAuthor(values.getTitle(), values.getAuthor());

        if (hasDuplicate) {
            throw new RuntimeException("이미 존재하는 책입니다");
        }

        book.update(values.getTitle(), values.getAuthor(), values.getImageURL(), values.getInfo());
    }

    @Transactional
    public void deleteBook(Long bookId) {
        bookRepository.delete(findOneBook(bookId));
    }

    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    public Book findOneBook(Long bookId) {
        return bookRepository.findByIdWithBorrower(bookId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 책입니다"));
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
