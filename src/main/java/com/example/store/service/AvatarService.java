package com.example.store.service;

import com.example.store.entity.AvatarEntity;
import com.example.store.entity.UserEntity;
import com.example.store.repository.AvatarRepository;
import com.example.store.repository.UserEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Service

public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final UserEntityRepository userEntityRepository;


    public AvatarService(AvatarRepository avatarRepository, UserEntityRepository userEntityRepository) {
        this.avatarRepository = avatarRepository;
        this.userEntityRepository = userEntityRepository;
    }

    /**
     * Method of saving the avatar for the user
     * @param principal The principal is the currently logged in user {@link Principal}.
     * @param file Image file
     * @return Avatar entity
     * @throws IOException default error for working with {@link MultipartFile}
     */
    public AvatarEntity saveAvatarForUser(Principal principal, MultipartFile file) throws IOException {
        AvatarEntity avatarEntity = new AvatarEntity();
        UserEntity userEntity = userEntityRepository.findByEmail(principal.getName()).get();
        try {
            avatarEntity.setData(file.getBytes());
            avatarEntity.setFileName(file.getName());
            avatarEntity.setMediaType(file.getContentType());
            avatarEntity.setUser(userEntity);
            avatarRepository.save(avatarEntity);
            userEntity.setAvatarEntity(avatarEntity);
        } catch (IOException e) {
            throw new IOException();
        }
        return avatarEntity;
    }

    /**
     * Method of getting an avatar by id
     * @param id must not be null
     * @return optional avatar
     */
    public Optional<AvatarEntity> findAvatar(Integer id) {
        return avatarRepository.findById(id);
    }
}
