package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.ActivityTag;
import org.poolc.api.activity.exception.NotAdminOrHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;

    @Transactional
    public void createActivity(ActivityCreateValues values) {
        Activity activity = new Activity(values.getTitle(), values.getDescription(),
                memberRepository.findById(values.getMemberID()).orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다!")),
                values.getStartDate(), values.getClassHour(), values.getIsSeminar(), values.getCapacity(), values.getHour(), false);
        activity.getTags().addAll(values.getTags().stream().map(t -> new ActivityTag(activity, t)).collect(Collectors.toList()));
        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long id, String uuid, String isAdmin) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다!"));
        if (!checkWhetherAdminOrHost(isAdmin, uuid, activity.getHost().getUUID())) {
            throw new NotAdminOrHostException("호스트나 관리자가 아닙니다");
        }
        activityRepository.delete(activity);
    }

    @Transactional
    public void updateActivity(String isAdmin, Long id, ActivityUpdateValues values) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
        if (!checkWhetherAdminOrHost(isAdmin, values.getMemberID(), activity.getHost().getUUID())) {
            throw new NotAdminOrHostException("호스트나 관리자가 아닙니다");
        }
        activity.update(values);
    }

    public List<Activity> findActivities() {
        return activityRepository.findActivitiesWithHostAndTags();
    }

    public Activity findOneActivity(Long id) {
        return activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
    }


    private boolean checkWhetherAdminOrHost(String isAdmin, String MemberID, String activityHostID) {

        if ((MemberID.equals(activityHostID)) || (isAdmin.equals("true"))) {
            return true;
        } else {
            return false;
        }
    }

}
