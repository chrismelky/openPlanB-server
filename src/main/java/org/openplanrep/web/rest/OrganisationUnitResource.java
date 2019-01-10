package org.openplanrep.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.openplanrep.domain.OrganisationUnit;
import org.openplanrep.service.OrganisationUnitService;
import org.openplanrep.web.rest.errors.BadRequestAlertException;
import org.openplanrep.web.rest.util.HeaderUtil;
import org.openplanrep.web.rest.util.PaginationUtil;
import org.openplanrep.service.dto.OrganisationUnitCriteria;
import org.openplanrep.service.OrganisationUnitQueryService;
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
 * REST controller for managing OrganisationUnit.
 */
@RestController
@RequestMapping("/api")
public class OrganisationUnitResource {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitResource.class);

    private static final String ENTITY_NAME = "organisationUnit";

    private final OrganisationUnitService organisationUnitService;

    private final OrganisationUnitQueryService organisationUnitQueryService;

    public OrganisationUnitResource(OrganisationUnitService organisationUnitService, OrganisationUnitQueryService organisationUnitQueryService) {
        this.organisationUnitService = organisationUnitService;
        this.organisationUnitQueryService = organisationUnitQueryService;
    }

    /**
     * POST  /organisation-units : Create a new organisationUnit.
     *
     * @param organisationUnit the organisationUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organisationUnit, or with status 400 (Bad Request) if the organisationUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organisation-units")
    @Timed
    public ResponseEntity<OrganisationUnit> createOrganisationUnit(@Valid @RequestBody OrganisationUnit organisationUnit) throws URISyntaxException {
        log.debug("REST request to save OrganisationUnit : {}", organisationUnit);
        if (organisationUnit.getId() != null) {
            throw new BadRequestAlertException("A new organisationUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganisationUnit result = organisationUnitService.save(organisationUnit);
        return ResponseEntity.created(new URI("/api/organisation-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organisation-units : Updates an existing organisationUnit.
     *
     * @param organisationUnit the organisationUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organisationUnit,
     * or with status 400 (Bad Request) if the organisationUnit is not valid,
     * or with status 500 (Internal Server Error) if the organisationUnit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organisation-units")
    @Timed
    public ResponseEntity<OrganisationUnit> updateOrganisationUnit(@Valid @RequestBody OrganisationUnit organisationUnit) throws URISyntaxException {
        log.debug("REST request to update OrganisationUnit : {}", organisationUnit);
        if (organisationUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganisationUnit result = organisationUnitService.save(organisationUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organisationUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organisation-units : get all the organisationUnits.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of organisationUnits in body
     */
    @GetMapping("/organisation-units")
    @Timed
    public ResponseEntity<List<OrganisationUnit>> getAllOrganisationUnits(OrganisationUnitCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganisationUnits by criteria: {}", criteria);
        Page<OrganisationUnit> page = organisationUnitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organisation-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organisation-units/:id : get the "id" organisationUnit.
     *
     * @param id the id of the organisationUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organisationUnit, or with status 404 (Not Found)
     */
    @GetMapping("/organisation-units/{id}")
    @Timed
    public ResponseEntity<OrganisationUnit> getOrganisationUnit(@PathVariable Long id) {
        log.debug("REST request to get OrganisationUnit : {}", id);
        Optional<OrganisationUnit> organisationUnit = organisationUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organisationUnit);
    }

    /**
     * DELETE  /organisation-units/:id : delete the "id" organisationUnit.
     *
     * @param id the id of the organisationUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organisation-units/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrganisationUnit(@PathVariable Long id) {
        log.debug("REST request to delete OrganisationUnit : {}", id);
        organisationUnitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
