package poolc.poolc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Book;
import poolc.poolc.domain.Member;
import poolc.poolc.enums.BookStatus;
import poolc.poolc.repository.BookRepository;
import poolc.poolc.repository.MemberRepository;

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
        book.setStatus(BookStatus.INAVAILABLE);
        Member member = memberRepository.findById(memberUUID).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다"));
        book.setBorrower(member);
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
        book.setStatus(BookStatus.AVAILABLE);
        book.setBorrower(null);
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
