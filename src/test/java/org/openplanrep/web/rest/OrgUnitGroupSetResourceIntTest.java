package org.openplanrep.web.rest;

import org.openplanrep.ServerApp;

import org.openplanrep.domain.OrgUnitGroupSet;
import org.openplanrep.domain.AttributeValue;
import org.openplanrep.repository.OrgUnitGroupSetRepository;
import org.openplanrep.service.OrgUnitGroupSetService;
import org.openplanrep.web.rest.errors.ExceptionTranslator;
import org.openplanrep.service.dto.OrgUnitGroupSetCriteria;
import org.openplanrep.service.OrgUnitGroupSetQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;


import static org.openplanrep.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrgUnitGroupSetResource REST controller.
 *
 * @see OrgUnitGroupSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApp.class)
public class OrgUnitGroupSetResourceIntTest {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrgUnitGroupSetRepository orgUnitGroupSetRepository;
    @Mock
    private OrgUnitGroupSetRepository orgUnitGroupSetRepositoryMock;
    
    @Mock
    private OrgUnitGroupSetService orgUnitGroupSetServiceMock;

    @Autowired
    private OrgUnitGroupSetService orgUnitGroupSetService;

    @Autowired
    private OrgUnitGroupSetQueryService orgUnitGroupSetQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrgUnitGroupSetMockMvc;

