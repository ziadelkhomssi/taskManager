package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
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

import java.util.List;

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

    public PageResponse<UserSummary> getSummaryList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<User> page;
        switch (query.filterBy()) {
            case "name" -> {
                page = userRepository.findAllUserByName(query.search(), pageable);
            }
            case "job" -> {

                page = userRepository.findAllUserByJob(query.search(), pageable);
            }

            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + query.filterBy()
            );
        }

        return new PageResponse<>(
                query.page(),
                query.size(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                userMapper.toSummaryList(page.getContent())
        );
    }

    public PageResponse<UserSummary> getProjectParticipants(Long projectId, PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<User> page;
        switch (query.filterBy()) {
            case "name" -> {
                page = userRepository.findAllProjectParticipantsByName(projectId, query.search(), pageable);
            }
            case "job" -> {
                page = userRepository.findAllProjectParticipantsByJob(projectId, query.search(), pageable);
            }

            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + query.filterBy()
            );
        }

        return new PageResponse<>(
                query.page(),
                query.size(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                userMapper.toSummaryList(page.getContent())
        );
    }

    public PageResponse<UserSummary> getSprintParticipants(Long sprintId, PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<User> page;
        switch (query.filterBy()) {
            case "name" -> {
                page = userRepository.findAllSprintParticipantsByName(sprintId, query.search(), pageable);
            }
            case "job" -> {
                page = userRepository.findAllSprintParticipantsByJob(sprintId, query.search(), pageable);
            }

            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + query.filterBy()
            );
        }

        return new PageResponse<>(
                query.page(),
                query.size(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                userMapper.toSummaryList(page.getContent())
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
