package com.ft.components;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Campaign;
import com.ft.domain.Sms;
import com.ft.repository.CampaignRepository;
import com.ft.repository.SmsRepository;
import com.ft.service.dto.VasCloudConfigurationDTO;
import com.ft.service.dto.VasCloudMsgDTO;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

@Component
@Scope("prototype")
public class VasCloudSmsSubmitCallable implements Callable<Long> {

    private final Logger log = LoggerFactory.getLogger(VasCloudSmsSubmitCallable.class);

    private List<Sms> smsList;

	private Campaign campaign;

	@Autowired
    SmsRepository smsRepo;

    @Autowired
    CampaignRepository cpRepo;

    @Autowired
	RestTemplate restTemplate;

    @Autowired
    ObjectMapper mapper;

    private Bucket bucket;

    public static Map<String, Bucket> bucketList = new ConcurrentHashMap<String, Bucket>();

    @Override
    public Long call() throws Exception {
        long i = 0L;
        VasCloudConfigurationDTO cfg = mapper.readValue(mapper.writeValueAsString(campaign.getCfg().get("VASCLOUD")), VasCloudConfigurationDTO.class);
        if (cfg.getRateLimit() != null) {
        	Bucket svcBucket = bucketList.get(cfg.getId());
        	if (svcBucket == null ) {
        		svcBucket = createBucket(cfg.getRateLimit());
        		bucketList.put(cfg.getId(), svcBucket);
        	}
        	svcBucket.asScheduler().consume(smsList.size());
        }
        createBucket(campaign.getRateLimit());
        for (Sms sms : smsList) {
        	log.debug("Gotta submit SMS: " + sms);
        	VasCloudMsgDTO request = createMsg(sms, campaign, cfg);
    	    try {
    	    	if (bucket != null) bucket.asScheduler().consume(1);
    	    	VasCloudMsgDTO response = restTemplate.postForObject(cfg.getEndPoint(), request, VasCloudMsgDTO.class);
    	    	sms
    	    	.submitRequestPayload(request.toString())
    	    	.submitResponsePayload(response.toString())
    	    	.submitAt(ZonedDateTime.now())
    	    	;
    	    	log.info("SMS VASCLOUD RESPONSE: " + response);
    	    	if (response.getCmd().get("error_id").equalsIgnoreCase("0")) {
    	    		log.info("Successful submit!!!");
    	    		i ++;
    	    		smsRepo.save(sms.state(9));
    	    	} else {
    	    		log.error("Failed to submit SMS" + response.getCmd().get("error_id") + "|" + response.getCmd().get("error_desc"));
    	    		smsRepo.save(sms.state(-9));
    	    	}
    	    } catch (HttpStatusCodeException s){
    	    	log.error("Cannot send message: " + s.getRawStatusCode() + " " + s.getStatusText() + ":" + s.getResponseBodyAsString());
    	    } catch (ResourceAccessException e) {
    			log.error("FAILED TO CONNECT TO ENDPOINT: " + e.getMessage());
            } catch (Exception e) {
                log.error("Exception: " + request, e);
            }
        }
        campaign.getCfg().put("submitCnt", campaign.getCfg().get("submitCnt") == null ? i : Long.parseLong(campaign.getCfg().get("submitCnt").toString()) + i);
        cpRepo.save(campaign);
        return i;
    }

    public VasCloudMsgDTO createMsg(Sms sms, Campaign cp, VasCloudConfigurationDTO cfg)  {
		VasCloudMsgDTO msg = new VasCloudMsgDTO();
		msg.setMsgType("REQUEST");
		msg.setModule("SMSGW");
		msg.setCmd(new ConcurrentHashMap<String, String>());

		long id = System.currentTimeMillis();
		String msisdn = sms.getDestination();
		String shortCode = cp.getShortCode();
		String authenticate = DigestUtils.md5DigestAsHex((
				DigestUtils.md5DigestAsHex((id + cfg.getUsername()).getBytes())
				+ DigestUtils.md5DigestAsHex(("smsgw@2016" + msisdn).getBytes())
				+ cfg.getPassword()
		).getBytes()) ;
		msg.getCmd().put("transaction_id", "" + id);
		msg.getCmd().put("mo_id", "0");
		msg.getCmd().put("destination_address", msisdn);
		msg.getCmd().put("source_address", shortCode);
		msg.getCmd().put("brandname", "");
		msg.getCmd().put("content_type", "TEXT");
		msg.getCmd().put("user_name", cfg.getUsername());
		msg.getCmd().put("authenticate", authenticate);
		msg.getCmd().put("info", sms.getShortMsg());
		msg.getCmd().put("command_code", "");
		msg.getCmd().put("cp_code", cfg.getCpCode());
		msg.getCmd().put("cp_charge", cfg.getCpCharge());
		msg.getCmd().put("service_code", cfg.getServiceCode());
		msg.getCmd().put("package_code", "PUSH_" + cfg.getPackageCode());
		msg.getCmd().put("package_price", "0");
		return msg;
	}

    public Bucket createBucket(Long tps) {
    	if (tps == null) return null;
        Bandwidth limit = Bandwidth.simple(tps, Duration.ofSeconds(1));
        // construct the bucket
        Bucket bucket = Bucket4j.builder().addLimit(limit).build();
        return bucket;
    }


    public List<Sms> getSmsList() {
		return smsList;
	}

	public void setSmsList(List<Sms> smsList) {
		this.smsList = smsList;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
}
