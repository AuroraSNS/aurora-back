package com.center.aurora.service.socket.notification.dto;

import com.center.aurora.domain.notification.Notification;
import com.center.aurora.domain.notification.NotificationStatus;
import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class NotificationListDto {

    private Long id;
    private String type;
    private Usr sender;
    private String message;
    private Long targetId;
    private LocalDateTime timeStamp;
    private Boolean isRead;

    public NotificationListDto(Notification entity) {
        this.id = entity.getId();
        this.type = entity.getType().name();
        this.sender = new Usr(entity.getWriter());
        this.message = entity.getMessage();
        this.targetId = entity.getTargetId();
        this.timeStamp = entity.getCreatedAt();
        this.isRead = entity.getStatus().name().equals(NotificationStatus.READ.name());
    }

    @NoArgsConstructor
    @Getter
    class Usr{
        private Long id;
        private String name;
        private String avatar;

        public Usr(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.avatar = user.getImage();
        }
    }

}
