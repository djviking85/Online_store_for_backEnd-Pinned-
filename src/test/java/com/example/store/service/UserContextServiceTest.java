package com.example.store.service;

import com.example.store.dto.Role;
import com.example.store.entity.UserEntity;
import com.example.store.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserContextService.class})
@ExtendWith(SpringExtension.class)
public class UserContextServiceTest {

    @Autowired
    UserContextService service;

    @MockBean
    UserEntityRepository repository;

    private UserEntity userEntity = new UserEntity();
    private final String EMAIL = "123@gmail.com";
    private final String PASSWORD = "password";

    @BeforeEach
    public void fillUserEntity(){
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setRole(Role.ADMIN);
    }

    @Test
    void doAllUsersToContext_success(){
        UserDetails userDetails = User.builder()
                .password(PASSWORD)
                .username(EMAIL)
                .roles("ADMIN")
                .build();
        when(repository.findAll()).thenReturn(List.of(userEntity));
        Assertions.assertEquals(service.doAllUsersToContext(), List.of(userDetails));
    }

}
