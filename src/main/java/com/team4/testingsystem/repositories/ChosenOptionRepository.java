package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ChosenOption;
import com.team4.testingsystem.entities.ChosenOptionID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChosenOptionRepository extends CrudRepository<ChosenOption, ChosenOptionID> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE ChosenOption CO SET CO = ?1 WHERE CO.chosenOptionID = ?2")
    int update(ChosenOption newCO, ChosenOptionID ID);

}