    private OrgUnitGroupSet orgUnitGroupSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrgUnitGroupSetResource orgUnitGroupSetResource = new OrgUnitGroupSetResource(orgUnitGroupSetService, orgUnitGroupSetQueryService);
        this.restOrgUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupSetResource)
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
    public static OrgUnitGroupSet createEntity(EntityManager em) {
        OrgUnitGroupSet orgUnitGroupSet = new OrgUnitGroupSet()
            .uuid(DEFAULT_UUID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return orgUnitGroupSet;
    }

    @Before
    public void initTest() {
        orgUnitGroupSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrgUnitGroupSet() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupSetRepository.findAll().size();

        // Create the OrgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(post("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isCreated());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUnitGroupSet testOrgUnitGroupSet = orgUnitGroupSetList.get(orgUnitGroupSetList.size() - 1);
        assertThat(testOrgUnitGroupSet.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testOrgUnitGroupSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrgUnitGroupSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrgUnitGroupSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrgUnitGroupSet.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrgUnitGroupSet.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void createOrgUnitGroupSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupSetRepository.findAll().size();

        // Create the OrgUnitGroupSet with an existing ID
        orgUnitGroupSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUnitGroupSetMockMvc.perform(post("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitGroupSetRepository.findAll().size();
        // set the field null
        orgUnitGroupSet.setName(null);

        // Create the OrgUnitGroupSet, which fails.

        restOrgUnitGroupSetMockMvc.perform(post("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isBadRequest());

        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSets() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    
    public void getAllOrgUnitGroupSetsWithEagerRelationshipsIsEnabled() throws Exception {
        OrgUnitGroupSetResource orgUnitGroupSetResource = new OrgUnitGroupSetResource(orgUnitGroupSetServiceMock, orgUnitGroupSetQueryService);
        when(orgUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrgUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?eagerload=true"))
        .andExpect(status().isOk());

        verify(orgUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllOrgUnitGroupSetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrgUnitGroupSetResource orgUnitGroupSetResource = new OrgUnitGroupSetResource(orgUnitGroupSetServiceMock, orgUnitGroupSetQueryService);
            when(orgUnitGroupSetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrgUnitGroupSetMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?eagerload=true"))
        .andExpect(status().isOk());

            verify(orgUnitGroupSetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrgUnitGroupSet() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get the orgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets/{id}", orgUnitGroupSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orgUnitGroupSet.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where uuid equals to DEFAULT_UUID
        defaultOrgUnitGroupSetShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the orgUnitGroupSetList where uuid equals to UPDATED_UUID
        defaultOrgUnitGroupSetShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultOrgUnitGroupSetShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the orgUnitGroupSetList where uuid equals to UPDATED_UUID
        defaultOrgUnitGroupSetShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where uuid is not null
        defaultOrgUnitGroupSetShouldBeFound("uuid.specified=true");

        // Get all the orgUnitGroupSetList where uuid is null
        defaultOrgUnitGroupSetShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where code equals to DEFAULT_CODE
        defaultOrgUnitGroupSetShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the orgUnitGroupSetList where code equals to UPDATED_CODE
        defaultOrgUnitGroupSetShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrgUnitGroupSetShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the orgUnitGroupSetList where code equals to UPDATED_CODE
        defaultOrgUnitGroupSetShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where code is not null
        defaultOrgUnitGroupSetShouldBeFound("code.specified=true");

        // Get all the orgUnitGroupSetList where code is null
        defaultOrgUnitGroupSetShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where name equals to DEFAULT_NAME
        defaultOrgUnitGroupSetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orgUnitGroupSetList where name equals to UPDATED_NAME
        defaultOrgUnitGroupSetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrgUnitGroupSetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orgUnitGroupSetList where name equals to UPDATED_NAME
        defaultOrgUnitGroupSetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where name is not null
        defaultOrgUnitGroupSetShouldBeFound("name.specified=true");

        // Get all the orgUnitGroupSetList where name is null
        defaultOrgUnitGroupSetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where description equals to DEFAULT_DESCRIPTION
        defaultOrgUnitGroupSetShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the orgUnitGroupSetList where description equals to UPDATED_DESCRIPTION
        defaultOrgUnitGroupSetShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrgUnitGroupSetShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the orgUnitGroupSetList where description equals to UPDATED_DESCRIPTION
        defaultOrgUnitGroupSetShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where description is not null
        defaultOrgUnitGroupSetShouldBeFound("description.specified=true");

        // Get all the orgUnitGroupSetList where description is null
        defaultOrgUnitGroupSetShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where created equals to DEFAULT_CREATED
        defaultOrgUnitGroupSetShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the orgUnitGroupSetList where created equals to UPDATED_CREATED
        defaultOrgUnitGroupSetShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultOrgUnitGroupSetShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the orgUnitGroupSetList where created equals to UPDATED_CREATED
        defaultOrgUnitGroupSetShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where created is not null
        defaultOrgUnitGroupSetShouldBeFound("created.specified=true");

        // Get all the orgUnitGroupSetList where created is null
        defaultOrgUnitGroupSetShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByLastUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where lastUpdate equals to DEFAULT_LAST_UPDATE
        defaultOrgUnitGroupSetShouldBeFound("lastUpdate.equals=" + DEFAULT_LAST_UPDATE);

        // Get all the orgUnitGroupSetList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultOrgUnitGroupSetShouldNotBeFound("lastUpdate.equals=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByLastUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where lastUpdate in DEFAULT_LAST_UPDATE or UPDATED_LAST_UPDATE
        defaultOrgUnitGroupSetShouldBeFound("lastUpdate.in=" + DEFAULT_LAST_UPDATE + "," + UPDATED_LAST_UPDATE);

        // Get all the orgUnitGroupSetList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultOrgUnitGroupSetShouldNotBeFound("lastUpdate.in=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByLastUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);

        // Get all the orgUnitGroupSetList where lastUpdate is not null
        defaultOrgUnitGroupSetShouldBeFound("lastUpdate.specified=true");

        // Get all the orgUnitGroupSetList where lastUpdate is null
        defaultOrgUnitGroupSetShouldNotBeFound("lastUpdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupSetsByAttributeValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        AttributeValue attributeValues = AttributeValueResourceIntTest.createEntity(em);
        em.persist(attributeValues);
        em.flush();
        orgUnitGroupSet.addAttributeValues(attributeValues);
        orgUnitGroupSetRepository.saveAndFlush(orgUnitGroupSet);
        Long attributeValuesId = attributeValues.getId();

        // Get all the orgUnitGroupSetList where attributeValues equals to attributeValuesId
        defaultOrgUnitGroupSetShouldBeFound("attributeValuesId.equals=" + attributeValuesId);

        // Get all the orgUnitGroupSetList where attributeValues equals to attributeValuesId + 1
        defaultOrgUnitGroupSetShouldNotBeFound("attributeValuesId.equals=" + (attributeValuesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrgUnitGroupSetShouldBeFound(String filter) throws Exception {
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroupSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrgUnitGroupSetShouldNotBeFound(String filter) throws Exception {
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingOrgUnitGroupSet() throws Exception {
        // Get the orgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(get("/api/org-unit-group-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrgUnitGroupSet() throws Exception {
        // Initialize the database
        orgUnitGroupSetService.save(orgUnitGroupSet);

        int databaseSizeBeforeUpdate = orgUnitGroupSetRepository.findAll().size();

        // Update the orgUnitGroupSet
        OrgUnitGroupSet updatedOrgUnitGroupSet = orgUnitGroupSetRepository.findById(orgUnitGroupSet.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUnitGroupSet are not directly saved in db
        em.detach(updatedOrgUnitGroupSet);
        updatedOrgUnitGroupSet
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restOrgUnitGroupSetMockMvc.perform(put("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrgUnitGroupSet)))
            .andExpect(status().isOk());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
        OrgUnitGroupSet testOrgUnitGroupSet = orgUnitGroupSetList.get(orgUnitGroupSetList.size() - 1);
        assertThat(testOrgUnitGroupSet.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testOrgUnitGroupSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrgUnitGroupSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUnitGroupSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrgUnitGroupSet.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrgUnitGroupSet.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrgUnitGroupSet() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitGroupSetRepository.findAll().size();

        // Create the OrgUnitGroupSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restOrgUnitGroupSetMockMvc.perform(put("/api/org-unit-group-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroupSet)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroupSet in the database
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrgUnitGroupSet() throws Exception {
        // Initialize the database
        orgUnitGroupSetService.save(orgUnitGroupSet);

        int databaseSizeBeforeDelete = orgUnitGroupSetRepository.findAll().size();

        // Get the orgUnitGroupSet
        restOrgUnitGroupSetMockMvc.perform(delete("/api/org-unit-group-sets/{id}", orgUnitGroupSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrgUnitGroupSet> orgUnitGroupSetList = orgUnitGroupSetRepository.findAll();
        assertThat(orgUnitGroupSetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUnitGroupSet.class);
        OrgUnitGroupSet orgUnitGroupSet1 = new OrgUnitGroupSet();
        orgUnitGroupSet1.setId(1L);
        OrgUnitGroupSet orgUnitGroupSet2 = new OrgUnitGroupSet();
        orgUnitGroupSet2.setId(orgUnitGroupSet1.getId());
        assertThat(orgUnitGroupSet1).isEqualTo(orgUnitGroupSet2);
        orgUnitGroupSet2.setId(2L);
        assertThat(orgUnitGroupSet1).isNotEqualTo(orgUnitGroupSet2);
        orgUnitGroupSet1.setId(null);
        assertThat(orgUnitGroupSet1).isNotEqualTo(orgUnitGroupSet2);
    }
}
