package com.team4.testingsystem.model.dto;

import com.team4.testingsystem.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class UserDTO implements Serializable {
    private Long id;
    private String name;
    private String login;
    private String roleName;
    private String avatar;

    private TestInfo assignedTest;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.login = user.getLogin();
        this.roleName = user.getRole().getRoleName();
        this.avatar = user.getAvatar();
    }

}
