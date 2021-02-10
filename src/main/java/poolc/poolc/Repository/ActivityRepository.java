package poolc.poolc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import poolc.poolc.domain.Activity;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {
    private final EntityManager em;

    public void save(Activity activity){
        em.persist(activity);
    }

    public void delete(Long id){
        em.remove(em.find(Activity.class,id));
    }

    public Activity findOne(Long id){
        return em.find(Activity.class, id);
    }

    public List<Activity> findAll(){
        return em.createQuery("select a from Activity a",Activity.class)
                .getResultList();
    }
}
