package com.team4.testingsystem.repositories;

import com.team4.testingsystem.model.entity.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    @Query(value = "select n from Notification n where n.user.id = ?1 "
                   + "order by n.createdAt desc ")
    List<Notification> getAllByUserId(Long userId);
}
