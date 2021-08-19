package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.GradesConverter;
import com.team4.testingsystem.converters.TestConverter;
import com.team4.testingsystem.converters.TestVerificationConverter;
import com.team4.testingsystem.dto.AssignTestRequest;
import com.team4.testingsystem.dto.ModuleGradesDTO;
import com.team4.testingsystem.dto.TestDTO;
import com.team4.testingsystem.dto.TestInfo;
import com.team4.testingsystem.dto.TestVerificationDTO;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Status;
import com.team4.testingsystem.services.ModuleGradesService;
import com.team4.testingsystem.services.RestrictionsService;
import com.team4.testingsystem.services.TestsService;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tests")
public class TestsController {

    private final TestsService testsService;
    private final ModuleGradesService moduleGradesService;
    public final RestrictionsService restrictionsService;
    private final GradesConverter gradesConverter;
    private final TestConverter testConverter;
    private final TestVerificationConverter verificationConverter;

    @Autowired
    public TestsController(TestsService testsService,
                           ModuleGradesService moduleGradesService,
                           RestrictionsService restrictionsService,
                           GradesConverter gradesConverter,
                           TestConverter testConverter,
                           TestVerificationConverter verificationConverter) {
        this.testsService = testsService;
        this.moduleGradesService = moduleGradesService;
        this.restrictionsService = restrictionsService;
        this.gradesConverter = gradesConverter;
        this.testConverter = testConverter;
        this.verificationConverter = verificationConverter;
    }

    @ApiOperation(value = "Get all tests assigned to the current user")
    @GetMapping(path = "/")
    public List<TestInfo> getCurrentUserTests(@RequestParam int pageNumb,
                                              @RequestParam int pageSize) {
        return convertToTestInfo(testsService
                .getByUserId(JwtTokenUtil.extractUserDetails().getId(), PageRequest.of(pageNumb, pageSize)));
    }

    @ApiOperation(value = "Get all tests assigned to the user by by the optional parameter level")
    @GetMapping(path = "/history/{userId}")
    @Secured("ROLE_HR")
    public List<TestInfo> getUsersTests(@PathVariable("userId") long userId,
                                        @RequestParam(required = false) Levels level,
                                        @RequestParam int pageNumb,
                                        @RequestParam int pageSize) {
        if (level != null) {
            return convertToTestInfo(testsService
                    .getTestsByUserIdAndLevel(userId, level, PageRequest.of(pageNumb, pageSize)));
        }
        return convertToTestInfo(testsService.getByUserId(userId, PageRequest.of(pageNumb, pageSize)));
    }

    @ApiOperation(value = "Use it to get a single test from the database by its id")
    @GetMapping(path = "/{id}")
    public TestDTO getById(@PathVariable("id") long id) {
        Test test = testsService.getById(id);

        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);

        restrictionsService.checkStatus(test, Status.STARTED);

        return testConverter.convertToDTO(test);
    }

    @ApiOperation(value = "Get test for coach verification")
    @GetMapping(path = "/verify/{testId}")
    @Secured("ROLE_COACH")
    public TestVerificationDTO getTestForVerification(@PathVariable long testId) {
        Test test = testsService.startTestVerification(testId);
        return verificationConverter.convertToVerificationDTO(test);
    }

    @ApiOperation(value = "Use it to get test grades for a test by modules")
    @GetMapping(path = "/grades/{testId}")
    public ModuleGradesDTO getGrades(@PathVariable("testId") long testId) {

        Test test = testsService.getById(testId);

        Long currentUserId = JwtTokenUtil.extractUserDetails().getId();
        restrictionsService.checkOwnerIsCurrentUser(test, currentUserId);

        return gradesConverter.convertListOfGradesToDTO(moduleGradesService
                .getGradesByTest(test));
    }

    @ApiOperation(value = "Is used to get all unverified tests")
    @GetMapping(path = "/unverified")
    @Secured("ROLE_ADMIN")
    public List<TestInfo> getUnverifiedTests(@RequestParam int pageNumb,
                                             @RequestParam int pageSize) {
        return convertToTestInfo(testsService
                .getAllUnverifiedTests(PageRequest.of(pageNumb, pageSize)));
    }

    @ApiOperation(value = "Is used to get all unverified tests, assigned to current coach")
    @GetMapping(path = "/unverified_assigned")
    @Secured("ROLE_COACH")
    public List<TestInfo> getUnverifiedTestsForCurrentCoach(@RequestParam int pageNumb,
                                                            @RequestParam int pageSize) {
        Long coachId = JwtTokenUtil.extractUserDetails().getId();
        return convertToTestInfo(testsService
                .getAllUnverifiedTestsByCoach(coachId, PageRequest.of(pageNumb, pageSize)));
    }

    @ApiOperation(value = "Is used to assign a test for the user (HR's ability)")
    @ApiResponse(code = 200, message = "Created test's id")
    @PostMapping(path = "/assign/{userId}")
    @Secured("ROLE_HR")
    public long assign(@PathVariable("userId") long userId, @RequestBody AssignTestRequest request) {
        return testsService.assignForUser(userId, request.getLevel(), request.getDeadline(), request.getPriority());
    }

    @ApiOperation(value = "Is used when to deassign tests (HR)")
    @PostMapping(path = "/deassign/{testId}")
    @Secured("ROLE_HR")
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
        testsService.finish(testId, Instant.now());
    }

    @ApiOperation(value = "Use it to assign a test for the coach")
    @ApiResponse(code = 409, message = "Coach can't verify his own test")
    @PostMapping(path = "/assign_coach/{testId}")
    @Secured("ROLE_ADMIN")
    public void assignCoach(@PathVariable("testId") long testId, @RequestParam long coachId) {
        testsService.assignCoach(testId, coachId);
    }

    @ApiOperation(value = "Use it to deassign coaches")
    @PostMapping(path = "/deassign_coach/{testId}")
    @Secured("ROLE_ADMIN")
    public void deassignCoach(@PathVariable("testId") long testId) {
        testsService.deassignCoach(testId);
    }

    private List<TestInfo> convertToTestInfo(List<Test> tests) {
        return tests.stream()
                .map(testConverter::convertToInfo)
                .collect(Collectors.toList());
    }
}
