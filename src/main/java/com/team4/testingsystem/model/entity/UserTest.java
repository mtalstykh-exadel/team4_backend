package com.team4.testingsystem.model.entity;

import com.team4.testingsystem.model.dto.TestInfo;
import com.team4.testingsystem.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
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
