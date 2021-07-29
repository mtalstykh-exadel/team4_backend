package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.TestGeneratingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TestGeneratingServiceImpl implements TestGeneratingService {

    private final QuestionRepository questionService;
    private final ContentFilesServiceImpl contentFilesService;

    private static final Pageable COUNT_QUESTIONS_GRAMMAR = Pageable.ofSize(12);
    private static final Pageable COUNT_QUESTIONS_LISTENING = Pageable.ofSize(10);
    private static final Pageable ONE_ELEMENT = Pageable.ofSize(1);

    @Autowired
    public TestGeneratingServiceImpl(QuestionRepository questionService,
                                     ContentFilesServiceImpl contentFilesService) {
        this.questionService = questionService;
        this.contentFilesService = contentFilesService;
    }

    @Override
    public Test generateTest(Test test) {
        setQuestionsByModules(test, Modules.GRAMMAR.getName(), COUNT_QUESTIONS_GRAMMAR);
        setQuestionsByContentFile(test);
        setQuestionsByModules(test,  Modules.ESSAY.getName(), ONE_ELEMENT);
        setQuestionsByModules(test,  Modules.SPEAKING.getName(), ONE_ELEMENT);
        return test;
    }


    private void setQuestionsByModules(Test test, String module, Pageable pageable){
        Collection<Question> questions = questionService
                .getRandomQuestions(test.getLevel().getName(), module, pageable);
        questions.forEach(test::setQuestion);
    }

    private void setQuestionsByContentFile(Test test){
        ContentFile contentFile = contentFilesService
                .getRandomContentFiles(test.getLevel().getName(), ONE_ELEMENT).get(0);
        Collection<Question> questions = questionService
                .getRandomQuestionByContentFile(contentFile.getId(), COUNT_QUESTIONS_LISTENING);
        questions.forEach(test::setQuestion);
    }

}
