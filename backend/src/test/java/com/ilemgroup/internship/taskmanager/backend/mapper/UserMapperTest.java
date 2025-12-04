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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void testToSummary() {
        User entity = new User("abc123", "John Doe", "Engineer", "pic.png", null, null, null);

        UserSummary dto = mapper.toSummary(entity);

        assertEquals("abc123", dto.id());
        assertEquals("John Doe", dto.name());
        assertEquals("pic.png", dto.profilePicture());
    }

    @Test
    void testToDetails() {
        User entity = new User("xyz", "Alice", "Manager", "alice.png", null, null, null);

        UserDetails dto = mapper.toDetails(entity);

        assertEquals("xyz", dto.id());
        assertEquals("Alice", dto.name());
        assertEquals("Manager", dto.job());
    }
}
