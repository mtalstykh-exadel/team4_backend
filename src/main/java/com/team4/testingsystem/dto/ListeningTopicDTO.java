package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ContentFile;

import java.io.Serializable;
import java.util.Objects;

public class ListeningTopicDTO implements Serializable {
    private Long id;
    private String url;
    private String topic;

    public ListeningTopicDTO() {
    }

    public ListeningTopicDTO(ContentFile contentFile) {
        id = contentFile.getId();
        url = contentFile.getUrl();
        topic = contentFile.getTopic();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListeningTopicDTO that = (ListeningTopicDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url) && Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, topic);
    }
}
