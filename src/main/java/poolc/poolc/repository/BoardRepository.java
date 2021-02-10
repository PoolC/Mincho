package poolc.poolc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poolc.poolc.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
