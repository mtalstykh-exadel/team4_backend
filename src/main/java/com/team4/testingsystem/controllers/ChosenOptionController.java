package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.ChosenOptionConverter;
import com.team4.testingsystem.dto.ChosenOptionDTO;
import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.Test;
import com.team4.testingsystem.services.ChosenOptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/chosen_option")
public class ChosenOptionController {

    private final ChosenOptionService chosenOptionService;
    private final ChosenOptionConverter converter;

    @Autowired
    public ChosenOptionController(ChosenOptionService chosenOptionService,
                                  ChosenOptionConverter converter) {
        this.chosenOptionService = chosenOptionService;
        this.converter = converter;
    }

    @ApiOperation(value = "Get a single chosen option by TestQuestionID")
    @GetMapping(path = "/{testId}/{questionId}")
    public ChosenOptionDTO getById(@PathVariable Long testId, @PathVariable Long questionId) {
        return new ChosenOptionDTO(chosenOptionService.getByTestAndQuestionId(testId, questionId));
    }

    @ApiOperation(value = "Use it to get all chosen options by test ID")
    @GetMapping(path = "/{testId}")
    public List<ChosenOptionDTO> getAllByTest(@PathVariable Long testId) {
        return chosenOptionService.getAllByTestId(testId).stream()
                .map(ChosenOptionDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Use it to add a chosen option")
    @PostMapping(path = "/")
    public void save(@RequestBody ChosenOptionDTO chosenOption) {
        chosenOptionService.save(converter.convertToEntity(chosenOption));
    }

    @ApiOperation(value = "Use it to add a list of chosen options")
    @PostMapping(path = "/all")
    public void saveAll(@RequestBody List<ChosenOptionDTO> chosenOptionDTOs) {
        List<ChosenOption> chosenOptions = chosenOptionDTOs.stream()
                .map(converter::convertToEntity)
                .collect(Collectors.toList());
        chosenOptionService.saveAll(chosenOptions);
    }
}
