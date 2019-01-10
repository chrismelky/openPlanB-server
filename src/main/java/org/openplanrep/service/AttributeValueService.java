package org.openplanrep.service;

import org.openplanrep.domain.AttributeValue;
import org.openplanrep.repository.AttributeValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing AttributeValue.
 */
@Service
@Transactional
public class AttributeValueService {

    private final Logger log = LoggerFactory.getLogger(AttributeValueService.class);

    private final AttributeValueRepository attributeValueRepository;

    public AttributeValueService(AttributeValueRepository attributeValueRepository) {
        this.attributeValueRepository = attributeValueRepository;
    }

    /**
     * Save a attributeValue.
     *
     * @param attributeValue the entity to save
     * @return the persisted entity
     */
    public AttributeValue save(AttributeValue attributeValue) {
        log.debug("Request to save AttributeValue : {}", attributeValue);        return attributeValueRepository.save(attributeValue);
    }

    /**
     * Get all the attributeValues.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AttributeValue> findAll() {
        log.debug("Request to get all AttributeValues");
        return attributeValueRepository.findAll();
    }


    /**
     * Get one attributeValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AttributeValue> findOne(Long id) {
        log.debug("Request to get AttributeValue : {}", id);
        return attributeValueRepository.findById(id);
    }

    /**
     * Delete the attributeValue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AttributeValue : {}", id);
        attributeValueRepository.deleteById(id);
    }
}
