package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tests")
public class TestsController {

    private TestsService testsService;

    @Autowired
    public TestsController(TestsService testsService) {
        this.testsService = testsService;
    }

    @ApiOperation(value = "Use it to get a single test from the database by its id")
    @GetMapping(path = "/")
    public Iterable<Test> getAll() {
        return testsService.getAll();
    }


    @ApiOperation(value = "(Experimental) Use it to get all tests from the database")
    @GetMapping(path = "/{id}")
    public Test getById(@PathVariable("id") long id) {
        return testsService.getById(id);
    }


    @ApiOperation(value = "(To be updated) Is used to assign a test for the user (HR's ability)")
    @ApiResponse(code = 200, message = "Created test's id")
    @PostMapping(path = "/assign/{userId}")
    public long assign(@PathVariable("userId") long userId) {
        return testsService.createForUser(userId);
    }

    @ApiOperation(value =
            "(To be updated) Is used when the user wants to learn one's level by oneself (without any HRs)")
    @ApiResponse(code = 200, message = "Started test's id")
    @PostMapping(path = "/start")
    public long startNotAssigned() {

        long userId = JwtTokenUtil.extractUserDetails().getId();
        long createdTestId = testsService.createForUser(userId);
        testsService.start(createdTestId);
        return createdTestId;
    }

    @ApiOperation(value = "Is used when the user starts test which was assigned by an HR")
    @PostMapping(path = "/start/{testId}")
    public void startAssigned(@PathVariable("testId") long testId) {
        testsService.start(testId);
    }

    @ApiOperation(value = "Is used to finish tests")
    @PostMapping(path = "/finish/{testId}")
    public void finish(@PathVariable("testId") long testId, @RequestParam int evaluation) {
        testsService.finish(testId, evaluation);
    }

    @ApiOperation(value = "Is used to update score after coach check")
    @PutMapping(path = "/{testId}")
    public void updateEvaluation(@PathVariable("testId") long testId, @RequestParam int evaluation) {
        testsService.updateEvaluation(testId, evaluation);
    }

    @ApiOperation(value = "(Experimental) Is used to remove a test from the database")
    @DeleteMapping(path = "/{id}")
    public void removeById(@PathVariable("id") long id) {
        testsService.removeById(id);
    }
}