package com.team4.testingsystem.services;

import com.team4.testingsystem.model.entity.Module;

public interface ModuleService {
    Module getModuleById(Long id);

    Module getModuleByName(String name);
}
