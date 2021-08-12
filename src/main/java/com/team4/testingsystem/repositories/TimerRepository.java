package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Timer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends CrudRepository<Timer, Long> {
}
