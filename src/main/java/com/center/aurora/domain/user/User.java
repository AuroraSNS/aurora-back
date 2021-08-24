package com.center.aurora.domain.user;

import com.center.aurora.domain.post.Image;
import com.center.aurora.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String email;

    private String image;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column
    private String providerId;

    @ManyToMany
    @JoinTable(name = "friend",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "user_id")
    )
    Set<User> friends = new TreeSet<>();

    @ManyToMany
    @JoinTable(name = "friend",
            joinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    Set<User> friendOf = new TreeSet<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    List<Post> posts = new ArrayList<>();

    public User update(String name) {
        this.name = name;
        return this;
    }

    public void addFriends(User friend){
        this.friends.add(friend);
        friend.getFriends().add(this);
    }

    public void deleteFriend(User friend){
        this.friends.remove(friend);
        friend.getFriends().remove(this);
    }

    @Builder
    public User(String name, String email, String image, String bio, Role role, AuthProvider provider, String providerId) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.bio = bio;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getImage(), user.getImage()) && Objects.equals(getBio(), user.getBio()) && getRole() == user.getRole() && getProvider() == user.getProvider() && Objects.equals(getProviderId(), user.getProviderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getImage(), getBio(), getRole(), getProvider(), getProviderId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", bio='" + bio + '\'' +
                ", role=" + role +
                ", provider=" + provider +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
