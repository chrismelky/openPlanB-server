package org.openplanrep.service;

import org.openplanrep.domain.OrgUnitGroupSet;
import org.openplanrep.repository.OrgUnitGroupSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing OrgUnitGroupSet.
 */
@Service
@Transactional
public class OrgUnitGroupSetService {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupSetService.class);

    private final OrgUnitGroupSetRepository orgUnitGroupSetRepository;

    public OrgUnitGroupSetService(OrgUnitGroupSetRepository orgUnitGroupSetRepository) {
        this.orgUnitGroupSetRepository = orgUnitGroupSetRepository;
    }

    /**
     * Save a orgUnitGroupSet.
     *
     * @param orgUnitGroupSet the entity to save
     * @return the persisted entity
     */
    public OrgUnitGroupSet save(OrgUnitGroupSet orgUnitGroupSet) {
        log.debug("Request to save OrgUnitGroupSet : {}", orgUnitGroupSet);        return orgUnitGroupSetRepository.save(orgUnitGroupSet);
    }

    /**
     * Get all the orgUnitGroupSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroupSet> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUnitGroupSets");
        return orgUnitGroupSetRepository.findAll(pageable);
    }

    /**
     * Get all the OrgUnitGroupSet with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<OrgUnitGroupSet> findAllWithEagerRelationships(Pageable pageable) {
        return orgUnitGroupSetRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one orgUnitGroupSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrgUnitGroupSet> findOne(Long id) {
        log.debug("Request to get OrgUnitGroupSet : {}", id);
        return orgUnitGroupSetRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orgUnitGroupSet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgUnitGroupSet : {}", id);
        orgUnitGroupSetRepository.deleteById(id);
    }
}
