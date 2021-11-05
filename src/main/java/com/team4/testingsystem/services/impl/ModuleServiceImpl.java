package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.Module;
import com.team4.testingsystem.exceptions.ModuleNotFoundException;
import com.team4.testingsystem.repositories.ModuleRepository;
import com.team4.testingsystem.services.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;

    @Override
    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElseThrow(ModuleNotFoundException::new);
    }

    @Override
    public Module getModuleByName(String name) {
        return moduleRepository.findByName(name).orElseThrow(ModuleNotFoundException::new);
    }
}
