package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.TestGeneratingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TestGeneratingServiceImpl implements TestGeneratingService {

    private final QuestionRepository questionService;
    private final ContentFilesServiceImpl contentFilesService;

    @Value("${test-generating.const.question.count}")
    private Integer count;

    private static final Integer oneElement = 1;


    @Autowired
    public TestGeneratingServiceImpl(QuestionRepository questionService,
                                     ContentFilesServiceImpl contentFilesService) {
        this.questionService = questionService;
        this.contentFilesService = contentFilesService;
    }

    @Override
    public Test formTest(Test test) {
        setQuestionsByModules(test, Modules.GRAMMAR.getName(), Pageable.ofSize(count));
        setQuestionsByContentFile(test);
        setQuestionsByModules(test, Modules.ESSAY.getName(), Pageable.ofSize(oneElement));
        setQuestionsByModules(test, Modules.SPEAKING.getName(), Pageable.ofSize(oneElement));
        return test;
    }


    private void setQuestionsByModules(Test test, String module, Pageable pageable) {
        Collection<Question> questions = questionService
                .getRandomQuestions(test.getLevel().getName(), module, pageable);
        questions.forEach(test::setQuestion);
    }

    private void setQuestionsByContentFile(Test test) {
        ContentFile contentFile = contentFilesService
                .getRandomContentFile(test.getLevel().getName());
        Collection<Question> questions = questionService
                .getRandomQuestionByContentFile(contentFile.getId(), Pageable.ofSize(count));
        questions.forEach(test::setQuestion);
    }

}
