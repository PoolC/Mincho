package org.poolc.api.project.repository;

import org.poolc.api.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "select distinct p from Project p left join fetch p.memberLoginIDs m where m = :loginId")
    List<Project> findProjectsByProjectMembers(@Param("loginId") String loginId);

    @Query(value = "select p from Project p order by created_at DESC")
    List<Project> findAllByOrderByCreatedAtDesc();
}
