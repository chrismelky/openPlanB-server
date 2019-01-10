package org.openplanrep.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.openplanrep.domain.OptionValue;
import org.openplanrep.service.OptionValueService;
import org.openplanrep.web.rest.errors.BadRequestAlertException;
import org.openplanrep.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OptionValue.
 */
@RestController
@RequestMapping("/api")
public class OptionValueResource {

    private final Logger log = LoggerFactory.getLogger(OptionValueResource.class);

    private static final String ENTITY_NAME = "optionValue";

    private final OptionValueService optionValueService;

    public OptionValueResource(OptionValueService optionValueService) {
        this.optionValueService = optionValueService;
    }

    /**
     * POST  /option-values : Create a new optionValue.
     *
     * @param optionValue the optionValue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new optionValue, or with status 400 (Bad Request) if the optionValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/option-values")
    @Timed
    public ResponseEntity<OptionValue> createOptionValue(@Valid @RequestBody OptionValue optionValue) throws URISyntaxException {
        log.debug("REST request to save OptionValue : {}", optionValue);
        if (optionValue.getId() != null) {
            throw new BadRequestAlertException("A new optionValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OptionValue result = optionValueService.save(optionValue);
        return ResponseEntity.created(new URI("/api/option-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /option-values : Updates an existing optionValue.
     *
     * @param optionValue the optionValue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated optionValue,
     * or with status 400 (Bad Request) if the optionValue is not valid,
     * or with status 500 (Internal Server Error) if the optionValue couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/option-values")
    @Timed
    public ResponseEntity<OptionValue> updateOptionValue(@Valid @RequestBody OptionValue optionValue) throws URISyntaxException {
        log.debug("REST request to update OptionValue : {}", optionValue);
        if (optionValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OptionValue result = optionValueService.save(optionValue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, optionValue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /option-values : get all the optionValues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of optionValues in body
     */
    @GetMapping("/option-values")
    @Timed
    public List<OptionValue> getAllOptionValues() {
        log.debug("REST request to get all OptionValues");
        return optionValueService.findAll();
    }

    /**
     * GET  /option-values/:id : get the "id" optionValue.
     *
     * @param id the id of the optionValue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the optionValue, or with status 404 (Not Found)
     */
    @GetMapping("/option-values/{id}")
    @Timed
    public ResponseEntity<OptionValue> getOptionValue(@PathVariable Long id) {
        log.debug("REST request to get OptionValue : {}", id);
        Optional<OptionValue> optionValue = optionValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionValue);
    }

    /**
     * DELETE  /option-values/:id : delete the "id" optionValue.
     *
     * @param id the id of the optionValue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/option-values/{id}")
    @Timed
    public ResponseEntity<Void> deleteOptionValue(@PathVariable Long id) {
        log.debug("REST request to delete OptionValue : {}", id);
        optionValueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
