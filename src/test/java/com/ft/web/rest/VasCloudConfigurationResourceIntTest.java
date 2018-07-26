package com.ft.web.rest;

import com.ft.CampaignManagerApp;

import com.ft.domain.VasCloudConfiguration;
import com.ft.repository.VasCloudConfigurationRepository;
import com.ft.service.VasCloudConfigurationService;
import com.ft.service.dto.VasCloudConfigurationDTO;
import com.ft.service.mapper.VasCloudConfigurationMapper;
import com.ft.web.rest.errors.ExceptionTranslator;

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

import java.util.List;


import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VasCloudConfigurationResource REST controller.
 *
 * @see VasCloudConfigurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CampaignManagerApp.class)
public class VasCloudConfigurationResourceIntTest {

    private static final String DEFAULT_END_POINT = "AAAAAAAAAA";
    private static final String UPDATED_END_POINT = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CP_CHARGE = "AAAAAAAAAA";
    private static final String UPDATED_CP_CHARGE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    @Autowired
    private VasCloudConfigurationRepository vasCloudConfigurationRepository;


    @Autowired
    private VasCloudConfigurationMapper vasCloudConfigurationMapper;
    

    @Autowired
    private VasCloudConfigurationService vasCloudConfigurationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restVasCloudConfigurationMockMvc;

    private VasCloudConfiguration vasCloudConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VasCloudConfigurationResource vasCloudConfigurationResource = new VasCloudConfigurationResource(vasCloudConfigurationService);
        this.restVasCloudConfigurationMockMvc = MockMvcBuilders.standaloneSetup(vasCloudConfigurationResource)
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
    public static VasCloudConfiguration createEntity() {
        VasCloudConfiguration vasCloudConfiguration = new VasCloudConfiguration()
            .endPoint(DEFAULT_END_POINT)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .serviceCode(DEFAULT_SERVICE_CODE)
            .cpCode(DEFAULT_CP_CODE)
            .cpCharge(DEFAULT_CP_CHARGE)
            .serviceId(DEFAULT_SERVICE_ID);
        return vasCloudConfiguration;
    }

    @Before
    public void initTest() {
        vasCloudConfigurationRepository.deleteAll();
        vasCloudConfiguration = createEntity();
    }

