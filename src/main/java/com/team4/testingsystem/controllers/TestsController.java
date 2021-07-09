package com.team4.testingsystem.controllers;

import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.repositories.TestsRepository;
import com.team4.testingsystem.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping(path="/tests")
public class TestsController {

    @Autowired
    private TestsRepository testsRepository;


    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Test> getAllTests() {
        System.out.println(testsRepository.findAll());
        return testsRepository.findAll();
    }

    @GetMapping(path="/get")
    public @ResponseBody
    Optional<Test> getTestById(@RequestParam long id) {
        try {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }
            return testsRepository.findById(id);
        }
        catch (NoSuchElementException e) {
            System.err.println(e);
        }
        return null;

    }

    @PostMapping(path="/add")
    public @ResponseBody String addNewTest (@RequestParam long userId,
                                            @RequestParam String createdAt,
                                            @RequestParam String updatedAt,
                                            @RequestParam String startedAt,
                                            @RequestParam String finishedAt,
                                            @RequestParam String status,
                                            @RequestParam int evaluation) {

        Test test = new Test();

        try {
            if (!usersRepository.existsById(userId)) {
                throw new NoSuchElementException("User not found");
            }
            test.setUser(usersRepository.findById(userId).get());
            test.setCreatedAt(LocalDateTime.parse(createdAt));
            test.setUpdatedAt(LocalDateTime.parse(updatedAt));
            test.setStartedAt(LocalDateTime.parse(startedAt));
            test.setFinishedAt(LocalDateTime.parse(finishedAt));
            test.setStatus(status);
            test.setEvaluation(evaluation);

            testsRepository.save(test);

            return "Success";
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return "Fail";
        }

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

        try {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }

            if (!usersRepository.existsById(userId)) {
                throw new NoSuchElementException("User not found");
            }
            Test test = testsRepository.findById(id).get();

            test.setUser(usersRepository.findById(userId).get());
            test.setCreatedAt(LocalDateTime.parse(createdAt));
            test.setUpdatedAt(LocalDateTime.parse(updatedAt));
            test.setStartedAt(LocalDateTime.parse(startedAt));
            test.setFinishedAt(LocalDateTime.parse(finishedAt));
            test.setStatus(status);
            test.setEvaluation(evaluation);

            testsRepository.save(test);

            return "Success";
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return "Fail";
        }
    }

    @DeleteMapping(path="/remove")
    public @ResponseBody String removeTestById(@RequestParam  long id) {
        try {
            if (!testsRepository.existsById(id)) {
                throw new NoSuchElementException("Test not found");
            }

            testsRepository.deleteById(id);
            return "Success";
        }
        catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
        }
        return "Fail";
    }

}