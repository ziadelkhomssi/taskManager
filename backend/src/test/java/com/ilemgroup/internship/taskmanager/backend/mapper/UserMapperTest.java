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
public class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void testToSummary() {
        User user = TestEntityFactory.createBaseUser();

        UserSummary dto = mapper.toSummary(user);

        assertEquals(user.getAzureOid(), dto.id());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getProfilePicture(), dto.profilePicture());
    }

    @Test
    void testToSummaryList() {
        User user = TestEntityFactory.createBaseUser();

        UserSummary summary = mapper.toSummary(user);

        assertEquals(user.getAzureOid(), summary.id());
        assertEquals(user.getName(), summary.name());
        assertEquals(user.getProfilePicture(), summary.profilePicture());
    }

    @Test
    void testToDetails() {
        User user = TestEntityFactory.createBaseUser();

        UserDetails dto = mapper.toDetails(user);

        assertEquals(user.getAzureOid(), dto.id());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getJob(), dto.job());
    }
}
