package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.exception.NotHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.vo.SessionCreateValues;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final EntityManager em;
    private final SessionRepository sessionRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public void createSession(String userID, SessionCreateValues sessionCreateValues) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(sessionCreateValues.getActivityID()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다"));
        checkUserIsHost(activity.getHost().getUUID(), userID);
        sessionRepository.save(new Session(activity, sessionCreateValues.getDescription(), sessionCreateValues.getDate(), sessionCreateValues.getSessionNumber()));
    }

    private void checkUserIsHost(String uuid, String userID) {
        if (!uuid.equals(userID)) {
            throw new NotHostException("호스트가 아닌 사람은 세션 정보를 입력할수 없습니다");
        }
    }

    public List<Session> findSessionsByActivityID(Long id) {
        return sessionRepository.findByActivity(activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 활동입니다")));
    }
}
