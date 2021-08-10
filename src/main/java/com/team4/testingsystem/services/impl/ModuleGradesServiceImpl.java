package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestModuleID;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.ModuleGradeNotFoundException;
import com.team4.testingsystem.repositories.ModuleGradesRepository;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuleGradesServiceImpl implements ModuleGradesService {

    private final ModuleGradesRepository moduleGradesRepository;
    private final ModuleService moduleService;

    @Autowired
    public ModuleGradesServiceImpl(ModuleGradesRepository moduleGradesRepository,
                                   ModuleService moduleService) {
        this.moduleGradesRepository = moduleGradesRepository;
        this.moduleService = moduleService;
    }

    @Override
    public Integer getGradeByModule(Map<String, Integer> gradeMap, Modules module) {
        return Optional.ofNullable(gradeMap.get(module.getName()))
                .orElseThrow(() -> new ModuleGradeNotFoundException(module.getName()));
    }

    @Override
    public Map<String, Integer> getGradesByTest(Test test) {
        List<ModuleGrade> grades = (List<ModuleGrade>) moduleGradesRepository.findAllById_Test(test);
        return grades
                .stream()
                .collect(Collectors.toMap(moduleGrade -> moduleGrade.getId().getModule().getName(),
                        ModuleGrade::getGrade));
    }

    @Override
    public void add(Test test, String moduleName, Integer grade) {

        Module module = moduleService.getModuleByName(moduleName);

        TestModuleID testModuleID = new TestModuleID(test, module);

        moduleGradesRepository.save(new ModuleGrade(testModuleID, grade));
    }
}
