package com.center.aurora.repository.user;

import com.center.aurora.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("Select u From User u where u.name like :name%")
    List<User> findFriendsByName(@Param("name") String name);
}
