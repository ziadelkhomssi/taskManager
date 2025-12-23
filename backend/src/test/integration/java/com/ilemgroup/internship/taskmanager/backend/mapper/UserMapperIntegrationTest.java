package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperIntegrationTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Autowired
    private UserMapper mapper;

    @Test
    void testToSummary() {
        User user = TestEntityFactory.createBaseUser();

        UserSummary summary = mapper.toSummary(user, BASE_URL);

        assertEquals(user.getAzureOid(), summary.id());
        assertEquals(user.getName(), summary.name());
        assertEquals(BASE_URL + "/user/profile-picture/" + user.getAzureOid(), summary.profilePictureUrl());
    }

    @Test
    void testToDetails() {
        User user = TestEntityFactory.createBaseUser();

        UserDetails details = mapper.toDetails(user, BASE_URL);

        assertEquals(user.getAzureOid(), details.id());
        assertEquals(user.getName(), details.name());
        assertEquals(user.getJob(), details.job());
        assertEquals(BASE_URL + "/user/profile-picture/" + user.getAzureOid(), details.profilePictureUrl());
    }
}
