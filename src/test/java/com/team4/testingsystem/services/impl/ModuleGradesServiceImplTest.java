package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.exceptions.ModuleGradeNotFoundException;
import com.team4.testingsystem.exceptions.ModuleNotFoundException;
import com.team4.testingsystem.repositories.ModuleGradesRepository;
import com.team4.testingsystem.services.ModuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ModuleGradesServiceImplTest {

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
    private ModuleGradesDTO moduleGradesDTO;

    @Mock
    private Module module;

    @Mock
    private ModuleService moduleService;

    @Test
    void getGradesByTestSuccess() {
        Mockito.when(moduleGradesRepository.findAllById_Test(test)).thenReturn(moduleGradeList);

        try (MockedStatic<ModuleGradesDTO> builderMockedStatic = Mockito.mockStatic(ModuleGradesDTO.class)) {
            builderMockedStatic.when(ModuleGradesDTO::builder).thenReturn(builder);

            Mockito.when(moduleGradeList.stream()).thenReturn(stream);

            Mockito.when(stream.filter(any())).thenReturn(stream);

            Mockito.when(stream.findAny()).thenReturn(Optional.of(moduleGrade));

            Mockito.when(moduleGrade.getGrade()).thenReturn(10);

            Mockito.when(builder.grammar(10)).thenReturn(builder);

            Mockito.when(builder.listening(10)).thenReturn(builder);

            Mockito.when(builder.essay(10)).thenReturn(builder);

            Mockito.when(builder.speaking(10)).thenReturn(builder);

            Mockito.when(builder.build()).thenReturn(moduleGradesDTO);

            Assertions.assertEquals(moduleGradesDTO, moduleGradesService.getGradesByTest(test));

        }
    }

    @Test
    void getGradesByTestFail() {
        Mockito.when(moduleGradesRepository.findAllById_Test(test)).thenReturn(moduleGradeList);

        try (MockedStatic<ModuleGradesDTO> builderMockedStatic = Mockito.mockStatic(ModuleGradesDTO.class)) {
            builderMockedStatic.when(ModuleGradesDTO::builder).thenReturn(builder);

            Mockito.when(moduleGradeList.stream()).thenReturn(stream);

            Mockito.when(stream.filter(any())).thenReturn(stream);

            Mockito.when(stream.findAny()).thenReturn(Optional.empty());
        }

        Assertions.assertThrows(ModuleGradeNotFoundException.class, () -> moduleGradesService.getGradesByTest(test));
    }

    @Test
    void addSuccess() {
        Mockito.when(moduleService.getModuleByName(anyString())).thenReturn(module);

        moduleGradesService.add(test, "name", 1);
        verify(moduleGradesRepository).save(any(ModuleGrade.class));
    }

    @Test
    void addFail() {
        Mockito.when(moduleService.getModuleByName(anyString())).thenThrow(ModuleNotFoundException.class);

        Assertions.assertThrows(ModuleNotFoundException.class,
                ()->moduleGradesService.add(test, "12345", 0));
    }

}



