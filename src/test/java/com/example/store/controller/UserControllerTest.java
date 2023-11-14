package com.example.store.controller;

import com.example.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private UserController controller;
    @MockBean
    private UserService service;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser()
    void setPassword_success() throws Exception {
        JSONObject newPasswordObject = new JSONObject();
        newPasswordObject.put("currentPassword", "PASSWORD");
        newPasswordObject.put("newPassword", "password");
        mockMvc.perform(post("/users/set_password")
                        .content(newPasswordObject.toJSONString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    void getUser_success() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    void updateUser_success() throws Exception {
        JSONObject updateUserObject = new JSONObject();
        updateUserObject.put("firstName", "ivan");
        updateUserObject.put("lastName", "ivanov");
        updateUserObject.put("phone", "+7 (777) 777-77-77");
        mockMvc.perform(patch("/users/me")
                        .content(updateUserObject.toJSONString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    void updateImage_success() throws Exception {
        MockPart image = new MockPart(
                "image",
                "image.png",
                "image.png".getBytes());
        image.getHeaders().setContentType(MediaType.IMAGE_PNG);
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.PATCH, "/users/me/image")
                .part(image)
                .with(csrf())
        ).andExpect(status().isOk());
    }



}