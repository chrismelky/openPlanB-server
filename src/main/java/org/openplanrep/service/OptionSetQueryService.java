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

import org.openplanrep.domain.OptionSet;
import org.openplanrep.domain.*; // for static metamodels
import org.openplanrep.repository.OptionSetRepository;
import org.openplanrep.service.dto.OptionSetCriteria;


/**
 * Service for executing complex queries for OptionSet entities in the database.
 * The main input is a {@link OptionSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OptionSet} or a {@link Page} of {@link OptionSet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionSetQueryService extends QueryService<OptionSet> {

    private final Logger log = LoggerFactory.getLogger(OptionSetQueryService.class);

    private final OptionSetRepository optionSetRepository;

    public OptionSetQueryService(OptionSetRepository optionSetRepository) {
        this.optionSetRepository = optionSetRepository;
    }

    /**
     * Return a {@link List} of {@link OptionSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OptionSet> findByCriteria(OptionSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OptionSet> specification = createSpecification(criteria);
        return optionSetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OptionSet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionSet> findByCriteria(OptionSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OptionSet> specification = createSpecification(criteria);
        return optionSetRepository.findAll(specification, page);
    }

    /**
     * Function to convert OptionSetCriteria to a {@link Specification}
     */
    private Specification<OptionSet> createSpecification(OptionSetCriteria criteria) {
        Specification<OptionSet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OptionSet_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), OptionSet_.uuid));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OptionSet_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OptionSet_.name));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), OptionSet_.created));
            }
            if (criteria.getLastUpdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdate(), OptionSet_.lastUpdate));
            }
            if (criteria.getOptionValuesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOptionValuesId(), OptionSet_.optionValues, OptionValue_.id));
            }
        }
        return specification;
    }

}
