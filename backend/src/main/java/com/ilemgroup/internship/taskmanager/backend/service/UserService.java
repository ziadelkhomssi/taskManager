package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.mapper.UserMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.AccessDeniedException;

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


        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
        return userMapper.toDetails(user, baseUrl);
    }

    public UserSummary getClientSummary() throws AccessDeniedException {
        String clientUserId = AuthorizationService.getClientUserId();
        User user = userRepository.findById(clientUserId)
                .orElseThrow(() -> new EntityNotFoundException("Client user not found: " + clientUserId));

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
        return userMapper.toSummary(user, baseUrl);
    }

    public Page<UserSummary> getSummaryList(
            Pageable pageable,
            String search,
            String filter,
            Long projectId,
            Long sprintId
    ) {
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
        if (search == null || search.isBlank()) {
            if (projectId != null) {
                return userRepository.findAllInProject(projectId, pageable).map(
                        user -> userMapper.toSummary(user, baseUrl)
                );
            } else if (sprintId != null) {
                return userRepository.findAllInSprint(sprintId, pageable).map(
                        user -> userMapper.toSummary(user, baseUrl)
                );
            }
            return userRepository.findAll(pageable).map(
                    user -> userMapper.toSummary(user, baseUrl)
            );
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
        ).map(user -> userMapper.toSummary(user, baseUrl));
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

    public Resource getProfilePicture(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        return new ClassPathResource(user.getProfilePicturePath());
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
