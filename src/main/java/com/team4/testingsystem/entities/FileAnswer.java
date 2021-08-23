package com.team4.testingsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "file_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileAnswer implements Serializable {

    @EmbeddedId
    private TestQuestionID id;

    @Column(name = "url")
    private String url;

    public TestQuestionID getId() {
        return id;
    }

}
