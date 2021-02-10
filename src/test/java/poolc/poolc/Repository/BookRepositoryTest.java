package poolc.poolc.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Book;
import poolc.poolc.repository.BookRepository;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 책등록() {
        Book book = new Book();
        book.setTitle("1");
        book.setAuthor("1");
        bookRepository.save(book);
        em.flush();
        em.clear();
        Book book2 = bookRepository.findOneWithBorrower(book.getId());
        System.out.println(book2.getBorrower());
    }
}
