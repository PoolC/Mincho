package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Activity;
import poolc.poolc.repository.ActivityRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class ActivityRepositoryTest {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void Activity생성() {
        //given
        Activity activity = new Activity("title", "host", LocalDate.now(), null, "9", false, 40L, false);

        //when
        activityRepository.save(activity);

        //then
        em.flush();
        em.clear();

        activity.equals(activityRepository.findById(activity.getId()));
    }

    @Test
    public void Activity삭제() {
        //given
        Activity activity = new Activity("title", "host", LocalDate.now(), null, "9", false, 40L, false);
        activityRepository.save(activity);

        //when
        activityRepository.delete(activity);

        //then
        em.flush();
        em.clear();

        Optional<Activity> findActivity = activityRepository.findById(activity.getId());
        Assertions.assertTrue(findActivity.isEmpty());
    }

    @Test
    public void Activity전체조회() {
        //given
        Activity activity1 = new Activity("title", "host", LocalDate.now(), null, "9", false, 40L, false);
        activityRepository.save(activity1);

        Activity activity2 = new Activity("title", "host", LocalDate.now(), null, "9", false, 40L, false);
        activityRepository.save(activity2);

        //when
        List<Activity> activityList = activityRepository.findAll();

        //then
        Assertions.assertEquals(2L, activityList.size());

    }
}