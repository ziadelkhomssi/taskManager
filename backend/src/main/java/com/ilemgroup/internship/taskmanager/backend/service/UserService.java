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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDetails getDetailsById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        return userMapper.toDetails(user);
    }

    public Page<UserSummary> getSummaryList(
            Pageable pageable, 
            String search, 
            String filter
    ) {
        Page<UserSummary> page;
        switch (filter) {
            case "name" -> {
                page = userRepository.findAllUserByName(search, pageable).map(userMapper::toSummary);
            }
            case "job" -> {

                page = userRepository.findAllUserByJob(search, pageable).map(userMapper::toSummary);
            }

            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return page;
    }

    public Page<UserSummary> getProjectParticipants(Long projectId, 
            Pageable pageable, 
            String search, 
            String filter
    ) {
        Page<UserSummary> page;
        switch (filter) {
            case "name" -> {
                page = userRepository.findAllProjectParticipantsByName(projectId, search, pageable).map(userMapper::toSummary);
            }
            case "job" -> {
                page = userRepository.findAllProjectParticipantsByJob(projectId, search, pageable).map(userMapper::toSummary);
            }

            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return page;
    }

    public Page<UserSummary> getSprintParticipants(Long sprintId, 
            Pageable pageable, 
            String search, 
            String filter
    ) {
        Page<UserSummary> page;
        switch (filter) {
            case "name" -> {
                page = userRepository.findAllSprintParticipantsByName(sprintId, search, pageable).map(userMapper::toSummary);;
            }
            case "job" -> {
                page = userRepository.findAllSprintParticipantsByJob(sprintId, search, pageable).map(userMapper::toSummary);;
            }

            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return page;
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
