package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.Test;

import com.team4.testingsystem.security.CustomUserDetails;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private long getUserId() {

 return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
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
    public void startNotAssigned() {
        testsService.start(testsService.createForUser(getUserId()));
    }

    @PostMapping(path = "/start/{testId}")
    public void startAssigned(@PathVariable("testId") long testId) {
        testsService.start(testId);
    }

    @PostMapping(path = "/finish/{testId}")
    public void finish(@PathVariable("testId") long testId, @RequestParam int evaluation) {
        testsService.finish(testId, evaluation);
    }

    @PutMapping(path="/update/{testId}")
    public void updateEvaluation(@PathVariable("testId") long testId, @RequestParam int evaluation) {
        testsService.updateEvaluation(testId, evaluation);
    }

    @DeleteMapping(path="/{id}")
    public void removeById(@PathVariable("id") long id) {
        testsService.removeById(id);
    }
}