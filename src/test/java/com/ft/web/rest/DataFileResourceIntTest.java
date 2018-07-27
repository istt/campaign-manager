package com.ft.web.rest;

import com.ft.CampaignManagerApp;

import com.ft.domain.DataFile;
import com.ft.repository.DataFileRepository;
import com.ft.service.DataFileService;
import com.ft.service.dto.DataFileDTO;
import com.ft.service.mapper.DataFileMapper;
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
import org.springframework.util.Base64Utils;

import java.util.List;


import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DataFileResource REST controller.
 *
 * @see DataFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CampaignManagerApp.class)
public class DataFileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    @Autowired
    private DataFileRepository dataFileRepository;


    @Autowired
    private DataFileMapper dataFileMapper;
    

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restDataFileMockMvc;

    private DataFile dataFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataFileResource dataFileResource = new DataFileResource(dataFileService);
        this.restDataFileMockMvc = MockMvcBuilders.standaloneSetup(dataFileResource)
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
    public static DataFile createEntity() {
        DataFile dataFile = new DataFile()
            .name(DEFAULT_NAME)
            .tags(DEFAULT_TAGS)
            .data(DEFAULT_DATA)
            .dataContentType(DEFAULT_DATA_CONTENT_TYPE);
        return dataFile;
    }

    @Before
    public void initTest() {
        dataFileRepository.deleteAll();
        dataFile = createEntity();
    }

    @Test
    public void createDataFile() throws Exception {
        int databaseSizeBeforeCreate = dataFileRepository.findAll().size();

        // Create the DataFile
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);
        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isCreated());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeCreate + 1);
        DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
        assertThat(testDataFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataFile.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testDataFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testDataFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    public void createDataFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataFileRepository.findAll().size();

        // Create the DataFile with an existing ID
        dataFile.setId("existing_id");
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllDataFiles() throws Exception {
        // Initialize the database
        dataFileRepository.save(dataFile);

        // Get all the dataFileList
        restDataFileMockMvc.perform(get("/api/data-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFile.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))));
    }
    

    @Test
    public void getDataFile() throws Exception {
        // Initialize the database
        dataFileRepository.save(dataFile);

        // Get the dataFile
        restDataFileMockMvc.perform(get("/api/data-files/{id}", dataFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataFile.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)));
    }
    @Test
    public void getNonExistingDataFile() throws Exception {
        // Get the dataFile
        restDataFileMockMvc.perform(get("/api/data-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDataFile() throws Exception {
        // Initialize the database
        dataFileRepository.save(dataFile);

        int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();

        // Update the dataFile
        DataFile updatedDataFile = dataFileRepository.findById(dataFile.getId()).get();
        updatedDataFile
            .name(UPDATED_NAME)
            .tags(UPDATED_TAGS)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE);
        DataFileDTO dataFileDTO = dataFileMapper.toDto(updatedDataFile);

        restDataFileMockMvc.perform(put("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isOk());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
        DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
        assertThat(testDataFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataFile.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testDataFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testDataFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
    }

    @Test
    public void updateNonExistingDataFile() throws Exception {
        int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();

        // Create the DataFile
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataFileMockMvc.perform(put("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteDataFile() throws Exception {
        // Initialize the database
        dataFileRepository.save(dataFile);

        int databaseSizeBeforeDelete = dataFileRepository.findAll().size();

        // Get the dataFile
        restDataFileMockMvc.perform(delete("/api/data-files/{id}", dataFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFile.class);
        DataFile dataFile1 = new DataFile();
        dataFile1.setId("id1");
        DataFile dataFile2 = new DataFile();
        dataFile2.setId(dataFile1.getId());
        assertThat(dataFile1).isEqualTo(dataFile2);
        dataFile2.setId("id2");
        assertThat(dataFile1).isNotEqualTo(dataFile2);
        dataFile1.setId(null);
        assertThat(dataFile1).isNotEqualTo(dataFile2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFileDTO.class);
        DataFileDTO dataFileDTO1 = new DataFileDTO();
        dataFileDTO1.setId("id1");
        DataFileDTO dataFileDTO2 = new DataFileDTO();
        assertThat(dataFileDTO1).isNotEqualTo(dataFileDTO2);
        dataFileDTO2.setId(dataFileDTO1.getId());
        assertThat(dataFileDTO1).isEqualTo(dataFileDTO2);
        dataFileDTO2.setId("id2");
        assertThat(dataFileDTO1).isNotEqualTo(dataFileDTO2);
        dataFileDTO1.setId(null);
        assertThat(dataFileDTO1).isNotEqualTo(dataFileDTO2);
    }
}
