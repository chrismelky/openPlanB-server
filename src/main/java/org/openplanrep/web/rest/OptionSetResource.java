package org.openplanrep.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.openplanrep.domain.OptionSet;
import org.openplanrep.service.OptionSetService;
import org.openplanrep.web.rest.errors.BadRequestAlertException;
import org.openplanrep.web.rest.util.HeaderUtil;
import org.openplanrep.web.rest.util.PaginationUtil;
import org.openplanrep.service.dto.OptionSetCriteria;
import org.openplanrep.service.OptionSetQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OptionSet.
 */
@RestController
@RequestMapping("/api")
public class OptionSetResource {

    private final Logger log = LoggerFactory.getLogger(OptionSetResource.class);

    private static final String ENTITY_NAME = "optionSet";

    private final OptionSetService optionSetService;

    private final OptionSetQueryService optionSetQueryService;

    public OptionSetResource(OptionSetService optionSetService, OptionSetQueryService optionSetQueryService) {
        this.optionSetService = optionSetService;
        this.optionSetQueryService = optionSetQueryService;
    }

    /**
     * POST  /option-sets : Create a new optionSet.
     *
     * @param optionSet the optionSet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new optionSet, or with status 400 (Bad Request) if the optionSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/option-sets")
    @Timed
    public ResponseEntity<OptionSet> createOptionSet(@Valid @RequestBody OptionSet optionSet) throws URISyntaxException {
        log.debug("REST request to save OptionSet : {}", optionSet);
        if (optionSet.getId() != null) {
            throw new BadRequestAlertException("A new optionSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OptionSet result = optionSetService.save(optionSet);
        return ResponseEntity.created(new URI("/api/option-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /option-sets : Updates an existing optionSet.
     *
     * @param optionSet the optionSet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated optionSet,
     * or with status 400 (Bad Request) if the optionSet is not valid,
     * or with status 500 (Internal Server Error) if the optionSet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/option-sets")
    @Timed
    public ResponseEntity<OptionSet> updateOptionSet(@Valid @RequestBody OptionSet optionSet) throws URISyntaxException {
        log.debug("REST request to update OptionSet : {}", optionSet);
        if (optionSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OptionSet result = optionSetService.save(optionSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, optionSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /option-sets : get all the optionSets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of optionSets in body
     */
    @GetMapping("/option-sets")
    @Timed
    public ResponseEntity<List<OptionSet>> getAllOptionSets(OptionSetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OptionSets by criteria: {}", criteria);
        Page<OptionSet> page = optionSetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/option-sets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /option-sets/:id : get the "id" optionSet.
     *
     * @param id the id of the optionSet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the optionSet, or with status 404 (Not Found)
     */
    @GetMapping("/option-sets/{id}")
    @Timed
    public ResponseEntity<OptionSet> getOptionSet(@PathVariable Long id) {
        log.debug("REST request to get OptionSet : {}", id);
        Optional<OptionSet> optionSet = optionSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionSet);
    }

    /**
     * DELETE  /option-sets/:id : delete the "id" optionSet.
     *
     * @param id the id of the optionSet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/option-sets/{id}")
    @Timed
    public ResponseEntity<Void> deleteOptionSet(@PathVariable Long id) {
        log.debug("REST request to delete OptionSet : {}", id);
        optionSetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
