package com.team4.testingsystem.dto;

import com.team4.testingsystem.enums.Status;

import java.io.Serializable;
import java.util.Objects;

public class ModuleGradesDTO implements Serializable {

    private int grammar;
    private int listening;
    private int essay;
    private int speaking;

    private String essayComment;
    private String speakingComment;

    private String level;
    private Status status;

    public ModuleGradesDTO() {
    }

    public int getGrammar() {
        return grammar;
    }

    public void setGrammar(int grammar) {
        this.grammar = grammar;
    }

    public int getListening() {
        return listening;
    }

    public void setListening(int listening) {
        this.listening = listening;
    }

    public int getEssay() {
        return essay;
    }

    public void setEssay(int essay) {
        this.essay = essay;
    }

    public int getSpeaking() {
        return speaking;
    }

    public void setSpeaking(int speaking) {
        this.speaking = speaking;
    }

    public String getEssayComment() {
        return essayComment;
    }

    public void setEssayComment(String essayComment) {
        this.essayComment = essayComment;
    }

    public String getSpeakingComment() {
        return speakingComment;
    }

    public void setSpeakingComment(String speakingComment) {
        this.speakingComment = speakingComment;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static ModuleGradesDTO.Builder builder() {
        return new ModuleGradesDTO.Builder();
    }

    public static class Builder {
        private final ModuleGradesDTO moduleGradesDTO;

        public Builder() {
            moduleGradesDTO = new ModuleGradesDTO();
        }

        public ModuleGradesDTO.Builder grammar(Integer grammar) {
            moduleGradesDTO.setGrammar(grammar);
            return this;
        }

        public ModuleGradesDTO.Builder listening(Integer listening) {
            moduleGradesDTO.setListening(listening);
            return this;
        }

        public ModuleGradesDTO.Builder essay(Integer essay) {
            moduleGradesDTO.setEssay(essay);
            return this;
        }

        public ModuleGradesDTO.Builder speaking(Integer speaking) {
            moduleGradesDTO.setSpeaking(speaking);
            return this;
        }


        public ModuleGradesDTO.Builder essayComment(String essayComment) {
            moduleGradesDTO.setEssayComment(essayComment);
            return this;
        }


        public ModuleGradesDTO.Builder speakingComment(String speakingComment) {
            moduleGradesDTO.setSpeakingComment(speakingComment);
            return this;
        }

        public ModuleGradesDTO.Builder level(String level) {
            moduleGradesDTO.setLevel(level);
            return this;
        }

        public ModuleGradesDTO.Builder status(Status status) {
            moduleGradesDTO.setStatus(status);
            return this;
        }

        public ModuleGradesDTO build() {
            return moduleGradesDTO;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModuleGradesDTO that = (ModuleGradesDTO) o;
        return Objects.equals(grammar, that.grammar)
                && Objects.equals(listening, that.listening)
                && Objects.equals(essay, that.essay)
                && Objects.equals(speaking, that.speaking)
                && Objects.equals(essayComment, that.essayComment)
                && Objects.equals(speakingComment, that.speakingComment)
                && Objects.equals(level, that.level)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grammar, listening, essay, speaking, essayComment, speakingComment, level, status);
    }
}
