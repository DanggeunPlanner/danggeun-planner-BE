package com.finalteam4.danggeunplanner.notification.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.notification.dto.reqeust.NotificationRequest;
import com.finalteam4.danggeunplanner.notification.dto.response.NotificationConfirmResponse;
import com.finalteam4.danggeunplanner.notification.dto.response.NotificationReadResponse;
import com.finalteam4.danggeunplanner.notification.dto.response.NotificationResponse;
import com.finalteam4.danggeunplanner.notification.entity.Notification;
import com.finalteam4.danggeunplanner.notification.repository.EmitterRepository;
import com.finalteam4.danggeunplanner.notification.repository.EmitterRepositoryImpl;
import com.finalteam4.danggeunplanner.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_NOTIFICATION;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final Long timeout = 60L * 1000L * 60L;

    public SseEmitter subscribe(Member member, String lastEventId) {
        String emitterId = makeTimeIncludeId(member);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(member);
        sendDummyData(emitterId, emitter, eventId, "EventStream Created. [memberId="+member.getId()+"]");

        if (hasLostData(lastEventId)){
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(member.getId()));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendDummyData(emitterId, emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

    public void send(NotificationRequest request){
        Notification notification = saveNotification(request);
        sendNotification(request, notification);
    }

    @Async
    public void sendNotification(NotificationRequest request, Notification notification) {
        String receiverId = String.valueOf(request.getMember().getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendDummyData(key, emitter, eventId, new NotificationResponse(notification));
                }
        );
    }

    @Transactional
    public Notification saveNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .content(request.getContent())
                .groupId(request.getGroupId())
                .isRead(false)
                .notificationType(request.getNotificationType())
                .member(request.getMember())
                .build();
        notificationRepository.save(notification);
        return notification;
    }

    private void sendDummyData(String emitterId, SseEmitter emitter, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }
    private String makeTimeIncludeId(Member member) {
        return member.getId() + "_" + System.currentTimeMillis();
    }
    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    @Transactional
    public List<NotificationResponse> findNotificationList(Member member) {
        List<Notification> notifications = notificationRepository.findAllByMember_Id(member.getId());
        List<NotificationResponse> response = new ArrayList<>();
        for (Notification notification : notifications) {
            response.add(new NotificationResponse(notification));
        }
        return response;
    }

    @Transactional
    public NotificationConfirmResponse confirmNotification(Member member, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_NOTIFICATION)
        );
        if (member.getId().equals(notification.getMember().getId())) {
            notificationRepository.deleteById(notificationId);
        }
        return new NotificationConfirmResponse(notification);
    }

    @Transactional
    public void readNotification(Member member) {
        notificationRepository.updateAllIsRead(member.getId());
    }

    @Transactional(readOnly = true)
    public NotificationReadResponse readFindNotification(Member member) {
        if (notificationRepository.existsByIsReadAndMember(false, member)) {
            return new NotificationReadResponse(false);
        }
        return new NotificationReadResponse(true);
    }

    @Transactional(readOnly = true)
    public Long countUnReadNotification(Member member) {
        return notificationRepository.countUnReadNotification(member.getId());
    }
}
