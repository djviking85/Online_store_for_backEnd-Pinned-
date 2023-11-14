package com.example.store.mapper;

import com.example.store.dto.Comment;
import com.example.store.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * This class implements the functionality of mapping comments
 */

@Mapper(componentModel = "spring")
public interface CommentMapper {
    /**
     * Method for mapping comment DTO to comment entity
     * @param comment {@link Comment}
     * @return {@link CommentEntity}
     */
    @Mapping(source = "author", target = "user.id")
    CommentEntity toEntity(Comment comment);

    /**
     * Method for mapping comment entity to comment DTO
     * @param commentEntity {@link CommentEntity}
     * @return {@link Comment}
     */
    @Mapping(source = "user.id", target = "author")
    Comment toDTO(CommentEntity commentEntity);

    /**
     * Method for mapping the comment entity list to the comment DTO list
     * @param commentEntityList list of {@link CommentEntity}
     * @return list of {@link Comment}
     */

    List<Comment> commentsListToDTO(List<CommentEntity> commentEntityList);
}
