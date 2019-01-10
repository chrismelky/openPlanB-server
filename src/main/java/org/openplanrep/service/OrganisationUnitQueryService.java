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

import org.openplanrep.domain.OrganisationUnit;
import org.openplanrep.domain.*; // for static metamodels
import org.openplanrep.repository.OrganisationUnitRepository;
import org.openplanrep.service.dto.OrganisationUnitCriteria;


/**
 * Service for executing complex queries for OrganisationUnit entities in the database.
 * The main input is a {@link OrganisationUnitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganisationUnit} or a {@link Page} of {@link OrganisationUnit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganisationUnitQueryService extends QueryService<OrganisationUnit> {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitQueryService.class);

    private final OrganisationUnitRepository organisationUnitRepository;

    public OrganisationUnitQueryService(OrganisationUnitRepository organisationUnitRepository) {
        this.organisationUnitRepository = organisationUnitRepository;
    }

    /**
     * Return a {@link List} of {@link OrganisationUnit} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganisationUnit> findByCriteria(OrganisationUnitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrganisationUnit> specification = createSpecification(criteria);
        return organisationUnitRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrganisationUnit} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganisationUnit> findByCriteria(OrganisationUnitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrganisationUnit> specification = createSpecification(criteria);
        return organisationUnitRepository.findAll(specification, page);
    }

    /**
     * Function to convert OrganisationUnitCriteria to a {@link Specification}
     */
    private Specification<OrganisationUnit> createSpecification(OrganisationUnitCriteria criteria) {
        Specification<OrganisationUnit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrganisationUnit_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), OrganisationUnit_.uuid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrganisationUnit_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrganisationUnit_.name));
            }
            if (criteria.getOpeningDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpeningDate(), OrganisationUnit_.openingDate));
            }
            if (criteria.getClosedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosedDate(), OrganisationUnit_.closedDate));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), OrganisationUnit_.url));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), OrganisationUnit_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), OrganisationUnit_.longitude));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), OrganisationUnit_.address));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), OrganisationUnit_.email));
            }
            if (criteria.getPhoneNumner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumner(), OrganisationUnit_.phoneNumner));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getParentId(), OrganisationUnit_.parent, OrganisationUnit_.id));
            }
            if (criteria.getOrgUnitGroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOrgUnitGroupId(), OrganisationUnit_.orgUnitGroup, OrgUnitGroup_.id));
            }
            if (criteria.getAttributeValuesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAttributeValuesId(), OrganisationUnit_.attributeValues, AttributeValue_.id));
            }
        }
        return specification;
    }

}
