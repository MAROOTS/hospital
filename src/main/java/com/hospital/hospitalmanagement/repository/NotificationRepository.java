package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Notification;
import com.hospital.hospitalmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification,String> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(
            User recipient);

    List<Notification> findByRecipientAndReadFalse(User recipient);

    long countByRecipientAndReadFalse(User recipient);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true " +
            "WHERE n.recipient = :recipient")
    void markAllAsRead(@Param("recipient") User recipient);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true " +
            "WHERE n.id = :id")
    void markAsRead(@Param("id") String id);
}
