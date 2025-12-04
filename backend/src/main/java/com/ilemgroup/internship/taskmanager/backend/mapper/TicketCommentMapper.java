package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfiguration.class, uses = {UserMapper.class})
public interface TicketCommentMapper {

    @Mapping(target = "userSummary", source = "user")
    @Mapping(target = "parentCommentId",
            expression = "java(comment.getParentComment() != null ? comment.getParentComment().getId() : null)")
    TicketCommentDetails toDetails(TicketComment comment);

}
