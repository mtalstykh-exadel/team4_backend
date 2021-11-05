package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.ChosenOptionConverter;
import com.team4.testingsystem.model.dto.ChosenOptionDTO;
import com.team4.testingsystem.model.entity.ChosenOption;
import com.team4.testingsystem.services.ChosenOptionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/chosen_option")
public class ChosenOptionController {

    private final ChosenOptionService chosenOptionService;
    private final ChosenOptionConverter converter;

    @ApiOperation(value = "Use it to add a list of chosen options")
    @PostMapping(path = "/all")
    public void saveAll(@RequestBody List<ChosenOptionDTO> chosenOptionDTOs) {
        List<ChosenOption> chosenOptions = chosenOptionDTOs.stream()
                .map(converter::convertToEntity)
                .collect(Collectors.toList());
        chosenOptionService.saveAll(chosenOptions);
    }
}
