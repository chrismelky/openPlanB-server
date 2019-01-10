package org.openplanrep.web.rest;

import org.openplanrep.ServerApp;

import org.openplanrep.domain.OrganisationUnit;
import org.openplanrep.domain.OrganisationUnit;
import org.openplanrep.domain.OrgUnitGroup;
import org.openplanrep.domain.AttributeValue;
import org.openplanrep.repository.OrganisationUnitRepository;
import org.openplanrep.service.OrganisationUnitService;
import org.openplanrep.web.rest.errors.ExceptionTranslator;
import org.openplanrep.service.dto.OrganisationUnitCriteria;
import org.openplanrep.service.OrganisationUnitQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static org.openplanrep.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrganisationUnitResource REST controller.
 *
 * @see OrganisationUnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApp.class)
public class OrganisationUnitResourceIntTest {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CLOSED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLOSED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMNER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMNER = "BBBBBBBBBB";

    @Autowired
    private OrganisationUnitRepository organisationUnitRepository;
    @Mock
    private OrganisationUnitRepository organisationUnitRepositoryMock;
    
    @Mock
    private OrganisationUnitService organisationUnitServiceMock;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private OrganisationUnitQueryService organisationUnitQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganisationUnitMockMvc;

    private OrganisationUnit organisationUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganisationUnitResource organisationUnitResource = new OrganisationUnitResource(organisationUnitService, organisationUnitQueryService);
        this.restOrganisationUnitMockMvc = MockMvcBuilders.standaloneSetup(organisationUnitResource)
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
    public static OrganisationUnit createEntity(EntityManager em) {
        OrganisationUnit organisationUnit = new OrganisationUnit()
            .uuid(DEFAULT_UUID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .openingDate(DEFAULT_OPENING_DATE)
            .closedDate(DEFAULT_CLOSED_DATE)
            .url(DEFAULT_URL)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .phoneNumner(DEFAULT_PHONE_NUMNER);
        return organisationUnit;
    }

    @Before
    public void initTest() {
        organisationUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganisationUnit() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitRepository.findAll().size();

        // Create the OrganisationUnit
        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isCreated());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeCreate + 1);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(DEFAULT_OPENING_DATE);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(DEFAULT_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOrganisationUnit.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testOrganisationUnit.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumner()).isEqualTo(DEFAULT_PHONE_NUMNER);
    }

    @Test
    @Transactional
    public void createOrganisationUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organisationUnitRepository.findAll().size();

        // Create the OrganisationUnit with an existing ID
        organisationUnit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationUnitRepository.findAll().size();
        // set the field null
        organisationUnit.setName(null);

        // Create the OrganisationUnit, which fails.

        restOrganisationUnitMockMvc.perform(post("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnits() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumner").value(hasItem(DEFAULT_PHONE_NUMNER.toString())));
    }
    
    public void getAllOrganisationUnitsWithEagerRelationshipsIsEnabled() throws Exception {
        OrganisationUnitResource organisationUnitResource = new OrganisationUnitResource(organisationUnitServiceMock, organisationUnitQueryService);
        when(organisationUnitServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrganisationUnitMockMvc = MockMvcBuilders.standaloneSetup(organisationUnitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?eagerload=true"))
        .andExpect(status().isOk());

        verify(organisationUnitServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllOrganisationUnitsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrganisationUnitResource organisationUnitResource = new OrganisationUnitResource(organisationUnitServiceMock, organisationUnitQueryService);
            when(organisationUnitServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrganisationUnitMockMvc = MockMvcBuilders.standaloneSetup(organisationUnitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?eagerload=true"))
        .andExpect(status().isOk());

            verify(organisationUnitServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get the organisationUnit
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units/{id}", organisationUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organisationUnit.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.openingDate").value(DEFAULT_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.closedDate").value(DEFAULT_CLOSED_DATE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumner").value(DEFAULT_PHONE_NUMNER.toString()));
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where uuid equals to DEFAULT_UUID
        defaultOrganisationUnitShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the organisationUnitList where uuid equals to UPDATED_UUID
        defaultOrganisationUnitShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultOrganisationUnitShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the organisationUnitList where uuid equals to UPDATED_UUID
        defaultOrganisationUnitShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where uuid is not null
        defaultOrganisationUnitShouldBeFound("uuid.specified=true");

        // Get all the organisationUnitList where uuid is null
        defaultOrganisationUnitShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where code equals to DEFAULT_CODE
        defaultOrganisationUnitShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the organisationUnitList where code equals to UPDATED_CODE
        defaultOrganisationUnitShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrganisationUnitShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the organisationUnitList where code equals to UPDATED_CODE
        defaultOrganisationUnitShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where code is not null
        defaultOrganisationUnitShouldBeFound("code.specified=true");

        // Get all the organisationUnitList where code is null
        defaultOrganisationUnitShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where name equals to DEFAULT_NAME
        defaultOrganisationUnitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organisationUnitList where name equals to UPDATED_NAME
        defaultOrganisationUnitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganisationUnitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organisationUnitList where name equals to UPDATED_NAME
        defaultOrganisationUnitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where name is not null
        defaultOrganisationUnitShouldBeFound("name.specified=true");

        // Get all the organisationUnitList where name is null
        defaultOrganisationUnitShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate equals to DEFAULT_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.equals=" + DEFAULT_OPENING_DATE);

        // Get all the organisationUnitList where openingDate equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.equals=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate in DEFAULT_OPENING_DATE or UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.in=" + DEFAULT_OPENING_DATE + "," + UPDATED_OPENING_DATE);

        // Get all the organisationUnitList where openingDate equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.in=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate is not null
        defaultOrganisationUnitShouldBeFound("openingDate.specified=true");

        // Get all the organisationUnitList where openingDate is null
        defaultOrganisationUnitShouldNotBeFound("openingDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate greater than or equals to DEFAULT_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.greaterOrEqualThan=" + DEFAULT_OPENING_DATE);

        // Get all the organisationUnitList where openingDate greater than or equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.greaterOrEqualThan=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where openingDate less than or equals to DEFAULT_OPENING_DATE
        defaultOrganisationUnitShouldNotBeFound("openingDate.lessThan=" + DEFAULT_OPENING_DATE);

        // Get all the organisationUnitList where openingDate less than or equals to UPDATED_OPENING_DATE
        defaultOrganisationUnitShouldBeFound("openingDate.lessThan=" + UPDATED_OPENING_DATE);
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate equals to DEFAULT_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.equals=" + DEFAULT_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.equals=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate in DEFAULT_CLOSED_DATE or UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.in=" + DEFAULT_CLOSED_DATE + "," + UPDATED_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.in=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate is not null
        defaultOrganisationUnitShouldBeFound("closedDate.specified=true");

        // Get all the organisationUnitList where closedDate is null
        defaultOrganisationUnitShouldNotBeFound("closedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate greater than or equals to DEFAULT_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.greaterOrEqualThan=" + DEFAULT_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate greater than or equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.greaterOrEqualThan=" + UPDATED_CLOSED_DATE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByClosedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where closedDate less than or equals to DEFAULT_CLOSED_DATE
        defaultOrganisationUnitShouldNotBeFound("closedDate.lessThan=" + DEFAULT_CLOSED_DATE);

        // Get all the organisationUnitList where closedDate less than or equals to UPDATED_CLOSED_DATE
        defaultOrganisationUnitShouldBeFound("closedDate.lessThan=" + UPDATED_CLOSED_DATE);
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where url equals to DEFAULT_URL
        defaultOrganisationUnitShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the organisationUnitList where url equals to UPDATED_URL
        defaultOrganisationUnitShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where url in DEFAULT_URL or UPDATED_URL
        defaultOrganisationUnitShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the organisationUnitList where url equals to UPDATED_URL
        defaultOrganisationUnitShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where url is not null
        defaultOrganisationUnitShouldBeFound("url.specified=true");

        // Get all the organisationUnitList where url is null
        defaultOrganisationUnitShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where latitude equals to DEFAULT_LATITUDE
        defaultOrganisationUnitShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the organisationUnitList where latitude equals to UPDATED_LATITUDE
        defaultOrganisationUnitShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultOrganisationUnitShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the organisationUnitList where latitude equals to UPDATED_LATITUDE
        defaultOrganisationUnitShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where latitude is not null
        defaultOrganisationUnitShouldBeFound("latitude.specified=true");

        // Get all the organisationUnitList where latitude is null
        defaultOrganisationUnitShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where longitude equals to DEFAULT_LONGITUDE
        defaultOrganisationUnitShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the organisationUnitList where longitude equals to UPDATED_LONGITUDE
        defaultOrganisationUnitShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultOrganisationUnitShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the organisationUnitList where longitude equals to UPDATED_LONGITUDE
        defaultOrganisationUnitShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where longitude is not null
        defaultOrganisationUnitShouldBeFound("longitude.specified=true");

        // Get all the organisationUnitList where longitude is null
        defaultOrganisationUnitShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where address equals to DEFAULT_ADDRESS
        defaultOrganisationUnitShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the organisationUnitList where address equals to UPDATED_ADDRESS
        defaultOrganisationUnitShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultOrganisationUnitShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the organisationUnitList where address equals to UPDATED_ADDRESS
        defaultOrganisationUnitShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where address is not null
        defaultOrganisationUnitShouldBeFound("address.specified=true");

        // Get all the organisationUnitList where address is null
        defaultOrganisationUnitShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where email equals to DEFAULT_EMAIL
        defaultOrganisationUnitShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the organisationUnitList where email equals to UPDATED_EMAIL
        defaultOrganisationUnitShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOrganisationUnitShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the organisationUnitList where email equals to UPDATED_EMAIL
        defaultOrganisationUnitShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where email is not null
        defaultOrganisationUnitShouldBeFound("email.specified=true");

        // Get all the organisationUnitList where email is null
        defaultOrganisationUnitShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByPhoneNumnerIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where phoneNumner equals to DEFAULT_PHONE_NUMNER
        defaultOrganisationUnitShouldBeFound("phoneNumner.equals=" + DEFAULT_PHONE_NUMNER);

        // Get all the organisationUnitList where phoneNumner equals to UPDATED_PHONE_NUMNER
        defaultOrganisationUnitShouldNotBeFound("phoneNumner.equals=" + UPDATED_PHONE_NUMNER);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByPhoneNumnerIsInShouldWork() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where phoneNumner in DEFAULT_PHONE_NUMNER or UPDATED_PHONE_NUMNER
        defaultOrganisationUnitShouldBeFound("phoneNumner.in=" + DEFAULT_PHONE_NUMNER + "," + UPDATED_PHONE_NUMNER);

        // Get all the organisationUnitList where phoneNumner equals to UPDATED_PHONE_NUMNER
        defaultOrganisationUnitShouldNotBeFound("phoneNumner.in=" + UPDATED_PHONE_NUMNER);
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByPhoneNumnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationUnitRepository.saveAndFlush(organisationUnit);

        // Get all the organisationUnitList where phoneNumner is not null
        defaultOrganisationUnitShouldBeFound("phoneNumner.specified=true");

        // Get all the organisationUnitList where phoneNumner is null
        defaultOrganisationUnitShouldNotBeFound("phoneNumner.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationUnitsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        OrganisationUnit parent = OrganisationUnitResourceIntTest.createEntity(em);
        em.persist(parent);
        em.flush();
        organisationUnit.setParent(parent);
        organisationUnitRepository.saveAndFlush(organisationUnit);
        Long parentId = parent.getId();

        // Get all the organisationUnitList where parent equals to parentId
        defaultOrganisationUnitShouldBeFound("parentId.equals=" + parentId);

        // Get all the organisationUnitList where parent equals to parentId + 1
        defaultOrganisationUnitShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByOrgUnitGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        OrgUnitGroup orgUnitGroup = OrgUnitGroupResourceIntTest.createEntity(em);
        em.persist(orgUnitGroup);
        em.flush();
        organisationUnit.setOrgUnitGroup(orgUnitGroup);
        organisationUnitRepository.saveAndFlush(organisationUnit);
        Long orgUnitGroupId = orgUnitGroup.getId();

        // Get all the organisationUnitList where orgUnitGroup equals to orgUnitGroupId
        defaultOrganisationUnitShouldBeFound("orgUnitGroupId.equals=" + orgUnitGroupId);

        // Get all the organisationUnitList where orgUnitGroup equals to orgUnitGroupId + 1
        defaultOrganisationUnitShouldNotBeFound("orgUnitGroupId.equals=" + (orgUnitGroupId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganisationUnitsByAttributeValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        AttributeValue attributeValues = AttributeValueResourceIntTest.createEntity(em);
        em.persist(attributeValues);
        em.flush();
        organisationUnit.addAttributeValues(attributeValues);
        organisationUnitRepository.saveAndFlush(organisationUnit);
        Long attributeValuesId = attributeValues.getId();

        // Get all the organisationUnitList where attributeValues equals to attributeValuesId
        defaultOrganisationUnitShouldBeFound("attributeValuesId.equals=" + attributeValuesId);

        // Get all the organisationUnitList where attributeValues equals to attributeValuesId + 1
        defaultOrganisationUnitShouldNotBeFound("attributeValuesId.equals=" + (attributeValuesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrganisationUnitShouldBeFound(String filter) throws Exception {
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisationUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedDate").value(hasItem(DEFAULT_CLOSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumner").value(hasItem(DEFAULT_PHONE_NUMNER.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrganisationUnitShouldNotBeFound(String filter) throws Exception {
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingOrganisationUnit() throws Exception {
        // Get the organisationUnit
        restOrganisationUnitMockMvc.perform(get("/api/organisation-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitService.save(organisationUnit);

        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();

        // Update the organisationUnit
        OrganisationUnit updatedOrganisationUnit = organisationUnitRepository.findById(organisationUnit.getId()).get();
        // Disconnect from session so that the updates on updatedOrganisationUnit are not directly saved in db
        em.detach(updatedOrganisationUnit);
        updatedOrganisationUnit
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .openingDate(UPDATED_OPENING_DATE)
            .closedDate(UPDATED_CLOSED_DATE)
            .url(UPDATED_URL)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phoneNumner(UPDATED_PHONE_NUMNER);

        restOrganisationUnitMockMvc.perform(put("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganisationUnit)))
            .andExpect(status().isOk());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
        OrganisationUnit testOrganisationUnit = organisationUnitList.get(organisationUnitList.size() - 1);
        assertThat(testOrganisationUnit.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testOrganisationUnit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganisationUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationUnit.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testOrganisationUnit.getClosedDate()).isEqualTo(UPDATED_CLOSED_DATE);
        assertThat(testOrganisationUnit.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOrganisationUnit.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOrganisationUnit.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testOrganisationUnit.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganisationUnit.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganisationUnit.getPhoneNumner()).isEqualTo(UPDATED_PHONE_NUMNER);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganisationUnit() throws Exception {
        int databaseSizeBeforeUpdate = organisationUnitRepository.findAll().size();

        // Create the OrganisationUnit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restOrganisationUnitMockMvc.perform(put("/api/organisation-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrganisationUnit in the database
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganisationUnit() throws Exception {
        // Initialize the database
        organisationUnitService.save(organisationUnit);

        int databaseSizeBeforeDelete = organisationUnitRepository.findAll().size();

        // Get the organisationUnit
        restOrganisationUnitMockMvc.perform(delete("/api/organisation-units/{id}", organisationUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrganisationUnit> organisationUnitList = organisationUnitRepository.findAll();
        assertThat(organisationUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationUnit.class);
        OrganisationUnit organisationUnit1 = new OrganisationUnit();
        organisationUnit1.setId(1L);
        OrganisationUnit organisationUnit2 = new OrganisationUnit();
        organisationUnit2.setId(organisationUnit1.getId());
        assertThat(organisationUnit1).isEqualTo(organisationUnit2);
        organisationUnit2.setId(2L);
        assertThat(organisationUnit1).isNotEqualTo(organisationUnit2);
        organisationUnit1.setId(null);
        assertThat(organisationUnit1).isNotEqualTo(organisationUnit2);
    }
}
