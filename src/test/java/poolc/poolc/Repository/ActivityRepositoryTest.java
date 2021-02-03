package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import poolc.poolc.domain.Activity;
import poolc.poolc.repository.ActivityRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
public class ActivityRepositoryTest {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void Activity생성(){
        Activity activity = new Activity("title", "host", LocalDate.now(),null,"9",false,40L,false);
        activityRepository.save(activity);

        activity.equals(activityRepository.findOne(activity.getId()));
    }

    @Test
    public void Activity삭제(){
        Activity activity = new Activity("title", "host", LocalDate.now(),null,"9",false,40L,false);
        activityRepository.save(activity);
        activityRepository.delete(activity.getId());

        em.flush();
        em.clear();

        Activity findActivity = activityRepository.findOne(activity.getId());
        Assertions.assertNull(findActivity);
    }

    @Test
    public void Activity전체조회() throws Exception{
        //given
        Activity activity1 = new Activity("title", "host", LocalDate.now(),null,"9",false,40L,false);
        activityRepository.save(activity1);

        Activity activity2 = new Activity("title", "host", LocalDate.now(),null,"9",false,40L,false);
        activityRepository.save(activity2);

        //when
        List<Activity> activityList = activityRepository.findAll();

        //then
        Assertions.assertEquals(2L, activityList.size());

    }
}
