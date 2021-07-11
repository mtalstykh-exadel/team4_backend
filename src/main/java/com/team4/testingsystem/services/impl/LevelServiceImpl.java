package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.services.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl implements LevelService {
    private final LevelRepository levelRepository;

    @Autowired
    public LevelServiceImpl(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public Level getLevelById(Long id) {
        Level level = levelRepository.findById(id).orElse(null);
        if (level == null) throw new NotFoundException();
        return level;
    }
}
