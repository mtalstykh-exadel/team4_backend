package com.team4.testingsystem.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModulesTest {

    @Test
    void getName() {
        Assertions.assertEquals("Listening", Modules.LISTENING.getName());
    }
}