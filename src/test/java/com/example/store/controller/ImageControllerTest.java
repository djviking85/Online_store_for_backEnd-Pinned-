package com.example.store.controller;

import com.example.store.entity.AvatarEntity;
import com.example.store.entity.ImageEntity;
import com.example.store.service.AvatarService;
import com.example.store.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @MockBean
    ImageService imageService;
    @MockBean
    AvatarService avatarService;

    private final MockMvc mockMvc;
    @Autowired
    ImageControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser
    void testGetImage() throws Exception {
        ImageEntity image = new ImageEntity();
        image.setId(1);
        image.setFileName("image.png");
        image.setData(image.getFileName().getBytes());

        when(imageService.findImage(image.getId())).thenReturn(Optional.of(image));

        mockMvc.perform(get("/images/{id}", image.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(image.getFileName()));

        verify(imageService, times(1)).findImage(image.getId());
    }

    @Test
    @WithMockUser
    void testGetAvatar() throws Exception {
        AvatarEntity image = new AvatarEntity();
        image.setId(1);
        image.setFileName("image.png");
        image.setData(image.getFileName().getBytes());

        when(avatarService.findAvatar(image.getId())).thenReturn(Optional.of(image));

        mockMvc.perform(get("/avatar/{id}", image.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(image.getFileName()));

        verify(avatarService, times(1)).findAvatar(image.getId());
    }
}