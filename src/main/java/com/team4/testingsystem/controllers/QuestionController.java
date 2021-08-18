package com.team4.testingsystem.controllers;

import com.team4.testingsystem.converters.ContentFileConverter;
import com.team4.testingsystem.converters.QuestionConverter;
import com.team4.testingsystem.dto.ContentFileDTO;
import com.team4.testingsystem.dto.ListeningTopicDTO;
import com.team4.testingsystem.dto.QuestionDTO;
import com.team4.testingsystem.entities.ContentFile;
import com.team4.testingsystem.entities.Question;
import com.team4.testingsystem.enums.Levels;
import com.team4.testingsystem.enums.Modules;
import com.team4.testingsystem.enums.QuestionStatus;
import com.team4.testingsystem.services.ContentFilesService;
import com.team4.testingsystem.services.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
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
    private final ContentFileConverter contentFileConverter;

    @Autowired
    public QuestionController(QuestionService questionService,
                              ContentFilesService contentFilesService,
                              QuestionConverter questionConverter,
                              ContentFileConverter contentFileConverter) {
        this.questionService = questionService;
        this.contentFilesService = contentFilesService;
        this.questionConverter = questionConverter;
        this.contentFileConverter = contentFileConverter;
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
                                          @RequestParam("module") Modules module,
                                          @RequestParam("status") QuestionStatus status,
                                          @RequestParam int pageNumb,
                                          @RequestParam int pageSize
    ) {
        return questionService.getQuestionsByLevelAndModuleName(
                level, module, status, PageRequest.of(pageNumb, pageSize)).stream()
                .map(QuestionDTO::create)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get content file with questions by it's id")
    @GetMapping("/listening/{contentFileId}")
    @Secured("ROLE_COACH")
    public ContentFileDTO getListening(@PathVariable("contentFileId") Long contentFileId) {
        ContentFile contentFile = contentFilesService.getById(contentFileId);
        return new ContentFileDTO(contentFile, contentFile.getQuestions().get(0).getLevel().getName());
    }

    @ApiOperation(value = "Add a new question")
    @PostMapping("/")
    @Secured("ROLE_COACH")
    public QuestionDTO addQuestion(@RequestBody QuestionDTO questionDTO) {
        Question question = questionService
                .createQuestion(questionConverter.convertToEntity(questionDTO));
        return QuestionDTO.createWithCorrectAnswers(question);
    }

    @ApiOperation(value = "Get all topics (or get by level)")
    @GetMapping(value = "/listening")
    public List<ListeningTopicDTO> getListeningTopics(@RequestParam(required = false) Levels level,
                                                      @RequestParam("status") QuestionStatus status,
                                                      @RequestParam int pageNumb,
                                                      @RequestParam int pageSize) {
        return convertToDTO(questionService.getListening(level, status, PageRequest.of(pageNumb, pageSize)));
    }

    @ApiOperation(value = "Add content file with questions")
    @PostMapping(value = "/listening")
    @Secured("ROLE_COACH")
    public ContentFileDTO addListening(@RequestPart MultipartFile file,
                                       @RequestPart ContentFileDTO data) {
        ContentFile contentFile = contentFilesService
                .add(file, contentFileConverter.convertToEntity(data));
        return new ContentFileDTO(contentFile);
    }

    @ApiOperation(value = "Update content file with questions or just questions for content file")
    @PutMapping(value = "/update/listening/{contentFileId}")
    @Secured("ROLE_COACH")
    public ContentFileDTO updateListening(@RequestPart(required = false) MultipartFile file,
                                          @PathVariable("contentFileId") Long id,
                                          @RequestPart ContentFileDTO data) {
        ContentFile contentFile = contentFilesService
                .update(file, id, contentFileConverter.convertToEntity(data));
        return new ContentFileDTO(contentFile);
    }

    @ApiOperation(value = "Archive/Unarchive the question")
    @PutMapping("/{id}")
    @Secured("ROLE_COACH")
    public void updateAvailability(@PathVariable("id") Long id,
                                   @RequestParam Boolean available) {
        questionService.updateAvailability(id, available);
    }

    @ApiOperation(value = "Archive/Unarchive the listening")
    @PutMapping("/listening/{contentFileId}")
    @Secured("ROLE_COACH")
    public void updateAvailabilityListening(@PathVariable("contentFileId") Long contentFileId,
                                            @RequestParam Boolean available) {
        contentFilesService.updateAvailability(contentFileId, available);
    }

    @ApiOperation(value = "Change the question")
    @PutMapping("/update/{id}")
    @Secured("ROLE_COACH")
    public QuestionDTO updateQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable("id") Long id) {
        Question resultQuestion = questionService
                .updateQuestion(questionConverter.convertToEntity(questionDTO, id), id);
        return QuestionDTO.createWithCorrectAnswers(resultQuestion);
    }

    private List<ListeningTopicDTO> convertToDTO(List<ContentFile> contentFiles) {
        return contentFiles.stream().map(ListeningTopicDTO::new).collect(Collectors.toList());
    }
}
