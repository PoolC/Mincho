package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
