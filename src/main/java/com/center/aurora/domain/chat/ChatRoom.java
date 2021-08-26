package com.center.aurora.domain.chat;

import com.center.aurora.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User participant1;

    @ManyToOne(fetch = FetchType.LAZY)
    private User participant2;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Message> messages = new ArrayList<>();

    @Builder
    public ChatRoom(User participant1, User participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;
    }
}
