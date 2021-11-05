package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

    @Modifying
    @Query(value = "update Question q set q.isAvailable = ?2 where q.id = ?1")
    void updateAvailability(Long id, boolean available);

    @Query(value = "select q from Question q "
        + "where q.isAvailable = true "
        + "and q.level.name = ?1 "
        + "and q.module.name = ?2 "
        + "order by function('random') ")
    List<Question> getRandomQuestions(String level, String module, Pageable pageable);

    @Query("select q from Question q "
        + "join q.contentFiles cf "
        + "where cf.id = ?1 "
        + "order by function('random') ")
    List<Question> getRandomQuestionByContentFile(Long id, Pageable pageable);

    @Query("select q from Question q "
        + "join q.tests t "
        + "where t.id = ?1 ")
    List<Question> getQuestionsByTestId(Long id);

    @Query("select q from Question q "
           + "where q.level.name = ?1 "
           + "and q.module.name = ?2 "
           + "and q.isAvailable = ?3 "
           + "order by q.id desc ")
    List<Question> getQuestionsByLevelAndModuleName(String level,
                                                    String module,
                                                    boolean isAvailable,
                                                    Pageable pageable);

    @Query("select q from Question q "
           + "join q.tests t "
           + "where t.id = ?1 and q.module.name = ?2 ")
    Optional<Question> getQuestionByTestIdAndModule(Long testId, String moduleName);
}
