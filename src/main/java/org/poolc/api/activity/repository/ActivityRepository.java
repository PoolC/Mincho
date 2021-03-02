package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {


    @Query(value = "select distinct a from Activity a left join fetch a.host left join fetch a.tags t")
    List<Activity> findActivitiesWithHostAndTags();

    @Query(value = "select distinct a from Activity a left join fetch a.host left join a.tags t where a.id=:id")
    Optional<Activity> findOneActivityWithHostAndTags(@Param("id") Long id);

}
