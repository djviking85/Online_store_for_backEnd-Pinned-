package com.example.store.entity;

import com.example.store.dto.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Entity class for user
 *
 * @autor Irina
 */
@Entity
@Data
public class UserEntity implements UserDetails {
    /**
     * Field id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Field email
     */
    @Column(unique = true)
    private String email;
    /**
     * Field first name
     */
    private String firstName;
    /**
     * Field last name
     */
    private String lastName;
    /**
     * Field phone
     */
    private String phone;
    /**
     * Field password
     */
    private String password;
    /**
     * Field ad
     */
    @OneToMany(mappedBy = "pk")
    private List<AdEntity> adEntity;
    /**
     * Field avatar
     */
//    @OneToOne
//    private ImageEntity image;
    @OneToOne
    private AvatarEntity avatarEntity;
    /**
     * Field comments
     */
    @OneToMany
    private List<CommentEntity> comment;
    /**
     * Field role
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Service method
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    /**
     * Method to get Password
     */
    @Override
    public String getPassword(){
        return password;
    }

    /**
     * Method to get email
     */
    @Override
    public String getUsername() {
        return email;
    }
    /**
     * Service method
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    /**
     * Service method
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
    /**
     * Service method
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    /**
     * Service method
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}