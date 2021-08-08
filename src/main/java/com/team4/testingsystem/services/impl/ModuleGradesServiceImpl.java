package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachGrade;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestModuleID;
import com.team4.testingsystem.entities.TestQuestionID;
import com.team4.testingsystem.repositories.ModuleGradesRepository;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.ModuleService;
import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class ModuleGradesServiceImpl implements ModuleGradesService {

    private TestsService testsService;
    private ModuleGradesRepository moduleGradesRepository;
    private ModuleService moduleService;

    @Autowired
    public ModuleGradesServiceImpl(TestsService testsService,
                                   ModuleGradesRepository moduleGradesRepository,
                                   ModuleService moduleService) {
        this.testsService = testsService;
        this.moduleGradesRepository = moduleGradesRepository;
        this.moduleService = moduleService;
    }

    @Override
    public Collection<ModuleGrade> getGradesByTest(Long testId){
        Test test = testsService.getById(testId);
        return moduleGradesRepository.findAllById_Test(test);
    }

    @Override
    public void add (Long testId, String moduleName, Integer grade){
        Test test = testsService.getById(testId);

        Module module = moduleService.getModuleByName(moduleName);

        TestModuleID testModuleID = new TestModuleID(test, module);

        moduleGradesRepository.save(new ModuleGrade(testModuleID, grade));
    }

}
