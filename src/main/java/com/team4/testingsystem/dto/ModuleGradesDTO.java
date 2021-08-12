package com.team4.testingsystem.dto;

import java.io.Serializable;
import java.util.Objects;

public class ModuleGradesDTO implements Serializable {

    private int grammar;

    private int listening;

    private int essay;

    private int speaking;

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
                && Objects.equals(speaking, that.speaking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grammar, listening, essay, speaking);
    }
}
