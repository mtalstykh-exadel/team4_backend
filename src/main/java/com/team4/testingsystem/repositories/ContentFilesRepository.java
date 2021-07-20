package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ContentFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ContentFilesRepository extends CrudRepository<ContentFile, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE ContentFile cf SET cf.url = ?1 WHERE cf.id = ?2")
    int changeUrl(String url, Long id);
}
