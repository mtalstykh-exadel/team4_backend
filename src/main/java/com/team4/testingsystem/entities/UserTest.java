package com.team4.testingsystem.entities;

import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.dto.UserDTO;

import java.io.Serializable;
import java.util.Objects;

public class UserTest implements Serializable {
    private User user;
    private Test test;

    public UserTest(User user, Test test) {
        this.user = user;
        this.test = test;
    }

    public UserDTO toUserDTO() {
        UserDTO userDTO = new UserDTO(user);
        if (test != null) {
            userDTO.setAssignedTest(new TestInfo(test));
        }
        return userDTO;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserTest userTest = (UserTest) o;
        return Objects.equals(user, userTest.user)
                && Objects.equals(test, userTest.test);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, test);
    }
}
