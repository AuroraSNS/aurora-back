package com.center.aurora.service.notification.dto.NotificationSendDto;

import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.service.notification.dto.NotificationRecvDto;

public class PostNotificationDto extends NotificationSendDto{

    public PostNotificationDto(NotificationRecvDto recvDto) {
        super(NotificationType.POST.name());
    }
}
