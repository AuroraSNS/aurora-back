package com.center.aurora.repository.user;
import com.center.aurora.domain.user.friend.FriendId;
import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    List<Friend> findAllByMe_Id(Long id);

    Friend findByMe_IdAndYou_Id(Long meId, Long you_Id);


}
