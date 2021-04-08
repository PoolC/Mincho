package org.poolc.api.member.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    public List<MutablePair<String, Long>> getHours(LocalDate startDate, LocalDate endDate) {
        List<Object[]> list = em.createNativeQuery("select attendance.member_loginid, sum(session.hour) from attendance join session on attendance.session_id=session.id join activity on session.activity_id=activity.id where activity.start_date between :startDate and :endDate group by attendance.member_loginid")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        return list.stream()
                .map(s -> new MutablePair<String, Long>(s[0].toString(), Long.parseLong(s[1].toString())))
                .collect(Collectors.toList());
    }
}