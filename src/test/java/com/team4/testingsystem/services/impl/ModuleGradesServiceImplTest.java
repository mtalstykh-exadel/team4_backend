package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.ModuleGradeNotFoundException;
import com.team4.testingsystem.exceptions.ModuleNotFoundException;
import com.team4.testingsystem.repositories.ModuleGradesRepository;
import com.team4.testingsystem.services.ModuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ModuleGradesServiceImplTest {

    final int GRAMMAR_SCORE = 1;

    @InjectMocks
    private ModuleGradesServiceImpl moduleGradesService;

    @Mock
    private List<ModuleGrade> moduleGradeList;

    @Mock
    private ModuleGradesRepository moduleGradesRepository;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private ModuleGradesDTO.Builder builder;

    @Mock
    private Stream<ModuleGrade> stream;

    @Mock
    private ModuleGrade moduleGrade;

    @Mock
    private Map<String, ModuleGrade> gradesMap;

    @Mock
    private Module module;

    @Mock
    private ModuleService moduleService;

    @Test
    void getGradeByModuleSuccess(){
        Mockito.when(gradesMap.get(Modules.GRAMMAR.getName())).thenReturn(moduleGrade);
        Mockito.when(moduleGrade.getGrade()).thenReturn((GRAMMAR_SCORE));

        Assertions.assertEquals(GRAMMAR_SCORE, moduleGradesService.getGradeByModule(gradesMap, Modules.GRAMMAR));
    }

    @Test
    void getGradeByModuleFail() {
        Mockito.when(gradesMap.get(Modules.LISTENING.getName())).thenReturn(null);
        Assertions.assertThrows(ModuleGradeNotFoundException.class,
                ()->moduleGradesService.getGradeByModule(gradesMap, Modules.LISTENING));
    }

    @Test
    void getGradesByTestSuccess() {
        Mockito.when(moduleGradesRepository.findAllById_Test(test)).thenReturn(moduleGradeList);

        Mockito.when(moduleGradeList.stream()).thenReturn(stream);

        Mockito.when(stream.collect(any())).thenReturn(gradesMap);

        Assertions.assertEquals(gradesMap, moduleGradesService.getGradesByTest(test));

    }

    @Test
    void addSuccess() {
        Mockito.when(moduleService.getModuleByName(anyString())).thenReturn(module);

        moduleGradesService.add(test, Modules.GRAMMAR.getName(), 1, null);
        verify(moduleGradesRepository).save(any(ModuleGrade.class));
    }

    @Test
    void addFail() {
        Mockito.when(moduleService.getModuleByName(anyString())).thenThrow(ModuleNotFoundException.class);

        Assertions.assertThrows(ModuleNotFoundException.class,
                ()->moduleGradesService.add(test, "12345", 0, null));
    }

}



