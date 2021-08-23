package com.team4.testingsystem.entities;

import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserTest implements Serializable {
    private User user;
    private Test test;

    public UserDTO toUserDTO() {
        UserDTO userDTO = new UserDTO(user);
        if (test != null) {
            userDTO.setAssignedTest(new TestInfo(test));
        }
        return userDTO;
    }

}
