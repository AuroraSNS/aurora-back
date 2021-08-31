package com.center.aurora.service.socket.notification.dto;

import com.center.aurora.domain.notification.Notification;
import com.center.aurora.domain.notification.NotificationStatus;
import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class FriendRequestListDto {

    private Long id;

    private Usr sender;

    private String message;

    private LocalDateTime timeStamp;

    private Boolean isRead;

    public FriendRequestListDto(Notification entity) {
        this.id = entity.getId();
        this.sender = new Usr(entity.getWriter());
        this.message = entity.getMessage();
        this.timeStamp = entity.getCreatedAt();
        this.isRead = entity.getStatus().name().equals(NotificationStatus.READ.name());
    }

    @NoArgsConstructor
    @Getter
    private class Usr {
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
