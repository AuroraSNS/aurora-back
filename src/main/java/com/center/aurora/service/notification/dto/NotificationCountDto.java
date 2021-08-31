package com.center.aurora.service.notification.dto;

import com.center.aurora.domain.notification.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
public class NotificationCountDto {

    private Long chatting;
    private Long normal;
    private Long friend;

    public NotificationCountDto(Map<String, Long> map) {
        this.chatting = map.get(NotificationType.CHATTING.name());
        this.normal = map.get(NotificationType.POST.name()) + map.get(NotificationType.FRIEND_ACCEPT.name());
        this.friend = map.get(NotificationType.FRIEND_REQUEST.name());
    }
}
