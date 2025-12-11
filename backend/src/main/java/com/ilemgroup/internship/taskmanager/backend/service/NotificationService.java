package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.mapper.NotificationMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.NotificationRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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

    public List<NotificationDetails> getDetailsList(PageQuery query) throws AccessDeniedException {
        String userId = AuthorizationService.getClientUserId();
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<Notification> page = notificationRepository.findAllByUserId(userId, pageable);
        return notificationMapper.toDetailsList(page.getContent());
    }

    // may want to make a batch version later, we'll see
    public void markAsRead(Long notificationId) throws AccessDeniedException {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found: " + notificationId));

        AuthorizationService.ensureSameUserOrAdmin(
                notification.getTicket().getUser().getAzureOid()
        );

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void createNotification(Long ticketId, NotificationType type) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + ticketId));

        Notification notification = new Notification();
        notification.setType(type);
        notification.setTicket(ticket);

        notificationRepository.save(notification);
        sendNotificationEmail(notification);
    }

    private void sendNotificationEmail(Notification notification) {
        // placeholder to actual NotificationEmailService class!
    }
}
