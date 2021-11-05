package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.Level;

public interface LevelService {
    Level getLevelById(Long id);

    Level getLevelByName(String name);
}
