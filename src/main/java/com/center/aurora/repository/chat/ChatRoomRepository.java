package com.center.aurora.repository.chat;

import com.center.aurora.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select ch from ChatRoom ch where ch.participant1.id = :id or ch.participant2.id = :id")
    public List<ChatRoom> findAllMyChatRoom(@Param("id") Long id);

    public Boolean existsChatRoomById(Long id);

    @Query("select ch from ChatRoom ch where ch.participant1.id = :id1 and ch.participant2.id = :id2")
    public ChatRoom findByParticipant1IdAndParticipant2Id(@Param("id1") Long id1, @Param("id2") Long id2);
}
