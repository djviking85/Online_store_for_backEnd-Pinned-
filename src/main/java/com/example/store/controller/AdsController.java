package com.example.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("ads")

public class AdsController {
    private final AdService adService;

    public AdsController(AdService adService) {
        this.adService = adService;
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class)
                    )}
            )
    })
    @GetMapping()
    public ResponseEntity<Ads> getAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class)
                    )}
            )
    })

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAds(@RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                     @RequestPart("image") MultipartFile image, Principal principal) throws IOException {
        Ad ad = adService.saveAd(createOrUpdateAd, image, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);

    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExtendedAd.class)
                    )}
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })

    @GetMapping("{id}")
    public ResponseEntity<ExtendedAd> getInformationAboutAd(@PathVariable int id) throws AdNotFoundException {
        return ResponseEntity.ok(adService.getInformationAboutAd(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Integer id) throws AdNotFoundException {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class)
                    )}
            )
    })

    @PatchMapping("{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAd createOrUpdateAd)
            throws AdNotFoundException {
        adService.updateAD(id, createOrUpdateAd);
        return ResponseEntity.ok().build();
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class)
                    )}
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })

    @GetMapping("/me")
    public ResponseEntity<Ads> getAllAdsByUser(Principal principal) throws AdNotFoundException {
        return ResponseEntity.ok(adService.getAllAdsByUser(principal));
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/octet-stream"
                    )}
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImageAd(@PathVariable int id, @RequestParam("image") MultipartFile image) throws IOException {
        adService.updateImage(id, image);
        return ResponseEntity.ok().build();
    }
}
