package com.ft.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ft.domain.Sms;
import com.ft.service.dto.SmsDTO;

public interface SmsCustomRepository {

	List<Sms> findAllPendingSms();

	Page<Sms> findAllPendingSms(Pageable pageable);

	long setExpiredSms();

	List<Object> statsByCampaignAndState(String campaignId, int state);

	List<Object> stats(SmsDTO predicate);

	List<Object> statsByCampaign(String campaignId);

}
