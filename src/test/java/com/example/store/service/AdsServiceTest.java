package com.example.store.service;

import com.example.store.dto.Ad;
import com.example.store.dto.Ads;
import com.example.store.dto.CreateOrUpdateAd;
import com.example.store.dto.ExtendedAd;
import com.example.store.entity.AdEntity;
import com.example.store.entity.ImageEntity;
import com.example.store.entity.UserEntity;
import com.example.store.exception.AdNotFoundException;
import com.example.store.mapper.AdMapper;
import com.example.store.repository.AdEntityRepository;
import com.example.store.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AdsServiceTest {
    @Mock
    private AdEntityRepository adEntityRepository;
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private AdMapper adMapper;
    @Mock
    private ImageService imageService;
    @Mock
    private Principal principal;
    @InjectMocks
    private AdService adService;
    Ad AD = new Ad();
    UserEntity USER = new UserEntity();
    MockMultipartFile FILE = new MockMultipartFile(
            "image",
            "image.png",
            "image/png",
            "Test Image".getBytes());
    ImageEntity IMAGE = new ImageEntity();

    CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("Кроссовки", 5000, "Крутые-кроссы");
    AdEntity adEntity = new AdEntity();
    List<AdEntity> adEntityList = new ArrayList<>();
    List<Ad> adList = new ArrayList<>();
    Ads ads = new Ads();


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        AD.setPk(1);
        AD.setTitle("Кроссовки");
        AD.setPrice(5000);
        AD.setAuthor(1);

        USER.setId(1);
        USER.setFirstName("User");
        USER.setLastName("User");
        USER.setEmail("user@mail.ru");
        USER.setPhone("+7(000)000-00-00");

        IMAGE.setData(FILE.getBytes());

        adEntity.setUser(USER);
        adEntity.setImage(IMAGE);
        adEntity.setPk(AD.getPk());
        adEntity.setDescription(createOrUpdateAd.getDescription());
        adEntity.setTitle(createOrUpdateAd.getTitle());

        adEntityList.add(adEntity);

        adList.add(AD);

        ads.setCount(adList.size());
        ads.setResults(adList);
    }


    @Test
    void testSaveAd() throws IOException {
        when(userEntityRepository.findByEmail(principal.getName())).thenReturn(Optional.of(USER));
        when(adMapper.mapCreateOrUpdateDtoTo(createOrUpdateAd)).thenReturn(adEntity);
        when(adService.saveAd(createOrUpdateAd, FILE, principal)).thenReturn(AD);

        Ad result = adService.saveAd(createOrUpdateAd, FILE, principal);

        assertEquals(AD, result);
        verify(adMapper, times(2)).mapCreateOrUpdateDtoTo(createOrUpdateAd);
        verify(userEntityRepository, times(2)).findByEmail(principal.getName());
    }

    @Test
    void testGetAllAds() {
        List<Ad> adList = List.of(AD);
        Ads ads = new Ads();
        ads.setCount(adList.size());
        ads.setResults(adList);

        List<AdEntity> list = List.of(adEntity);

        when(adEntityRepository.findAll()).thenReturn(list);
        when(adMapper.mapToListDto(list)).thenReturn(List.of(AD));


        Ads result = adService.getAllAds();

        assertEquals(ads, result);
        verify(adMapper, times(1)).mapToListDto(list);
        verify(adEntityRepository, times(1)).findAll();
    }

    @Test
    void testGetInformationAboutAd() throws AdNotFoundException {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPrice(AD.getPrice());
        extendedAd.setPk(AD.getPk());
        extendedAd.setPhone(USER.getPhone());
        extendedAd.setEmail(USER.getEmail());
        extendedAd.setTitle(AD.getTitle());
        extendedAd.setImage(FILE.toString());
        extendedAd.setDescription(createOrUpdateAd.getDescription());

        when(adEntityRepository.findById(any(Integer.class))).thenReturn(Optional.of(adEntity));
        when(adMapper.mapToExtAdDto(adEntity)).thenReturn(extendedAd);

        ExtendedAd result = adService.getInformationAboutAd(AD.getPk());

        assertEquals(extendedAd, result);
        verify(adMapper, times(1)).mapToExtAdDto(adEntity);
        verify(adEntityRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void testDeleteAd() throws AdNotFoundException {
        when(adEntityRepository.findById(any(Integer.class))).thenReturn(Optional.of(adEntity));

        adService.deleteAd(AD.getPk());

        verify(adEntityRepository, times(1)).deleteById(any(Integer.class));
        verify(adEntityRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void testUpdateAD() throws AdNotFoundException {
        when(adEntityRepository.findById(any(Integer.class))).thenReturn(Optional.of(adEntity));
        when(adMapper.mapToDto(any(AdEntity.class))).thenReturn(AD);

        Ad result = adService.updateAD(AD.getPk(), createOrUpdateAd);

        assertEquals(AD, result);
        verify(adMapper, times(1)).mapToDto(adEntity);
        verify(adEntityRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void testGetAllAdsByUser() throws AdNotFoundException {
        when(userEntityRepository.findByEmail(principal.getName())).thenReturn(Optional.of(USER));
        when(adEntityRepository.findAllByUserId(any(Integer.class))).thenReturn(adEntityList);
        when(adMapper.mapToListDto(adEntityList)).thenReturn(adList);

        Ads result = adService.getAllAdsByUser(principal);

        assertEquals(ads, result);
        verify(adMapper, times(1)).mapToListDto(adEntityList);
        verify(adEntityRepository, times(1)).findAllByUserId(any(Integer.class));
        verify(userEntityRepository, times(1)).findByEmail(principal.getName());
    }

    @Test
    void testUpdateImage() throws IOException {
        when(adEntityRepository.findById(any(Integer.class))).thenReturn(Optional.of(adEntity));

        adService.updateImage(AD.getPk(), FILE);

        verify(adEntityRepository, times(1)).findById(any(Integer.class));
        verify(imageService, times(1)).updateImageForAd(adEntity, FILE);
    }

    @Test
    void testCheckAccessForAd() {
        when(adEntityRepository.findById(any(Integer.class))).thenReturn(Optional.of(adEntity));

        assertTrue(adService.checkAccessForAd(USER.getUsername(), AD.getPk()));
        verify(adEntityRepository, times(1)).findById(any(Integer.class));
    }

}