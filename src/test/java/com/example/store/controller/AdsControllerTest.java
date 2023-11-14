package com.example.store.controller;

import com.example.store.dto.Ad;
import com.example.store.dto.Ads;
import com.example.store.dto.CreateOrUpdateAd;
import com.example.store.dto.ExtendedAd;
import com.example.store.entity.AdEntity;
import com.example.store.repository.AdEntityRepository;
import com.example.store.service.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(AdsController.class)
public class AdsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdEntityRepository adEntityRepository;
    @MockBean
    private AdService adService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @WithMockUser
    void testGetAds() throws Exception {
        Ads fakeAds = new Ads();

        when(adService.getAllAds()).thenReturn(fakeAds);

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());
        verify(adService, times(1)).getAllAds();
    }

    @Test
    @WithMockUser
    void testAddAds() throws Exception {
        MockMultipartFile filePart = new MockMultipartFile(
                "image",
                "image.png",
                "image/png",
                "Test Image".getBytes());

        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Кроссовки");
        ad.setPrice(5000);
        ad.setAuthor(1);
        ad.setImage(filePart.toString());

        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd(ad.getTitle(), ad.getPrice(), "Крутые-кроссы");
        MockPart jsonPart = new MockPart("properties", null, objectMapper.writeValueAsBytes(createOrUpdateAd));
        jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        when(adService.saveAd(any(CreateOrUpdateAd.class), any(MultipartFile.class), any(Principal.class)))
                .thenReturn(ad);

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/ads")
                .part(jsonPart)
                .file(filePart)
                .with(csrf())
        ).andExpect(status().isCreated());
        verify(adService, times(1))
                .saveAd(any(CreateOrUpdateAd.class), any(MultipartFile.class), any(Principal.class));
    }

    @Test
    @WithMockUser
    void testGetInformationAboutAd() throws Exception {
        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Кроссовки");
        ad.setPrice(5000);
        ad.setAuthor(1);

        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(ad.getPk());
        extendedAd.setTitle(ad.getTitle());
        extendedAd.setPrice(ad.getPrice());

        when(adService.getInformationAboutAd(any(Integer.class))).thenReturn(extendedAd);

        mockMvc.perform(get("/ads/{id}", ad.getPk()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pk").value(extendedAd.getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(extendedAd.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(extendedAd.getPrice()));

        verify(adService, times(1)).getInformationAboutAd(any(Integer.class));
    }

    @Test
    @WithMockUser
    void testDeleteAd() throws Exception {
        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Кроссовки");
        ad.setPrice(5000);
        ad.setAuthor(1);
        mockMvc.perform(delete("/ads/{id}", ad.getPk())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(adService, times(1)).deleteAd(any(Integer.class));
    }

    @Test
    @WithMockUser
    void testUpdateAd() throws Exception {
        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Кроссовки");
        ad.setPrice(5000);
        ad.setAuthor(1);
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd(
                "New title", ad.getPrice() + 1, "New description");
        JSONObject createOrUpdateAdObject = new JSONObject();
        createOrUpdateAdObject.put("title", createOrUpdateAd.getTitle());
        createOrUpdateAdObject.put("price", createOrUpdateAd.getPrice());
        createOrUpdateAdObject.put("description", createOrUpdateAd.getDescription());

        when(adService.updateAD(any(Integer.class), any(CreateOrUpdateAd.class))).
                thenReturn(ad);

        mockMvc.perform(patch("/ads/{id}", ad.getPk())
                        .content(createOrUpdateAdObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(adService, times(1)).updateAD(any(Integer.class), any(CreateOrUpdateAd.class));

    }


    @Test
    @WithMockUser
    void testGetAllAdsByUser() throws Exception {
        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Кроссовки");
        ad.setPrice(5000);
        ad.setAuthor(1);
        Ads ads = new Ads();
        ads.setCount(1);
        ads.setResults(List.of(ad));

        when(adService.getAllAdsByUser(any(Principal.class))).thenReturn(ads);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk());

        verify(adService, times(1)).getAllAdsByUser(any(Principal.class));
    }

    @Test
    @WithMockUser
    void testUpdateImageAd() throws Exception {
        AdEntity ad = new AdEntity();
        ad.setPk(1);
        ad.setTitle("Кроссовки");
        ad.setPrice(5000);

        MockPart image = new MockPart(
                "image",
                "image.png",
                "image.png".getBytes());
        image.getHeaders().setContentType(MediaType.IMAGE_PNG);


        when(adEntityRepository.findById(any(Integer.class))).thenReturn(Optional.of(ad));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/ads/{id}/image", ad.getPk())
                        .part(image)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(adService, times(1))
                .updateImage(any(Integer.class), any(MultipartFile.class));
    }
}
