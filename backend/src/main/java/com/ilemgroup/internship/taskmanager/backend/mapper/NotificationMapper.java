package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfiguration.class, uses = {TicketMapper.class})
public interface NotificationMapper {

    @Mapping(target = "type", expression = "java(notification.getType())")
    @Mapping(target = "ticketSummary", source = "ticket")
    NotificationDetails toDetails(Notification notification);
}
