package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.entities.Timer;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.repositories.TimerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.List;
import java.util.TimerTask;

@ExtendWith(MockitoExtension.class)
class TimerServiceImplTest {
    @Mock
    private TimerRepository timerRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TimerServiceImpl timerService;

    @Mock
    private com.team4.testingsystem.entities.Test test;

    @Mock
    private Timer timer;

    private static final Long TEST_ID = 1L;
    private static final Long TIMER_ID = 2L;

    @Test
    void createTimerAlreadyExpired() {
        Mockito.when(test.getId()).thenReturn(TEST_ID);
        Instant scheduledTime = Instant.now().minusSeconds(100);
        timerService.createTimer(test, Status.STARTED, scheduledTime);
        ArgumentCaptor<Timer> captor = ArgumentCaptor.forClass(Timer.class);
        Mockito.verify(timerRepository).save(captor.capture());
        Assertions.assertEquals(test, captor.getValue().getTest());
        Assertions.assertEquals(Status.STARTED, captor.getValue().getStatus());
        Assertions.assertEquals(scheduledTime, captor.getValue().getScheduledTime());
        Mockito.verify(eventPublisher).publishEvent(captor.getValue());
        Mockito.verify(timerRepository).deleteByTestIdAndStatus(TEST_ID, Status.STARTED);
    }

    @Test
    void createTimerSuccess() {
        Mockito.when(test.getId()).thenReturn(TEST_ID);
        try (MockedConstruction<java.util.Timer> mocked = Mockito.mockConstruction(java.util.Timer.class)) {
            Instant scheduledTime = Instant.now().plusSeconds(100);
            timerService.createTimer(test, Status.STARTED, scheduledTime);
            ArgumentCaptor<Timer> captor = ArgumentCaptor.forClass(Timer.class);
            Mockito.verify(timerRepository).save(captor.capture());
            Assertions.assertEquals(test, captor.getValue().getTest());
            Assertions.assertEquals(Status.STARTED, captor.getValue().getStatus());
            Assertions.assertEquals(scheduledTime, captor.getValue().getScheduledTime());
            ArgumentCaptor<TimerTask> taskCaptor = ArgumentCaptor.forClass(TimerTask.class);
            Mockito.verify(mocked.constructed().get(0))
                    .schedule(taskCaptor.capture(), Mockito.longThat(delay -> delay > 0));
            taskCaptor.getValue().run();
            Mockito.verify(eventPublisher).publishEvent(captor.getValue());
            Mockito.verify(timerRepository).deleteByTestIdAndStatus(TEST_ID, Status.STARTED);
        }
    }

    @Test
    void startAllTimersSuccess() {
        Instant scheduledTime = Instant.now().plusSeconds(100);
        Mockito.when(timerRepository.findAll()).thenReturn(List.of(timer));
        Mockito.when(timer.getTest()).thenReturn(test);
        Mockito.when(timer.getStatus()).thenReturn(Status.STARTED);
        Mockito.when(timer.getScheduledTime()).thenReturn(scheduledTime);
        Mockito.when(test.getId()).thenReturn(TEST_ID);
        try (MockedConstruction<java.util.Timer> mocked = Mockito.mockConstruction(java.util.Timer.class)) {
            timerService.startAllTimers();
            ArgumentCaptor<TimerTask> taskCaptor = ArgumentCaptor.forClass(TimerTask.class);
            Mockito.verify(mocked.constructed().get(0))
                    .schedule(taskCaptor.capture(), Mockito.longThat(delay -> delay > 0));
            taskCaptor.getValue().run();
            Mockito.verify(eventPublisher).publishEvent(timer);
            Mockito.verify(timerRepository).deleteByTestIdAndStatus(TEST_ID, Status.STARTED);
        }
    }

    @Test
    void startAllTimersSuccessWithZeroDelay() {
        Instant scheduledTime = Instant.EPOCH;
        Mockito.when(timerRepository.findAll()).thenReturn(List.of(timer));
        Mockito.when(timer.getTest()).thenReturn(test);
        Mockito.when(timer.getStatus()).thenReturn(Status.STARTED);
        Mockito.when(timer.getScheduledTime()).thenReturn(scheduledTime);
        Mockito.when(test.getId()).thenReturn(TEST_ID);
        try (MockedStatic<Instant> instantMockedStatic = Mockito.mockStatic(Instant.class)) {
            instantMockedStatic.when(Instant::now).thenReturn(Instant.EPOCH);
            try (MockedConstruction<java.util.Timer> mocked = Mockito.mockConstruction(java.util.Timer.class)) {
                timerService.startAllTimers();
                Mockito.verify(mocked.constructed().get(0)).cancel();
            }
        }
    }


    @Test
    void existsByIdNotFound() {
        Mockito.when(timerRepository.existsById(TIMER_ID)).thenReturn(false);
        Assertions.assertFalse(timerService.existsById(TIMER_ID));
    }

    @Test
    void existsByIdSuccess() {
        Mockito.when(timerRepository.existsById(TIMER_ID)).thenReturn(true);
        Assertions.assertTrue(timerService.existsById(TIMER_ID));
    }

    @Test
    void deleteTimerSuccess() {
        timerService.deleteTimer(TEST_ID, Status.ASSIGNED);
        Mockito.verify(timerRepository).deleteByTestIdAndStatus(TEST_ID, Status.ASSIGNED);
    }
}
