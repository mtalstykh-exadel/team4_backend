package com.team4.testingsystem.converters;

import com.team4.testingsystem.model.dto.AnswerDTO;
import com.team4.testingsystem.model.dto.ListeningTopicDTO;
import com.team4.testingsystem.model.dto.QuestionDTO;
import com.team4.testingsystem.model.dto.TestDTO;
import com.team4.testingsystem.model.dto.UserDTO;
import com.team4.testingsystem.model.entity.Answer;
import com.team4.testingsystem.model.entity.ChosenOption;
import com.team4.testingsystem.model.entity.ContentFile;
import com.team4.testingsystem.model.entity.Level;
import com.team4.testingsystem.model.entity.Module;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.model.entity.TestQuestionID;
import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.Priority;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.services.AnswerService;
import com.team4.testingsystem.services.ChosenOptionService;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.ErrorReportsService;
import com.team4.testingsystem.services.QuestionService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TestConverterTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private ContentFilesService contentFilesService;

    @Mock
    private ChosenOptionService chosenOptionService;

    @Mock
    private AnswerService answerService;

    @Mock
    private ErrorReportsService errorReportsService;

    @InjectMocks
    private TestConverter testConverter;

    private static final String ESSAY_TEXT = "essay";
    private static final String SPEAKING_URL = "url";

    @Test
    void convertToDTO() {
        User user = EntityCreatorUtil.createUser();
        User coach = EntityCreatorUtil.createUser();
        Level level = EntityCreatorUtil.createLevel();
        com.team4.testingsystem.model.entity.Test test = com.team4.testingsystem.model.entity.Test.builder()
                .user(user)
                .status(Status.STARTED)
                .priority(Priority.LOW)
                .level(level)
                .assignedAt(Instant.now())
                .completedAt(Instant.now().plusSeconds(1))
                .verifiedAt(Instant.now().plusSeconds(2))
                .startedAt(Instant.now().plusSeconds(3))
                .deadline(Instant.now().plusSeconds(4))
                .finishTime(Instant.now().plusSeconds(5))
                .coach(coach)
                .build();

        List<Question> questions = new ArrayList<>();
        List<ChosenOption> chosenOptions = new ArrayList<>();
        for (Modules module : Modules.values()) {
            Question question = EntityCreatorUtil.createQuestion(user);
            question.setId((long) questions.size() + 1);
            Module module1 = new Module();
            module1.setName(module.getName());
            question.setModule(module1);

            if (module.equals(Modules.GRAMMAR) || module.equals(Modules.LISTENING)) {
                List<Answer> answers = Stream.generate(() -> EntityCreatorUtil.createAnswer(question))
                        .limit(4)
                        .collect(Collectors.toList());
                answers.get(0).setCorrect(true);
                chosenOptions.add(new ChosenOption(new TestQuestionID(test, question), answers.get(0)));

                question.setAnswers(answers);
            }

            questions.add(question);
        }
        ContentFile contentFile = new ContentFile("url");
        Mockito.when(questionService.getQuestionsByTestId(test.getId())).thenReturn(questions);
        Mockito.when(chosenOptionService.getAllByTestId(test.getId())).thenReturn(chosenOptions);
        Mockito.when(contentFilesService.getContentFileByQuestionId(any()))
                .thenReturn(contentFile);

        Mockito.when(answerService.tryDownloadEssay(test.getId())).thenReturn(Optional.of(ESSAY_TEXT));
        Mockito.when(answerService.tryDownloadSpeaking(test.getId())).thenReturn(Optional.of(SPEAKING_URL));
        Mockito.when(errorReportsService.getReportsByTest(test.getId())).thenReturn(List.of());

        TestDTO testDTO = testConverter.convertToDTO(test);

        Assertions.assertEquals(test.getId(), testDTO.getId());
        Assertions.assertEquals(level.getName(), testDTO.getLevel());

        Assertions.assertEquals(test.getAssignedAt(), testDTO.getAssignedAt());
        Assertions.assertEquals(test.getCompletedAt(), testDTO.getCompletedAt());
        Assertions.assertEquals(test.getStartedAt(), testDTO.getStartedAt());
        Assertions.assertEquals(test.getDeadline(), testDTO.getDeadline());
        Assertions.assertEquals(test.getFinishTime(), testDTO.getFinishTime());

        Assertions.assertEquals(Priority.LOW.getName(), testDTO.getPriority());
        Assertions.assertEquals(Status.STARTED.name(), testDTO.getStatus());
        Assertions.assertEquals(new UserDTO(coach), testDTO.getCoach());

        Assertions.assertEquals(ESSAY_TEXT, testDTO.getEssayText());
        Assertions.assertEquals(SPEAKING_URL, testDTO.getSpeakingUrl());
        Assertions.assertEquals(List.of(), testDTO.getErrorReports());
        Assertions.assertEquals(new ListeningTopicDTO(contentFile), testDTO.getContentFile());
        for (Modules module : Modules.values()) {
            QuestionDTO expectedQuestion = questions.stream()
                    .filter(question -> question.getModule().getName().equals(module.getName()))
                    .map(QuestionDTO::create)
                    .findFirst()
                    .orElseThrow();

            QuestionDTO actualQuestion = testDTO.getQuestions().get(module.getName()).get(0);

            Assertions.assertEquals(expectedQuestion.getId(), actualQuestion.getId());

            if (module == Modules.GRAMMAR) {
                actualQuestion.getAnswers().stream()
                        .filter(answer -> answer.getChecked() != null)
                        .filter(AnswerDTO::getChecked)
                        .findFirst()
                        .orElseThrow();
            }
        }
    }
}
