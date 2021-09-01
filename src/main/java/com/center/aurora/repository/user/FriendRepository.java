package com.center.aurora.repository.user;

import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {
}
