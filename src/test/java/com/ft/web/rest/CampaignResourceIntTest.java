package com.ft.web.rest;

import com.ft.CampaignManagerApp;

import com.ft.domain.Campaign;
import com.ft.repository.CampaignRepository;
import com.ft.service.CampaignService;
import com.ft.service.dto.CampaignDTO;
import com.ft.service.mapper.CampaignMapper;
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

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.ft.web.rest.TestUtil.sameInstant;
import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CampaignResource REST controller.
 *
 * @see CampaignResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CampaignManagerApp.class)
public class CampaignResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String DEFAULT_SHORT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CALLBACK_URL = "AAAAAAAAAA";
    private static final String UPDATED_CALLBACK_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_APPROVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SHORT_MSG = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_MSG = "BBBBBBBBBB";

    private static final byte[] DEFAULT_MSISDN_LIST = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MSISDN_LIST = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_MSISDN_LIST_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MSISDN_LIST_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_START_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXPIRED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_WORKING_HOURS = "AAAAAAAAAA";
    private static final String UPDATED_WORKING_HOURS = "BBBBBBBBBB";

    private static final String DEFAULT_WORKING_WEEKDAYS = "AAAAAAAAAA";
    private static final String UPDATED_WORKING_WEEKDAYS = "BBBBBBBBBB";

    private static final String DEFAULT_WORKING_DAYS = "AAAAAAAAAA";
    private static final String UPDATED_WORKING_DAYS = "BBBBBBBBBB";

    private static final String DEFAULT_SP_SVC = "AAAAAAAAAA";
    private static final String UPDATED_SP_SVC = "BBBBBBBBBB";

    private static final String DEFAULT_SP_ID = "AAAAAAAAAA";
    private static final String UPDATED_SP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CP_ID = "AAAAAAAAAA";
    private static final String UPDATED_CP_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_MSG_QUOTA = 1;
    private static final Integer UPDATED_MSG_QUOTA = 2;

    private static final Integer DEFAULT_SUB_QUOTA = 1;
    private static final Integer UPDATED_SUB_QUOTA = 2;

    private static final Long DEFAULT_RATE_LIMIT = 1L;
    private static final Long UPDATED_RATE_LIMIT = 2L;

    @Autowired
    private CampaignRepository campaignRepository;


    @Autowired
    private CampaignMapper campaignMapper;


    @Autowired
    private CampaignService campaignService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCampaignMockMvc;

    private Campaign campaign;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CampaignResource campaignResource = new CampaignResource(campaignService);
        this.restCampaignMockMvc = MockMvcBuilders.standaloneSetup(campaignResource)
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
    public static Campaign createEntity() {
        Campaign campaign = new Campaign()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .code(DEFAULT_CODE)
            .externalId(DEFAULT_EXTERNAL_ID)
            .channel(DEFAULT_CHANNEL)
            .state(DEFAULT_STATE)
            .shortCode(DEFAULT_SHORT_CODE)
            .callbackUrl(DEFAULT_CALLBACK_URL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .approvedBy(DEFAULT_APPROVED_BY)
            .approvedAt(DEFAULT_APPROVED_AT)
            .shortMsg(DEFAULT_SHORT_MSG)
            .msisdnList(DEFAULT_MSISDN_LIST)
            .msisdnListContentType(DEFAULT_MSISDN_LIST_CONTENT_TYPE)
            .startAt(DEFAULT_START_AT)
            .expiredAt(DEFAULT_EXPIRED_AT)
//            .workingHours(DEFAULT_WORKING_HOURS)
//            .workingWeekdays(DEFAULT_WORKING_WEEKDAYS)
//            .workingDays(DEFAULT_WORKING_DAYS)
            .spSvc(DEFAULT_SP_SVC)
            .spId(DEFAULT_SP_ID)
            .cpId(DEFAULT_CP_ID)
            .msgQuota(DEFAULT_MSG_QUOTA)
            .subQuota(DEFAULT_SUB_QUOTA)
            .rateLimit(DEFAULT_RATE_LIMIT);
        return campaign;
    }

    @Before
    public void initTest() {
        campaignRepository.deleteAll();
        campaign = createEntity();
    }

    @Test
    public void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);
        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isCreated());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCampaign.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCampaign.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testCampaign.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testCampaign.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCampaign.getShortCode()).isEqualTo(DEFAULT_SHORT_CODE);
        assertThat(testCampaign.getCallbackUrl()).isEqualTo(DEFAULT_CALLBACK_URL);
        assertThat(testCampaign.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCampaign.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCampaign.getApprovedBy()).isEqualTo(DEFAULT_APPROVED_BY);
        assertThat(testCampaign.getApprovedAt()).isEqualTo(DEFAULT_APPROVED_AT);
        assertThat(testCampaign.getShortMsg()).isEqualTo(DEFAULT_SHORT_MSG);
        assertThat(testCampaign.getMsisdnList()).isEqualTo(DEFAULT_MSISDN_LIST);
        assertThat(testCampaign.getMsisdnListContentType()).isEqualTo(DEFAULT_MSISDN_LIST_CONTENT_TYPE);
        assertThat(testCampaign.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testCampaign.getExpiredAt()).isEqualTo(DEFAULT_EXPIRED_AT);
        assertThat(testCampaign.getWorkingHours()).isEqualTo(DEFAULT_WORKING_HOURS);
        assertThat(testCampaign.getWorkingWeekdays()).isEqualTo(DEFAULT_WORKING_WEEKDAYS);
        assertThat(testCampaign.getWorkingDays()).isEqualTo(DEFAULT_WORKING_DAYS);
        assertThat(testCampaign.getSpSvc()).isEqualTo(DEFAULT_SP_SVC);
        assertThat(testCampaign.getSpId()).isEqualTo(DEFAULT_SP_ID);
        assertThat(testCampaign.getCpId()).isEqualTo(DEFAULT_CP_ID);
        assertThat(testCampaign.getMsgQuota()).isEqualTo(DEFAULT_MSG_QUOTA);
        assertThat(testCampaign.getSubQuota()).isEqualTo(DEFAULT_SUB_QUOTA);
        assertThat(testCampaign.getRateLimit()).isEqualTo(DEFAULT_RATE_LIMIT);
    }

    @Test
    public void createCampaignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign with an existing ID
        campaign.setId("existing_id");
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setName(null);

        // Create the Campaign, which fails.
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setCode(null);

        // Create the Campaign, which fails.
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkChannelIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setChannel(null);

        // Create the Campaign, which fails.
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkShortCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().size();
        // set the field null
        campaign.setShortCode(null);

        // Create the Campaign, which fails.
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCampaigns() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign);

        // Get all the campaignList
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].shortCode").value(hasItem(DEFAULT_SHORT_CODE.toString())))
            .andExpect(jsonPath("$.[*].callbackUrl").value(hasItem(DEFAULT_CALLBACK_URL.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY.toString())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].shortMsg").value(hasItem(DEFAULT_SHORT_MSG.toString())))
            .andExpect(jsonPath("$.[*].msisdnListContentType").value(hasItem(DEFAULT_MSISDN_LIST_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].msisdnList").value(hasItem(Base64Utils.encodeToString(DEFAULT_MSISDN_LIST))))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(sameInstant(DEFAULT_START_AT))))
            .andExpect(jsonPath("$.[*].expiredAt").value(hasItem(sameInstant(DEFAULT_EXPIRED_AT))))
            .andExpect(jsonPath("$.[*].workingHours").value(hasItem(DEFAULT_WORKING_HOURS.toString())))
            .andExpect(jsonPath("$.[*].workingWeekdays").value(hasItem(DEFAULT_WORKING_WEEKDAYS.toString())))
            .andExpect(jsonPath("$.[*].workingDays").value(hasItem(DEFAULT_WORKING_DAYS.toString())))
            .andExpect(jsonPath("$.[*].spSvc").value(hasItem(DEFAULT_SP_SVC.toString())))
            .andExpect(jsonPath("$.[*].spId").value(hasItem(DEFAULT_SP_ID.toString())))
            .andExpect(jsonPath("$.[*].cpId").value(hasItem(DEFAULT_CP_ID.toString())))
            .andExpect(jsonPath("$.[*].msgQuota").value(hasItem(DEFAULT_MSG_QUOTA)))
            .andExpect(jsonPath("$.[*].subQuota").value(hasItem(DEFAULT_SUB_QUOTA)))
            .andExpect(jsonPath("$.[*].rateLimit").value(hasItem(DEFAULT_RATE_LIMIT.doubleValue())));
    }


    @Test
    public void getCampaign() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign);

        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", campaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campaign.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID.toString()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.shortCode").value(DEFAULT_SHORT_CODE.toString()))
            .andExpect(jsonPath("$.callbackUrl").value(DEFAULT_CALLBACK_URL.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY.toString()))
            .andExpect(jsonPath("$.approvedAt").value(sameInstant(DEFAULT_APPROVED_AT)))
            .andExpect(jsonPath("$.shortMsg").value(DEFAULT_SHORT_MSG.toString()))
            .andExpect(jsonPath("$.msisdnListContentType").value(DEFAULT_MSISDN_LIST_CONTENT_TYPE))
            .andExpect(jsonPath("$.msisdnList").value(Base64Utils.encodeToString(DEFAULT_MSISDN_LIST)))
            .andExpect(jsonPath("$.startAt").value(sameInstant(DEFAULT_START_AT)))
            .andExpect(jsonPath("$.expiredAt").value(sameInstant(DEFAULT_EXPIRED_AT)))
            .andExpect(jsonPath("$.workingHours").value(DEFAULT_WORKING_HOURS.toString()))
            .andExpect(jsonPath("$.workingWeekdays").value(DEFAULT_WORKING_WEEKDAYS.toString()))
            .andExpect(jsonPath("$.workingDays").value(DEFAULT_WORKING_DAYS.toString()))
            .andExpect(jsonPath("$.spSvc").value(DEFAULT_SP_SVC.toString()))
            .andExpect(jsonPath("$.spId").value(DEFAULT_SP_ID.toString()))
            .andExpect(jsonPath("$.cpId").value(DEFAULT_CP_ID.toString()))
            .andExpect(jsonPath("$.msgQuota").value(DEFAULT_MSG_QUOTA))
            .andExpect(jsonPath("$.subQuota").value(DEFAULT_SUB_QUOTA))
            .andExpect(jsonPath("$.rateLimit").value(DEFAULT_RATE_LIMIT.doubleValue()));
    }
    @Test
    public void getNonExistingCampaign() throws Exception {
        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCampaign() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign);

        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign
        Campaign updatedCampaign = campaignRepository.findById(campaign.getId()).get();
        updatedCampaign
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .externalId(UPDATED_EXTERNAL_ID)
            .channel(UPDATED_CHANNEL)
            .state(UPDATED_STATE)
            .shortCode(UPDATED_SHORT_CODE)
            .callbackUrl(UPDATED_CALLBACK_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .approvedBy(UPDATED_APPROVED_BY)
            .approvedAt(UPDATED_APPROVED_AT)
            .shortMsg(UPDATED_SHORT_MSG)
            .msisdnList(UPDATED_MSISDN_LIST)
            .msisdnListContentType(UPDATED_MSISDN_LIST_CONTENT_TYPE)
            .startAt(UPDATED_START_AT)
            .expiredAt(UPDATED_EXPIRED_AT)
//            .workingHours(UPDATED_WORKING_HOURS)
//            .workingWeekdays(UPDATED_WORKING_WEEKDAYS)
//            .workingDays(UPDATED_WORKING_DAYS)
            .spSvc(UPDATED_SP_SVC)
            .spId(UPDATED_SP_ID)
            .cpId(UPDATED_CP_ID)
            .msgQuota(UPDATED_MSG_QUOTA)
            .subQuota(UPDATED_SUB_QUOTA)
            .rateLimit(UPDATED_RATE_LIMIT);
        CampaignDTO campaignDTO = campaignMapper.toDto(updatedCampaign);

        restCampaignMockMvc.perform(put("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCampaign.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCampaign.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testCampaign.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testCampaign.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCampaign.getShortCode()).isEqualTo(UPDATED_SHORT_CODE);
        assertThat(testCampaign.getCallbackUrl()).isEqualTo(UPDATED_CALLBACK_URL);
        assertThat(testCampaign.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCampaign.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCampaign.getApprovedBy()).isEqualTo(UPDATED_APPROVED_BY);
        assertThat(testCampaign.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testCampaign.getShortMsg()).isEqualTo(UPDATED_SHORT_MSG);
        assertThat(testCampaign.getMsisdnList()).isEqualTo(UPDATED_MSISDN_LIST);
        assertThat(testCampaign.getMsisdnListContentType()).isEqualTo(UPDATED_MSISDN_LIST_CONTENT_TYPE);
        assertThat(testCampaign.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testCampaign.getExpiredAt()).isEqualTo(UPDATED_EXPIRED_AT);
        assertThat(testCampaign.getWorkingHours()).isEqualTo(UPDATED_WORKING_HOURS);
        assertThat(testCampaign.getWorkingWeekdays()).isEqualTo(UPDATED_WORKING_WEEKDAYS);
        assertThat(testCampaign.getWorkingDays()).isEqualTo(UPDATED_WORKING_DAYS);
        assertThat(testCampaign.getSpSvc()).isEqualTo(UPDATED_SP_SVC);
        assertThat(testCampaign.getSpId()).isEqualTo(UPDATED_SP_ID);
        assertThat(testCampaign.getCpId()).isEqualTo(UPDATED_CP_ID);
        assertThat(testCampaign.getMsgQuota()).isEqualTo(UPDATED_MSG_QUOTA);
        assertThat(testCampaign.getSubQuota()).isEqualTo(UPDATED_SUB_QUOTA);
        assertThat(testCampaign.getRateLimit()).isEqualTo(UPDATED_RATE_LIMIT);
    }

    @Test
    public void updateNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCampaignMockMvc.perform(put("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCampaign() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign);

        int databaseSizeBeforeDelete = campaignRepository.findAll().size();

        // Get the campaign
        restCampaignMockMvc.perform(delete("/api/campaigns/{id}", campaign.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campaign.class);
        Campaign campaign1 = new Campaign();
        campaign1.setId("id1");
        Campaign campaign2 = new Campaign();
        campaign2.setId(campaign1.getId());
        assertThat(campaign1).isEqualTo(campaign2);
        campaign2.setId("id2");
        assertThat(campaign1).isNotEqualTo(campaign2);
        campaign1.setId(null);
        assertThat(campaign1).isNotEqualTo(campaign2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampaignDTO.class);
        CampaignDTO campaignDTO1 = new CampaignDTO();
        campaignDTO1.setId("id1");
        CampaignDTO campaignDTO2 = new CampaignDTO();
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
        campaignDTO2.setId(campaignDTO1.getId());
        assertThat(campaignDTO1).isEqualTo(campaignDTO2);
        campaignDTO2.setId("id2");
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
        campaignDTO1.setId(null);
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
    }
}
