package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.mapper.UserMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.function.Supplier;

@Service
@Transactional
public class UserService {
    private enum Filters {
        NAME,
        JOB
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDetails getDetailsById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        return userMapper.toDetails(user);
    }

    public UserSummary getClientSummary() throws AccessDeniedException {
        String clientUserId = AuthorizationService.getClientUserId();
        User user = userRepository.findById(clientUserId)
                .orElseThrow(() -> new EntityNotFoundException("Client user not found: " + clientUserId));

        return userMapper.toSummary(user);
    }

    public Page<UserSummary> getSummaryList(
            Pageable pageable,
            String search,
            String filter,
            Long projectId,
            Long sprintId
    ) {
        if (search == null || search.isBlank()) {
            if (projectId != null) {
                return userRepository.findAllInProject(projectId, pageable).map(userMapper::toSummary);
            } else if (sprintId != null) {
                return userRepository.findAllInSprint(sprintId, pageable).map(userMapper::toSummary);
            }
            return userRepository.findAll(pageable).map(userMapper::toSummary);
        }

        try {
            Filters.valueOf(filter.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return userRepository.findAllWithFilter(
                search,
                filter.toUpperCase(),
                projectId,
                sprintId,
                pageable
        ).map(userMapper::toSummary);
    }

    public Page<UserSummary> getAllUsers(
            Pageable pageable,
            String search,
            String filter
    ) {
        return getSummaryList(
                pageable,
                search,
                filter,
                null,
                null
        );
    }

    public Page<UserSummary> getProjectParticipants(
            Long projectId,
            Pageable pageable, 
            String search, 
            String filter
    ) {
        return getSummaryList(
                pageable,
                search,
                filter,
                projectId,
                null
        );
    }

    public Page<UserSummary> getSprintParticipants(
            Long sprintId,
            Pageable pageable, 
            String search, 
            String filter
    ) {
        return getSummaryList(
                pageable,
                search,
                filter,
                null,
                sprintId
        );
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        User existing = userRepository.findById(user.getAzureOid())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + user.getAzureOid()));

        userRepository.save(user);
    }

    public void deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
