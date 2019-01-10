package org.openplanrep.web.rest;

import org.openplanrep.ServerApp;

import org.openplanrep.domain.Attribute;
import org.openplanrep.domain.OptionSet;
import org.openplanrep.repository.AttributeRepository;
import org.openplanrep.service.AttributeService;
import org.openplanrep.web.rest.errors.ExceptionTranslator;
import org.openplanrep.service.dto.AttributeCriteria;
import org.openplanrep.service.AttributeQueryService;

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
import java.util.List;


import static org.openplanrep.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AttributeResource REST controller.
 *
 * @see AttributeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApp.class)
public class AttributeResourceIntTest {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MANDATORY = false;
    private static final Boolean UPDATED_MANDATORY = true;

    private static final Boolean DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE = false;
    private static final Boolean UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE = true;

    private static final Boolean DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE = false;
    private static final Boolean UPDATED_ORG_UNIT_GROUP_ATTRIBUTE = true;

    private static final Boolean DEFAULT_ORG_UNIT_ATTRIBUTE = false;
    private static final Boolean UPDATED_ORG_UNIT_ATTRIBUTE = true;

    private static final Boolean DEFAULT_USER_ATTRIBUTE = false;
    private static final Boolean UPDATED_USER_ATTRIBUTE = true;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    @Autowired
    private AttributeRepository attributeRepository;

    

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private AttributeQueryService attributeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttributeMockMvc;

