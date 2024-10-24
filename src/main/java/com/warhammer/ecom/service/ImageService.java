package com.warhammer.ecom.service;

import com.warhammer.ecom.domain.Image;
import com.warhammer.ecom.repository.ImageRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.warhammer.ecom.domain.Image}.
 */
@Service
@Transactional
public class ImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Save a image.
     *
     * @param image the entity to save.
     * @return the persisted entity.
     */
    public Image save(Image image) {
        LOG.debug("Request to save Image : {}", image);
        return imageRepository.save(image);
    }

    /**
     * Update a image.
     *
     * @param image the entity to save.
     * @return the persisted entity.
     */
    public Image update(Image image) {
        LOG.debug("Request to update Image : {}", image);
        return imageRepository.save(image);
    }

    /**
     * Partially update a image.
     *
     * @param image the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Image> partialUpdate(Image image) {
        LOG.debug("Request to partially update Image : {}", image);

        return imageRepository
            .findById(image.getId())
            .map(existingImage -> {
                if (image.getUrl() != null) {
                    existingImage.setUrl(image.getUrl());
                }

                return existingImage;
            })
            .map(imageRepository::save);
    }

    /**
     * Get all the images.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Image> findAll() {
        LOG.debug("Request to get all Images");
        return imageRepository.findAll();
    }

    /**
     * Get one image by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Image> findOne(Long id) {
        LOG.debug("Request to get Image : {}", id);
        return imageRepository.findById(id);
    }

    /**
     * Delete the image by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Image : {}", id);
        imageRepository.deleteById(id);
    }
}
