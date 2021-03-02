package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByActivity(Activity activity);

    @Query(value = "select distinct s from Session s left join fetch s.attendedMemberLoginIDs where s.id=:id")
    Optional<Session> findByIdWithAttendances(@Param("id") Long id);


}
