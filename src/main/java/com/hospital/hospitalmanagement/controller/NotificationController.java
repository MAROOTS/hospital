package com.hospital.hospitalmanagement.controller;
import com.hospital.hospitalmanagement.dto.response.NotificationResponse;
import com.hospital.hospitalmanagement.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications",
        description = "Real-time notification management")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get all my notifications")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>>
    getMyNotifications(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                notificationService.getMyNotifications(email));
    }

    @Operation(summary = "Get unread notification count")
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(Map.of("count",
                notificationService.getUnreadCount(email)));
    }

    @Operation(summary = "Mark all notifications as read")
    @PatchMapping("/mark-all-read")
    public ResponseEntity<String> markAllAsRead(
            @AuthenticationPrincipal String email) {
        notificationService.markAllAsRead(email);
        return ResponseEntity.ok(
                "All notifications marked as read");
    }

    @Operation(summary = "Mark single notification as read")
    @PatchMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }
}
