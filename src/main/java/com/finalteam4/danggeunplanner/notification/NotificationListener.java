package com.finalteam4.danggeunplanner.notification;

import com.finalteam4.danggeunplanner.notification.dto.reqeust.NotificationRequest;
import com.finalteam4.danggeunplanner.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @TransactionalEventListener
    @Async
    public void handleNotification(NotificationRequest request){
        notificationService.send(request);
    }
}
