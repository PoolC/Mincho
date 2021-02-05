package poolc.poolc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import poolc.poolc.domain.Activity;
import poolc.poolc.domain.Session;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SessionRepository {
    private final EntityManager em;

    public void save(Session session){
        em.persist(session);
    }

    public void delete(Long id){
        em.remove(em.find(Session.class, id));
    }

    public Session findOne(Long id){
        return em.find(Session.class, id);
    }

    public List<Session> findAllByActivity(Long activityID){
        return em.createQuery("select s from Session s where s.activity = :Activity", Session.class)
                .setParameter("Activity", em.find(Activity.class,activityID))
                .getResultList();
    }
}
