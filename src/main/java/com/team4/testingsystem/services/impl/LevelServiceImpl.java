package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.exceptions.LevelNotFoundException;
import com.team4.testingsystem.repositories.LevelRepository;
import com.team4.testingsystem.services.LevelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LevelServiceImpl implements LevelService {
    private final LevelRepository levelRepository;

    @Override
    public Level getLevelById(Long id) {
        return levelRepository.findById(id).orElseThrow(LevelNotFoundException::new);
    }

    @Override
    public Level getLevelByName(String name) {
        return levelRepository.findByName(name).orElseThrow(LevelNotFoundException::new);
    }
}
