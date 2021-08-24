package com.team4.testingsystem.dto;

import com.team4.testingsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ModuleGradesDTO implements Serializable {

    private int grammar;
    private int listening;
    private int essay;
    private int speaking;

    private String essayComment;
    private String speakingComment;

    private String level;
    private Status status;

}