    @Test
    public void createVasCloudConfiguration() throws Exception {
        int databaseSizeBeforeCreate = vasCloudConfigurationRepository.findAll().size();

        // Create the VasCloudConfiguration
        VasCloudConfigurationDTO vasCloudConfigurationDTO = vasCloudConfigurationMapper.toDto(vasCloudConfiguration);
        restVasCloudConfigurationMockMvc.perform(post("/api/vas-cloud-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vasCloudConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the VasCloudConfiguration in the database
        List<VasCloudConfiguration> vasCloudConfigurationList = vasCloudConfigurationRepository.findAll();
        assertThat(vasCloudConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        VasCloudConfiguration testVasCloudConfiguration = vasCloudConfigurationList.get(vasCloudConfigurationList.size() - 1);
        assertThat(testVasCloudConfiguration.getEndPoint()).isEqualTo(DEFAULT_END_POINT);
        assertThat(testVasCloudConfiguration.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testVasCloudConfiguration.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testVasCloudConfiguration.getServiceCode()).isEqualTo(DEFAULT_SERVICE_CODE);
        assertThat(testVasCloudConfiguration.getCpCode()).isEqualTo(DEFAULT_CP_CODE);
        assertThat(testVasCloudConfiguration.getCpCharge()).isEqualTo(DEFAULT_CP_CHARGE);
        assertThat(testVasCloudConfiguration.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
    }

    @Test
    public void createVasCloudConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vasCloudConfigurationRepository.findAll().size();

        // Create the VasCloudConfiguration with an existing ID
        vasCloudConfiguration.setId("existing_id");
        VasCloudConfigurationDTO vasCloudConfigurationDTO = vasCloudConfigurationMapper.toDto(vasCloudConfiguration);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVasCloudConfigurationMockMvc.perform(post("/api/vas-cloud-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vasCloudConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VasCloudConfiguration in the database
        List<VasCloudConfiguration> vasCloudConfigurationList = vasCloudConfigurationRepository.findAll();
        assertThat(vasCloudConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllVasCloudConfigurations() throws Exception {
        // Initialize the database
        vasCloudConfigurationRepository.save(vasCloudConfiguration);

        // Get all the vasCloudConfigurationList
        restVasCloudConfigurationMockMvc.perform(get("/api/vas-cloud-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vasCloudConfiguration.getId())))
            .andExpect(jsonPath("$.[*].endPoint").value(hasItem(DEFAULT_END_POINT.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].serviceCode").value(hasItem(DEFAULT_SERVICE_CODE.toString())))
            .andExpect(jsonPath("$.[*].cpCode").value(hasItem(DEFAULT_CP_CODE.toString())))
            .andExpect(jsonPath("$.[*].cpCharge").value(hasItem(DEFAULT_CP_CHARGE.toString())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())));
    }
    

    @Test
    public void getVasCloudConfiguration() throws Exception {
        // Initialize the database
        vasCloudConfigurationRepository.save(vasCloudConfiguration);

        // Get the vasCloudConfiguration
        restVasCloudConfigurationMockMvc.perform(get("/api/vas-cloud-configurations/{id}", vasCloudConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vasCloudConfiguration.getId()))
            .andExpect(jsonPath("$.endPoint").value(DEFAULT_END_POINT.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.serviceCode").value(DEFAULT_SERVICE_CODE.toString()))
            .andExpect(jsonPath("$.cpCode").value(DEFAULT_CP_CODE.toString()))
            .andExpect(jsonPath("$.cpCharge").value(DEFAULT_CP_CHARGE.toString()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.toString()));
    }
    @Test
    public void getNonExistingVasCloudConfiguration() throws Exception {
        // Get the vasCloudConfiguration
        restVasCloudConfigurationMockMvc.perform(get("/api/vas-cloud-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateVasCloudConfiguration() throws Exception {
        // Initialize the database
        vasCloudConfigurationRepository.save(vasCloudConfiguration);

        int databaseSizeBeforeUpdate = vasCloudConfigurationRepository.findAll().size();

        // Update the vasCloudConfiguration
        VasCloudConfiguration updatedVasCloudConfiguration = vasCloudConfigurationRepository.findById(vasCloudConfiguration.getId()).get();
        updatedVasCloudConfiguration
            .endPoint(UPDATED_END_POINT)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .serviceCode(UPDATED_SERVICE_CODE)
            .cpCode(UPDATED_CP_CODE)
            .cpCharge(UPDATED_CP_CHARGE)
            .serviceId(UPDATED_SERVICE_ID);
        VasCloudConfigurationDTO vasCloudConfigurationDTO = vasCloudConfigurationMapper.toDto(updatedVasCloudConfiguration);

        restVasCloudConfigurationMockMvc.perform(put("/api/vas-cloud-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vasCloudConfigurationDTO)))
            .andExpect(status().isOk());

        // Validate the VasCloudConfiguration in the database
        List<VasCloudConfiguration> vasCloudConfigurationList = vasCloudConfigurationRepository.findAll();
        assertThat(vasCloudConfigurationList).hasSize(databaseSizeBeforeUpdate);
        VasCloudConfiguration testVasCloudConfiguration = vasCloudConfigurationList.get(vasCloudConfigurationList.size() - 1);
        assertThat(testVasCloudConfiguration.getEndPoint()).isEqualTo(UPDATED_END_POINT);
        assertThat(testVasCloudConfiguration.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testVasCloudConfiguration.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testVasCloudConfiguration.getServiceCode()).isEqualTo(UPDATED_SERVICE_CODE);
        assertThat(testVasCloudConfiguration.getCpCode()).isEqualTo(UPDATED_CP_CODE);
        assertThat(testVasCloudConfiguration.getCpCharge()).isEqualTo(UPDATED_CP_CHARGE);
        assertThat(testVasCloudConfiguration.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
    }

    @Test
    public void updateNonExistingVasCloudConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = vasCloudConfigurationRepository.findAll().size();

        // Create the VasCloudConfiguration
        VasCloudConfigurationDTO vasCloudConfigurationDTO = vasCloudConfigurationMapper.toDto(vasCloudConfiguration);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVasCloudConfigurationMockMvc.perform(put("/api/vas-cloud-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vasCloudConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VasCloudConfiguration in the database
        List<VasCloudConfiguration> vasCloudConfigurationList = vasCloudConfigurationRepository.findAll();
        assertThat(vasCloudConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteVasCloudConfiguration() throws Exception {
        // Initialize the database
        vasCloudConfigurationRepository.save(vasCloudConfiguration);

        int databaseSizeBeforeDelete = vasCloudConfigurationRepository.findAll().size();

        // Get the vasCloudConfiguration
        restVasCloudConfigurationMockMvc.perform(delete("/api/vas-cloud-configurations/{id}", vasCloudConfiguration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VasCloudConfiguration> vasCloudConfigurationList = vasCloudConfigurationRepository.findAll();
        assertThat(vasCloudConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VasCloudConfiguration.class);
        VasCloudConfiguration vasCloudConfiguration1 = new VasCloudConfiguration();
        vasCloudConfiguration1.setId("id1");
        VasCloudConfiguration vasCloudConfiguration2 = new VasCloudConfiguration();
        vasCloudConfiguration2.setId(vasCloudConfiguration1.getId());
        assertThat(vasCloudConfiguration1).isEqualTo(vasCloudConfiguration2);
        vasCloudConfiguration2.setId("id2");
        assertThat(vasCloudConfiguration1).isNotEqualTo(vasCloudConfiguration2);
        vasCloudConfiguration1.setId(null);
        assertThat(vasCloudConfiguration1).isNotEqualTo(vasCloudConfiguration2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VasCloudConfigurationDTO.class);
        VasCloudConfigurationDTO vasCloudConfigurationDTO1 = new VasCloudConfigurationDTO();
        vasCloudConfigurationDTO1.setId("id1");
        VasCloudConfigurationDTO vasCloudConfigurationDTO2 = new VasCloudConfigurationDTO();
        assertThat(vasCloudConfigurationDTO1).isNotEqualTo(vasCloudConfigurationDTO2);
        vasCloudConfigurationDTO2.setId(vasCloudConfigurationDTO1.getId());
        assertThat(vasCloudConfigurationDTO1).isEqualTo(vasCloudConfigurationDTO2);
        vasCloudConfigurationDTO2.setId("id2");
        assertThat(vasCloudConfigurationDTO1).isNotEqualTo(vasCloudConfigurationDTO2);
        vasCloudConfigurationDTO1.setId(null);
        assertThat(vasCloudConfigurationDTO1).isNotEqualTo(vasCloudConfigurationDTO2);
    }
}
