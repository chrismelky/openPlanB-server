package org.openplanrep.service;

import org.openplanrep.domain.OptionSet;
import org.openplanrep.repository.OptionSetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing OptionSet.
 */
@Service
@Transactional
public class OptionSetService {

    private final Logger log = LoggerFactory.getLogger(OptionSetService.class);

    private final OptionSetRepository optionSetRepository;

    public OptionSetService(OptionSetRepository optionSetRepository) {
        this.optionSetRepository = optionSetRepository;
    }

    /**
     * Save a optionSet.
     *
     * @param optionSet the entity to save
     * @return the persisted entity
     */
    public OptionSet save(OptionSet optionSet) {
        log.debug("Request to save OptionSet : {}", optionSet);        return optionSetRepository.save(optionSet);
    }

    /**
     * Get all the optionSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OptionSet> findAll(Pageable pageable) {
        log.debug("Request to get all OptionSets");
        return optionSetRepository.findAll(pageable);
    }


    /**
     * Get one optionSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OptionSet> findOne(Long id) {
        log.debug("Request to get OptionSet : {}", id);
        return optionSetRepository.findById(id);
    }

    /**
     * Delete the optionSet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OptionSet : {}", id);
        optionSetRepository.deleteById(id);
    }
}
