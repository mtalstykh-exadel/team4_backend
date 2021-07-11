package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Module;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.User;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UsersServiceImpl usersService;
    private final LevelServiceImpl levelService;
    private final ModuleServiceImpl moduleService;

    @Autowired
    QuestionServiceImpl(QuestionRepository questionRepository, UsersServiceImpl usersService, LevelServiceImpl levelService, ModuleServiceImpl moduleService) {
        this.questionRepository = questionRepository;
        this.usersService = usersService;
        this.levelService = levelService;
        this.moduleService = moduleService;
    }

    @Override
    public Question getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) throw new NotFoundException();
        return question;
    }

    @Override
    public Question createQuestion(String questionBody, boolean isAvailable, Long creatorUserId, Long levelID, Long moduleID) {
        Question question = new Question();
        question.setQuestionBody(questionBody);
        question.setAvailable(isAvailable);
        User user = usersService.getUserById(creatorUserId);
        question.setCreator(user);
        Level level = levelService.getLevelById(levelID);
        question.setLevel(level);
        Module module = moduleService.getModuleById(moduleID);
        question.setModule(module);
        questionRepository.save(question);
        return question;
    }

    @Override
    public Question updateAvailability(Long id, boolean isAvailable) {
        Question question = getQuestionById(id);
        question.setAvailable(isAvailable);
        questionRepository.save(question);
        return question;
    }

}
