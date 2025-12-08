package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.mapper.NotificationMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.NotificationRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    public List<NotificationDetails> getDetailsList(PageQuery query) {
        String userId = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Specification<@NonNull Notification> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Join<Notification, Ticket> ticketJoin = root.join("ticket");
            Join<Ticket, User> userJoin = ticketJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("azureOid"), userId);
        };

        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<@NonNull Notification> page = notificationRepository.findAll(specification, pageable);

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
