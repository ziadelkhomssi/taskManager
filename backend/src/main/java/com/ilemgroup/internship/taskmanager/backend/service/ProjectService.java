package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.mapper.ProjectMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


@Service
@Transactional
public class ProjectService {
    @Value("${project.image.base-path}")
    private String PROJECT_PROFILE_PICTURE_BASE_PATH;

    private enum Filters {
        PROJECT,
        STATUS,
        SPRINT,
        USER
    }

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    public ProjectDetails getDetailsById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        return projectMapper.toDetails(project, baseUrl);
    }

    public Page<ProjectSummary> getSummaryList(
            Pageable pageable, 
            String search, 
            String filter
    ) {
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        if (search == null || search.isBlank()) {
            return projectRepository.findAll(pageable).map(
                    project -> projectMapper.toSummary(project, baseUrl)
            );
        }

        try {
            Filters.valueOf(filter.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return projectRepository.findAllWithFilter(
                search,
                filter.toUpperCase(),
                pageable
        ).map(project -> projectMapper.toSummary(project, baseUrl));
    }


    public Resource getProfilePicture(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));

        Resource resource = resourceLoader.getResource("file:" + project.getProfilePicturePath());
        if (resource.exists()) {
            return resource;
        }

        return new ByteArrayResource(new byte[0]);
    }

    public void createProject(ProjectCreate command, MultipartFile profilePicture) {
        Project project = projectMapper.createToEntity(command);
        project = projectRepository.save(project);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            storeProfilePicture(project.getId(), profilePicture);
            project.setProfilePicturePath(
                    getProjectProfilePicturePath(project.getId())
            );
        }

        projectRepository.save(project);
    }

    public void updateProject(ProjectUpdate command, MultipartFile profilePicture) {
        Project old = projectRepository.findById(command.id())
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + command.id()));

        Project updated = projectMapper.updateEntity(command, old);
        if (profilePicture != null && !profilePicture.isEmpty()) {
            storeProfilePicture(updated.getId(), profilePicture);
            updated.setProfilePicturePath(
                    getProjectProfilePicturePath(updated.getId())
            );
        }

        projectRepository.save(updated);
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found: " + id);
        }
        projectRepository.deleteById(id);
    }

    private String getProjectProfilePicturePath(Long id) {
        return PROJECT_PROFILE_PICTURE_BASE_PATH + "/" + id + "/profile_picture.png";
    }

    private void storeProfilePicture(Long projectId, MultipartFile file) {
        try {
            Path projectDirectory = Path.of(PROJECT_PROFILE_PICTURE_BASE_PATH).resolve(
                    String.valueOf(projectId)
            );
            Files.createDirectories(projectDirectory);

            Path imagePath = projectDirectory.resolve("profile_picture.png");
            Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store project profile picture!", e);
        }
    }
}



