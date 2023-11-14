package com.example.store.service;

import com.example.store.dto.NewPassword;
import com.example.store.dto.Register;
import com.example.store.dto.UpdateUser;
import com.example.store.dto.User;
import com.example.store.entity.AvatarEntity;
import com.example.store.entity.UserEntity;
import com.example.store.mapper.UserMapper;
import com.example.store.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Service

public class UserService {
    private final UserEntityRepository repository;
    private final UserDetailsManager manager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AvatarService avatarService;
    public UserService(UserEntityRepository repository, UserDetailsManager manager, UserMapper userMapper, PasswordEncoder passwordEncoder, AvatarService avatarService) {
        this.repository = repository;
        this.manager = manager;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.avatarService = avatarService;
    }
    /**
     * Method for updating the user's password
     * @param principal - can be used to represent any entity, such as an individual, a corporation, and a login id.
     * @param newPassword - updated password for the user
     */

    public NewPassword setPassword(Principal principal, String newPassword){
        UserEntity userEntity = repository.findByEmail(principal.getName()).get();
        NewPassword newPasswordDTO = new NewPassword(userEntity.getPassword(), newPassword);
        manager.changePassword(userEntity.getPassword(), passwordEncoder.encode(newPassword));
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        repository.save(userEntity);
        return newPasswordDTO;
    }
    /**
     * The method returns data about the user
     * @param principal - can be used to represent any entity, such as an individual, a corporation, and a login id.
     */

    public User getUser(Principal principal) {
        return userMapper.toDTO(repository.findByEmail(principal.getName()).orElseThrow(EntityNotFoundException::new));
    }
    /**
     * Method for updating user data
     * @param principal - can be used to represent any entity, such as an individual, a corporation, and a login id.
     * @param firstName - user's name
     * @param lastName - user's last name
     * @param phone - user's phone number
     */
    public UpdateUser updateUser(Principal principal, String firstName, String lastName, String phone)  {//ок
        Optional<UserEntity> userEntity = repository.findByEmail(principal.getName());
        if (userEntity.isPresent()){
            UserEntity newUserEntity = userEntity.get();
            newUserEntity.setFirstName(firstName);
            newUserEntity.setLastName(lastName);
            newUserEntity.setPhone(phone);
            repository.save(newUserEntity);
            return new UpdateUser(firstName, lastName, phone);
        }
        return new UpdateUser(firstName,lastName,phone);
    }
    /**
     * The method updates the user's photo
     * @param principal - can be used to represent any entity, such as an individual, a corporation, and a login id.
     * @param image - user's avatar
     */

    public User updateImage(Principal principal, MultipartFile image) throws IOException {
        Optional<UserEntity> userEntity = repository.findByEmail(principal.getName());
        if (userEntity.isPresent()){
            AvatarEntity uploadImage = avatarService.saveAvatarForUser(principal, image);
            UserEntity newUserEntity = userEntity.get();
            newUserEntity.setAvatarEntity(uploadImage);
            repository.save(newUserEntity);
            return userMapper.toDTO(newUserEntity);
        }
        return null;
    }
    /**
     * The method adds the user to the table
     * @param register - full user information
     */
    public void addInTable(Register register){
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userEntity.setEmail(register.getUsername());
        userEntity.setPhone(register.getPhone());
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        userEntity.setRole(register.getRole());
        repository.save(userEntity);
    }
}
