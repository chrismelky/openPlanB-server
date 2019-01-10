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

import org.openplanrep.domain.OrgUnitLevel;
import org.openplanrep.domain.*; // for static metamodels
import org.openplanrep.repository.OrgUnitLevelRepository;
import org.openplanrep.service.dto.OrgUnitLevelCriteria;


/**
 * Service for executing complex queries for OrgUnitLevel entities in the database.
 * The main input is a {@link OrgUnitLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrgUnitLevel} or a {@link Page} of {@link OrgUnitLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrgUnitLevelQueryService extends QueryService<OrgUnitLevel> {

    private final Logger log = LoggerFactory.getLogger(OrgUnitLevelQueryService.class);

    private final OrgUnitLevelRepository orgUnitLevelRepository;

    public OrgUnitLevelQueryService(OrgUnitLevelRepository orgUnitLevelRepository) {
        this.orgUnitLevelRepository = orgUnitLevelRepository;
    }

    /**
     * Return a {@link List} of {@link OrgUnitLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrgUnitLevel> findByCriteria(OrgUnitLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrgUnitLevel> specification = createSpecification(criteria);
        return orgUnitLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrgUnitLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUnitLevel> findByCriteria(OrgUnitLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrgUnitLevel> specification = createSpecification(criteria);
        return orgUnitLevelRepository.findAll(specification, page);
    }

    /**
     * Function to convert OrgUnitLevelCriteria to a {@link Specification}
     */
    private Specification<OrgUnitLevel> createSpecification(OrgUnitLevelCriteria criteria) {
        Specification<OrgUnitLevel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrgUnitLevel_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), OrgUnitLevel_.uuid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrgUnitLevel_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrgUnitLevel_.name));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLevel(), OrgUnitLevel_.level));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), OrgUnitLevel_.created));
            }
            if (criteria.getLastUpdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdate(), OrgUnitLevel_.lastUpdate));
            }
        }
        return specification;
    }

}
