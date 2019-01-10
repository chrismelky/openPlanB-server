package org.openplanrep.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.openplanrep.domain.OrgUnitGroup;
import org.openplanrep.domain.*; // for static metamodels
import org.openplanrep.repository.OrgUnitGroupRepository;
import org.openplanrep.service.dto.OrgUnitGroupCriteria;


/**
 * Service for executing complex queries for OrgUnitGroup entities in the database.
 * The main input is a {@link OrgUnitGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgUnitGroup} or a {@link Page} of {@link OrgUnitGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgUnitGroupQueryService extends QueryService<OrgUnitGroup> {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupQueryService.class);

    private final OrgUnitGroupRepository orgUnitGroupRepository;

    public OrgUnitGroupQueryService(OrgUnitGroupRepository orgUnitGroupRepository) {
        this.orgUnitGroupRepository = orgUnitGroupRepository;
    }

    /**
     * Return a {@link List} of {@link OrgUnitGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgUnitGroup> findByCriteria(OrgUnitGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgUnitGroup> specification = createSpecification(criteria);
        return orgUnitGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrgUnitGroup} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroup> findByCriteria(OrgUnitGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgUnitGroup> specification = createSpecification(criteria);
        return orgUnitGroupRepository.findAll(specification, page);
    }

    /**
     * Function to convert OrgUnitGroupCriteria to a {@link Specification}
     */
    private Specification<OrgUnitGroup> createSpecification(OrgUnitGroupCriteria criteria) {
        Specification<OrgUnitGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrgUnitGroup_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUid(), OrgUnitGroup_.uid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrgUnitGroup_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrgUnitGroup_.name));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), OrgUnitGroup_.created));
            }
            if (criteria.getLastUpdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdate(), OrgUnitGroup_.lastUpdate));
            }
            if (criteria.getOrgUnitGroupSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOrgUnitGroupSetId(), OrgUnitGroup_.orgUnitGroupSet, OrgUnitGroupSet_.id));
            }
            if (criteria.getAttributeValuesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAttributeValuesId(), OrgUnitGroup_.attributeValues, AttributeValue_.id));
            }
        }
        return specification;
    }

}
