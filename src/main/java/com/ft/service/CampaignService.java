package com.ft.service;

import com.ft.domain.Campaign;
import com.ft.domain.Sms;
import com.ft.repository.CampaignRepository;
import com.ft.repository.SmsRepository;
import com.ft.service.dto.CampaignDTO;
import com.ft.service.mapper.CampaignMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Service Implementation for managing Campaign.
 */
@Service
public class CampaignService {

    private final Logger log = LoggerFactory.getLogger(CampaignService.class);

    private final CampaignRepository campaignRepository;

    private final CampaignMapper campaignMapper;

    public CampaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
    }

    /**
     * Save a campaign.
     *
     * @param campaignDTO the entity to save
     * @return the persisted entity
     */
    public CampaignDTO save(CampaignDTO campaignDTO) {
        log.debug("Request to save Campaign : {}", campaignDTO);
        Campaign campaign = campaignMapper.toEntity(campaignDTO);
        campaign = campaignRepository.save(campaign);
        return campaignMapper.toDto(campaign);
    }

    /**
     * Get all the campaigns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<CampaignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Campaigns");
        return campaignRepository.findAll(pageable)
            .map(campaignMapper::toDto);
    }


    /**
     * Get one campaign by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<CampaignDTO> findOne(String id) {
        log.debug("Request to get Campaign : {}", id);
        return campaignRepository.findById(id)
            .map(campaignMapper::toDto);
    }

    /**
     * Delete the campaign by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Campaign : {}", id);
        campaignRepository.deleteById(id);
    }

    final static Pattern patt = Pattern.compile("[0-9]{8,12}");

    @Autowired
    SmsRepository smsRepo;

    public int processDataFile(CampaignDTO cp) {
    	int result = 0;
    	if (cp.getMsisdnListContentType().contains("text")) {
    		Matcher m = patt.matcher(new String(cp.getMsisdnList()));
            while (m.find()) {
                    String msisdn = msisdnFormat(m.group());
                    result++;
                    smsRepo.save(
                    		new Sms()
                    		.source(cp.getShortCode())
                    		.destination(msisdn)
                    		// FIXME: Provide available template engines
                    		.shortMsg(cp.getShortMsg().replaceAll("[MSISDN]", msisdn))
                    		// Campaign related info
                    		.campaignId(cp.getId())
                    		.submitAt(cp.getStartAt())
                    		.expiredAt(cp.getExpiredAt())
                    		.state(0)
                    		// CDR Repo info
                    		.spSvc(cp.getSpSvc())
                    		.spId(cp.getSpId())
                    		.cpId(cp.getCpId())
                    );
            }

    	}
    	return result;
    }

    /**
     * Return the correct MSISDN format for the whole number
     *
     * @param msisdn
     * @return
     */
    public String msisdnFormat(String msisdn) {
            try {
                    msisdn = "" + Long.parseLong(msisdn.trim());
                    if (!msisdn.substring(0, "84".length()).equalsIgnoreCase("84")) {
                            msisdn = "84" + msisdn;
                    }
            } catch (Exception e) {}
            return String.valueOf(Long.parseLong(msisdn));
    }

}
