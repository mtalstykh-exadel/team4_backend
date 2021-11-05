package com.team4.testingsystem.converters.notifications;

import com.team4.testingsystem.model.dto.NotificationDTO;
import com.team4.testingsystem.model.entity.CoachAnswer;
import com.team4.testingsystem.model.entity.ErrorReport;
import com.team4.testingsystem.model.entity.ErrorReportAnswer;
import com.team4.testingsystem.model.entity.Level;
import com.team4.testingsystem.model.entity.Notification;
import com.team4.testingsystem.model.entity.Question;
import com.team4.testingsystem.model.entity.TestQuestionID;
import com.team4.testingsystem.model.entity.User;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.NotificationType;
import com.team4.testingsystem.services.ErrorReportAnswerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestVerifiedNotificationConverterTest {
    @Mock
    private Notification notification;

    @Mock
    private com.team4.testingsystem.model.entity.Test test;

    @Mock
    private Level level;

    @Mock
    private User user;

    @Mock
    private ErrorReportAnswer errorReportAnswer;

    @Mock
    private ErrorReport errorReport;

    @Mock
    private CoachAnswer coachAnswer;

    @Mock
    private Question question;

    @Mock
    private TestQuestionID testQuestionID;

    @Mock
    private ErrorReportAnswerService errorReportAnswerService;

    @InjectMocks
    private TestVerifiedNotificationConverter converter;

    private static final Long NOTIFICATION_ID = 1L;
    private static final Long TEST_ID = 2L;
    private static final String LEVEL_NAME = Levels.A1.name();
    private static final String USER_EMAIL = "some@e.mail";
    private static final String USER_NAME = "user name";
    private static final String LANGUAGE = "rus";
    private static final String QUESTION_BODY = "question?";
    private static final String ERROR_REPORT = "error here!";
    private static final String REPORT_ANSWER = "nope, no error";

    @Test
    public void convertToDTO() {
        Instant createdAt = Instant.now();

        Mockito.when(notification.getId()).thenReturn(NOTIFICATION_ID);
        Mockito.when(notification.getCreatedAt()).thenReturn(createdAt);
        Mockito.when(notification.getTest()).thenReturn(test);
        Mockito.when(notification.getType()).thenReturn(NotificationType.TEST_VERIFIED);

        Mockito.when(notification.getUser()).thenReturn(user);
        Mockito.when(user.getLogin()).thenReturn(USER_EMAIL);
        Mockito.when(user.getName()).thenReturn(USER_NAME);
        Mockito.when(user.getLanguage()).thenReturn(LANGUAGE);

        Mockito.when(test.getId()).thenReturn(TEST_ID);
        Mockito.when(test.getLevel()).thenReturn(level);

        Mockito.when(level.getName()).thenReturn(LEVEL_NAME);

        Mockito.when(errorReportAnswerService.getReportsWithAnswersByTest(TEST_ID))
                .thenReturn(List.of(errorReportAnswer));

        Mockito.when(errorReportAnswer.getReport()).thenReturn(errorReport);
        Mockito.when(errorReport.getReportBody()).thenReturn(ERROR_REPORT);
        Mockito.when(errorReport.getId()).thenReturn(testQuestionID);
        Mockito.when(testQuestionID.getQuestion()).thenReturn(question);
        Mockito.when(question.getBody()).thenReturn(QUESTION_BODY);

        Mockito.when(errorReportAnswer.getAnswer()).thenReturn(coachAnswer);
        Mockito.when(coachAnswer.getComment()).thenReturn(REPORT_ANSWER);

        NotificationDTO dto = converter.convertToDTO(notification);

        Assertions.assertEquals(NOTIFICATION_ID, dto.getId());
        Assertions.assertEquals(NotificationType.TEST_VERIFIED, dto.getType());
        Assertions.assertEquals(TEST_ID, dto.getTestId());
        Assertions.assertEquals(createdAt, dto.getCreatedAt());
        Assertions.assertEquals(USER_EMAIL, dto.getUserEmail());
        Assertions.assertEquals(USER_NAME, dto.getUserName());
        Assertions.assertEquals(LANGUAGE, dto.getLanguage());
        Assertions.assertEquals(LEVEL_NAME, dto.getLevel());
        Assertions.assertEquals(1, dto.getReportAnswers().size());
        Assertions.assertEquals(QUESTION_BODY, dto.getReportAnswers().get(0).getQuestion());
        Assertions.assertEquals(ERROR_REPORT, dto.getReportAnswers().get(0).getReport());
        Assertions.assertEquals(REPORT_ANSWER, dto.getReportAnswers().get(0).getAnswer());

        Assertions.assertNull(dto.getFinishTime());
        Assertions.assertNull(dto.getDeadline());
        Assertions.assertNull(dto.getPriority());
    }

    @Test
    public void converterType() {
        Assertions.assertEquals(NotificationType.TEST_VERIFIED, converter.converterType());
    }
}
