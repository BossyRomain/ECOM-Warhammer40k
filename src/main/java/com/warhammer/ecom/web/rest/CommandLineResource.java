package com.warhammer.ecom.web.rest;

import com.warhammer.ecom.domain.CommandLine;
import com.warhammer.ecom.repository.CommandLineRepository;
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
 * REST controller for managing {@link com.warhammer.ecom.domain.CommandLine}.
 */
@RestController
@RequestMapping("/api/command-lines")
@Transactional
public class CommandLineResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommandLineResource.class);

    private static final String ENTITY_NAME = "commandLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandLineRepository commandLineRepository;

    public CommandLineResource(CommandLineRepository commandLineRepository) {
        this.commandLineRepository = commandLineRepository;
    }

    /**
     * {@code POST  /command-lines} : Create a new commandLine.
     *
     * @param commandLine the commandLine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commandLine, or with status {@code 400 (Bad Request)} if the commandLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommandLine> createCommandLine(@RequestBody CommandLine commandLine) throws URISyntaxException {
        LOG.debug("REST request to save CommandLine : {}", commandLine);
        if (commandLine.getId() != null) {
            throw new BadRequestAlertException("A new commandLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commandLine = commandLineRepository.save(commandLine);
        return ResponseEntity.created(new URI("/api/command-lines/" + commandLine.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commandLine.getId().toString()))
            .body(commandLine);
    }

    /**
     * {@code PUT  /command-lines/:id} : Updates an existing commandLine.
     *
     * @param id the id of the commandLine to save.
     * @param commandLine the commandLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandLine,
     * or with status {@code 400 (Bad Request)} if the commandLine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commandLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommandLine> updateCommandLine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandLine commandLine
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommandLine : {}, {}", id, commandLine);
        if (commandLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commandLine = commandLineRepository.save(commandLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandLine.getId().toString()))
            .body(commandLine);
    }

    /**
     * {@code PATCH  /command-lines/:id} : Partial updates given fields of an existing commandLine, field will ignore if it is null
     *
     * @param id the id of the commandLine to save.
     * @param commandLine the commandLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandLine,
     * or with status {@code 400 (Bad Request)} if the commandLine is not valid,
     * or with status {@code 404 (Not Found)} if the commandLine is not found,
     * or with status {@code 500 (Internal Server Error)} if the commandLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommandLine> partialUpdateCommandLine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandLine commandLine
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommandLine partially : {}, {}", id, commandLine);
        if (commandLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommandLine> result = commandLineRepository
            .findById(commandLine.getId())
            .map(existingCommandLine -> {
                if (commandLine.getQuanity() != null) {
                    existingCommandLine.setQuanity(commandLine.getQuanity());
                }

                return existingCommandLine;
            })
            .map(commandLineRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandLine.getId().toString())
        );
    }

    /**
     * {@code GET  /command-lines} : get all the commandLines.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commandLines in body.
     */
    @GetMapping("")
    public List<CommandLine> getAllCommandLines() {
        LOG.debug("REST request to get all CommandLines");
        return commandLineRepository.findAll();
    }

    /**
     * {@code GET  /command-lines/:id} : get the "id" commandLine.
     *
     * @param id the id of the commandLine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commandLine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommandLine> getCommandLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommandLine : {}", id);
        Optional<CommandLine> commandLine = commandLineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commandLine);
    }

    /**
     * {@code DELETE  /command-lines/:id} : delete the "id" commandLine.
     *
     * @param id the id of the commandLine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommandLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommandLine : {}", id);
        commandLineRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
