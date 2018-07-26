package com.ft.service.vascloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ft.CampaignManagerApp;
import com.ft.config.ApplicationProperties;
import com.ft.service.dto.VasCloudConfigurationDTO;
import com.ft.service.dto.VasCloudMsgDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CampaignManagerApp.class)
public class VasCloudRequestIntTest {

	private final Logger log = LoggerFactory.getLogger(VasCloudRequestIntTest.class);

	XmlMapper mapper = new XmlMapper();

	@Autowired
	ApplicationProperties props;

	@Autowired
	RestTemplate restTemplate;

	@Test
	public void testCreateMsg() throws JsonProcessingException {
		log.info(mapper.writeValueAsString(createMsg()));
	}
	public VasCloudMsgDTO createMsg()  {
		VasCloudMsgDTO msg = new VasCloudMsgDTO();
		msg.setMsgType("REQUEST");
		msg.setModule("SMSGW");

		VasCloudConfigurationDTO cfg = props.getVasCloud();
		long id = System.currentTimeMillis();
		String msisdn = "84915221122";
		String shortCode = "9198";
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
		msg.getCmd().put("info", "(QC)MIEN PHI HOAN TOAN 3G/4G TOC DO CAO danh cho TB " + msisdn + " khi soan DK gui 9198 trai nghiem tai http://vina-travel.vn. Sau km 5.000d/ngay.LH 19006821");
		msg.getCmd().put("command_code", "");
		msg.getCmd().put("cp_code", cfg.getCpCode());
		msg.getCmd().put("cp_charge", cfg.getCpCharge());
		msg.getCmd().put("service_code", cfg.getServiceCode());
		msg.getCmd().put("package_code", "PUSH_TTTT");
		msg.getCmd().put("package_price", "0");
		return msg;
	}

	@Test
	public void sendMsg()  {
	    VasCloudMsgDTO request = createMsg();
	    try {
	    	VasCloudMsgDTO response = restTemplate.postForObject(props.getVasCloud().getEndPoint(), request, VasCloudMsgDTO.class);
	    	log.info(mapper.writeValueAsString(response));
	    	if (response.getCmd().get("error_id").equalsIgnoreCase("0")) {
	    		log.info("Successful submit!!!");
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

}
