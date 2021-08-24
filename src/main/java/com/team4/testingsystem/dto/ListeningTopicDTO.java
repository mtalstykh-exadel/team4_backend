package com.team4.testingsystem.dto;

import com.team4.testingsystem.entities.ContentFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ListeningTopicDTO implements Serializable {
    private Long id;
    private String url;
    private String topic;

    public ListeningTopicDTO(ContentFile contentFile) {
        id = contentFile.getId();
        url = contentFile.getUrl();
        topic = contentFile.getTopic();
    }

}
