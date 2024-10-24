package com.warhammer.ecom.web.rest;

import static com.warhammer.ecom.domain.ImageAsserts.*;
import static com.warhammer.ecom.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.IntegrationTest;
import com.warhammer.ecom.domain.Image;
import com.warhammer.ecom.repository.ImageRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageMockMvc;

    private Image image;

    private Image insertedImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity() {
        return new Image().url(DEFAULT_URL).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createUpdatedEntity() {
        return new Image().url(UPDATED_URL).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        image = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedImage != null) {
            imageRepository.delete(insertedImage);
            insertedImage = null;
        }
    }

    @Test
    @Transactional
    void createImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Image
        var returnedImage = om.readValue(
            restImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Image.class
        );

        // Validate the Image in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertImageUpdatableFieldsEquals(returnedImage, getPersistedImage(returnedImage));

        insertedImage = returnedImage;
    }

    @Test
    @Transactional
    void createImageWithExistingId() throws Exception {
        // Create the Image with an existing ID
        image.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getImage() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc
            .perform(get(ENTITY_API_URL_ID, image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImage() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image
        Image updatedImage = imageRepository.findById(image.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach(updatedImage);
        updatedImage.url(UPDATED_URL).description(UPDATED_DESCRIPTION);

        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageToMatchAllProperties(updatedImage);
    }

    @Test
    @Transactional
    void putNonExistingImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(put(ENTITY_API_URL_ID, image.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(image))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageWithPatch() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage.description(UPDATED_DESCRIPTION);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedImage, image), getPersistedImage(image));
    }

    @Test
    @Transactional
    void fullUpdateImageWithPatch() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage.url(UPDATED_URL).description(UPDATED_DESCRIPTION);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageUpdatableFieldsEquals(partialUpdatedImage, getPersistedImage(partialUpdatedImage));
    }

    @Test
    @Transactional
    void patchNonExistingImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, image.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(image))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(image))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(image)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImage() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the image
        restImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, image.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return imageRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Image getPersistedImage(Image image) {
        return imageRepository.findById(image.getId()).orElseThrow();
    }

    protected void assertPersistedImageToMatchAllProperties(Image expectedImage) {
        assertImageAllPropertiesEquals(expectedImage, getPersistedImage(expectedImage));
    }

    protected void assertPersistedImageToMatchUpdatableProperties(Image expectedImage) {
        assertImageAllUpdatablePropertiesEquals(expectedImage, getPersistedImage(expectedImage));
    }
}
