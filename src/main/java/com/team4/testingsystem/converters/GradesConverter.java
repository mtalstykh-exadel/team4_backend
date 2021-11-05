package com.team4.testingsystem.converters;

import com.team4.testingsystem.model.dto.ModuleGradesDTO;
import com.team4.testingsystem.model.entity.ModuleGrade;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class GradesConverter {

    private final ModuleGradesService moduleGradesService;
    private final RestrictionsService restrictionsService;

    public ModuleGradesDTO convertListOfGradesToDTO(Test test) {
        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);

        Map<String, ModuleGrade> allGrades = moduleGradesService.getGradesByTest(test);
        return ModuleGradesDTO.builder()
                .grammar(moduleGradesService.getGradeByModule(allGrades, Modules.GRAMMAR))
                .listening(moduleGradesService.getGradeByModule(allGrades, Modules.LISTENING))
                .essay(moduleGradesService.getGradeByModule(allGrades, Modules.ESSAY))
                .speaking(moduleGradesService.getGradeByModule(allGrades, Modules.SPEAKING))
                .essayComment(moduleGradesService.getCoachCommentByModule(allGrades, Modules.ESSAY))
                .speakingComment(moduleGradesService.getCoachCommentByModule(allGrades, Modules.SPEAKING))
                .level(test.getLevel().getName())
                .status(test.getStatus())
                .build();
    }
}
