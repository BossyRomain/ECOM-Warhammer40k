package com.warhammer.ecom.web.rest;

import com.warhammer.ecom.domain.Allegiance;
import com.warhammer.ecom.repository.AllegianceRepository;
import com.warhammer.ecom.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.warhammer.ecom.domain.Allegiance}.
 */
@RestController
@RequestMapping("/api/allegiances")
@Transactional
public class AllegianceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AllegianceResource.class);

    private static final String ENTITY_NAME = "allegiance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllegianceRepository allegianceRepository;

    public AllegianceResource(AllegianceRepository allegianceRepository) {
        this.allegianceRepository = allegianceRepository;
    }

    /**
     * {@code POST  /allegiances} : Create a new allegiance.
     *
     * @param allegiance the allegiance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allegiance, or with status {@code 400 (Bad Request)} if the allegiance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Allegiance> createAllegiance(@RequestBody Allegiance allegiance) throws URISyntaxException {
        LOG.debug("REST request to save Allegiance : {}", allegiance);
        if (allegiance.getId() != null) {
            throw new BadRequestAlertException("A new allegiance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        allegiance = allegianceRepository.save(allegiance);
        return ResponseEntity.created(new URI("/api/allegiances/" + allegiance.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, allegiance.getId().toString()))
            .body(allegiance);
    }

    /**
     * {@code PUT  /allegiances/:id} : Updates an existing allegiance.
     *
     * @param id the id of the allegiance to save.
     * @param allegiance the allegiance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allegiance,
     * or with status {@code 400 (Bad Request)} if the allegiance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allegiance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Allegiance> updateAllegiance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Allegiance allegiance
    ) throws URISyntaxException {
        LOG.debug("REST request to update Allegiance : {}, {}", id, allegiance);
        if (allegiance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allegiance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allegianceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        allegiance = allegianceRepository.save(allegiance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allegiance.getId().toString()))
            .body(allegiance);
    }

    /**
     * {@code PATCH  /allegiances/:id} : Partial updates given fields of an existing allegiance, field will ignore if it is null
     *
     * @param id the id of the allegiance to save.
     * @param allegiance the allegiance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allegiance,
     * or with status {@code 400 (Bad Request)} if the allegiance is not valid,
     * or with status {@code 404 (Not Found)} if the allegiance is not found,
     * or with status {@code 500 (Internal Server Error)} if the allegiance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Allegiance> partialUpdateAllegiance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Allegiance allegiance
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Allegiance partially : {}, {}", id, allegiance);
        if (allegiance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allegiance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allegianceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Allegiance> result = allegianceRepository
            .findById(allegiance.getId())
            .map(existingAllegiance -> {
                if (allegiance.getGroup() != null) {
                    existingAllegiance.setGroup(allegiance.getGroup());
                }
                if (allegiance.getFaction() != null) {
                    existingAllegiance.setFaction(allegiance.getFaction());
                }

                return existingAllegiance;
            })
            .map(allegianceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allegiance.getId().toString())
        );
    }

    /**
     * {@code GET  /allegiances} : get all the allegiances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allegiances in body.
     */
    @GetMapping("")
    public List<Allegiance> getAllAllegiances() {
        LOG.debug("REST request to get all Allegiances");
        return allegianceRepository.findAll();
    }

    /**
     * {@code GET  /allegiances/:id} : get the "id" allegiance.
     *
     * @param id the id of the allegiance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allegiance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Allegiance> getAllegiance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Allegiance : {}", id);
        Optional<Allegiance> allegiance = allegianceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(allegiance);
    }

    /**
     * {@code DELETE  /allegiances/:id} : delete the "id" allegiance.
     *
     * @param id the id of the allegiance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllegiance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Allegiance : {}", id);
        allegianceRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
