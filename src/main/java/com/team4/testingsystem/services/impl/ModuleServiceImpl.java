package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.exceptions.QuestionNotFoundException;
import com.team4.testingsystem.repositories.ModuleRepository;
import com.team4.testingsystem.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @Override
    public Module getModuleByName(String name) {
        return moduleRepository.findByName(name).orElseThrow(QuestionNotFoundException::new);
    }
}
