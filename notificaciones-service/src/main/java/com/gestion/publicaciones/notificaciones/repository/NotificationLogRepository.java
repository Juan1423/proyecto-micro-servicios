package com.gestion.publicaciones.notificaciones.repository;

import com.gestion.publicaciones.notificaciones.domain.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {
}