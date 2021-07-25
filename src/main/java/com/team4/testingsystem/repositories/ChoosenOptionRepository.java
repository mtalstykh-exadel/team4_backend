package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.ChoosenOption;
import com.team4.testingsystem.entities.Level;
import com.team4.testingsystem.entities.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChoosenOptionRepository extends CrudRepository<ChoosenOption, Long> {

    ChoosenOption update();

}