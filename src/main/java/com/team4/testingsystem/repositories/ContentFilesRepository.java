package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ContentFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContentFilesRepository extends CrudRepository<ContentFile, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE ContentFile cf SET cf.url = ?1 WHERE cf.id = ?2")
    int changeUrl(String url, Long id);

    @Query(value = "select * from language_testing.content_file as cf "
                   + "join language_testing.question_content_file as qcf on cf.id = qcf.content_file_id "
                   + "join language_testing.question as q on qcf.question_id = q.id "
                   + "join language_testing.level as l on l.id = q.level_id "
                   + "where l.level_name = ?1 and cf.is_available = true "
                   + "order by RANDOM() limit 1; ", nativeQuery = true)
    ContentFile getRandomFiles(String level);

    @Query("select cf from ContentFile cf "
           + "join cf.questions q "
           + "where q.id = ?1")
    ContentFile getContentFileByQuestionId(Long id);

    @Transactional
    @Modifying
    @Query(value = "update ContentFile cf set cf.available = ?2 where cf.id = ?1")
    int updateAvailable(Long id, boolean available);

    List<ContentFile> findAllByAvailableOrderByIdDesc(boolean isAvailable, Pageable pageable);

    @Query("select distinct cf from ContentFile cf "
           + "join cf.questions q "
           + "where q.level.name = ?1 "
           + "and cf.available = ?2 "
           + "order by cf.id desc ")
    List<ContentFile> getContentFiles(String level, boolean isAvailable, Pageable pageable);
}
