package poolc.poolc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poolc.poolc.domain.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
