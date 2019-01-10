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

import org.openplanrep.domain.OrgUnitGroupSet;
import org.openplanrep.domain.*; // for static metamodels
import org.openplanrep.repository.OrgUnitGroupSetRepository;
import org.openplanrep.service.dto.OrgUnitGroupSetCriteria;


/**
 * Service for executing complex queries for OrgUnitGroupSet entities in the database.
 * The main input is a {@link OrgUnitGroupSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgUnitGroupSet} or a {@link Page} of {@link OrgUnitGroupSet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgUnitGroupSetQueryService extends QueryService<OrgUnitGroupSet> {

    private final Logger log = LoggerFactory.getLogger(OrgUnitGroupSetQueryService.class);

    private final OrgUnitGroupSetRepository orgUnitGroupSetRepository;

    public OrgUnitGroupSetQueryService(OrgUnitGroupSetRepository orgUnitGroupSetRepository) {
        this.orgUnitGroupSetRepository = orgUnitGroupSetRepository;
    }

    /**
     * Return a {@link List} of {@link OrgUnitGroupSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgUnitGroupSet> findByCriteria(OrgUnitGroupSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgUnitGroupSet> specification = createSpecification(criteria);
        return orgUnitGroupSetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrgUnitGroupSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitGroupSet> findByCriteria(OrgUnitGroupSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgUnitGroupSet> specification = createSpecification(criteria);
        return orgUnitGroupSetRepository.findAll(specification, page);
    }

    /**
     * Function to convert OrgUnitGroupSetCriteria to a {@link Specification}
     */
    private Specification<OrgUnitGroupSet> createSpecification(OrgUnitGroupSetCriteria criteria) {
        Specification<OrgUnitGroupSet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrgUnitGroupSet_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), OrgUnitGroupSet_.uuid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrgUnitGroupSet_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrgUnitGroupSet_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), OrgUnitGroupSet_.description));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), OrgUnitGroupSet_.created));
            }
            if (criteria.getLastUpdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdate(), OrgUnitGroupSet_.lastUpdate));
            }
            if (criteria.getAttributeValuesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAttributeValuesId(), OrgUnitGroupSet_.attributeValues, AttributeValue_.id));
            }
        }
        return specification;
    }

}
