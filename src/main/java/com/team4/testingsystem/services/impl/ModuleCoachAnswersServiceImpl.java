package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.CoachAnswer;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.ModuleCoachAnswer;
import com.team4.testingsystem.repositories.ModuleCoachAnswerRepository;
import com.team4.testingsystem.services.ModuleCoachAnswersService;
import com.team4.testingsystem.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleCoachAnswersServiceImpl implements ModuleCoachAnswersService {
    private final ModuleService moduleService;
    private final ModuleCoachAnswerRepository moduleCoachAnswerRepository;

    @Autowired
    public ModuleCoachAnswersServiceImpl(ModuleService moduleService,
                                         ModuleCoachAnswerRepository moduleCoachAnswerRepository) {
        this.moduleService = moduleService;
        this.moduleCoachAnswerRepository = moduleCoachAnswerRepository;
    }

    @Override
    public void add(String moduleName, List<CoachAnswer> coachAnswers)  {

        Module module = moduleService.getModuleByName(moduleName);

        List<ModuleCoachAnswer> moduleCoachAnswers = coachAnswers.stream()
                .map(coachAnswer -> new ModuleCoachAnswer(module, coachAnswer))
                .collect(Collectors.toList());

        moduleCoachAnswerRepository.saveAll(moduleCoachAnswers);
    }
}
