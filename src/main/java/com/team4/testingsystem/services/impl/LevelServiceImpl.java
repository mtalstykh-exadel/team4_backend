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
        return levelRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Level getLevelByName(String name) {
        return levelRepository.findByName(name).orElseThrow(NotFoundException::new);
    }
}
