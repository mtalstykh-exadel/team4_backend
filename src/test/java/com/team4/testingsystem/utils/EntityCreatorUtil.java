package com.team4.testingsystem.utils;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;

public class EntityCreatorUtil {

    public static Question createQuestion() {
        return new Question.Builder()
                .id(1L)
                .questionBody("some text")
                .module(createModule())
                .level(createLevel())
                .creator(createUser())
                .isAvailable(true)
                .build();
    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setLanguage("en");
        user.setLogin("login");
        user.setPassword("password");
        return user;
    }

    public static Module createModule() {
        Module module = new Module();
        module.setId(1L);
        module.setName("new module");
        return module;
    }

    public static Level createLevel() {
        Level level = new Level();
        level.setId(1L);
        level.setName("new level");
        return level;
    }



}
