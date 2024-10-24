package com.warhammer.ecom.web.rest;

import static com.warhammer.ecom.domain.CommandLineAsserts.*;
import static com.warhammer.ecom.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.IntegrationTest;
import com.warhammer.ecom.domain.CommandLine;
import com.warhammer.ecom.repository.CommandLineRepository;
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
 * Integration tests for the {@link CommandLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommandLineResourceIT {

    private static final Integer DEFAULT_QUANITY = 1;
    private static final Integer UPDATED_QUANITY = 2;

    private static final String ENTITY_API_URL = "/api/command-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommandLineRepository commandLineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandLineMockMvc;

    private CommandLine commandLine;

    private CommandLine insertedCommandLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandLine createEntity() {
        return new CommandLine().quanity(DEFAULT_QUANITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandLine createUpdatedEntity() {
        return new CommandLine().quanity(UPDATED_QUANITY);
    }

    @BeforeEach
    public void initTest() {
        commandLine = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCommandLine != null) {
            commandLineRepository.delete(insertedCommandLine);
            insertedCommandLine = null;
        }
    }

    @Test
    @Transactional
    void createCommandLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CommandLine
        var returnedCommandLine = om.readValue(
            restCommandLineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commandLine)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommandLine.class
        );

        // Validate the CommandLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommandLineUpdatableFieldsEquals(returnedCommandLine, getPersistedCommandLine(returnedCommandLine));

        insertedCommandLine = returnedCommandLine;
    }

    @Test
    @Transactional
    void createCommandLineWithExistingId() throws Exception {
        // Create the CommandLine with an existing ID
        commandLine.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commandLine)))
            .andExpect(status().isBadRequest());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommandLines() throws Exception {
        // Initialize the database
        insertedCommandLine = commandLineRepository.saveAndFlush(commandLine);

        // Get all the commandLineList
        restCommandLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].quanity").value(hasItem(DEFAULT_QUANITY)));
    }

    @Test
    @Transactional
    void getCommandLine() throws Exception {
        // Initialize the database
        insertedCommandLine = commandLineRepository.saveAndFlush(commandLine);

        // Get the commandLine
        restCommandLineMockMvc
            .perform(get(ENTITY_API_URL_ID, commandLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commandLine.getId().intValue()))
            .andExpect(jsonPath("$.quanity").value(DEFAULT_QUANITY));
    }

    @Test
    @Transactional
    void getNonExistingCommandLine() throws Exception {
        // Get the commandLine
        restCommandLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommandLine() throws Exception {
        // Initialize the database
        insertedCommandLine = commandLineRepository.saveAndFlush(commandLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commandLine
        CommandLine updatedCommandLine = commandLineRepository.findById(commandLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommandLine are not directly saved in db
        em.detach(updatedCommandLine);
        updatedCommandLine.quanity(UPDATED_QUANITY);

        restCommandLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommandLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommandLine))
            )
            .andExpect(status().isOk());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommandLineToMatchAllProperties(updatedCommandLine);
    }

    @Test
    @Transactional
    void putNonExistingCommandLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commandLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commandLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommandLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commandLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commandLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommandLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commandLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commandLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandLineWithPatch() throws Exception {
        // Initialize the database
        insertedCommandLine = commandLineRepository.saveAndFlush(commandLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commandLine using partial update
        CommandLine partialUpdatedCommandLine = new CommandLine();
        partialUpdatedCommandLine.setId(commandLine.getId());

        partialUpdatedCommandLine.quanity(UPDATED_QUANITY);

        restCommandLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommandLine))
            )
            .andExpect(status().isOk());

        // Validate the CommandLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommandLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommandLine, commandLine),
            getPersistedCommandLine(commandLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommandLineWithPatch() throws Exception {
        // Initialize the database
        insertedCommandLine = commandLineRepository.saveAndFlush(commandLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commandLine using partial update
        CommandLine partialUpdatedCommandLine = new CommandLine();
        partialUpdatedCommandLine.setId(commandLine.getId());

        partialUpdatedCommandLine.quanity(UPDATED_QUANITY);

        restCommandLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommandLine))
            )
            .andExpect(status().isOk());

        // Validate the CommandLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommandLineUpdatableFieldsEquals(partialUpdatedCommandLine, getPersistedCommandLine(partialUpdatedCommandLine));
    }

    @Test
    @Transactional
    void patchNonExistingCommandLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commandLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commandLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommandLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commandLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commandLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommandLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commandLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commandLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommandLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommandLine() throws Exception {
        // Initialize the database
        insertedCommandLine = commandLineRepository.saveAndFlush(commandLine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commandLine
        restCommandLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, commandLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commandLineRepository.count();
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

    protected CommandLine getPersistedCommandLine(CommandLine commandLine) {
        return commandLineRepository.findById(commandLine.getId()).orElseThrow();
    }

    protected void assertPersistedCommandLineToMatchAllProperties(CommandLine expectedCommandLine) {
        assertCommandLineAllPropertiesEquals(expectedCommandLine, getPersistedCommandLine(expectedCommandLine));
    }

    protected void assertPersistedCommandLineToMatchUpdatableProperties(CommandLine expectedCommandLine) {
        assertCommandLineAllUpdatablePropertiesEquals(expectedCommandLine, getPersistedCommandLine(expectedCommandLine));
    }
}
