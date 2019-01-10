package org.openplanrep.service;

import org.openplanrep.domain.OrgUnitGroup;
import org.openplanrep.repository.OrgUnitGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing OrgUnitGroup.
 */
@Service
@Transactional
public class OrgUnitGroupService {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupService.class);

    private final OrgUnitGroupRepository orgUnitGroupRepository;

    public OrgUnitGroupService(OrgUnitGroupRepository orgUnitGroupRepository) {
        this.orgUnitGroupRepository = orgUnitGroupRepository;
    }

    /**
     * Save a orgUnitGroup.
     *
     * @param orgUnitGroup the entity to save
     * @return the persisted entity
     */
    public OrgUnitGroup save(OrgUnitGroup orgUnitGroup) {
        log.debug("Request to save OrgUnitGroup : {}", orgUnitGroup);        return orgUnitGroupRepository.save(orgUnitGroup);
    }

    /**
     * Get all the orgUnitGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroup> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUnitGroups");
        return orgUnitGroupRepository.findAll(pageable);
    }

    /**
     * Get all the OrgUnitGroup with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<OrgUnitGroup> findAllWithEagerRelationships(Pageable pageable) {
        return orgUnitGroupRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one orgUnitGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrgUnitGroup> findOne(Long id) {
        log.debug("Request to get OrgUnitGroup : {}", id);
        return orgUnitGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orgUnitGroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgUnitGroup : {}", id);
        orgUnitGroupRepository.deleteById(id);
    }
}
