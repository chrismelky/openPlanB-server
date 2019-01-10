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

import org.openplanrep.domain.Attribute;
import org.openplanrep.domain.*; // for static metamodels
import org.openplanrep.repository.AttributeRepository;
import org.openplanrep.service.dto.AttributeCriteria;


/**
 * Service for executing complex queries for Attribute entities in the database.
 * The main input is a {@link AttributeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Attribute} or a {@link Page} of {@link Attribute} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttributeQueryService extends QueryService<Attribute> {

    private final Logger log = LoggerFactory.getLogger(AttributeQueryService.class);

    private final AttributeRepository attributeRepository;

    public AttributeQueryService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    /**
     * Return a {@link List} of {@link Attribute} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Attribute> findByCriteria(AttributeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Attribute} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Attribute> findByCriteria(AttributeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification, page);
    }

    /**
     * Function to convert AttributeCriteria to a {@link Specification}
     */
    private Specification<Attribute> createSpecification(AttributeCriteria criteria) {
        Specification<Attribute> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Attribute_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), Attribute_.uuid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Attribute_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Attribute_.name));
            }
            if (criteria.getValueType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValueType(), Attribute_.valueType));
            }
            if (criteria.getMandatory() != null) {
                specification = specification.and(buildSpecification(criteria.getMandatory(), Attribute_.mandatory));
            }
            if (criteria.getOrgUnitGroupSetAttribute() != null) {
                specification = specification.and(buildSpecification(criteria.getOrgUnitGroupSetAttribute(), Attribute_.orgUnitGroupSetAttribute));
            }
            if (criteria.getOrgUnitGroupAttribute() != null) {
                specification = specification.and(buildSpecification(criteria.getOrgUnitGroupAttribute(), Attribute_.orgUnitGroupAttribute));
            }
            if (criteria.getOrgUnitAttribute() != null) {
                specification = specification.and(buildSpecification(criteria.getOrgUnitAttribute(), Attribute_.orgUnitAttribute));
            }
            if (criteria.getUserAttribute() != null) {
                specification = specification.and(buildSpecification(criteria.getUserAttribute(), Attribute_.userAttribute));
            }
            if (criteria.getSortOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSortOrder(), Attribute_.sortOrder));
            }
            if (criteria.getOptionSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOptionSetId(), Attribute_.optionSet, OptionSet_.id));
            }
        }
        return specification;
    }

}
