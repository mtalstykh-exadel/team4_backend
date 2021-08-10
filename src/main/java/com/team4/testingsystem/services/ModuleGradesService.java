package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;

import java.util.Map;

public interface ModuleGradesService {

    Integer getGradeByModule(Map<String, Integer> gradesMap, Modules module);

    Map<String, Integer> getGradesByTest(Test test);

    void add(Test test, String moduleName, Integer grade);
}
