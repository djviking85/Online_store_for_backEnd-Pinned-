package com.example.store.service;

import com.example.store.dto.NewPassword;
import com.example.store.dto.Register;
import com.example.store.dto.UpdateUser;
import com.example.store.dto.User;
import com.example.store.entity.AvatarEntity;
import com.example.store.entity.UserEntity;
import com.example.store.mapper.UserMapper;
import com.example.store.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.management.remote.JMXPrincipal;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @Autowired
    private UserService service;
    @MockBean
    private UserEntityRepository repository;

    @MockBean
    private UserMapper userMapper;
    @MockBean
    private AvatarService avatarService;
    @MockBean
    private UserDetailsManager manager;

    @MockBean
    private PasswordEncoder passwordEncoder;


    private UserEntity userEntity = new UserEntity();
    private User userDTO = new User();
    private final String EMAIL = "123@gmail.com";
    private final String PASSWORD = "password";
    private final String NEW_PASSWORD = "password123";
    private final Principal principal = new JMXPrincipal(EMAIL);
    @BeforeEach
    public void fillUserEntity(){
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userDTO.setEmail(EMAIL);
    }


    @Test
    void setPassword_success(){
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        doNothing().when(manager).changePassword(any(), any());
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD);
        when(repository.save(any())).thenReturn(userEntity);
        Assertions.assertEquals(service.setPassword(principal,NEW_PASSWORD),new NewPassword(PASSWORD,NEW_PASSWORD));
    }

    @Test
    void getUser_success(){
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);
        Assertions.assertEquals(service.getUser(principal), userDTO);
    }

    @Test
    void updateUser_success(){
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        when(repository.save(any())).thenReturn(userEntity);
        Assertions.assertEquals(service.updateUser(principal, "1","2","+7 (777) 777-77-77")
                ,new UpdateUser("1", "2", "+7 (777) 777-77-77"));
    }

    @Test
    void updateImage_success() throws IOException {
        MultipartFile file = new MockMultipartFile("file",new  byte[]{0, 1});
        AvatarEntity image = new AvatarEntity();
        image.setFileName("file");
        image.setData(new  byte[]{0, 1});
        userDTO.setImage("/avatar/1");
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(userEntity));
        when(avatarService.saveAvatarForUser(any(),any())).thenReturn(image);
        userEntity.setAvatarEntity(image);
        when(repository.save(any())).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);
        Assertions.assertEquals(service.updateImage(principal, file),userDTO);
    }

    @Test
    void addInTable_success(){
        service.addInTable(new Register());
        verify(repository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
    }
}
