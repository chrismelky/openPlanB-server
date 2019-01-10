package org.openplanrep.web.rest;

import org.openplanrep.ServerApp;

import org.openplanrep.domain.OptionSet;
import org.openplanrep.domain.OptionValue;
import org.openplanrep.repository.OptionSetRepository;
import org.openplanrep.service.OptionSetService;
import org.openplanrep.web.rest.errors.ExceptionTranslator;
import org.openplanrep.service.dto.OptionSetCriteria;
import org.openplanrep.service.OptionSetQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static org.openplanrep.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OptionSetResource REST controller.
 *
 * @see OptionSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApp.class)
public class OptionSetResourceIntTest {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OptionSetRepository optionSetRepository;

    

    @Autowired
    private OptionSetService optionSetService;

    @Autowired
    private OptionSetQueryService optionSetQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOptionSetMockMvc;

    private OptionSet optionSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OptionSetResource optionSetResource = new OptionSetResource(optionSetService, optionSetQueryService);
        this.restOptionSetMockMvc = MockMvcBuilders.standaloneSetup(optionSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionSet createEntity(EntityManager em) {
        OptionSet optionSet = new OptionSet()
            .uuid(DEFAULT_UUID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return optionSet;
    }

    @Before
    public void initTest() {
        optionSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createOptionSet() throws Exception {
        int databaseSizeBeforeCreate = optionSetRepository.findAll().size();

        // Create the OptionSet
        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isCreated());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeCreate + 1);
        OptionSet testOptionSet = optionSetList.get(optionSetList.size() - 1);
        assertThat(testOptionSet.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testOptionSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptionSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOptionSet.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOptionSet.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void createOptionSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = optionSetRepository.findAll().size();

        // Create the OptionSet with an existing ID
        optionSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionSetRepository.findAll().size();
        // set the field null
        optionSet.setName(null);

        // Create the OptionSet, which fails.

        restOptionSetMockMvc.perform(post("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOptionSets() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList
        restOptionSetMockMvc.perform(get("/api/option-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getOptionSet() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get the optionSet
        restOptionSetMockMvc.perform(get("/api/option-sets/{id}", optionSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(optionSet.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    public void getAllOptionSetsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where uuid equals to DEFAULT_UUID
        defaultOptionSetShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the optionSetList where uuid equals to UPDATED_UUID
        defaultOptionSetShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultOptionSetShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the optionSetList where uuid equals to UPDATED_UUID
        defaultOptionSetShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where uuid is not null
        defaultOptionSetShouldBeFound("uuid.specified=true");

        // Get all the optionSetList where uuid is null
        defaultOptionSetShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where code equals to DEFAULT_CODE
        defaultOptionSetShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the optionSetList where code equals to UPDATED_CODE
        defaultOptionSetShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOptionSetShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the optionSetList where code equals to UPDATED_CODE
        defaultOptionSetShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where code is not null
        defaultOptionSetShouldBeFound("code.specified=true");

        // Get all the optionSetList where code is null
        defaultOptionSetShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where name equals to DEFAULT_NAME
        defaultOptionSetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the optionSetList where name equals to UPDATED_NAME
        defaultOptionSetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOptionSetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the optionSetList where name equals to UPDATED_NAME
        defaultOptionSetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where name is not null
        defaultOptionSetShouldBeFound("name.specified=true");

        // Get all the optionSetList where name is null
        defaultOptionSetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where created equals to DEFAULT_CREATED
        defaultOptionSetShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the optionSetList where created equals to UPDATED_CREATED
        defaultOptionSetShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultOptionSetShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the optionSetList where created equals to UPDATED_CREATED
        defaultOptionSetShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where created is not null
        defaultOptionSetShouldBeFound("created.specified=true");

        // Get all the optionSetList where created is null
        defaultOptionSetShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByLastUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where lastUpdate equals to DEFAULT_LAST_UPDATE
        defaultOptionSetShouldBeFound("lastUpdate.equals=" + DEFAULT_LAST_UPDATE);

        // Get all the optionSetList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultOptionSetShouldNotBeFound("lastUpdate.equals=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByLastUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where lastUpdate in DEFAULT_LAST_UPDATE or UPDATED_LAST_UPDATE
        defaultOptionSetShouldBeFound("lastUpdate.in=" + DEFAULT_LAST_UPDATE + "," + UPDATED_LAST_UPDATE);

        // Get all the optionSetList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultOptionSetShouldNotBeFound("lastUpdate.in=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void getAllOptionSetsByLastUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionSetRepository.saveAndFlush(optionSet);

        // Get all the optionSetList where lastUpdate is not null
        defaultOptionSetShouldBeFound("lastUpdate.specified=true");

        // Get all the optionSetList where lastUpdate is null
        defaultOptionSetShouldNotBeFound("lastUpdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOptionSetsByOptionValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        OptionValue optionValues = OptionValueResourceIntTest.createEntity(em);
        em.persist(optionValues);
        em.flush();
        optionSet.addOptionValues(optionValues);
        optionSetRepository.saveAndFlush(optionSet);
        Long optionValuesId = optionValues.getId();

        // Get all the optionSetList where optionValues equals to optionValuesId
        defaultOptionSetShouldBeFound("optionValuesId.equals=" + optionValuesId);

        // Get all the optionSetList where optionValues equals to optionValuesId + 1
        defaultOptionSetShouldNotBeFound("optionValuesId.equals=" + (optionValuesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOptionSetShouldBeFound(String filter) throws Exception {
        restOptionSetMockMvc.perform(get("/api/option-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOptionSetShouldNotBeFound(String filter) throws Exception {
        restOptionSetMockMvc.perform(get("/api/option-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingOptionSet() throws Exception {
        // Get the optionSet
        restOptionSetMockMvc.perform(get("/api/option-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOptionSet() throws Exception {
        // Initialize the database
        optionSetService.save(optionSet);

        int databaseSizeBeforeUpdate = optionSetRepository.findAll().size();

        // Update the optionSet
        OptionSet updatedOptionSet = optionSetRepository.findById(optionSet.getId()).get();
        // Disconnect from session so that the updates on updatedOptionSet are not directly saved in db
        em.detach(updatedOptionSet);
        updatedOptionSet
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restOptionSetMockMvc.perform(put("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOptionSet)))
            .andExpect(status().isOk());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeUpdate);
        OptionSet testOptionSet = optionSetList.get(optionSetList.size() - 1);
        assertThat(testOptionSet.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testOptionSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptionSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOptionSet.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOptionSet.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOptionSet() throws Exception {
        int databaseSizeBeforeUpdate = optionSetRepository.findAll().size();

        // Create the OptionSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restOptionSetMockMvc.perform(put("/api/option-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionSet)))
            .andExpect(status().isBadRequest());

        // Validate the OptionSet in the database
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOptionSet() throws Exception {
        // Initialize the database
        optionSetService.save(optionSet);

        int databaseSizeBeforeDelete = optionSetRepository.findAll().size();

        // Get the optionSet
        restOptionSetMockMvc.perform(delete("/api/option-sets/{id}", optionSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OptionSet> optionSetList = optionSetRepository.findAll();
        assertThat(optionSetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionSet.class);
        OptionSet optionSet1 = new OptionSet();
        optionSet1.setId(1L);
        OptionSet optionSet2 = new OptionSet();
        optionSet2.setId(optionSet1.getId());
        assertThat(optionSet1).isEqualTo(optionSet2);
        optionSet2.setId(2L);
        assertThat(optionSet1).isNotEqualTo(optionSet2);
        optionSet1.setId(null);
        assertThat(optionSet1).isNotEqualTo(optionSet2);
    }
}