    private Attribute attribute;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttributeResource attributeResource = new AttributeResource(attributeService, attributeQueryService);
        this.restAttributeMockMvc = MockMvcBuilders.standaloneSetup(attributeResource)
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
    public static Attribute createEntity(EntityManager em) {
        Attribute attribute = new Attribute()
            .uuid(DEFAULT_UUID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .valueType(DEFAULT_VALUE_TYPE)
            .mandatory(DEFAULT_MANDATORY)
            .orgUnitGroupSetAttribute(DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE)
            .orgUnitGroupAttribute(DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE)
            .orgUnitAttribute(DEFAULT_ORG_UNIT_ATTRIBUTE)
            .userAttribute(DEFAULT_USER_ATTRIBUTE)
            .sortOrder(DEFAULT_SORT_ORDER);
        return attribute;
    }

    @Before
    public void initTest() {
        attribute = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttribute() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isCreated());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate + 1);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testAttribute.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttribute.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testAttribute.isMandatory()).isEqualTo(DEFAULT_MANDATORY);
        assertThat(testAttribute.isOrgUnitGroupSetAttribute()).isEqualTo(DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE);
        assertThat(testAttribute.isOrgUnitGroupAttribute()).isEqualTo(DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE);
        assertThat(testAttribute.isOrgUnitAttribute()).isEqualTo(DEFAULT_ORG_UNIT_ATTRIBUTE);
        assertThat(testAttribute.isUserAttribute()).isEqualTo(DEFAULT_USER_ATTRIBUTE);
        assertThat(testAttribute.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    public void createAttributeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute with an existing ID
        attribute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setName(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setValueType(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMandatoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributeRepository.findAll().size();
        // set the field null
        attribute.setMandatory(null);

        // Create the Attribute, which fails.

        restAttributeMockMvc.perform(post("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttributes() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].orgUnitGroupSetAttribute").value(hasItem(DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].orgUnitGroupAttribute").value(hasItem(DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].orgUnitAttribute").value(hasItem(DEFAULT_ORG_UNIT_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].userAttribute").value(hasItem(DEFAULT_USER_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }
    

    @Test
    @Transactional
    public void getAttribute() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", attribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attribute.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.mandatory").value(DEFAULT_MANDATORY.booleanValue()))
            .andExpect(jsonPath("$.orgUnitGroupSetAttribute").value(DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE.booleanValue()))
            .andExpect(jsonPath("$.orgUnitGroupAttribute").value(DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE.booleanValue()))
            .andExpect(jsonPath("$.orgUnitAttribute").value(DEFAULT_ORG_UNIT_ATTRIBUTE.booleanValue()))
            .andExpect(jsonPath("$.userAttribute").value(DEFAULT_USER_ATTRIBUTE.booleanValue()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    public void getAllAttributesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where uuid equals to DEFAULT_UUID
        defaultAttributeShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the attributeList where uuid equals to UPDATED_UUID
        defaultAttributeShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllAttributesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultAttributeShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the attributeList where uuid equals to UPDATED_UUID
        defaultAttributeShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllAttributesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where uuid is not null
        defaultAttributeShouldBeFound("uuid.specified=true");

        // Get all the attributeList where uuid is null
        defaultAttributeShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where code equals to DEFAULT_CODE
        defaultAttributeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the attributeList where code equals to UPDATED_CODE
        defaultAttributeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAttributesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultAttributeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the attributeList where code equals to UPDATED_CODE
        defaultAttributeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAttributesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where code is not null
        defaultAttributeShouldBeFound("code.specified=true");

        // Get all the attributeList where code is null
        defaultAttributeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name equals to DEFAULT_NAME
        defaultAttributeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attributeList where name equals to UPDATED_NAME
        defaultAttributeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttributeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attributeList where name equals to UPDATED_NAME
        defaultAttributeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttributesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name is not null
        defaultAttributeShouldBeFound("name.specified=true");

        // Get all the attributeList where name is null
        defaultAttributeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where valueType equals to DEFAULT_VALUE_TYPE
        defaultAttributeShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the attributeList where valueType equals to UPDATED_VALUE_TYPE
        defaultAttributeShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttributesByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultAttributeShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the attributeList where valueType equals to UPDATED_VALUE_TYPE
        defaultAttributeShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttributesByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where valueType is not null
        defaultAttributeShouldBeFound("valueType.specified=true");

        // Get all the attributeList where valueType is null
        defaultAttributeShouldNotBeFound("valueType.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByMandatoryIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where mandatory equals to DEFAULT_MANDATORY
        defaultAttributeShouldBeFound("mandatory.equals=" + DEFAULT_MANDATORY);

        // Get all the attributeList where mandatory equals to UPDATED_MANDATORY
        defaultAttributeShouldNotBeFound("mandatory.equals=" + UPDATED_MANDATORY);
    }

    @Test
    @Transactional
    public void getAllAttributesByMandatoryIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where mandatory in DEFAULT_MANDATORY or UPDATED_MANDATORY
        defaultAttributeShouldBeFound("mandatory.in=" + DEFAULT_MANDATORY + "," + UPDATED_MANDATORY);

        // Get all the attributeList where mandatory equals to UPDATED_MANDATORY
        defaultAttributeShouldNotBeFound("mandatory.in=" + UPDATED_MANDATORY);
    }

    @Test
    @Transactional
    public void getAllAttributesByMandatoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where mandatory is not null
        defaultAttributeShouldBeFound("mandatory.specified=true");

        // Get all the attributeList where mandatory is null
        defaultAttributeShouldNotBeFound("mandatory.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitGroupSetAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitGroupSetAttribute equals to DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE
        defaultAttributeShouldBeFound("orgUnitGroupSetAttribute.equals=" + DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE);

        // Get all the attributeList where orgUnitGroupSetAttribute equals to UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE
        defaultAttributeShouldNotBeFound("orgUnitGroupSetAttribute.equals=" + UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitGroupSetAttributeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitGroupSetAttribute in DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE or UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE
        defaultAttributeShouldBeFound("orgUnitGroupSetAttribute.in=" + DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE + "," + UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE);

        // Get all the attributeList where orgUnitGroupSetAttribute equals to UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE
        defaultAttributeShouldNotBeFound("orgUnitGroupSetAttribute.in=" + UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitGroupSetAttributeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitGroupSetAttribute is not null
        defaultAttributeShouldBeFound("orgUnitGroupSetAttribute.specified=true");

        // Get all the attributeList where orgUnitGroupSetAttribute is null
        defaultAttributeShouldNotBeFound("orgUnitGroupSetAttribute.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitGroupAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitGroupAttribute equals to DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE
        defaultAttributeShouldBeFound("orgUnitGroupAttribute.equals=" + DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE);

        // Get all the attributeList where orgUnitGroupAttribute equals to UPDATED_ORG_UNIT_GROUP_ATTRIBUTE
        defaultAttributeShouldNotBeFound("orgUnitGroupAttribute.equals=" + UPDATED_ORG_UNIT_GROUP_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitGroupAttributeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitGroupAttribute in DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE or UPDATED_ORG_UNIT_GROUP_ATTRIBUTE
        defaultAttributeShouldBeFound("orgUnitGroupAttribute.in=" + DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE + "," + UPDATED_ORG_UNIT_GROUP_ATTRIBUTE);

        // Get all the attributeList where orgUnitGroupAttribute equals to UPDATED_ORG_UNIT_GROUP_ATTRIBUTE
        defaultAttributeShouldNotBeFound("orgUnitGroupAttribute.in=" + UPDATED_ORG_UNIT_GROUP_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitGroupAttributeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitGroupAttribute is not null
        defaultAttributeShouldBeFound("orgUnitGroupAttribute.specified=true");

        // Get all the attributeList where orgUnitGroupAttribute is null
        defaultAttributeShouldNotBeFound("orgUnitGroupAttribute.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitAttribute equals to DEFAULT_ORG_UNIT_ATTRIBUTE
        defaultAttributeShouldBeFound("orgUnitAttribute.equals=" + DEFAULT_ORG_UNIT_ATTRIBUTE);

        // Get all the attributeList where orgUnitAttribute equals to UPDATED_ORG_UNIT_ATTRIBUTE
        defaultAttributeShouldNotBeFound("orgUnitAttribute.equals=" + UPDATED_ORG_UNIT_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitAttributeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitAttribute in DEFAULT_ORG_UNIT_ATTRIBUTE or UPDATED_ORG_UNIT_ATTRIBUTE
        defaultAttributeShouldBeFound("orgUnitAttribute.in=" + DEFAULT_ORG_UNIT_ATTRIBUTE + "," + UPDATED_ORG_UNIT_ATTRIBUTE);

        // Get all the attributeList where orgUnitAttribute equals to UPDATED_ORG_UNIT_ATTRIBUTE
        defaultAttributeShouldNotBeFound("orgUnitAttribute.in=" + UPDATED_ORG_UNIT_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByOrgUnitAttributeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where orgUnitAttribute is not null
        defaultAttributeShouldBeFound("orgUnitAttribute.specified=true");

        // Get all the attributeList where orgUnitAttribute is null
        defaultAttributeShouldNotBeFound("orgUnitAttribute.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesByUserAttributeIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where userAttribute equals to DEFAULT_USER_ATTRIBUTE
        defaultAttributeShouldBeFound("userAttribute.equals=" + DEFAULT_USER_ATTRIBUTE);

        // Get all the attributeList where userAttribute equals to UPDATED_USER_ATTRIBUTE
        defaultAttributeShouldNotBeFound("userAttribute.equals=" + UPDATED_USER_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByUserAttributeIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where userAttribute in DEFAULT_USER_ATTRIBUTE or UPDATED_USER_ATTRIBUTE
        defaultAttributeShouldBeFound("userAttribute.in=" + DEFAULT_USER_ATTRIBUTE + "," + UPDATED_USER_ATTRIBUTE);

        // Get all the attributeList where userAttribute equals to UPDATED_USER_ATTRIBUTE
        defaultAttributeShouldNotBeFound("userAttribute.in=" + UPDATED_USER_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllAttributesByUserAttributeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where userAttribute is not null
        defaultAttributeShouldBeFound("userAttribute.specified=true");

        // Get all the attributeList where userAttribute is null
        defaultAttributeShouldNotBeFound("userAttribute.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the attributeList where sortOrder equals to UPDATED_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the attributeList where sortOrder equals to UPDATED_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder is not null
        defaultAttributeShouldBeFound("sortOrder.specified=true");

        // Get all the attributeList where sortOrder is null
        defaultAttributeShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder greater than or equals to DEFAULT_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.greaterOrEqualThan=" + DEFAULT_SORT_ORDER);

        // Get all the attributeList where sortOrder greater than or equals to UPDATED_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.greaterOrEqualThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllAttributesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where sortOrder less than or equals to DEFAULT_SORT_ORDER
        defaultAttributeShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the attributeList where sortOrder less than or equals to UPDATED_SORT_ORDER
        defaultAttributeShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }


    @Test
    @Transactional
    public void getAllAttributesByOptionSetIsEqualToSomething() throws Exception {
        // Initialize the database
        OptionSet optionSet = OptionSetResourceIntTest.createEntity(em);
        em.persist(optionSet);
        em.flush();
        attribute.setOptionSet(optionSet);
        attributeRepository.saveAndFlush(attribute);
        Long optionSetId = optionSet.getId();

        // Get all the attributeList where optionSet equals to optionSetId
        defaultAttributeShouldBeFound("optionSetId.equals=" + optionSetId);

        // Get all the attributeList where optionSet equals to optionSetId + 1
        defaultAttributeShouldNotBeFound("optionSetId.equals=" + (optionSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAttributeShouldBeFound(String filter) throws Exception {
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].orgUnitGroupSetAttribute").value(hasItem(DEFAULT_ORG_UNIT_GROUP_SET_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].orgUnitGroupAttribute").value(hasItem(DEFAULT_ORG_UNIT_GROUP_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].orgUnitAttribute").value(hasItem(DEFAULT_ORG_UNIT_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].userAttribute").value(hasItem(DEFAULT_USER_ATTRIBUTE.booleanValue())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAttributeShouldNotBeFound(String filter) throws Exception {
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingAttribute() throws Exception {
        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Update the attribute
        Attribute updatedAttribute = attributeRepository.findById(attribute.getId()).get();
        // Disconnect from session so that the updates on updatedAttribute are not directly saved in db
        em.detach(updatedAttribute);
        updatedAttribute
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .valueType(UPDATED_VALUE_TYPE)
            .mandatory(UPDATED_MANDATORY)
            .orgUnitGroupSetAttribute(UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE)
            .orgUnitGroupAttribute(UPDATED_ORG_UNIT_GROUP_ATTRIBUTE)
            .orgUnitAttribute(UPDATED_ORG_UNIT_ATTRIBUTE)
            .userAttribute(UPDATED_USER_ATTRIBUTE)
            .sortOrder(UPDATED_SORT_ORDER);

        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttribute)))
            .andExpect(status().isOk());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);
        Attribute testAttribute = attributeList.get(attributeList.size() - 1);
        assertThat(testAttribute.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testAttribute.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttribute.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testAttribute.isMandatory()).isEqualTo(UPDATED_MANDATORY);
        assertThat(testAttribute.isOrgUnitGroupSetAttribute()).isEqualTo(UPDATED_ORG_UNIT_GROUP_SET_ATTRIBUTE);
        assertThat(testAttribute.isOrgUnitGroupAttribute()).isEqualTo(UPDATED_ORG_UNIT_GROUP_ATTRIBUTE);
        assertThat(testAttribute.isOrgUnitAttribute()).isEqualTo(UPDATED_ORG_UNIT_ATTRIBUTE);
        assertThat(testAttribute.isUserAttribute()).isEqualTo(UPDATED_USER_ATTRIBUTE);
        assertThat(testAttribute.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingAttribute() throws Exception {
        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Create the Attribute

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restAttributeMockMvc.perform(put("/api/attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attribute)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeDelete = attributeRepository.findAll().size();

        // Get the attribute
        restAttributeMockMvc.perform(delete("/api/attributes/{id}", attribute.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Attribute> attributeList = attributeRepository.findAll();
        assertThat(attributeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribute.class);
        Attribute attribute1 = new Attribute();
        attribute1.setId(1L);
        Attribute attribute2 = new Attribute();
        attribute2.setId(attribute1.getId());
        assertThat(attribute1).isEqualTo(attribute2);
        attribute2.setId(2L);
        assertThat(attribute1).isNotEqualTo(attribute2);
        attribute1.setId(null);
        assertThat(attribute1).isNotEqualTo(attribute2);
    }
}
