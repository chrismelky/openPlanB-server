package org.openplanrep.web.rest;

import org.openplanrep.ServerApp;

import org.openplanrep.domain.OrgUnitGroup;
import org.openplanrep.domain.OrgUnitGroupSet;
import org.openplanrep.domain.AttributeValue;
import org.openplanrep.repository.OrgUnitGroupRepository;
import org.openplanrep.service.OrgUnitGroupService;
import org.openplanrep.web.rest.errors.ExceptionTranslator;
import org.openplanrep.service.dto.OrgUnitGroupCriteria;
import org.openplanrep.service.OrgUnitGroupQueryService;

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
 * Test class for the OrgUnitGroupResource REST controller.
 *
 * @see OrgUnitGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApp.class)
public class OrgUnitGroupResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrgUnitGroupRepository orgUnitGroupRepository;
    @Mock
    private OrgUnitGroupRepository orgUnitGroupRepositoryMock;
    
    @Mock
    private OrgUnitGroupService orgUnitGroupServiceMock;

    @Autowired
    private OrgUnitGroupService orgUnitGroupService;

    @Autowired
    private OrgUnitGroupQueryService orgUnitGroupQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrgUnitGroupMockMvc;

    private OrgUnitGroup orgUnitGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrgUnitGroupResource orgUnitGroupResource = new OrgUnitGroupResource(orgUnitGroupService, orgUnitGroupQueryService);
        this.restOrgUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupResource)
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
    public static OrgUnitGroup createEntity(EntityManager em) {
        OrgUnitGroup orgUnitGroup = new OrgUnitGroup()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        // Add required entity
        OrgUnitGroupSet orgUnitGroupSet = OrgUnitGroupSetResourceIntTest.createEntity(em);
        em.persist(orgUnitGroupSet);
        em.flush();
        orgUnitGroup.setOrgUnitGroupSet(orgUnitGroupSet);
        return orgUnitGroup;
    }

    @Before
    public void initTest() {
        orgUnitGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrgUnitGroup() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupRepository.findAll().size();

        // Create the OrgUnitGroup
        restOrgUnitGroupMockMvc.perform(post("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isCreated());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeCreate + 1);
        OrgUnitGroup testOrgUnitGroup = orgUnitGroupList.get(orgUnitGroupList.size() - 1);
        assertThat(testOrgUnitGroup.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testOrgUnitGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrgUnitGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrgUnitGroup.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrgUnitGroup.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void createOrgUnitGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orgUnitGroupRepository.findAll().size();

        // Create the OrgUnitGroup with an existing ID
        orgUnitGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUnitGroupMockMvc.perform(post("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orgUnitGroupRepository.findAll().size();
        // set the field null
        orgUnitGroup.setName(null);

        // Create the OrgUnitGroup, which fails.

        restOrgUnitGroupMockMvc.perform(post("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isBadRequest());

        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroups() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    
    public void getAllOrgUnitGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        OrgUnitGroupResource orgUnitGroupResource = new OrgUnitGroupResource(orgUnitGroupServiceMock, orgUnitGroupQueryService);
        when(orgUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrgUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(orgUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllOrgUnitGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrgUnitGroupResource orgUnitGroupResource = new OrgUnitGroupResource(orgUnitGroupServiceMock, orgUnitGroupQueryService);
            when(orgUnitGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrgUnitGroupMockMvc = MockMvcBuilders.standaloneSetup(orgUnitGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(orgUnitGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrgUnitGroup() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get the orgUnitGroup
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups/{id}", orgUnitGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orgUnitGroup.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where uid equals to DEFAULT_UID
        defaultOrgUnitGroupShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the orgUnitGroupList where uid equals to UPDATED_UID
        defaultOrgUnitGroupShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where uid in DEFAULT_UID or UPDATED_UID
        defaultOrgUnitGroupShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the orgUnitGroupList where uid equals to UPDATED_UID
        defaultOrgUnitGroupShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where uid is not null
        defaultOrgUnitGroupShouldBeFound("uid.specified=true");

        // Get all the orgUnitGroupList where uid is null
        defaultOrgUnitGroupShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where code equals to DEFAULT_CODE
        defaultOrgUnitGroupShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the orgUnitGroupList where code equals to UPDATED_CODE
        defaultOrgUnitGroupShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrgUnitGroupShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the orgUnitGroupList where code equals to UPDATED_CODE
        defaultOrgUnitGroupShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where code is not null
        defaultOrgUnitGroupShouldBeFound("code.specified=true");

        // Get all the orgUnitGroupList where code is null
        defaultOrgUnitGroupShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where name equals to DEFAULT_NAME
        defaultOrgUnitGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orgUnitGroupList where name equals to UPDATED_NAME
        defaultOrgUnitGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrgUnitGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orgUnitGroupList where name equals to UPDATED_NAME
        defaultOrgUnitGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where name is not null
        defaultOrgUnitGroupShouldBeFound("name.specified=true");

        // Get all the orgUnitGroupList where name is null
        defaultOrgUnitGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where created equals to DEFAULT_CREATED
        defaultOrgUnitGroupShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the orgUnitGroupList where created equals to UPDATED_CREATED
        defaultOrgUnitGroupShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultOrgUnitGroupShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the orgUnitGroupList where created equals to UPDATED_CREATED
        defaultOrgUnitGroupShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where created is not null
        defaultOrgUnitGroupShouldBeFound("created.specified=true");

        // Get all the orgUnitGroupList where created is null
        defaultOrgUnitGroupShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByLastUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where lastUpdate equals to DEFAULT_LAST_UPDATE
        defaultOrgUnitGroupShouldBeFound("lastUpdate.equals=" + DEFAULT_LAST_UPDATE);

        // Get all the orgUnitGroupList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultOrgUnitGroupShouldNotBeFound("lastUpdate.equals=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByLastUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where lastUpdate in DEFAULT_LAST_UPDATE or UPDATED_LAST_UPDATE
        defaultOrgUnitGroupShouldBeFound("lastUpdate.in=" + DEFAULT_LAST_UPDATE + "," + UPDATED_LAST_UPDATE);

        // Get all the orgUnitGroupList where lastUpdate equals to UPDATED_LAST_UPDATE
        defaultOrgUnitGroupShouldNotBeFound("lastUpdate.in=" + UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByLastUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);

        // Get all the orgUnitGroupList where lastUpdate is not null
        defaultOrgUnitGroupShouldBeFound("lastUpdate.specified=true");

        // Get all the orgUnitGroupList where lastUpdate is null
        defaultOrgUnitGroupShouldNotBeFound("lastUpdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrgUnitGroupsByOrgUnitGroupSetIsEqualToSomething() throws Exception {
        // Initialize the database
        OrgUnitGroupSet orgUnitGroupSet = OrgUnitGroupSetResourceIntTest.createEntity(em);
        em.persist(orgUnitGroupSet);
        em.flush();
        orgUnitGroup.setOrgUnitGroupSet(orgUnitGroupSet);
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);
        Long orgUnitGroupSetId = orgUnitGroupSet.getId();

        // Get all the orgUnitGroupList where orgUnitGroupSet equals to orgUnitGroupSetId
        defaultOrgUnitGroupShouldBeFound("orgUnitGroupSetId.equals=" + orgUnitGroupSetId);

        // Get all the orgUnitGroupList where orgUnitGroupSet equals to orgUnitGroupSetId + 1
        defaultOrgUnitGroupShouldNotBeFound("orgUnitGroupSetId.equals=" + (orgUnitGroupSetId + 1));
    }


    @Test
    @Transactional
    public void getAllOrgUnitGroupsByAttributeValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        AttributeValue attributeValues = AttributeValueResourceIntTest.createEntity(em);
        em.persist(attributeValues);
        em.flush();
        orgUnitGroup.addAttributeValues(attributeValues);
        orgUnitGroupRepository.saveAndFlush(orgUnitGroup);
        Long attributeValuesId = attributeValues.getId();

        // Get all the orgUnitGroupList where attributeValues equals to attributeValuesId
        defaultOrgUnitGroupShouldBeFound("attributeValuesId.equals=" + attributeValuesId);

        // Get all the orgUnitGroupList where attributeValues equals to attributeValuesId + 1
        defaultOrgUnitGroupShouldNotBeFound("attributeValuesId.equals=" + (attributeValuesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrgUnitGroupShouldBeFound(String filter) throws Exception {
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnitGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrgUnitGroupShouldNotBeFound(String filter) throws Exception {
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingOrgUnitGroup() throws Exception {
        // Get the orgUnitGroup
        restOrgUnitGroupMockMvc.perform(get("/api/org-unit-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrgUnitGroup() throws Exception {
        // Initialize the database
        orgUnitGroupService.save(orgUnitGroup);

        int databaseSizeBeforeUpdate = orgUnitGroupRepository.findAll().size();

        // Update the orgUnitGroup
        OrgUnitGroup updatedOrgUnitGroup = orgUnitGroupRepository.findById(orgUnitGroup.getId()).get();
        // Disconnect from session so that the updates on updatedOrgUnitGroup are not directly saved in db
        em.detach(updatedOrgUnitGroup);
        updatedOrgUnitGroup
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restOrgUnitGroupMockMvc.perform(put("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrgUnitGroup)))
            .andExpect(status().isOk());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeUpdate);
        OrgUnitGroup testOrgUnitGroup = orgUnitGroupList.get(orgUnitGroupList.size() - 1);
        assertThat(testOrgUnitGroup.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testOrgUnitGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrgUnitGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgUnitGroup.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrgUnitGroup.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrgUnitGroup() throws Exception {
        int databaseSizeBeforeUpdate = orgUnitGroupRepository.findAll().size();

        // Create the OrgUnitGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restOrgUnitGroupMockMvc.perform(put("/api/org-unit-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orgUnitGroup)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnitGroup in the database
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrgUnitGroup() throws Exception {
        // Initialize the database
        orgUnitGroupService.save(orgUnitGroup);

        int databaseSizeBeforeDelete = orgUnitGroupRepository.findAll().size();

        // Get the orgUnitGroup
        restOrgUnitGroupMockMvc.perform(delete("/api/org-unit-groups/{id}", orgUnitGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrgUnitGroup> orgUnitGroupList = orgUnitGroupRepository.findAll();
        assertThat(orgUnitGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUnitGroup.class);
        OrgUnitGroup orgUnitGroup1 = new OrgUnitGroup();
        orgUnitGroup1.setId(1L);
        OrgUnitGroup orgUnitGroup2 = new OrgUnitGroup();
        orgUnitGroup2.setId(orgUnitGroup1.getId());
        assertThat(orgUnitGroup1).isEqualTo(orgUnitGroup2);
        orgUnitGroup2.setId(2L);
        assertThat(orgUnitGroup1).isNotEqualTo(orgUnitGroup2);
        orgUnitGroup1.setId(null);
        assertThat(orgUnitGroup1).isNotEqualTo(orgUnitGroup2);
    }
}
