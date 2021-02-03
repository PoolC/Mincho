package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Activity;
import poolc.poolc.domain.Session;
import poolc.poolc.repository.ActivityRepository;
import poolc.poolc.repository.SessionRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
public class SessionRepositoryTest {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    EntityManager em;

    @Test
    public void Session생성() throws Exception{
        //given
        Activity activity = new Activity("title", "host", LocalDate.now(),null,"9",false,40L,false);
        activityRepository.save(activity);


        //when
        Session session = new Session(activity, "aaa", LocalDate.now(), 1L);
        sessionRepository.save(session);


        //then
        em.flush();
        em.clear();
        session.equals(em.find(Session.class, session.getId()));

    }

    @Test
    public void Session삭제() throws Exception{
        //given
        Activity activity = new Activity("title", "host", LocalDate.now(),null,"9",false,40L,false);
        activityRepository.save(activity);
        Session session = new Session(activity, "aaa", LocalDate.now(), 1L);
        sessionRepository.save(session);
        //when
        sessionRepository.delete(session.getId());
        //then
        em.flush();
        em.clear();

        Assertions.assertNull(sessionRepository.findOne(session.getId()));

    }

    @Test
    public void Activity의Session전체조회() throws Exception{
        //given
        Activity activity = new Activity("title", "host", LocalDate.now(),null,"9",false,40L,false);
        activityRepository.save(activity);
        Session session1 = new Session(activity, "aaa", LocalDate.now(), 1L);
        sessionRepository.save(session1);
        Session session2 = new Session(activity, "bbb", LocalDate.now(), 2L);
        sessionRepository.save(session2);
        //when
        List<Session> sessionList = sessionRepository.findAllByActivity(activity.getId());
        //then
//        Assertions.assertEquals(2L,sessionList.size());
    }


}
