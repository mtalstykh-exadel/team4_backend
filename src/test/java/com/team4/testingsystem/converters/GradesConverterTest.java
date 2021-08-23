package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GradesConverterTest {

    private final int GRAMMAR_SCORE = 1;

    private final int LISTENING_SCORE = 2;

    private final int ESSAY_SCORE = 3;

    private final int SPEAKING_SCORE = 4;

    private final long GOOD_USER_ID = 1L;


    @InjectMocks
    private GradesConverter gradesConverter;

    @Mock
    private ModuleGradesService moduleGradesService;

    @Mock
    private Map<String, ModuleGrade> gradesMap;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private CustomUserDetails customUserDetails;

    @Mock
    private RestrictionsService restrictionsService;

    @Mock
    private Level level;

    @Test
    void convertListOfGradesToDTOSuccess() {
        try (MockedStatic<JwtTokenUtil> mockedStatic = Mockito.mockStatic(JwtTokenUtil.class)) {
            mockedStatic.when(JwtTokenUtil::extractUserDetails).thenReturn(customUserDetails);
            Mockito.when(customUserDetails.getId()).thenReturn(GOOD_USER_ID);


            Mockito.when(moduleGradesService.getGradesByTest(test)).thenReturn(gradesMap);

            Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.GRAMMAR)).thenReturn(GRAMMAR_SCORE);

            Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.LISTENING)).thenReturn(LISTENING_SCORE);

            Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.ESSAY)).thenReturn(ESSAY_SCORE);

            Mockito.when(moduleGradesService.getGradeByModule(gradesMap, Modules.SPEAKING)).thenReturn(SPEAKING_SCORE);

            Mockito.when(moduleGradesService.getCoachCommentByModule(gradesMap, Modules.ESSAY)).thenReturn("Cool essay");

            Mockito.when(moduleGradesService.getCoachCommentByModule(gradesMap, Modules.SPEAKING))
                    .thenReturn("Cool speaking");

            ModuleGradesDTO moduleGradesDTO = EntityCreatorUtil.createModuleGradesDTO();

            Mockito.when(test.getStatus()).thenReturn(moduleGradesDTO.getStatus());
            Mockito.when(test.getLevel()).thenReturn(level);
            Mockito.when(level.getName()).thenReturn(moduleGradesDTO.getLevel());

            gradesConverter.convertListOfGradesToDTO(test);

            verify(restrictionsService).checkOwnerIsCurrentUser(test, GOOD_USER_ID);

            Assertions.assertEquals(moduleGradesDTO, gradesConverter.convertListOfGradesToDTO(test));
        }
    }
}
