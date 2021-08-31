package com.center.aurora.service.notification.dto.NotificationSendDto;

import com.center.aurora.domain.notification.NotificationType;
import com.center.aurora.service.notification.dto.NotificationRecvDto;


public class ChatNotificationDto extends NotificationSendDto{

    public ChatNotificationDto(NotificationRecvDto recvDto) {
        super(NotificationType.CHATTING.name());
    }
}
