package com.example.store.controller;

import com.example.store.dto.NewPassword;
import com.example.store.dto.UpdateUser;
import com.example.store.dto.User;
import com.example.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")

public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Update user password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user password is update",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            },
            tags = "Users"
    )
    @PostMapping("set_password")//ok
    public ResponseEntity<?> setPassword(Principal principal, @RequestBody NewPassword newPassword) {
        service.setPassword(principal, newPassword.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "Return user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user return",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            },
            tags = "Users"
    )

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Principal principal){
        try{
            return ResponseEntity.ok(service.getUser(principal));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Operation(summary = "Update user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user is update",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            },
            tags = "Users"
    )

    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(Principal principal, @RequestBody UpdateUser updateUser){
        return ResponseEntity.ok(service.updateUser(principal, updateUser.getFirstName(), updateUser.getLastName(), updateUser.getPhone()));

    }

    @Operation(summary = "Update user image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "image is update",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            },
            tags = "Users"
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(Principal principal, @RequestParam MultipartFile image) throws IOException {
        service.updateImage(principal, image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
