package org.openplanrep.service;

import org.openplanrep.domain.OrganisationUnit;
import org.openplanrep.repository.OrganisationUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing OrganisationUnit.
 */
@Service
@Transactional
public class OrganisationUnitService {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitService.class);

    private final OrganisationUnitRepository organisationUnitRepository;

    public OrganisationUnitService(OrganisationUnitRepository organisationUnitRepository) {
        this.organisationUnitRepository = organisationUnitRepository;
    }

    /**
     * Save a organisationUnit.
     *
     * @param organisationUnit the entity to save
     * @return the persisted entity
     */
    public OrganisationUnit save(OrganisationUnit organisationUnit) {
        log.debug("Request to save OrganisationUnit : {}", organisationUnit);        return organisationUnitRepository.save(organisationUnit);
    }

    /**
     * Get all the organisationUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrganisationUnit> findAll(Pageable pageable) {
        log.debug("Request to get all OrganisationUnits");
        return organisationUnitRepository.findAll(pageable);
    }

    /**
     * Get all the OrganisationUnit with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<OrganisationUnit> findAllWithEagerRelationships(Pageable pageable) {
        return organisationUnitRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one organisationUnit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrganisationUnit> findOne(Long id) {
        log.debug("Request to get OrganisationUnit : {}", id);
        return organisationUnitRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the organisationUnit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrganisationUnit : {}", id);
        organisationUnitRepository.deleteById(id);
    }
}
