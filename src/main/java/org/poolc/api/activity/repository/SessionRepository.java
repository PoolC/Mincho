package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

}
