package poolc.poolc.Service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Book;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.ProjectMember;
import poolc.poolc.enums.BookStatus;
import poolc.poolc.service.BookService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookServiceTest {

    @Autowired
    BookService bookService;

    @Autowired
    EntityManager em;

    @Test
    public void 책등록() {
        Book book = new Book();
        book.setAuthor("윤석");
        book.setTitle("짱");
        assertNull(book.getId());
        bookService.saveBook(book);
        assertNotNull(book.getId());
    }

    @Test
    public void 책삭제() {
        Book book = new Book();
        book.setAuthor("윤석");
        book.setTitle("짱");
        bookService.saveBook(book);
        bookService.deleteBook(book.getId());
        assertThrows(NoSuchElementException.class, () -> {
            bookService.findOneBook(book.getId());
        });
    }

    @Test
    public void 없는책삭제() {
        assertThrows(NoSuchElementException.class, () -> {
            bookService.deleteBook(1l);
        });
    }

    @Test
    public void 여러가지책조회() {
        Book book = new Book();
        book.setAuthor("윤석");
        book.setTitle("짱");
        Book book2 = new Book();
        book2.setAuthor("윤석");
        book2.setTitle("짱짱");
        bookService.saveBook(book);
        bookService.saveBook(book2);
        em.flush();
        em.clear();
        List<Book> books = bookService.findBooks();
        assertEquals(2, books.size());
    }

    @Test
    public void 책하나조회() {
        Book book = new Book();
        book.setAuthor("윤석");
        book.setTitle("짱");
        bookService.saveBook(book);
        Book book2 = new Book();
        book2.setAuthor("윤석2");
        book2.setTitle("짱2");
        bookService.saveBook(book2);
        em.flush();
        em.clear();
        Book book3 = bookService.findOneBook(book.getId());
        assertEquals(book3, book);
    }

    @Test
    public void 책하나조회예외() {
        assertThrows(NoSuchElementException.class, () -> {
            bookService.findOneBook(1235l);
        });
    }

    @Test
    public void 책대여() {
        String a = "ddd";
        List<ProjectMember> projectMembers = new ArrayList<>();
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);

        Book book = new Book();
        book.setAuthor("윤석");
        book.setTitle("짱");

        em.persist(member);
        bookService.saveBook(book);
        bookService.borrowBook(member.getUUID(), book.getId());

        em.flush();
        em.clear();

        assertEquals(member, bookService.findOneBook(book.getId()).getBorrower());
    }

    @Test
    public void 없는책대여() {
        assertThrows(NoSuchElementException.class, () -> bookService.borrowBook("1", 1l));
    }

    @Test
    public void 대여된책대여() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        book.setStatus(BookStatus.INAVAILABLE);
        bookService.saveBook(book);
        em.flush();
        em.clear();
        assertThrows(IllegalStateException.class, () -> bookService.borrowBook("1", book.getId()));
    }

    @Test
    public void 없는사람대여() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        bookService.saveBook(book);
        assertThrows(NoSuchElementException.class, () -> bookService.borrowBook("a1", book.getId()));
    }

    @Test
    public void 책반납() {
        String a = "ddd";
        List<ProjectMember> projectMembers = new ArrayList<>();
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);
        Book book = new Book();
        book.setAuthor("윤석");
        book.setTitle("짱");
        em.persist(member);
        bookService.saveBook(book);
        bookService.borrowBook(member.getUUID(), book.getId());
        em.flush();
        em.clear();
        System.out.println(member.getUUID());
        System.out.println(book.getBorrower().getUUID());
        bookService.returnBook(member.getUUID(), book.getId());
        em.flush();
        em.clear();
        assertNull(bookService.findOneBook(book.getId()).getBorrower());
    }

    @Test
    public void 없는책반납() {
        assertThrows(NoSuchElementException.class, () -> bookService.returnBook("1", 1l)).getMessage();
    }

    @Test
    public void 대여되지않은책반납() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        bookService.saveBook(book);
        em.flush();
        em.clear();
        assertThrows(IllegalStateException.class, () -> bookService.returnBook("1", book.getId()));
    }

    @Test
    public void 내책아닌책반납() {
        String a = "ddd";
        List<ProjectMember> projectMembers = new ArrayList<>();
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(), "aaa", "나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers);
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        bookService.saveBook(book);
        em.persist(member);
        bookService.borrowBook(member.getUUID(), book.getId());
        em.flush();
        em.clear();
        assertThrows(IllegalStateException.class, () -> bookService.returnBook("23232", book.getId()));
    }
}
