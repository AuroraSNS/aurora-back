package com.center.aurora.domain.notification;

import com.center.aurora.domain.user.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Enumerated(value = EnumType.STRING)
    private NotificationStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
