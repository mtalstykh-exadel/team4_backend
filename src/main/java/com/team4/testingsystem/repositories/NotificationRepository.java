package com.team4.testingsystem.repositories;

import com.team4.testingsystem.entities.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    @Query(value = "select n from Notification n where n.user.id = ?1")
    List<Notification> getAllByUserId(Long userId);
}
