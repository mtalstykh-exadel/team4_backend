package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.QuestionConverter;
import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.ListeningTopicRequest;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/question")
public class QuestionController {
    private final QuestionService questionService;
    private final ContentFilesService contentFilesService;
    private final QuestionConverter questionConverter;

    @Autowired
    public QuestionController(QuestionService questionService,
                              ContentFilesService contentFilesService,
                              QuestionConverter questionConverter) {
        this.questionService = questionService;
        this.contentFilesService = contentFilesService;
        this.questionConverter = questionConverter;
    }

    @ApiOperation(value = "Get a single question from the database by it's id")
    @GetMapping("/{id}")
    @Secured("ROLE_COACH")
    public QuestionDTO getQuestion(@PathVariable("id") Long id) {
        return QuestionDTO.createWithCorrectAnswers(questionService.getById(id));
    }

    @ApiOperation(value = "Get questions from the database by it's level and module")
    @GetMapping("/")
    @Secured("ROLE_COACH")
    public List<QuestionDTO> getQuestions(@RequestParam("level") Levels level,
                                          @RequestParam("module") Modules module) {
        return questionService.getQuestionsByLevelAndModuleName(level, module).stream()
                .map(QuestionDTO::create)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get content file with questions by it's id")
    @GetMapping("/listening/{contentFileId}")
    @Secured("ROLE_COACH")
    public ContentFileDTO getListening(@PathVariable("contentFileId") Long contentFileId) {
        return new ContentFileDTO(contentFilesService.getById(contentFileId));
    }

    @ApiOperation(value = "Add a new question")
    @PostMapping("/")
    @Secured("ROLE_COACH")
    public QuestionDTO addQuestion(@RequestBody QuestionDTO questionDTO) {
        Question question = questionService
                .createQuestion(questionConverter.convertToEntity(questionDTO));
        if (questionDTO.getAnswers() != null) {
            questionService.addAnswers(question, questionDTO.getAnswers());
        }
        return QuestionDTO.createWithCorrectAnswers(question);
    }

    @ApiOperation(value = "Add content file with questions")
    @PostMapping(value = "/listening")
    @Secured("ROLE_COACH")
    public ContentFileDTO addListening(@RequestPart MultipartFile file,
                                       @RequestPart ListeningTopicRequest data) {
        ContentFile contentFile = contentFilesService
                .add(file, data.getTopic(), convertToEntity(data.getQuestions()));
        return new ContentFileDTO(contentFile);
    }

    @ApiOperation(value = "Update content file with questions or just questions for content file")
    @PutMapping(value = "/listening/{contentFileId}")
    @Secured("ROLE_COACH")
    public ContentFileDTO updateListening(@RequestPart(required = false) MultipartFile file,
                                          @PathVariable("contentFileId") Long id,
                                          @RequestPart ListeningTopicRequest data) {
        ContentFile contentFile = contentFilesService
                .update(file, id, data.getTopic(), convertToEntity(data.getQuestions()));
        return new ContentFileDTO(contentFile);
    }

    @ApiOperation(value = "Archive the question")
    @DeleteMapping("/{id}")
    @Secured("ROLE_COACH")
    public void archiveQuestion(@PathVariable("id") Long id) {
        questionService.archiveQuestion(id);
    }

    @ApiOperation(value = "Change the question")
    @PutMapping("/{id}")
    @Secured("ROLE_COACH")
    public QuestionDTO updateQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable("id") Long id) {
        Question resultQuestion = questionService
                .updateQuestion(questionConverter.convertToEntity(questionDTO, id), id);
        if (questionDTO.getAnswers() != null) {
            questionService.addAnswers(resultQuestion, questionDTO.getAnswers());
        }
        return QuestionDTO.createWithCorrectAnswers(resultQuestion);
    }

    private List<Question> convertToEntity(List<QuestionDTO> questionsDTO) {
        return questionsDTO.stream().map(questionConverter::convertToEntity).collect(Collectors.toList());
    }
}
