package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.ContentFile;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.model.entity.Test;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.exceptions.NotEnoughQuestionsException;
import com.team4.testingsystem.exceptions.NotFoundException;
import com.team4.testingsystem.repositories.QuestionRepository;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.TestGeneratingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestGeneratingServiceImpl implements TestGeneratingService {

    private final QuestionRepository questionRepository;
    private final ContentFilesService contentFilesService;

    @Value("${test-generating.const.question.count}")
    private Integer count;

    private static final Integer oneElement = 1;


    @Autowired
    public TestGeneratingServiceImpl(QuestionRepository questionRepository,
                                     ContentFilesService contentFilesService) {
        this.questionRepository = questionRepository;
        this.contentFilesService = contentFilesService;
    }

    @Override
    public Test formTest(Test test) {
        setQuestionsByModules(test,
                Modules.GRAMMAR.getName(),
                Pageable.ofSize(count));
        setQuestionsByContentFile(test);
        setQuestionsByModules(test, Modules.ESSAY.getName(), Pageable.ofSize(oneElement));
        setQuestionsByModules(test, Modules.SPEAKING.getName(), Pageable.ofSize(oneElement));
        return test;
    }

    private void setQuestionsByModules(Test test, String module, Pageable pageable) {
        List<Question> questions = questionRepository
                .getRandomQuestions(test.getLevel().getName(), module, pageable);

        if (questions.size() < pageable.getPageSize()) {
            throw new NotEnoughQuestionsException("Not enough questions for " + module + " " + test.getLevel());
        }

        test.addAllQuestions(questions);
    }

    private void setQuestionsByContentFile(Test test) {

        try {
            ContentFile contentFile = contentFilesService
                    .getRandomContentFile(test.getLevel().getName());

            List<Question> questions = questionRepository
                    .getRandomQuestionByContentFile(contentFile.getId(), Pageable.ofSize(count));

            test.addAllQuestions(questions);
        } catch (NotFoundException e) {
            throw new NotEnoughQuestionsException("There are no content files in the database for this level");
        }
    }
}
