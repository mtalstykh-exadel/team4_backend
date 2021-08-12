package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.User;

import java.io.Serializable;
import java.util.Objects;

public class UserDTO implements Serializable {
    private Long id;
    private String name;
    private String login;
    private String roleName;
    private String avatar;

    private TestInfo assignedTest;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.login = user.getLogin();
        this.roleName = user.getRole().getRoleName();
        this.avatar = user.getAvatar();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public TestInfo getAssignedTest() {
        return assignedTest;
    }

    public void setAssignedTest(TestInfo assignedTest) {
        this.assignedTest = assignedTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id)
                && Objects.equals(name, userDTO.name)
                && Objects.equals(login, userDTO.login)
                && Objects.equals(roleName, userDTO.roleName)
                && Objects.equals(avatar, userDTO.avatar)
                && Objects.equals(assignedTest, userDTO.assignedTest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, roleName, avatar, assignedTest);
    }
}
