package com.warhammer.ecom.web.rest;

import static com.warhammer.ecom.domain.AllegianceAsserts.*;
import static com.warhammer.ecom.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.IntegrationTest;
import com.warhammer.ecom.domain.Allegiance;
import com.warhammer.ecom.domain.enumeration.Faction;
import com.warhammer.ecom.domain.enumeration.Group;
import com.warhammer.ecom.repository.AllegianceRepository;
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
 * Integration tests for the {@link AllegianceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllegianceResourceIT {

    private static final Group DEFAULT_GROUP = Group.Xenos;
    private static final Group UPDATED_GROUP = Group.Xenos;

    private static final Faction DEFAULT_FACTION = Faction.Space_Marine;
    private static final Faction UPDATED_FACTION = Faction.Space_Marine;

    private static final String ENTITY_API_URL = "/api/allegiances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AllegianceRepository allegianceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllegianceMockMvc;

    private Allegiance allegiance;

    private Allegiance insertedAllegiance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allegiance createEntity() {
        return new Allegiance().group(DEFAULT_GROUP).faction(DEFAULT_FACTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allegiance createUpdatedEntity() {
        return new Allegiance().group(UPDATED_GROUP).faction(UPDATED_FACTION);
    }

    @BeforeEach
    public void initTest() {
        allegiance = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAllegiance != null) {
            allegianceRepository.delete(insertedAllegiance);
            insertedAllegiance = null;
        }
    }

    @Test
    @Transactional
    void createAllegiance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Allegiance
        var returnedAllegiance = om.readValue(
            restAllegianceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allegiance)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Allegiance.class
        );

        // Validate the Allegiance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAllegianceUpdatableFieldsEquals(returnedAllegiance, getPersistedAllegiance(returnedAllegiance));

        insertedAllegiance = returnedAllegiance;
    }

    @Test
    @Transactional
    void createAllegianceWithExistingId() throws Exception {
        // Create the Allegiance with an existing ID
        allegiance.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllegianceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allegiance)))
            .andExpect(status().isBadRequest());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAllegiances() throws Exception {
        // Initialize the database
        insertedAllegiance = allegianceRepository.saveAndFlush(allegiance);

        // Get all the allegianceList
        restAllegianceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allegiance.getId().intValue())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())))
            .andExpect(jsonPath("$.[*].faction").value(hasItem(DEFAULT_FACTION.toString())));
    }

    @Test
    @Transactional
    void getAllegiance() throws Exception {
        // Initialize the database
        insertedAllegiance = allegianceRepository.saveAndFlush(allegiance);

        // Get the allegiance
        restAllegianceMockMvc
            .perform(get(ENTITY_API_URL_ID, allegiance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allegiance.getId().intValue()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.toString()))
            .andExpect(jsonPath("$.faction").value(DEFAULT_FACTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAllegiance() throws Exception {
        // Get the allegiance
        restAllegianceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllegiance() throws Exception {
        // Initialize the database
        insertedAllegiance = allegianceRepository.saveAndFlush(allegiance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allegiance
        Allegiance updatedAllegiance = allegianceRepository.findById(allegiance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAllegiance are not directly saved in db
        em.detach(updatedAllegiance);
        updatedAllegiance.group(UPDATED_GROUP).faction(UPDATED_FACTION);

        restAllegianceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAllegiance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAllegiance))
            )
            .andExpect(status().isOk());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAllegianceToMatchAllProperties(updatedAllegiance);
    }

    @Test
    @Transactional
    void putNonExistingAllegiance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allegiance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllegianceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allegiance.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allegiance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllegiance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allegiance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllegianceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allegiance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllegiance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allegiance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllegianceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allegiance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllegianceWithPatch() throws Exception {
        // Initialize the database
        insertedAllegiance = allegianceRepository.saveAndFlush(allegiance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allegiance using partial update
        Allegiance partialUpdatedAllegiance = new Allegiance();
        partialUpdatedAllegiance.setId(allegiance.getId());

        partialUpdatedAllegiance.group(UPDATED_GROUP);

        restAllegianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllegiance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllegiance))
            )
            .andExpect(status().isOk());

        // Validate the Allegiance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllegianceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAllegiance, allegiance),
            getPersistedAllegiance(allegiance)
        );
    }

    @Test
    @Transactional
    void fullUpdateAllegianceWithPatch() throws Exception {
        // Initialize the database
        insertedAllegiance = allegianceRepository.saveAndFlush(allegiance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allegiance using partial update
        Allegiance partialUpdatedAllegiance = new Allegiance();
        partialUpdatedAllegiance.setId(allegiance.getId());

        partialUpdatedAllegiance.group(UPDATED_GROUP).faction(UPDATED_FACTION);

        restAllegianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllegiance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllegiance))
            )
            .andExpect(status().isOk());

        // Validate the Allegiance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllegianceUpdatableFieldsEquals(partialUpdatedAllegiance, getPersistedAllegiance(partialUpdatedAllegiance));
    }

    @Test
    @Transactional
    void patchNonExistingAllegiance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allegiance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllegianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allegiance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allegiance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllegiance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allegiance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllegianceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allegiance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllegiance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allegiance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllegianceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(allegiance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allegiance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllegiance() throws Exception {
        // Initialize the database
        insertedAllegiance = allegianceRepository.saveAndFlush(allegiance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the allegiance
        restAllegianceMockMvc
            .perform(delete(ENTITY_API_URL_ID, allegiance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return allegianceRepository.count();
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

    protected Allegiance getPersistedAllegiance(Allegiance allegiance) {
        return allegianceRepository.findById(allegiance.getId()).orElseThrow();
    }

    protected void assertPersistedAllegianceToMatchAllProperties(Allegiance expectedAllegiance) {
        assertAllegianceAllPropertiesEquals(expectedAllegiance, getPersistedAllegiance(expectedAllegiance));
    }

    protected void assertPersistedAllegianceToMatchUpdatableProperties(Allegiance expectedAllegiance) {
        assertAllegianceAllUpdatablePropertiesEquals(expectedAllegiance, getPersistedAllegiance(expectedAllegiance));
    }
}
