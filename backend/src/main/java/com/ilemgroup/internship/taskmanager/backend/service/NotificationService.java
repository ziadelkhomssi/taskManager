package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.mapper.NotificationMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.NotificationRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.lang.reflect.Array;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private SMTPEmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;

    public Page<NotificationDetails> getAllForClient(
            Pageable pageable
    ) throws AccessDeniedException {
        String userId = AuthorizationService.getClientUserId();
        return notificationRepository.findAllByUserId(userId, pageable)
                .map(notificationMapper::toDetails);
    }

    public boolean hasUnreadNotificationsForClient() throws AccessDeniedException {
        String userId = AuthorizationService.getClientUserId();
        return notificationRepository.hasUnreadForUser(userId);
    }

    // may want to make a batch version later, we'll see
    public void markAsRead(Long notificationId) throws AccessDeniedException {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found: " + notificationId));

        AuthorizationService.ensureSameUserOrAdmin(
                notification.getTicket().getUser().getAzureOid()
        );

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public void createNotification(Long ticketId, NotificationType type) throws MessagingException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + ticketId));

        Notification notification = new Notification();
        notification.setType(type);
        notification.setTicket(ticket);

        notification = notificationRepository.save(notification);
        sendNotificationEmail(notification);
    }

    private void sendNotificationEmail(Notification notification) throws MessagingException {
        Context context = new Context();
        String body = "";
        String title = "";
        User recipient = notification.getTicket().getUser();

        switch (notification.getType()) {
            case TICKET_ASSIGNED -> {
                title = "Task Manager - Ticket Assigned";
                context.setVariable("assigneeName", recipient.getName());
                context.setVariable("ticketTitle", notification.getTicket().getTitle());
                body = templateEngine.process("ticket-assigned", context);
            }

            case TICKET_STATUS_CHANGED -> {
                title = "Task Manager - Ticket Status Changed";
                context.setVariable("recipientName", recipient.getName());
                context.setVariable("ticketTitle", notification.getTicket().getTitle());
                context.setVariable("ticketStatus", notification.getTicket().getStatus());
                body = templateEngine.process("ticket-status-changed", context);
            }

            default -> {
                return;
            }
        }

        emailService.sendHtml(
                recipient.getEmail(),
                title,
                body
        );
    }
}
