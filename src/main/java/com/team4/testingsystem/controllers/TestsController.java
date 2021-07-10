package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.TestEntity;

import com.team4.testingsystem.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path="/tests")
public class TestsController {

    @Autowired
    private TestsService testsService;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<TestEntity> getAllTests() {
        return testsService.getAllTests();
    }

    @GetMapping(path = "/get")
    public @ResponseBody
    Optional<TestEntity> getTestById(@RequestParam long id) {
        return testsService.getTestById(id);
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewTest(@RequestParam long userId,
                      @RequestParam String createdAt,
                      @RequestParam String updatedAt,
                      @RequestParam String startedAt,
                      @RequestParam String finishedAt,
                      @RequestParam String status,
                      @RequestParam int evaluation) {

        return testsService.addNewTest(userId, createdAt, updatedAt, startedAt, finishedAt, status, evaluation);
    }

    @PostMapping(path="/update")
    public @ResponseBody String updateTest (@RequestParam long id,
                                            @RequestParam long userId,
                                            @RequestParam String createdAt,
                                            @RequestParam String updatedAt,
                                            @RequestParam String startedAt,
                                            @RequestParam String finishedAt,
                                            @RequestParam String status,
                                            @RequestParam int evaluation) {

       return testsService.updateTest(id, userId, createdAt, updatedAt, startedAt, finishedAt, status, evaluation);
    }

    @DeleteMapping(path="/remove")
    public @ResponseBody String removeTestById(@RequestParam  long id) {
        return testsService.removeTestById(id);
    }
}