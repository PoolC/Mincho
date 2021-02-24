package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByActivity(Activity activity);
}
