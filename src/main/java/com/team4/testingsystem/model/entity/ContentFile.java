package com.team4.testingsystem.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "content_file")
@NoArgsConstructor
@Data
public class ContentFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "question_content_file",
            joinColumns = @JoinColumn(name = "content_file_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    List<Question> questions = new ArrayList<>();

    @Column(name = "url")
    private String url;

    @Column(name = "topic")
    private String topic;

    @Column(name = "is_available")
    private boolean available = true;

    public ContentFile(String url) {
        this.url = url;
    }

    public ContentFile(String url, String topic, List<Question> questions) {
        this.questions = questions;
        this.url = url;
        this.topic = topic;
    }
}
