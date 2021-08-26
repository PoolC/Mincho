package org.poolc.api.book.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookStatus;
import org.poolc.api.book.exception.DuplicateBookException;
import org.poolc.api.book.repository.BookRepository;
import org.poolc.api.book.vo.BookCreateValues;
import org.poolc.api.book.vo.BookUpdateValues;
import org.poolc.api.common.exception.ConflictException;
import org.poolc.api.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public void saveBook(BookCreateValues values) {
        boolean hasDuplicate = bookRepository.existsByTitleAndAuthor(values.getTitle(), values.getAuthor());

        if (hasDuplicate) {
            throw new DuplicateBookException("이미 존재하는 책입니다");
        }

        bookRepository.save(new Book(values.getTitle(),
                values.getAuthor(),
                values.getImageURL(),
                values.getInfo(),
                BookStatus.AVAILABLE));
    }

    @Transactional
    public void updateBook(Long bookId, BookUpdateValues values) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 책입니다"));
        duplicateBookCheck(book, values);

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
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 책입니다"));
    }

    @Transactional
    public void borrowBook(Member member, Long bookId) {
        Book book = findOneBook(bookId);
        validateAvailableBook(book);
        book.borrowBook(member);
        bookRepository.flush();
    }

    @Transactional
    public void returnBook(Member member, Long bookId) {
        Book book = findOneBook(bookId);
        validateMyBook(book, member.getUUID());
        book.returnBook();
        bookRepository.flush();
    }

    private void validateAvailableBook(Book book) {
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new ConflictException("대여된 책입니다!");
        }
    }

    private void validateMyBook(Book book, String memberUUID) {
        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new ConflictException("대여되지 않은 책입니다!");
        }

        if (!book.getBorrower().getUUID().equals(memberUUID)) {
            throw new ConflictException("본인이 빌린 책이 아닙니다!");
        }
    }

    private void duplicateBookCheck(Book book, BookUpdateValues values) {
        if (!((book.getTitle().equals(values.getTitle())) && (book.getAuthor().equals(values.getAuthor())))) {

            boolean hasDuplicate = bookRepository.existsByTitleAndAuthor(values.getTitle(), values.getAuthor());

            if (hasDuplicate) {
                throw new DuplicateBookException("이미 존재하는 책입니다");
            }
        }
    }
}
