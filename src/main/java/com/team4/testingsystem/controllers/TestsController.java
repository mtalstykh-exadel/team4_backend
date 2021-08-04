package com.team4.testingsystem.controllers;

import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/tests")
public class TestsController {

    private final TestsService testsService;

    @Autowired
    public TestsController(TestsService testsService) {
        this.testsService = testsService;
    }

    @ApiOperation(value = "Get all tests assigned to the current user")
    @GetMapping(path = "/")
    public Iterable<Test> getCurrentUserTests() {
        return testsService.getByUserId(JwtTokenUtil.extractUserDetails().getId());
    }

    @ApiOperation(value = "Get all tests assigned to the user")
    @GetMapping(path = "/history/{userId}")
    public Iterable<Test> getUsersTests(@PathVariable("userId") long userId) {
        return testsService.getByUserId(userId);
    }

    @ApiOperation(value = "Use it to get a single test from the database by its id")
    @GetMapping(path = "/{id}")
    public Test getById(@PathVariable("id") long id) {
        return testsService.getById(id);
    }


    @GetMapping(path = "/unverified")
    public List<TestDTO> getUnverifiedTests(){
        List<Test> tests = testsService.getByStatus(Status.COMPLETED);
        List<TestDTO> testDTOS = new ArrayList<>();
        tests.forEach(test -> testDTOS.add(new TestDTO(test)));
        return testDTOS;
    }

    @ApiOperation(value = "(To be updated) Is used to assign a test for the user (HR's ability)")
    @ApiResponse(code = 200, message = "Created test's id")
    @PostMapping(path = "/assign/{userId}")
    public long assign(@PathVariable("userId") long userId, @RequestParam Levels level) {
        return testsService.createForUser(userId, level);
    }

    @ApiOperation(value =
            "(To be updated) Is used when the user wants to learn one's level by oneself (without any HRs)")
    @ApiResponse(code = 200, message = "Started test's id")
    @PostMapping(path = "/start")
    public long startNotAssigned(@RequestParam Levels level) {
        long userId = JwtTokenUtil.extractUserDetails().getId();
        long createdTestId = testsService.createForUser(userId, level);
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

    @ApiOperation(value = "Use it to assign a test for the coach")
    @PostMapping(path = "/assign_coach/{testId}")
    @ApiResponse(code = 409, message = "Coach can't verify his own test")
    public void assignCoach(@PathVariable("testId") long testId, @RequestParam long coachId) {
        testsService.assignCoach(testId, coachId);
    }

    @ApiOperation(value = "Use it to deassign coaches")
    @PostMapping(path = "/deassign_coach/{testId}")
    public void deassignCoach(@PathVariable("testId") long testId) {
        testsService.deassignCoach(testId);
    }
}
