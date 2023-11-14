package com.example.store.mapper;

import com.example.store.dto.User;
import com.example.store.entity.AvatarEntity;
import com.example.store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * This class implements user mapping functionality
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Method for mapping user entity to user DTO
     * @param userEntity {@link UserEntity}
     * @return {@link User}
     */
    @Mapping(target = "image", source = "avatarEntity.user", qualifiedByName = "avatarToString")
    User toDTO(UserEntity userEntity);

    /**
     * Method for mapping an avatar to a String value
     * @param userEntity {@link UserEntity}
     * @return The link for displaying the avatar is passed to the controller {@link ru.skypro.homework.controller.ImageController}
     */
    @Named("avatarToString")
    default String avatarToString(UserEntity userEntity) {
        if (userEntity == null){
            return "";
        }
        AvatarEntity avatarEntity = userEntity.getAvatarEntity();
        return "/avatar/" + avatarEntity.getId().toString();
    }
}
