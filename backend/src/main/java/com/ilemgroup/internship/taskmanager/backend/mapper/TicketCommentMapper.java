package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCommentCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketCommentUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfiguration.class, uses = {UserMapper.class})
public interface TicketCommentMapper {

    @Mapping(target = "userSummary", source = "user")
    @Mapping(target = "parentCommentId",
            expression = "java(ticketComment.getParentComment() != null ? ticketComment.getParentComment().getId() : null)")
    TicketCommentDetails toDetails(TicketComment ticketComment);

    List<TicketCommentDetails> toDetailsList(List<TicketComment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TicketComment createToEntity(TicketCommentCreate command);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TicketComment updateEntity(TicketCommentUpdate command, @MappingTarget TicketComment ticketComment);
}

