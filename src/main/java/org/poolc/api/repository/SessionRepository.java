package org.poolc.api.repository;

import lombok.RequiredArgsConstructor;
import org.poolc.api.domain.Activity;
import org.poolc.api.domain.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SessionRepository {
    private final EntityManager em;

    public void save(Session session) {
        em.persist(session);
    }

    public void delete(Long id) {
        em.remove(em.find(Session.class, id));
    }

    public Session findOne(Long id) {
        return em.find(Session.class, id);
    }

    public List<Session> findAllByActivity(Long activityID) {
        return em.createQuery("select s from Session s where s.activity = :Activity", Session.class)
                .setParameter("Activity", em.find(Activity.class, activityID))
                .getResultList();
    }
}
