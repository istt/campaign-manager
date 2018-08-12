package com.ft.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.domain.Campaign;
import com.ft.domain.DataFile;
import com.ft.domain.Sms;
import com.ft.repository.CampaignRepository;
import com.ft.repository.DataFileRepository;
import com.ft.repository.SmsRepository;
import com.ft.service.dto.CampaignDTO;
import com.ft.service.mapper.CampaignMapper;
import com.querydsl.core.types.Predicate;
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
        smsRepo.deleteByStateAndCampaignId(0, id);
    }

    final static Pattern patt = Pattern.compile("[0-9]{8,12}");

    @Autowired
    SmsRepository smsRepo;

    @Autowired
    DataFileRepository dataFileRepo;

    public CampaignDTO processDatafiles(CampaignDTO cp) {
    	long result = 0L;
    	for (int i = 0; i < cp.getDatafiles().size(); i++) {
    		Optional<DataFile> df = dataFileRepo.findById(cp.getDatafiles().get(i).getId());
    		if (df.isPresent()) {
    			result += processDatafiles(cp, df.get());
    			cp.getDatafiles().get(i).setProcessAt(ZonedDateTime.now());
    		}
    	}
    	// Reset it back to Approved if not disabled
    	if (result > 0) cp.setState(cp.getState() <= 0 ? 0 : 1);
    	return save(cp);
    }
    public long processDatafiles(CampaignDTO cp, DataFile dataFile) {
    	long result = 0L;
    	if (dataFile.getDataContentType().contains("text")) {
    		Map<String, String> valueMap = new HashMap<String, String> ();
    		if (dataFile.getDataCsvHeaders().isEmpty() || (dataFile.getDataCsvHeaders().size() == 1)) {
    			Matcher m = patt.matcher(new String(dataFile.getData()));
                while (m.find()) {
                        String msisdn = msisdnFormat(m.group());
                        result++;
                        valueMap.put("msisdn", msisdn);
//                        log.debug("Properties: " + valueMap);
    					smsRepo.save(
                        		new Sms()
                        		.source(cp.getShortCode())
                        		.destination(msisdn)
                        		// FIXME: Provide available template engines
                        		.shortMsg(StringSubstitutor.replace(cp.getShortMsg(), valueMap, "{{", "}}"))
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
    		} else { // Advanced header replacements
    			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dataFile.getData())));
    			String line = null;
    			try {
					while ((line = reader.readLine()) != null) {
						result++;
						String[] fields = line.split("[;,|]");
//						log.debug("Fields size: " + fields.length);
						int i = 0;
						for (String key: dataFile.getDataCsvHeaders()) {
							if (i < fields.length) {
								log.debug("Found: " + key + " ==> " + fields[i]);
								valueMap.put(key, fields[i]);
							}
							i++;
						}
//						log.debug("Properties: " + valueMap);
						smsRepo.save(
                        		new Sms()
                        		.source(cp.getShortCode())
                        		.destination(msisdnFormat(valueMap.get("msisdn")))
                        		// FIXME: Provide available template engines
                        		.shortMsg(StringSubstitutor.replace(cp.getShortMsg(), valueMap, "{{", "}}"))
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
				} catch (IOException e) {
					log.error("Cannot process input", e);
				}

    		}
            if (cp.getStats() == null) cp.setStats(new ConcurrentHashMap<String, Object>());
            cp.getStats().put("msgCnt", cp.getStats().get("msgCnt") == null ? result : Long.parseLong(cp.getStats().get("msgCnt").toString()) + result);
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

	public List<Campaign> getPendingCampaigns() {
		return campaignRepository.findAllPendingCampaign();
	}

	public Page<CampaignDTO> findAll(Predicate predicate, Pageable pageable) {
		log.debug("Request to get all Campaigns with predicate " + predicate);
        return campaignRepository.findAll(predicate, pageable)
            .map(campaignMapper::toDto);
	}

	@Scheduled(fixedDelay = 60000)
    public long updateCampaignStates() {
		long i = 0L;
		// Update those campaign with state = 0 and expiredAt < now() to be -9
		i += campaignRepository.setExpiredCampaign();
		i += campaignRepository.setFinishCampaign();
		return i;
	}

}
