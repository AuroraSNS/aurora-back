package com.center.aurora.repository.socket;

import com.center.aurora.domain.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
}
