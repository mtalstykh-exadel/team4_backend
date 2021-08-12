package com.team4.testingsystem.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "user")
    private Collection<Test> tests;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_login")
    private String login;

    @Column(name = "user_password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private UserRole role;

    @Column(name = "language")
    private String language;

    @Column(name = "avatar")
    private String avatar;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder id(Long id) {
            user.setId(id);
            return this;
        }

        public Builder name(String name) {
            user.setName(name);
            return this;
        }

        public Builder login(String login) {
            user.setLogin(login);
            return this;
        }

        public Builder password(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder role(UserRole role) {
            user.setRole(role);
            return this;
        }

        public Builder language(String language) {
            user.setLanguage(language);
            return this;
        }

        public Builder tests(Collection<Test> tests) {
            user.setTests(tests);
            return this;
        }

        public Builder avatar(String avatar) {
            user.setAvatar(avatar);
            return this;
        }

        public User build() {
            return user;
        }
    }

}
