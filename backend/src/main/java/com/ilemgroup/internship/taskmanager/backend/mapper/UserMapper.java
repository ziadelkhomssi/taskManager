package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.ClientDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.service.AuthorizationService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfiguration.class, uses = {AuthorizationService.class})
public interface UserMapper {

    @Mapping(target = "id", source = "azureOid")
    @Mapping(
            target = "profilePictureUrl",
            expression = "java(baseUrl + \"/user/profile-picture/\" + user.getAzureOid())"
    )
    UserSummary toSummary(User user, @Context String baseUrl);

    @Mapping(target = "id", source = "azureOid")
    List<UserSummary> toSummaryList(List<User> user);

    @Mapping(target = "id", source = "azureOid")
    @Mapping(
            target = "profilePictureUrl",
            expression = "java(baseUrl + \"/user/profile-picture/\" + user.getAzureOid())"
    )
    UserDetails toDetails(User user, @Context String baseUrl);

    @Mapping(target = "userId", source = "azureOid")
    @Mapping(
            target = "profilePictureUrl",
            expression = "java(baseUrl + \"/user/profile-picture/\" + user.getAzureOid())"
    )
    @Mapping(target = "permissions", source = "role", qualifiedByName = "buildPermissionsFromRole")
    ClientDetails toClientDetails(User user, @Context String baseUrl);
}
