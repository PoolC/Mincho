package poolc.poolc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import poolc.poolc.domain.Project;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {
    private final EntityManager em;

    public void save(Project project) {
        em.persist(project);
    }

    public Project findOne(Long id) {
        return em.find(Project.class, id);
    }

    public void delete(Project project) {
        em.remove(project);
    }

    public List<Project> findAll() {
        return em.createQuery("select p from Project p").getResultList();
    }

    public List<Project> findAllWithMembers() {
        List<Project> resultList = em.createQuery("select p from Project p join fetch p.members", Project.class)
                .getResultList();
        return resultList;
    }
}
