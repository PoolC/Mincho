package org.poolc.api.activity.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.ActivityTag;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.exception.NotAdminOrHostException;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.activity.vo.YearSemester;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final EntityManager em;

    @Transactional
    public void createActivity(ActivityCreateValues values, Member member) {
        Activity activity = new Activity(member, values);
        activity.getTags().addAll(values.getTags().stream().map(t -> new ActivityTag(activity, t)).collect(Collectors.toList()));
        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long id, Member member) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당 활동이 존재하지 않습니다!"));
        if (!checkWhetherAdminOrHost(member.isAdmin(), member.getUUID(), activity.getHost().getUUID())) {
            throw new NotAdminOrHostException("호스트나 관리자가 아닙니다");
        }
        activityRepository.delete(activity);
    }

    @Transactional
    public void updateActivity(Member member, Long id, ActivityUpdateValues values) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
        if (!checkWhetherAdminOrHost(member.isAdmin(), member.getUUID(), activity.getHost().getUUID())) {
            throw new NotAdminOrHostException("호스트나 관리자가 아닙니다");
        }
        activity.update(values);
    }

    @Transactional
    public void openActivity(Long id) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
        checkActivityIsClosed(activity);
        activity.open();
    }

    @Transactional
    public void closeActivity(Long id) {
        Activity activity = activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
        checkActivityIsOpen(activity);
        activity.close();
    }

    @Transactional
    public Activity apply(Long id, Member user) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 없습니다"));
        if (!activity.getAvailable()) {
            throw new IllegalStateException("아직 신청할수 없습니다.");
        } else if (!activity.checkMemberContain(user.getLoginID()) && activity.getCapacity() <= activity.getMemberLoginIDs().size()) {
            throw new IllegalStateException("정원을 초과하였습니다");
        } else {
            activity.toggleApply(user.getLoginID());
        }
        return activity;
    }

    public List<Activity> findActivities() {
        return activityRepository.findActivitiesWithHostAndTags();
    }

    public List<Activity> findActivitiesInSemester(String val) {
        YearSemester yearSemester = getYearSemesterFromString(val);
        LocalDate startDate = getFirstDateFromYearSemester(yearSemester);
        LocalDate endDate = getLastDateFromYearSemester(yearSemester);
        return activityRepository.findActivitiesWithHostAndTagsInSemester(startDate, endDate);
    }


    public Activity findOneActivity(Long id) {
        return activityRepository.findOneActivityWithHostAndTags(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다"));
    }

    public List<Member> findActivityMembers(Long id) {
        return memberRepository.findAllMembersByLoginIDList(activityRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 활동이 존재하지 않습니다")).getMemberLoginIDs());
    }

    public List<Activity> findActivitiesByHost(Member host) {
        return activityRepository.findActivitiesByHost(host);
    }

    public List<Activity> findActivitiesByActivityMembers(String loginId) {
        return activityRepository.findActivitiesByActivityMembers(loginId);
    }

    @Transactional
    public Map<Member, Boolean> findActivityMembersWithAttendance(Long id) {
        Session session = sessionRepository.findByIdWithAttendances(id).orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다"));
        List<Member> members = memberRepository.findAllMembersByLoginIDList(session.getActivity().getMemberLoginIDs());
        Map<Member, Boolean> result = new HashMap<>();
        members.stream().forEach(m -> result.put(m, false));
        session.getAttendedMemberLoginIDs().forEach(m -> result.put(memberRepository.findByLoginID(m).orElseThrow(() -> new NoSuchElementException("해당하는 세션이 없습니다")), true));
        return result;
    }


    public List<YearSemester> findActivityYears() {
        return activityRepository.findAll().stream().map(a -> YearSemester.of(a.getStartDate())).distinct().collect(Collectors.toList());
    }

    public YearSemester getYearSemesterFromString(String val) {
        String[] splits = val.split("-");
        if (splits.length != 2) {
            throw new IllegalArgumentException("제대로된 형식의 시간이 아닙니다");
        }
        Integer year = tryParse(splits[0]);
        Integer semester = tryParse(splits[1]);
        return new YearSemester(year, semester);
    }

    public LocalDate getFirstDateFromYearSemester(YearSemester yearSemester) {
        if (yearSemester.getSemester() == 1) {
            return LocalDate.of(yearSemester.getYear(), 3, 1);
        } else {
            return LocalDate.of(yearSemester.getYear(), 9, 1);
        }
    }

    public LocalDate getLastDateFromYearSemester(YearSemester yearSemester) {
        if (yearSemester.getSemester() == 1) {
            return LocalDate.of(yearSemester.getYear(), 8, 1).with(lastDayOfMonth());
        } else {
            return LocalDate.of(yearSemester.getYear() + 1, 2, 1).with(lastDayOfMonth());
        }
    }

    private void checkActivityIsClosed(Activity activity) {
        if (activity.getAvailable()) {
            throw new IllegalStateException("이미 신청가능한 활동입니다");
        }
    }

    private void checkActivityIsOpen(Activity activity) {
        if (!activity.getAvailable()) {
            throw new IllegalStateException("이미 닫혀있는 활동입니다");
        }
    }

    private Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("제대로된 형식의 시간이 아닙니다");
        }
    }

    private boolean checkWhetherAdminOrHost(Boolean isAdmin, String MemberID, String activityHostID) {

        if ((MemberID.equals(activityHostID)) || (isAdmin)) {
            return true;
        } else {
            return false;
        }
    }
}
