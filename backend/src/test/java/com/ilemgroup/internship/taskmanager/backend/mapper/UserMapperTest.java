package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(MapperTestConfiguration.class)
public class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void testToSummary() {
        User user = new User("abc123", "John Doe", "Engineer", "picture.png", null, null, null);

        UserSummary dto = mapper.toSummary(user);

        assertEquals("abc123", dto.id());
        assertEquals("John Doe", dto.name());
        assertEquals("picture.png", dto.profilePicture());
    }

    @Test
    void testToSummaryList() {
        User user = new User(
                "abc123", "John Doe", "Engineer", "picture1.png",
                null, null, null
        );

        UserSummary summary = mapper.toSummary(user);

        assertEquals("abc123", summary.id());
        assertEquals("John Doe", summary.name());
        assertEquals("picture1.png", summary.profilePicture());
    }

    @Test
    void testToDetails() {
        User user = new User(
                "xyz", "Alice", "Manager", "alice.png",
                null, null, null
        );

        UserDetails dto = mapper.toDetails(user);

        assertEquals("xyz", dto.id());
        assertEquals("Alice", dto.name());
        assertEquals("Manager", dto.job());
    }
}
