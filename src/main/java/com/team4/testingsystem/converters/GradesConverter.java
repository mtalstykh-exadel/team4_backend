package com.team4.testingsystem.converters;

import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.entities.ModuleGrade;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ModuleGradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GradesConverter {

    private final ModuleGradesService moduleGradesService;

    @Autowired
    public GradesConverter(ModuleGradesService moduleGradesService) {
        this.moduleGradesService = moduleGradesService;
    }

    public ModuleGradesDTO convertListOfGradesToDTO(Test test) {
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
