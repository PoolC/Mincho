package org.poolc.api.book.repository;

import org.poolc.api.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "select b from Book b left join fetch b.borrower where b.id=:id")
    Optional<Book> findByIdWithBorrower(@Param("id") Long id);

}