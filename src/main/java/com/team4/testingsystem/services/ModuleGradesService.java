package com.team4.testingsystem.services;

import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.entities.Test;

public interface ModuleGradesService {

    ModuleGradesDTO getGradesByTest(Test test);

    void add(Test test, String moduleName, Integer grade);
}
