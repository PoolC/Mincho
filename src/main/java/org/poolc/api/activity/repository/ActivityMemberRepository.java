package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.ActivityMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityMemberRepository extends JpaRepository<ActivityMember, Long> {
    
}
