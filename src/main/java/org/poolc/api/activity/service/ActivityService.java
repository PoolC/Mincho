package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.ActivityMember;
import org.poolc.api.activity.domain.ActivityTag;
import org.poolc.api.activity.exception.NotAdminOrHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.member.domain.Member;
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

    @Transactional
    public void apply(Long id, String uuid) {
        Activity activity = activityRepository.findOneActivityWithMembers(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 없습니다"));
        if (!activity.getAvailable()) {
            throw new IllegalStateException("아직 신청할수 없습니다.");
        } else if (activity.getCapacity() <= activity.getMembers().size()) {
            throw new IllegalStateException("정원을 초과하였습니다");
        } else {
            Member member = memberRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("해당하는 회원이 없습니다"));
            ActivityMember activityMember = new ActivityMember(activity, member);
            activity.apply(activityMember);
        }
    }

    public List<Activity> findActivities() {
        return activityRepository.findActivitiesWithHostAndTags();
    }

    public Activity findOneActivity(Long id) {
        return activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
    }

    public List<ActivityMember> findActivityMembers(Long id) {
        return activityRepository.findOneActivityWithMembers(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 없습니다")).getMembers();
    }

    private boolean checkWhetherAdminOrHost(String isAdmin, String MemberID, String activityHostID) {

        if ((MemberID.equals(activityHostID)) || (isAdmin.equals("true"))) {
            return true;
        } else {
            return false;
        }
    }

}
