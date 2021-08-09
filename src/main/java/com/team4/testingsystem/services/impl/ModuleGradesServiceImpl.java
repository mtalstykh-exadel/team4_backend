package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.entities.TestModuleID;
import com.team4.testingsystem.exceptions.ModuleGradeNotFoundException;
import com.team4.testingsystem.repositories.ModuleGradesRepository;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private Integer getGradeByModule(List<ModuleGrade> allTestGrades, String moduleName) {
        return allTestGrades
                .stream()
                .filter(grade -> grade.getId().getModule().getName().equals(moduleName))
                .findAny()
                .orElseThrow(() -> new ModuleGradeNotFoundException(moduleName))
                .getGrade();
    }

    @Override
    public ModuleGradesDTO getGradesByTest(Test test) {

        List<ModuleGrade> allTestGrades = (List<ModuleGrade>) moduleGradesRepository.findAllById_Test(test);

        return ModuleGradesDTO.builder()
                .grammar(getGradeByModule(allTestGrades, "Grammar"))
                .listening(getGradeByModule(allTestGrades, "Listening"))
                .essay(getGradeByModule(allTestGrades, "Essay"))
                .speaking(getGradeByModule(allTestGrades, "Speaking"))
                .build();

    }

    @Override
    public void add(Test test, String moduleName, Integer grade) {

        Module module = moduleService.getModuleByName(moduleName);

        TestModuleID testModuleID = new TestModuleID(test, module);

        moduleGradesRepository.save(new ModuleGrade(testModuleID, grade));
    }
}
