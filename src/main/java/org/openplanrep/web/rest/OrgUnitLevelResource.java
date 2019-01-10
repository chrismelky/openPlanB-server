package org.openplanrep.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.openplanrep.domain.OrgUnitLevel;
import org.openplanrep.service.OrgUnitLevelService;
import org.openplanrep.web.rest.errors.BadRequestAlertException;
import org.openplanrep.web.rest.util.HeaderUtil;
import org.openplanrep.web.rest.util.PaginationUtil;
import org.openplanrep.service.dto.OrgUnitLevelCriteria;
import org.openplanrep.service.OrgUnitLevelQueryService;
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
 * REST controller for managing OrgUnitLevel.
 */
@RestController
@RequestMapping("/api")
public class OrgUnitLevelResource {

    private final Logger log = LoggerFactory.getLogger(OrgUnitLevelResource.class);

    private static final String ENTITY_NAME = "orgUnitLevel";

    private final OrgUnitLevelService orgUnitLevelService;

    private final OrgUnitLevelQueryService orgUnitLevelQueryService;

    public OrgUnitLevelResource(OrgUnitLevelService orgUnitLevelService, OrgUnitLevelQueryService orgUnitLevelQueryService) {
        this.orgUnitLevelService = orgUnitLevelService;
        this.orgUnitLevelQueryService = orgUnitLevelQueryService;
    }

    /**
     * POST  /org-unit-levels : Create a new orgUnitLevel.
     *
     * @param orgUnitLevel the orgUnitLevel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orgUnitLevel, or with status 400 (Bad Request) if the orgUnitLevel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/org-unit-levels")
    @Timed
    public ResponseEntity<OrgUnitLevel> createOrgUnitLevel(@Valid @RequestBody OrgUnitLevel orgUnitLevel) throws URISyntaxException {
        log.debug("REST request to save OrgUnitLevel : {}", orgUnitLevel);
        if (orgUnitLevel.getId() != null) {
            throw new BadRequestAlertException("A new orgUnitLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUnitLevel result = orgUnitLevelService.save(orgUnitLevel);
        return ResponseEntity.created(new URI("/api/org-unit-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /org-unit-levels : Updates an existing orgUnitLevel.
     *
     * @param orgUnitLevel the orgUnitLevel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orgUnitLevel,
     * or with status 400 (Bad Request) if the orgUnitLevel is not valid,
     * or with status 500 (Internal Server Error) if the orgUnitLevel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/org-unit-levels")
    @Timed
    public ResponseEntity<OrgUnitLevel> updateOrgUnitLevel(@Valid @RequestBody OrgUnitLevel orgUnitLevel) throws URISyntaxException {
        log.debug("REST request to update OrgUnitLevel : {}", orgUnitLevel);
        if (orgUnitLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrgUnitLevel result = orgUnitLevelService.save(orgUnitLevel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orgUnitLevel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /org-unit-levels : get all the orgUnitLevels.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of orgUnitLevels in body
     */
    @GetMapping("/org-unit-levels")
    @Timed
    public ResponseEntity<List<OrgUnitLevel>> getAllOrgUnitLevels(OrgUnitLevelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrgUnitLevels by criteria: {}", criteria);
        Page<OrgUnitLevel> page = orgUnitLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/org-unit-levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /org-unit-levels/:id : get the "id" orgUnitLevel.
     *
     * @param id the id of the orgUnitLevel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orgUnitLevel, or with status 404 (Not Found)
     */
    @GetMapping("/org-unit-levels/{id}")
    @Timed
    public ResponseEntity<OrgUnitLevel> getOrgUnitLevel(@PathVariable Long id) {
        log.debug("REST request to get OrgUnitLevel : {}", id);
        Optional<OrgUnitLevel> orgUnitLevel = orgUnitLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgUnitLevel);
    }

    /**
     * DELETE  /org-unit-levels/:id : delete the "id" orgUnitLevel.
     *
     * @param id the id of the orgUnitLevel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/org-unit-levels/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrgUnitLevel(@PathVariable Long id) {
        log.debug("REST request to delete OrgUnitLevel : {}", id);
        orgUnitLevelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
