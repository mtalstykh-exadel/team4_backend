package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.GradesConverter;
import com.team4.testingsystem.converters.TestConverter;
import com.team4.testingsystem.dto.AssignTestRequest;
import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tests")
public class TestsController {

    private final TestsService testsService;
    private final ModuleGradesService moduleGradesService;
    private final GradesConverter gradesConverter;
    private final TestConverter testConverter;

    @Autowired
    public TestsController(TestsService testsService,
                           ModuleGradesService moduleGradesService,
                           GradesConverter gradesConverter,
                           TestConverter testConverter) {
        this.testsService = testsService;
        this.moduleGradesService = moduleGradesService;
        this.gradesConverter = gradesConverter;
        this.testConverter = testConverter;
    }

    @ApiOperation(value = "Get all tests assigned to the current user")
    @GetMapping(path = "/")
    public Iterable<TestDTO> getCurrentUserTests() {
        return convertToDTO(testsService.getByUserId(JwtTokenUtil.extractUserDetails().getId()));
    }

    @ApiOperation(value = "Get all tests assigned to the user")
    @GetMapping(path = "/history/{userId}")
    public List<TestDTO> getUsersTests(@PathVariable("userId") long userId) {
        return convertToDTO(testsService.getByUserId(userId));
    }

    @ApiOperation(value = "Use it to get a single test from the database by its id")
    @GetMapping(path = "/{id}")
    public TestDTO getById(@PathVariable("id") long id) {
        return testConverter.convertToDTO(testsService.getById(id));
    }

    @ApiOperation(value = "Use it to get test grades for a test by modules")
    @GetMapping(path = "/grades/{testId}")
    public ModuleGradesDTO getGrades(@PathVariable("testId") long testId) {
        return gradesConverter.convertListOfGradesToDTO(moduleGradesService
                .getGradesByTest(testsService.getById(testId)));
    }

    @ApiOperation(value = "Is used to get all unverified tests")
    @GetMapping(path = "/unverified")
    public List<TestDTO> getUnverifiedTests() {
        Status[] statuses = {Status.COMPLETED, Status.IN_VERIFICATION};
        return convertToDTO(testsService.getByStatuses(statuses));
    }

    @ApiOperation(value = "Is used to get time left")
    @GetMapping(path = "/time/{testId}")
    public long getTimeLeft(@PathVariable("testId") long testId){
        return testsService.getTimeLeft(testId);
    }

    @ApiOperation(value = "Is used to assign a test for the user (HR's ability)")
    @ApiResponse(code = 200, message = "Created test's id")
    @PostMapping(path = "/assign/{userId}")
    public long assign(@PathVariable("userId") long userId, @RequestBody AssignTestRequest request) {
        return testsService.assignForUser(userId, request.getLevel(), request.getDeadline(), request.getPriority());
    }

    @ApiOperation(value = "Is used when to deassign tests (HR)")
    @PostMapping(path = "/deassign/{testId}")
    public void deassign(@PathVariable("testId") long testId) {
        testsService.deassign(testId);
    }

    @ApiOperation(value =
            "Is used when the user wants to learn one's level by oneself (without any HRs)")
    @ApiResponse(code = 409, message = "You can start only 3 tests per day. If you want more, ask HR")
    @PostMapping(path = "/start")
    public TestDTO startNotAssigned(@RequestParam Levels level) {
        long userId = JwtTokenUtil.extractUserDetails().getId();
        long createdTestId = testsService.startForUser(userId, level);
        return testConverter.convertToDTO(testsService.start(createdTestId));
    }


    @ApiOperation(value = "Is used when the user starts the test which was assigned by an HR")
    @PostMapping(path = "/start/{testId}")
    public TestDTO startAssigned(@PathVariable("testId") long testId) {
        return testConverter.convertToDTO(testsService.start(testId));
    }

    @ApiOperation(value = "Is used to finish tests")
    @PostMapping(path = "/finish/{testId}")
    public void finish(@PathVariable("testId") long testId) {
        testsService.finish(testId);
    }

    @ApiOperation(value = "Is used to update score after coach check")
    @PutMapping(path = "/{testId}")
    public void update(@PathVariable("testId") long testId) {
        testsService.update(testId);
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

    private List<TestDTO> convertToDTO(List<Test> tests) {
        return tests.stream()
                .map(testConverter::convertToDTO)
                .collect(Collectors.toList());
    }
}
