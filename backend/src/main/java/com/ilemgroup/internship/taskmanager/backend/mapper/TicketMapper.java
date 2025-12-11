package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfiguration.class, uses = {UserMapper.class})
public interface TicketMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "status", source = "status")
    TicketSummary toSummary(Ticket ticket);

    List<TicketSummary> toSummaryList(List<Ticket> ticketList);

    @Mapping(target = "userSummary", source = "user")
    TicketDetails toDetails(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "closedBy", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    @Mapping(target = "user", ignore = true)
    Ticket createToEntity(TicketCreate command);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "closedBy", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    @Mapping(target = "user", ignore = true)
    Ticket updateEntity(TicketUpdate command, @MappingTarget Ticket ticket);

}
