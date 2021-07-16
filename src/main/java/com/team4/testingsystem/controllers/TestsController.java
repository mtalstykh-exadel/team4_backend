package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.Test;

import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping(path="/tests")
public class TestsController {

    @Autowired
    private TestsService testsService;

    private long getUserId() {

    return JwtTokenUtil.extractUserDetails().getId();
    }

    @GetMapping(path = "/")
    public Iterable<Test> getAll() {
        return testsService.getAll();
    }

    @GetMapping(path = "/{id}")
    public Test getById(@PathVariable("id") long id) {
        return testsService.getById(id);
    }

    @PostMapping(path = "/assign/{userId}")
    public long assign(@PathVariable("userId") long userId) {
        return testsService.createForUser(userId);
    }

    @PostMapping(path = "/start")
    public long startNotAssigned() {
        long createdTestId = testsService.createForUser(getUserId());
        testsService.start(createdTestId);
        return createdTestId;
    }

    @PostMapping(path = "/start/{testId}")
    public void startAssigned(@PathVariable("testId") long testId) {
        testsService.start(testId);
    }

    @PostMapping(path = "/finish/{testId}")
    public void finish(@PathVariable("testId") long testId, @RequestParam int evaluation) {
        testsService.finish(testId, evaluation);
    }

    @PutMapping(path="/{testId}")
    public void updateEvaluation(@PathVariable("testId") long testId, @RequestParam int evaluation) {
        testsService.updateEvaluation(testId, evaluation);
    }

    @DeleteMapping(path="/{id}")
    public void removeById(@PathVariable("id") long id) {
        testsService.removeById(id);
    }
}