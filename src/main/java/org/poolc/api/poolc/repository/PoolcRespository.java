package org.poolc.api.poolc.repository;

import org.poolc.api.poolc.domain.Poolc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoolcRespository extends JpaRepository<Poolc, Long> {
}
