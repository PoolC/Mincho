package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {


    @Query(value = "select distinct a from Activity a left join fetch a.host join fetch a.tags t join fetch t.tagID")
    List<Activity> findActivitiesWithHostAndTags();
}
