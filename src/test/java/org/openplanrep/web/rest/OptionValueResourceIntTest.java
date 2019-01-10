package org.openplanrep.web.rest;

import org.openplanrep.ServerApp;

import org.openplanrep.domain.OptionValue;
import org.openplanrep.repository.OptionValueRepository;
import org.openplanrep.service.OptionValueService;
import org.openplanrep.web.rest.errors.ExceptionTranslator;

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
 * Test class for the OptionValueResource REST controller.
 *
 * @see OptionValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApp.class)
public class OptionValueResourceIntTest {

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
    private OptionValueRepository optionValueRepository;

    

    @Autowired
    private OptionValueService optionValueService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOptionValueMockMvc;

    private OptionValue optionValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OptionValueResource optionValueResource = new OptionValueResource(optionValueService);
        this.restOptionValueMockMvc = MockMvcBuilders.standaloneSetup(optionValueResource)
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
    public static OptionValue createEntity(EntityManager em) {
        OptionValue optionValue = new OptionValue()
            .uuid(DEFAULT_UUID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return optionValue;
    }

    @Before
    public void initTest() {
        optionValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createOptionValue() throws Exception {
        int databaseSizeBeforeCreate = optionValueRepository.findAll().size();

        // Create the OptionValue
        restOptionValueMockMvc.perform(post("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isCreated());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeCreate + 1);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testOptionValue.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptionValue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOptionValue.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOptionValue.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void createOptionValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = optionValueRepository.findAll().size();

        // Create the OptionValue with an existing ID
        optionValue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionValueMockMvc.perform(post("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionValueRepository.findAll().size();
        // set the field null
        optionValue.setName(null);

        // Create the OptionValue, which fails.

        restOptionValueMockMvc.perform(post("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isBadRequest());

        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOptionValues() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList
        restOptionValueMockMvc.perform(get("/api/option-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getOptionValue() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get the optionValue
        restOptionValueMockMvc.perform(get("/api/option-values/{id}", optionValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(optionValue.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingOptionValue() throws Exception {
        // Get the optionValue
        restOptionValueMockMvc.perform(get("/api/option-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOptionValue() throws Exception {
        // Initialize the database
        optionValueService.save(optionValue);

        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();

        // Update the optionValue
        OptionValue updatedOptionValue = optionValueRepository.findById(optionValue.getId()).get();
        // Disconnect from session so that the updates on updatedOptionValue are not directly saved in db
        em.detach(updatedOptionValue);
        updatedOptionValue
            .uuid(UPDATED_UUID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restOptionValueMockMvc.perform(put("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOptionValue)))
            .andExpect(status().isOk());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testOptionValue.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptionValue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOptionValue.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOptionValue.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();

        // Create the OptionValue

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restOptionValueMockMvc.perform(put("/api/option-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(optionValue)))
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOptionValue() throws Exception {
        // Initialize the database
        optionValueService.save(optionValue);

        int databaseSizeBeforeDelete = optionValueRepository.findAll().size();

        // Get the optionValue
        restOptionValueMockMvc.perform(delete("/api/option-values/{id}", optionValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionValue.class);
        OptionValue optionValue1 = new OptionValue();
        optionValue1.setId(1L);
        OptionValue optionValue2 = new OptionValue();
        optionValue2.setId(optionValue1.getId());
        assertThat(optionValue1).isEqualTo(optionValue2);
        optionValue2.setId(2L);
        assertThat(optionValue1).isNotEqualTo(optionValue2);
        optionValue1.setId(null);
        assertThat(optionValue1).isNotEqualTo(optionValue2);
    }
}
