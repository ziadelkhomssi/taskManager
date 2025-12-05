package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfiguration.class, uses = {UserMapper.class})
public interface TicketCommentMapper {

    @Mapping(target = "userSummary", source = "user")
    @Mapping(target = "parentCommentId",
            expression = "java(ticketComment.getParentComment() != null ? ticketComment.getParentComment().getId() : null)")
    TicketCommentDetails toDetails(TicketComment ticketComment);

    List<TicketCommentDetails> toDetailsList(List<TicketComment> comments);
}

