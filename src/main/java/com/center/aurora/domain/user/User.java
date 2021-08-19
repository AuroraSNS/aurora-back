package com.center.aurora.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public User update(String name) {
        this.name = name;
        return this;
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
}
