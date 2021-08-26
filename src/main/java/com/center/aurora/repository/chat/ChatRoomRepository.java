package com.center.aurora.repository.chat;

import com.center.aurora.domain.chat.ChatRoom;
import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    public List<ChatRoom> findAllByParticipant1OrParticipant2(Long id1, Long id2);

    @Query("select ch from ChatRoom ch where ch.participant1.id = :id or ch.participant2.id = :id")
    public List<ChatRoom> findAllMyChatRoom(@Param("id") Long id);

    public Boolean existsChatRoomById(Long id);
}