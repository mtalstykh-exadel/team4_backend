package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class GradesConverterTest {

    private final int GRAMMAR_SCORE = 1;

    private final int LISTENING_SCORE = 2;

    private final int ESSAY_SCORE = 3;

    private final int SPEAKING_SCORE = 4;


    @InjectMocks
    private GradesConverter gradesConverter;

    @Mock
    private ModuleGradesService moduleGradesService;

    @Mock
    private Map<String, ModuleGrade> gradesMap;

    @Test
    void convertListOfGradesToDTOSuccess(){
        Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.GRAMMAR)).thenReturn(GRAMMAR_SCORE);

        Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.LISTENING)).thenReturn(LISTENING_SCORE);

        Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.ESSAY)).thenReturn(ESSAY_SCORE);

        Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.SPEAKING)).thenReturn(SPEAKING_SCORE);

        Mockito.when(moduleGradesService.getCoachCommentByModule(gradesMap, Modules.ESSAY)).thenReturn("Cool essay");

        Mockito.when(moduleGradesService.getCoachCommentByModule(gradesMap, Modules.SPEAKING))
                .thenReturn("Cool speaking");

        ModuleGradesDTO moduleGradesDTO = EntityCreatorUtil.createModuleGradesDTO();

        Assertions.assertEquals(moduleGradesDTO, gradesConverter.convertListOfGradesToDTO(gradesMap));
    }
}
