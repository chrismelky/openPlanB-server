package org.openplanrep.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.openplanrep.domain.OrgUnitGroup;
import org.openplanrep.service.OrgUnitGroupService;
import org.openplanrep.web.rest.errors.BadRequestAlertException;
import org.openplanrep.web.rest.util.HeaderUtil;
import org.openplanrep.web.rest.util.PaginationUtil;
import org.openplanrep.service.dto.OrgUnitGroupCriteria;
import org.openplanrep.service.OrgUnitGroupQueryService;
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
 * REST controller for managing OrgUnitGroup.
 */
@RestController
@RequestMapping("/api")
public class OrgUnitGroupResource {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupResource.class);

    private static final String ENTITY_NAME = "orgUnitGroup";

    private final OrgUnitGroupService orgUnitGroupService;

    private final OrgUnitGroupQueryService orgUnitGroupQueryService;

    public OrgUnitGroupResource(OrgUnitGroupService orgUnitGroupService, OrgUnitGroupQueryService orgUnitGroupQueryService) {
        this.orgUnitGroupService = orgUnitGroupService;
        this.orgUnitGroupQueryService = orgUnitGroupQueryService;
    }

    /**
     * POST  /org-unit-groups : Create a new orgUnitGroup.
     *
     * @param orgUnitGroup the orgUnitGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orgUnitGroup, or with status 400 (Bad Request) if the orgUnitGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/org-unit-groups")
    @Timed
    public ResponseEntity<OrgUnitGroup> createOrgUnitGroup(@Valid @RequestBody OrgUnitGroup orgUnitGroup) throws URISyntaxException {
        log.debug("REST request to save OrgUnitGroup : {}", orgUnitGroup);
        if (orgUnitGroup.getId() != null) {
            throw new BadRequestAlertException("A new orgUnitGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgUnitGroup result = orgUnitGroupService.save(orgUnitGroup);
        return ResponseEntity.created(new URI("/api/org-unit-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /org-unit-groups : Updates an existing orgUnitGroup.
     *
     * @param orgUnitGroup the orgUnitGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orgUnitGroup,
     * or with status 400 (Bad Request) if the orgUnitGroup is not valid,
     * or with status 500 (Internal Server Error) if the orgUnitGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/org-unit-groups")
    @Timed
    public ResponseEntity<OrgUnitGroup> updateOrgUnitGroup(@Valid @RequestBody OrgUnitGroup orgUnitGroup) throws URISyntaxException {
        log.debug("REST request to update OrgUnitGroup : {}", orgUnitGroup);
        if (orgUnitGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrgUnitGroup result = orgUnitGroupService.save(orgUnitGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orgUnitGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /org-unit-groups : get all the orgUnitGroups.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of orgUnitGroups in body
     */
    @GetMapping("/org-unit-groups")
    @Timed
    public ResponseEntity<List<OrgUnitGroup>> getAllOrgUnitGroups(OrgUnitGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrgUnitGroups by criteria: {}", criteria);
        Page<OrgUnitGroup> page = orgUnitGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/org-unit-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /org-unit-groups/:id : get the "id" orgUnitGroup.
     *
     * @param id the id of the orgUnitGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orgUnitGroup, or with status 404 (Not Found)
     */
    @GetMapping("/org-unit-groups/{id}")
    @Timed
    public ResponseEntity<OrgUnitGroup> getOrgUnitGroup(@PathVariable Long id) {
        log.debug("REST request to get OrgUnitGroup : {}", id);
        Optional<OrgUnitGroup> orgUnitGroup = orgUnitGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgUnitGroup);
    }

    /**
     * DELETE  /org-unit-groups/:id : delete the "id" orgUnitGroup.
     *
     * @param id the id of the orgUnitGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/org-unit-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrgUnitGroup(@PathVariable Long id) {
        log.debug("REST request to delete OrgUnitGroup : {}", id);
        orgUnitGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
