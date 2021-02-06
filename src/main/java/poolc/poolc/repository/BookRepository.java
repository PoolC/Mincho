package poolc.poolc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import poolc.poolc.domain.Book;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final EntityManager em;

    public void save(Book book) {
        em.persist(book);
    }

    public Book findOne(Long id) {
        return em.find(Book.class, id);
    }

    public Book findOneWithBorrower(Long id) {
        List<Book> resultList = em.createQuery("select b from Book b left join fetch b.borrower where b.id=:id", Book.class)
                .setParameter("id", id)
                .getResultList();
        if (resultList.size() == 0) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    public List<Book> findAll() {
        return em.createQuery("select b from Book b", Book.class)
                .getResultList();
    }

    public void delete(Book book) {
        em.remove(book);
    }
}