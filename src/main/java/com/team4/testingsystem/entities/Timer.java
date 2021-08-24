package com.team4.testingsystem.entities;

import com.team4.testingsystem.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "timer")
@NoArgsConstructor
@Data
public class Timer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "scheduled_time")
    private Instant scheduledTime;

    public Timer(Test test, Status status, Instant scheduledTime) {
        this.test = test;
        this.status = status;
        this.scheduledTime = scheduledTime;
    }
}
