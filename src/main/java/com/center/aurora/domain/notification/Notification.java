package com.center.aurora.domain.notification;

import com.center.aurora.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient")
    private User recipient;

    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    private Long targetId;

    private String message;

    @Enumerated(value = EnumType.STRING)
    private NotificationStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Notification(User writer, User recipient, NotificationType type, Long targetId, String message, NotificationStatus status) {
        this.writer = writer;
        this.recipient = recipient;
        this.type = type;
        this.targetId = targetId;
        this.message = message;
        this.status = status;
    }

    public void read(){
        this.status = NotificationStatus.READ;
    }
}
