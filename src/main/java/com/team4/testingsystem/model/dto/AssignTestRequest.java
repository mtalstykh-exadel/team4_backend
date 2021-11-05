package com.team4.testingsystem.model.dto;

import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssignTestRequest implements Serializable {
    private Levels level;
    private Instant deadline;
    private Priority priority;

}
