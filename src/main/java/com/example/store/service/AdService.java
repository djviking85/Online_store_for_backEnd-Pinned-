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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class AdService {
    private final AdEntityRepository adRepository;
    private final UserEntityRepository userEntityRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;


    public AdService(AdEntityRepository adRepository, UserEntityRepository userEntityRepository, AdMapper adMapper, ImageService imageService) {
        this.adRepository = adRepository;
        this.userEntityRepository = userEntityRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
    }

    /**
     * Saving an ad to a database
     * @param createOrUpdateAd DTO template for mapping to an entity
     * @param image Image file
     * @param principal The principal is the currently logged in user {@link Principal}.
     * @return an entity transformed into a DTO by mapping
     * @throws IOException default error for working with {@link MultipartFile}
     */

    public Ad saveAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, Principal principal) throws IOException {
        String email = principal.getName();
        Optional<UserEntity> optionalUserEntity = userEntityRepository.findByEmail(email);
        UserEntity userEntity = optionalUserEntity.get();
        AdEntity adEntity = adMapper.mapCreateOrUpdateDtoTo(createOrUpdateAd);
        ImageEntity uploadImage = imageService.saveImageForAd(adEntity, image);
        adEntity.setImage(uploadImage);
        adEntity.setUser(userEntity);
        adRepository.save(adEntity);
        return adMapper.mapToDto(adEntity);
    }

    /**
     * Method for getting all ads
     * @return an list transformed into a DTO by mapping
     */

    public Ads getAllAds() {
        List<AdEntity> list = adRepository.findAll();
        List<Ad> adDtoList = adMapper.mapToListDto(list);
        Ads ads = new Ads();
        ads.setCount(adDtoList.size());
        ads.setResults(adDtoList);
        return ads;
    }

    /**
     * Method of getting an ad by ID
     * @param idAd must not be null
     * @return an entity transformed into a DTO by mapping
     * @throws AdNotFoundException the checked exception
     */

    public ExtendedAd getInformationAboutAd(int idAd) throws AdNotFoundException {
        Optional<AdEntity> optionalAdlEntity = adRepository.findById(idAd);
        if (optionalAdlEntity.isPresent()) {
            AdEntity adEntity = optionalAdlEntity.get();
            return adMapper.mapToExtAdDto(adEntity);
        }
        throw new AdNotFoundException();
    }

    /**
     * Method of deleting an ad by ID
     * It can be used only by authorized users or by a user with the ADMIN role
     * @param idAd must not be null
     * @throws AdNotFoundException the checked exception
     */
    @PreAuthorize(value = "hasRole('ADMIN') or @adService.checkAccessForAd(principal.username, #idAd)")
    public void deleteAd(Integer idAd) throws AdNotFoundException {
        Optional<AdEntity> optionalAdEntity = adRepository.findById(idAd);
        if (optionalAdEntity.isPresent()) {
            AdEntity adEntity = optionalAdEntity.get();
            adRepository.deleteById(adEntity.getPk());
        } else {
            throw new AdNotFoundException();
        }
    }

    /**
     * Ad update method
     * @param adId must not be null
     * @param createOrUpdateAd {@link CreateOrUpdateAd}
     * @return an entity transformed into a DTO by mapping
     * @throws AdNotFoundException the checked exception
     */
    @PreAuthorize("hasRole('ADMIN') or @adService.checkAccessForAd(principal.username, #adId)")
    public Ad updateAD(Integer adId, CreateOrUpdateAd createOrUpdateAd) throws AdNotFoundException {
        Optional<AdEntity> optionalAdlEntity = adRepository.findById(adId);
        if (optionalAdlEntity.isPresent()) {
            AdEntity adEntity = optionalAdlEntity.get();
            adEntity.setTitle(createOrUpdateAd.getTitle());
            adEntity.setPrice(createOrUpdateAd.getPrice());
            adEntity.setDescription(createOrUpdateAd.getDescription());
            adRepository.save(adEntity);
            return adMapper.mapToDto(adEntity);
        } else {
            throw new AdNotFoundException();
        }
    }

    /**
     * Getting all the ads of a specific user
     * @param principal The principal is the currently logged in user {@link Principal}.
     * @return All ads of a specific user. Learn more in {@link Ads}
     * @throws AdNotFoundException the checked exception
     */

    public Ads getAllAdsByUser(Principal principal) throws AdNotFoundException {
        Optional<UserEntity> optionalUserEntity = userEntityRepository.findByEmail(principal.getName());
        System.out.println(principal.getName());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            System.out.println(userEntity.getEmail());
            List<AdEntity> adEntity = adRepository.findAllByUserId(userEntity.getId());
            System.out.println(adEntity.size());
            List<Ad> adsListDto = adMapper.mapToListDto(adEntity);
            Ads ads = new Ads();
            ads.setCount(adsListDto.size());
            ads.setResults(adsListDto);
            return ads;
        } else {
            throw new AdNotFoundException();
        }
    }

    /**
     * Image update method
     * @param adId must not be null
     * @param file image file
     * @throws IOException default error for working with {@link MultipartFile}
     */
    public void updateImage(int adId, MultipartFile file) throws IOException {
        Optional<AdEntity> optionalAdlEntity = adRepository.findById(adId);
        if (optionalAdlEntity.isPresent()){
            AdEntity adEntity = optionalAdlEntity.get();
            imageService.updateImageForAd(adEntity, file);
        } else {
            throw new IOException();
        }
    }
    /**
     * Method for checking the current user's access to the ad
     * @param username name of the current user. Must not be null
     * @param id id of the current ad. Must not be null
     */
    public boolean checkAccessForAd(String username, Integer id) {
        Optional<AdEntity> optional = adRepository.findById(id);
        return optional.map(adEntity -> adEntity.getUser().getUsername().equals(username)).orElse(false);
    }
}
