package com.team4.testingsystem.services;

import com.team4.testingsystem.entities.ModuleGrade;

import java.util.Collection;

public interface ModuleGradesService {

    Collection<ModuleGrade> getGradesByTest(Long testId);

    void add(Long testId, String moduleName, Integer grade);
}
