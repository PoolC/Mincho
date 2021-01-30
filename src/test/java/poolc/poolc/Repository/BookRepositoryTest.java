package poolc.poolc.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Book;
import poolc.poolc.repository.BookRepository;
import poolc.poolc.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 신규책등록() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        bookRepository.save(book);
    }

    @Test
    public void 신규책조회() {
        Book book = new Book();
        book.setTitle("정윤석");
        book.setAuthor("1");
        bookRepository.save(book);
        Book book2 = new Book();
        book2.setTitle("박형철");
        book2.setAuthor("2");
        bookRepository.save(book2);
        List<Book> all = bookRepository.findAll();
        for (Book book1 : all) {
            System.out.println(book1.getTitle());
        }
    }

    @Test
    public void 책삭제() {
        Book book = new Book();
        book.setTitle("정윤석");
        book.setAuthor("1");
        bookRepository.save(book);
        bookRepository.delete(book);
        em.flush();
        em.clear();
        Book one = bookRepository.findOne(book.getID());
        assertNull(one);
    }
}