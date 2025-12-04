package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfiguration.class)
public interface UserMapper {

    @Mapping(target = "id", source = "azureOid")
    UserSummary toSummary(User user);

    @Mapping(target = "id", source = "azureOid")
    UserDetails toDetails(User user);

}
