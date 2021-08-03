package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    Optional<Question> findById(Long id);

    Question save(Question question);

    @Modifying
    @Query(value = "update Question q set q.isAvailable = false where q.id = ?1")
    void archiveQuestion(Long id);

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
           + "where t.id = ?1 "
           + "and q.module.name = ?2")
    List<Question> getQuestionsByTestIdAndModule(Long id, String module);

    @Query(value = "select * from language_testing.question as q "
                   + "join language_testing.module as m on q.module_id = m.id "
                   + "join language_testing.test_question as tq on q.id = tq.question_id "
                   + "join language_testing.test as t on t.id = tq.test_id "
                   + "where t.id = ?1 "
                   + "and m.module_name = ?2 "
                   + "limit 1; ", nativeQuery = true)
    Optional<Question> getQuestionByTestIdAndModule(Long id, String module);

}