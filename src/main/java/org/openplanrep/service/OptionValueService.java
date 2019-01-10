package org.openplanrep.service;

import org.openplanrep.domain.OptionValue;
import org.openplanrep.repository.OptionValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing OptionValue.
 */
@Service
@Transactional
public class OptionValueService {

    private final Logger log = LoggerFactory.getLogger(OptionValueService.class);

    private final OptionValueRepository optionValueRepository;

    public OptionValueService(OptionValueRepository optionValueRepository) {
        this.optionValueRepository = optionValueRepository;
    }

    /**
     * Save a optionValue.
     *
     * @param optionValue the entity to save
     * @return the persisted entity
     */
    public OptionValue save(OptionValue optionValue) {
        log.debug("Request to save OptionValue : {}", optionValue);        return optionValueRepository.save(optionValue);
    }

    /**
     * Get all the optionValues.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OptionValue> findAll() {
        log.debug("Request to get all OptionValues");
        return optionValueRepository.findAll();
    }


    /**
     * Get one optionValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OptionValue> findOne(Long id) {
        log.debug("Request to get OptionValue : {}", id);
        return optionValueRepository.findById(id);
    }

    /**
     * Delete the optionValue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OptionValue : {}", id);
        optionValueRepository.deleteById(id);
    }
}
