package com.example.store.service;

import com.example.store.entity.AdEntity;
import com.example.store.entity.ImageEntity;
import com.example.store.repository.ImageRepository;
import com.example.store.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ImageService.class})
@ExtendWith(SpringExtension.class)
class ImageServiceTest {

    @MockBean
    ImageRepository imageRepository;
    @MockBean
    UserEntityRepository userEntityRepository;

    private final ImageService imageService;

    @Autowired
    ImageServiceTest(ImageService imageService) {
        this.imageService = imageService;
    }

    @Test
    void testSaveImageForAd() throws IOException {
        ImageEntity image = new ImageEntity();
        MockMultipartFile file = new MockMultipartFile("image", "image.png".getBytes());
        AdEntity ad = new AdEntity();
        image.setData(file.getBytes());
        image.setFileName(file.getName());
        image.setMediaType(file.getContentType());
        image.setAd(ad);

        when(imageRepository.save(any(ImageEntity.class))).thenReturn(image);

        assertEquals(image, imageService.saveImageForAd(ad, file));
        verify(imageRepository, times(1)).save(any(ImageEntity.class));
    }

    @Test
    void testUpdateImageForAd() throws IOException {
        ImageEntity image = new ImageEntity();
        MockMultipartFile file = new MockMultipartFile("image", "image.png".getBytes());
        AdEntity ad = new AdEntity();
        ad.setImage(image);
        image.setData(file.getBytes());
        image.setFileName(file.getName());
        image.setMediaType(file.getContentType());
        image.setAd(ad);

        when(imageRepository.save(any(ImageEntity.class))).thenReturn(image);

        assertEquals(image, imageService.saveImageForAd(ad, file));
        verify(imageRepository, times(1)).save(any(ImageEntity.class));
    }

    @Test
    void testFindImage() {
        ImageEntity image = new ImageEntity();
        image.setId(1);

        when(imageService.findImage(image.getId())).thenReturn(Optional.of(image));

        assertEquals(image, imageService.findImage(image.getId()).get());
        verify(imageRepository, times(1)).findById(any(Integer.class));
    }

}
