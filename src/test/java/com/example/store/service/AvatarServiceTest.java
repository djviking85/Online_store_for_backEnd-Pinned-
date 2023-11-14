package com.example.store.service;

import com.example.store.entity.AvatarEntity;
import com.example.store.entity.UserEntity;
import com.example.store.repository.AvatarRepository;
import com.example.store.repository.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AvatarService.class})
@ExtendWith(SpringExtension.class)
class AvatarServiceTest {

    @MockBean
    AvatarRepository avatarRepository;
    @MockBean
    UserEntityRepository userEntityRepository;

    private final AvatarService avatarService;

    @Autowired
    AvatarServiceTest(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @Test
    void testSaveAvatarForUser() throws IOException {
        AvatarEntity image = new AvatarEntity();
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "email";
            }
        };
        MockMultipartFile file = new MockMultipartFile("image", "image.png".getBytes());
        UserEntity user = new UserEntity();
        image.setData(file.getBytes());
        image.setFileName(file.getName());
        image.setMediaType(file.getContentType());
        image.setUser(user);

        when(avatarRepository.save(any(AvatarEntity.class))).thenReturn(image);
        when(userEntityRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        assertEquals(image, avatarService.saveAvatarForUser(principal, file));
        verify(avatarRepository, times(1)).save(any(AvatarEntity.class));
        verify(userEntityRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void testFindAvatar() {
        AvatarEntity image = new AvatarEntity();
        image.setId(1);

        when(avatarService.findAvatar(image.getId())).thenReturn(Optional.of(image));

        assertEquals(image, avatarService.findAvatar(image.getId()).get());
        verify(avatarRepository, times(1)).findById(any(Integer.class));
    }
}
