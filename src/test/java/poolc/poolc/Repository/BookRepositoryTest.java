package poolc.poolc.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.book.domain.Book;
import poolc.poolc.book.repository.BookRepository;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.ProjectMember;
import poolc.poolc.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 책등록() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        assertNull(book.getId());
        bookRepository.save(book);
        assertNotNull(book.getId());
    }

    @Test
    public void 책조회() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        bookRepository.save(book);
        assertEquals(book, bookRepository.findById(book.getId()).get());
    }

    @Test
    public void 없는책조회() {
        assertEquals(bookRepository.findById(11l), Optional.empty());
    }

    @Test
    public void 책삭제() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        bookRepository.save(book);
        em.flush();
        em.clear();
        bookRepository.delete(book);
        assertEquals(bookRepository.findById(book.getId()), Optional.empty());
    }

    @Test
    public void 대여자와함께조회() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        List<ProjectMember> projectMembers = new ArrayList<>();

        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);
        memberRepository.save(member);

        em.flush();
        em.clear();

        book.setBorrower(member);
        bookRepository.save(book);

        em.flush();
        em.clear();

        Optional<Book> newBook = bookRepository.findByIdWithBorrower(book.getId());
        assertEquals(newBook.get(), book);
    }

    @Test
    public void 책하나조회예외() {
        Optional<Book> book = bookRepository.findByIdWithBorrower(12345l);
        assertEquals(book, Optional.empty());
    }
}
