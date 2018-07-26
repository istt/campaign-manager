package com.ft.components;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

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
public class VasCloudSmsSubmitCallable implements Callable<Integer> {

    private final Logger log = LoggerFactory.getLogger(VasCloudSmsSubmitCallable.class);

    private List<Sms> smsList;

	private Campaign campaign;

	@Autowired
    SmsRepository smsRepo;

    @Autowired
    CampaignRepository cpRepo;

    @Autowired
	RestTemplate restTemplate;

    private Bucket bucket;

    @Override
    public Integer call() throws Exception {
        int i = 0;
        VasCloudConfigurationDTO cfg = (VasCloudConfigurationDTO) campaign.getCfg().get("VASCLOUD");
        for (Sms sms : smsList) {
        	VasCloudMsgDTO request = createMsg(sms, campaign, cfg);
    	    try {
    	    	VasCloudMsgDTO response = restTemplate.postForObject(cfg.getEndPoint(), request, VasCloudMsgDTO.class);
    	    	log.info("SMS VASCLOUD RESPONSE: " + response);
    	    	if (response.getCmd().get("error_id").equalsIgnoreCase("0")) {
    	    		log.info("Successful submit!!!");
    	    		i ++;
    	    	} else {
    	    		log.error("Failed to submit SMS" + response.getCmd().get("error_id") + "|" + response.getCmd().get("error_desc"));
    	    	}
    	    } catch (HttpStatusCodeException s){
    	    	log.error("Cannot send message: " + s.getRawStatusCode() + " " + s.getStatusText() + ":" + s.getResponseBodyAsString());
    	    } catch (ResourceAccessException e) {
    			log.error("FAILED TO CONNECT TO ENDPOINT: " + e.getMessage());
            } catch (Exception e) {
                log.error("CANNOT CHARGE: " + request, e);
            }
        }
        return i;
    }

    public VasCloudMsgDTO createMsg(Sms sms, Campaign cp, VasCloudConfigurationDTO cfg)  {
		VasCloudMsgDTO msg = new VasCloudMsgDTO();
		msg.setMsgType("REQUEST");
		msg.setModule("SMSGW");

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

    public Bucket submitSmTpsBucket() {
        Bandwidth limit = Bandwidth.simple(campaign.getRateLimit(), Duration.ofSeconds(1));
        // construct the bucket
        Bucket bucket = Bucket4j.builder().addLimit(limit).build();
        log.info("Campaign: " + campaign.getName() + " running speed: " + campaign.getRateLimit() + " TPS");
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